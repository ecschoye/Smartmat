package ntnu.idatt2106.backend.service;

import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.RefrigeratorNotFoundException;
import ntnu.idatt2106.backend.exceptions.SaveException;
import ntnu.idatt2106.backend.exceptions.UnauthorizedException;
import ntnu.idatt2106.backend.exceptions.UserNotFoundException;
import ntnu.idatt2106.backend.model.Grocery;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.RefrigeratorGrocery;
import ntnu.idatt2106.backend.model.SubCategory;
import ntnu.idatt2106.backend.model.dto.GroceryDTO;
import ntnu.idatt2106.backend.model.dto.RefrigeratorGroceryDTO;
import ntnu.idatt2106.backend.model.enums.FridgeRole;
import ntnu.idatt2106.backend.model.requests.SaveGroceryListRequest;
import ntnu.idatt2106.backend.repository.GroceryRepository;
import ntnu.idatt2106.backend.repository.RefrigeratorGroceryRepository;
import ntnu.idatt2106.backend.repository.SubCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for handling operations regarding groceries inside a refrigerator
 */
@Service
@RequiredArgsConstructor
public class GroceryService {

    private static final FridgeRole ADD_PRIVILEGE = FridgeRole.SUPERUSER;
    private static final FridgeRole REMOVE_PRIVILEGE = FridgeRole.SUPERUSER;

    private final Logger logger = LoggerFactory.getLogger(GroceryService.class);

    private final CookieService cookieService;
    private final JwtService jwtService;
    private final RefrigeratorGroceryRepository refrigeratorGroceryRepository;
    private final GroceryRepository groceryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final RefrigeratorService refrigeratorService;

