package ntnu.idatt2106.backend.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.*;
import ntnu.idatt2106.backend.model.*;
import ntnu.idatt2106.backend.model.dto.GroceryDTO;
import ntnu.idatt2106.backend.model.dto.shoppingCartElement.ShoppingCartElementDTO;
import ntnu.idatt2106.backend.model.dto.shoppingCartElement.ShoppingCartElementDTOComparator;
import ntnu.idatt2106.backend.model.enums.FridgeRole;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingCart;
import ntnu.idatt2106.backend.model.requests.SaveGroceryListRequest;
import ntnu.idatt2106.backend.model.requests.SaveGroceryRequest;
import ntnu.idatt2106.backend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingListRepository shoppingListRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final UserRepository userRepository;
    private final RefrigeratorUserRepository refrigeratorUserRepository;
    private final GroceryRepository groceryRepository;
    private final GroceryShoppingCartRepository groceryShoppingCartRepository;

    private final GroceryService groceryService;
    private final CookieService cookieService;
    private final JwtService jwtService;
    private Logger logger = LoggerFactory.getLogger(ShoppingCartService.class);

    /**
     * Getter for the shopping list by the shopping list id
     * @param shoppingListId ID to the shopping list
     * @return Shopping list object
     * @throws ShoppingListNotFound If it is not find any shopping list for the shopping list id in the parameter
     */
    protected ShoppingList getShoppingListById(long shoppingListId) throws ShoppingListNotFound {
        return shoppingListRepository.findById(shoppingListId)
                .orElseThrow(() -> new ShoppingListNotFound("Shopping list not found for shopping list id " + shoppingListId));
    }

    /**
     * Getter for the shopping list by the shopping list id
     * @param shoppingListId ID to the shopping list
     * @return Shopping list object
     * @throws ShoppingCartNotFound If it is not find any shopping cart for the shopping list id in the parameter
     */
    protected ShoppingCart getShoppingCartById(long shoppingListId) throws ShoppingCartNotFound {
        return shoppingCartRepository.findByShoppingListId(shoppingListId)
                .orElseThrow(() -> new ShoppingCartNotFound("Shopping cart not found for shopping list id " + shoppingListId));
    }

    /**
     * Creates a new shopping cart if it does not already exist a shopping cart for the refrigerator id
     * The shopping cart id to an already existing list is returned if it already exists a shopping cart for the given refrigerator
     * @param shoppingListId ID of connected shopping list
     * @return shopping cart id for the shopping list id in the parameter
     */
    public long createShoppingCart(long shoppingListId) throws ShoppingListNotFound {
        ShoppingList shoppingList = getShoppingListById(shoppingListId);

        try {
            ShoppingCart shoppingCart = getShoppingCartById(shoppingListId);
            return shoppingCart.getId();
        } catch (ShoppingCartNotFound e) {
            ShoppingCart newShoppingCart = new ShoppingCart();
            newShoppingCart.setShoppingList(shoppingList);

            shoppingCartRepository.save(newShoppingCart);
            logger.info("Created shopping cart with id {}", newShoppingCart.getId());
            return newShoppingCart.getId();
        }
    }

    /**
     * Getter for all groceries in the shopping cart specified in the parameter
     * @param shoppingCartId ID to the shopping cart to retrieve groceries from
     * @return All groceries from the shopping cart with the shopping cart id specified in the parameter
     * @exception NoGroceriesFound Could not find any groceries
     */
    public List<ShoppingCartElementDTO> getGroceries(long shoppingCartId) throws NoGroceriesFound {
        List<GroceryShoppingCart> groceries = shoppingCartRepository.findByShoppingCartId(shoppingCartId);
        if (groceries.isEmpty()) {
            logger.info("Received no groceries from the database");
            throw new NoGroceriesFound("Could not find any groceries for shopping cart id " + shoppingCartId);
        }
        return groceries.stream().map(ShoppingCartElementDTO::new).
                sorted(new ShoppingCartElementDTOComparator()).collect(Collectors.toList());
    }

    // todo: is duplicate with method in ShoppingListService - remove
    private String extractEmail(HttpServletRequest httpRequest) {
        String token = cookieService.extractTokenFromCookie(httpRequest);
        return jwtService.extractClaim(token, Claims::getSubject);
    }

    private boolean isSuperUser(String eMail, long shoppingListId) {
        //find the refrigerator connected to the shoppingList
        Optional<Refrigerator> refrigerator = Optional.of(shoppingListRepository.findRefrigeratorById(shoppingListId));
        //find the role to the eMail in the refrigerator
        Optional<User> user = userRepository.findByEmail(eMail);

        if (user.isEmpty()) {
            logger.info("User is empty");
            return false;
        }

        Optional<RefrigeratorUser> refrigeratorUser = refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(user.get().getId(), refrigerator.get().getId());

        if (refrigeratorUser.isEmpty()) {
            logger.info("Refrigerator user is empty");
            return false;
        }

        logger.info("isUserSuper user {}", refrigeratorUser.get().getFridgeRole() == FridgeRole.SUPERUSER);
        return refrigeratorUser.get().getFridgeRole() == FridgeRole.SUPERUSER;
    }

    public Optional<GroceryShoppingCart> saveGrocery(SaveGroceryRequest groceryRequest, HttpServletRequest httpRequest) throws UnauthorizedException {
        String eMail = extractEmail(httpRequest);
        logger.info("Saving grocery: {} to shopping cart with id {}", groceryRequest.getName(), groceryRequest.getForeignKey());

        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(groceryRequest.getForeignKey());
        if (shoppingCart.isEmpty()) {
            logger.info("Could not find a shopping cart with id {}", groceryRequest.getForeignKey());
            return Optional.empty();
        }

        logger.info("Found shopping cart for shopping cart id {}", shoppingCart.get().getId());

        if (!isSuperUser(eMail, shoppingCart.get().getId())) {
            logger.info("You have to be super user to add a grocery to the shopping cart");
            throw new UnauthorizedException("It is only possible for super users to add grocery in shopping cart");
        }

        Optional<SubCategory> subCategory = subCategoryRepository.findById(groceryRequest.getSubCategoryId());
        if (subCategory.isEmpty()) {
            logger.info("Could not find a shopping list with id {}", groceryRequest.getForeignKey());
            return Optional.empty();
        }

        logger.info("Found subcategory with id {}", subCategory.get().getId());
        Grocery grocery = Grocery.builder()
                .name(groceryRequest.getName())
                .groceryExpiryDays(groceryRequest.getGroceryExpiryDays())
                .description(groceryRequest.getDescription())
                .subCategory(subCategory.get())
                .build();
        groceryRepository.save(grocery);
        logger.info("Created grocery with name {}", grocery.getName());

        GroceryShoppingCart groceryShoppingCart = new GroceryShoppingCart();
        groceryShoppingCart.setGrocery(grocery);
        groceryShoppingCart.setShoppingCart(shoppingCart.get());
        groceryShoppingCart.setQuantity(groceryRequest.getQuantity());

        logger.info("Saved new grocery to the grocery list");

        return Optional.of(groceryShoppingCartRepository.save(groceryShoppingCart));
    }

    /**
     * Transfers one grocery from the shopping cart to the refrigerator
     * @param shoppingCartItemId ID to the grocery in the shopping cart
     * @param httpRequest http request
     * @throws UserNotFoundException If the user is not found
     * @throws SaveException If there occurred an error while saving
     * @throws UnauthorizedException If not authorized
     * @throws RefrigeratorNotFoundException If no refrigerator was found
     * @throws NoGroceriesFound If no groceries was found in the shopping cart
     */
    public void transferGroceryToRefrigerator(long shoppingCartItemId, HttpServletRequest httpRequest) throws NoGroceriesFound, UserNotFoundException, SaveException, UnauthorizedException, RefrigeratorNotFoundException {
        GroceryShoppingCart shoppingCartItem = groceryShoppingCartRepository.findById(shoppingCartItemId)
                        .orElseThrow(() -> new NoGroceriesFound("Could not find shopping cart item"));

        long refrigeratorId = shoppingCartItem.getShoppingCart().getShoppingList().getRefrigerator().getId();
        GroceryDTO groceryDTO = new GroceryDTO(shoppingCartItem.getGrocery());
        List<GroceryDTO> groceries = new ArrayList<>();
        groceries.add(groceryDTO);

        SaveGroceryListRequest saveGrocery = new SaveGroceryListRequest(refrigeratorId, groceries);
        for (int i = 0; i < shoppingCartItem.getQuantity(); i++) {
            groceryService.addGrocery(saveGrocery, httpRequest);
        }
        groceryShoppingCartRepository.delete(shoppingCartItem);
    }

    /**
     * Transfers all groceries from the shopping cart to the refrigerator
     * @param groceryIds Array with grocery ids to transfer
     * @param httpRequest http request
     * @throws UserNotFoundException If the user is not found
     * @throws SaveException If there occurred an error while saving
     * @throws UnauthorizedException If not authorized
     * @throws RefrigeratorNotFoundException If no refrigerator was found
     * @throws NoGroceriesFound If no groceries was found in the shopping cart
     */
    public void transferAllGroceriesToRefrigerator(long[] groceryIds, HttpServletRequest httpRequest) throws UserNotFoundException, SaveException, UnauthorizedException, RefrigeratorNotFoundException, NoGroceriesFound {
        for (long groceryId:groceryIds) {
            transferGroceryToRefrigerator(groceryId, httpRequest);
        }
    }
}