    /**
     * Saves a grocery to a refrigerator. If it is a custom
     * grocery, it gets added to the groceries first.
     *
     * @param saveRequest Request to save a number of groceries
     * @param httpRequest The http request
     * @throws RefrigeratorNotFoundException If refrigerator not found
     * @throws UserNotFoundException If user not found
     * @throws UnauthorizedException If user not authorized to perform action
     * @throws SaveException If save fails
     */
    @Transactional(propagation =  Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addGrocery(SaveGroceryListRequest saveRequest, HttpServletRequest httpRequest) throws RefrigeratorNotFoundException, UserNotFoundException, UnauthorizedException, SaveException {
        Refrigerator refrigerator = refrigeratorService.getRefrigerator(saveRequest.getRefrigeratorId());
        FridgeRole FridgeRole = getFridgeRole(refrigerator, httpRequest);
        if(FridgeRole != ADD_PRIVILEGE) throw new UnauthorizedException("User not authorized to add groceries");

        logger.info("Saving grocery list to refrigerator");
        //Handle each grocery in the list individually based on custom grocery or existing
        for (GroceryDTO groceryDTO: saveRequest.getGroceryList()) {
            Grocery grocery;
            //If custom add custom grocery, else fetch
            if(groceryDTO.isCustom()) grocery = addCustomGrocery(groceryDTO);
            else grocery = getGroceryById(groceryDTO.getId());

            //Define refrigerator grocery
            RefrigeratorGrocery refrigeratorGrocery = new RefrigeratorGrocery();
            refrigeratorGrocery.setGrocery(grocery);
            refrigeratorGrocery.setRefrigerator(refrigerator);
            refrigeratorGrocery.setPhysicalExpireDate(getPhysicalExpireDate(groceryDTO.getGroceryExpiryDays()));

            saveRefrigeratorGrocery(refrigeratorGrocery);
        }
    }

    /**
     * Adds a custom grocery to the grocery list
     *
     * @param groceryDTO the data received from request
     * @return the added grocery
     * @throws SaveException if save fails
     */
    private Grocery addCustomGrocery(GroceryDTO groceryDTO) throws SaveException {
        Grocery newGrocery = new Grocery();
        newGrocery.setName(groceryDTO.getName());
        SubCategory subCategory = subCategoryRepository.findById(groceryDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Subcategory not found by id"));
        newGrocery.setSubCategory(subCategory);
        if(groceryDTO.getDescription() != null){
            newGrocery.setDescription(groceryDTO.getDescription());
        }
        newGrocery.setGroceryExpiryDays(groceryDTO.getGroceryExpiryDays());

        return saveGrocery(newGrocery);
    }

    /**
     * Extracts username from cookie
     * @param httpRequest Request we are extracting from
     * @return username
     */
    public String extractEmail(HttpServletRequest httpRequest) {
        String token = cookieService.extractTokenFromCookie(httpRequest);
        return jwtService.extractClaim(token, Claims::getSubject);
    }

    /**
     * Gets a list of DTO's representing groceries
     * in refrigerator.
     *
     * @param refrigeratorId Refrigerator id
     * @param request Http request
     * @return List of RefrigeratorGrocery DTO's
     * @throws RefrigeratorNotFoundException If refrigerator not found
     * @throws UserNotFoundException if user not found
     * @throws UnauthorizedException if user not member
     */
    public List<RefrigeratorGroceryDTO> getGroceriesByRefrigerator(long refrigeratorId, HttpServletRequest request) throws RefrigeratorNotFoundException, UserNotFoundException, UnauthorizedException {
        //Throws if refrigerator does not exist
        Refrigerator refrigerator = refrigeratorService.getRefrigerator(refrigeratorId);
        getFridgeRole(refrigerator,request);

        List<RefrigeratorGrocery> groceries = refrigeratorGroceryRepository.findAllByRefrigeratorId(refrigerator.getId());
        List<RefrigeratorGroceryDTO> refGroceryDTOS = new ArrayList<>();
        for (RefrigeratorGrocery grocery: groceries) {
            refGroceryDTOS.add(new RefrigeratorGroceryDTO(grocery));
        }
        return refGroceryDTOS;
    }

    /**
     * Gets grocery by id
     *
     * @param groceryId id of grocery
     * @return the grocery
     */
    public Grocery getGroceryById(long groceryId){
        return groceryRepository.findById(groceryId)
                .orElseThrow(() -> new EntityNotFoundException("Existing grocery not found"));
    }

    /**
     * Gets refigeratorGrocery by id
     *
     * @param refrigeratorGroceryId refigeratorGrocery id
     * @return the refrigeratorGrocery
     */
    public RefrigeratorGrocery getRefrigeratorGroceryById(long refrigeratorGroceryId) throws EntityNotFoundException{
        return refrigeratorGroceryRepository.findById(refrigeratorGroceryId)
                .orElseThrow(() -> new EntityNotFoundException("refrigeratorGrocery not found"));
    }

    /**
     * Gets the FridgeRole of user that requested action
     *
     * @param refrigerator refrigerator FridgeRole is in
     * @param request request to api
     * @return FridgeRole of user
     * @throws UserNotFoundException if user not found
     * @throws UnauthorizedException if user not member
     */
    public FridgeRole getFridgeRole(Refrigerator refrigerator, HttpServletRequest request) throws UserNotFoundException, UnauthorizedException {
        logger.info("Checking if user is member");
        String email = extractEmail(request);
        //Throws if user is not member
        return refrigeratorService.getFridgeRole(refrigerator, email);
    }

    /**
     * Creates a physical expire date by getting todays
     * date and adding integer number of days
     *
     * @param groceryExpiryDays expected shelf life
     * @return expected expiry date
     */
    public Date getPhysicalExpireDate(int groceryExpiryDays) {
        Calendar calendar = Calendar.getInstance(); // get the current date and time
        calendar.add(Calendar.DAY_OF_MONTH, groceryExpiryDays); // add groceryExpiryDays to the current date
        return calendar.getTime();
    }

    /**
     * Removes a refrigeratorGrocery by id
     *
     * @param refrigeratorGroceryId id
     * @param request http request by user
     * @throws UserNotFoundException If refrigeratorGrocery not found
     * @throws UnauthorizedException If user does not have permission
     */
    @Transactional(propagation =  Propagation.REQUIRED, rollbackFor = Exception.class)
    public void removeRefrigeratorGrocery(long refrigeratorGroceryId, HttpServletRequest request) throws UserNotFoundException, UnauthorizedException, EntityNotFoundException {
        RefrigeratorGrocery refrigeratorGrocery = getRefrigeratorGroceryById(refrigeratorGroceryId);
        if(getFridgeRole(refrigeratorGrocery.getRefrigerator(), request) != REMOVE_PRIVILEGE) {
            throw new UnauthorizedException("User does not have permission to remove this grocery");
        }
        refrigeratorGroceryRepository.deleteById(refrigeratorGroceryId);
    }

    /**
     * Saves a grocery to the grocery table
     *
     * @param grocery the grocery to be saved
     * @throws SaveException If save fails
     */
    public Grocery saveGrocery(Grocery grocery) throws SaveException{
        try {
            return groceryRepository.save(grocery);
        } catch (Exception e) {
            throw new SaveException(e.getMessage());
        }
    }

    /**
     * Saves a refrigerator grocery to the refrigeratorGrocery table
     *
     * @param grocery the grocery to be saved
     * @throws SaveException If save fails
     */
    public void saveRefrigeratorGrocery(RefrigeratorGrocery grocery) throws SaveException {
        try {
            refrigeratorGroceryRepository.save(grocery);
        } catch (Exception e) {
            throw new SaveException(e.getMessage());
        }
    }

    public List<Grocery> getAllGroceries() {
        return groceryRepository.findAll();
    }

    public List<GroceryDTO> getAllGroceriesDTO() {
        List<Grocery> groceries = groceryRepository.findAll();
        return groceries.stream().map(GroceryDTO::new).collect(Collectors.toList());
    }
}
