package ntnu.idatt2106.backend.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.*;
import ntnu.idatt2106.backend.model.*;
import ntnu.idatt2106.backend.model.category.Category;
import ntnu.idatt2106.backend.model.category.CategoryComparator;
import ntnu.idatt2106.backend.model.dto.shoppingCartElement.ShoppingCartElementDTO;
import ntnu.idatt2106.backend.model.dto.shoppingCartElement.ShoppingCartElementDTOComparator;
import ntnu.idatt2106.backend.model.dto.shoppingListElement.ShoppingListElementDTO;
import ntnu.idatt2106.backend.model.dto.shoppingListElement.ShoppingListElementDTOComparator;
import ntnu.idatt2106.backend.model.enums.FridgeRole;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingList;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorShoppingList;
import ntnu.idatt2106.backend.model.requests.SaveGroceryRequest;
import ntnu.idatt2106.backend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
public class ShoppingListService {
    private final ShoppingListRepository shoppingListRepository;
    private final GroceryShoppingListRepository groceryShoppingListRepository;
    private final RefrigeratorShoppingListRepository refrigeratorShoppingListRepository;

    private final ShoppingCartService shoppingCartService;
    private final RefrigeratorService refrigeratorService;
    private final GroceryService groceryService;

    private Logger logger = LoggerFactory.getLogger(ShoppingListService.class);

    /**
     * Getter for the shopping list by the refrigerator id
     * @param refrigeratorId ID to the refrigerator
     * @return Shopping list object
     * @throws ShoppingListNotFound If it is not find any shopping list for the refrigerator id in the parameter
     */
    protected ShoppingList getShoppingListByRefrigeratorId(long refrigeratorId) throws ShoppingListNotFound {
        return shoppingListRepository.findByRefrigeratorId(refrigeratorId)
                .orElseThrow(() -> new ShoppingListNotFound("Shopping list not found for refrigerator id " + refrigeratorId));
    }

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
     * Creates a new shopping list if it does not already exist a shopping list for the refrigerator id
     * The shopping list id to an already existing list is returned if it already exists a shopping list for the given refrigerator
     * @param refrigeratorId ID of connected refrigerator
     * @return shopping list id for the refrigerator id in the parameter
     * @throws RefrigeratorNotFoundException If no refrigerator is found the refrigerator id in the parameter
     */
    public long createShoppingList(long refrigeratorId) throws RefrigeratorNotFoundException {
        Refrigerator refrigerator = refrigeratorService.getRefrigerator(refrigeratorId);
        try {
            ShoppingList shoppingList = getShoppingListByRefrigeratorId(refrigeratorId);
            logger.info("Shopping list already exists for refrigerator id {}", refrigeratorId);
            return shoppingList.getId();
        } catch (ShoppingListNotFound e) {
            ShoppingList newShoppingList = new ShoppingList();
            newShoppingList.setRefrigerator(refrigerator);

            shoppingListRepository.save(newShoppingList);
            logger.info("Created shopping list with id {}", newShoppingList.getId());
            return newShoppingList.getId();
        }
    }

    /**
     * Getter for all groceries in the shopping list specified in the parameter
     * @param shoppingListId ID to the shopping list to retrieve groceries from
     * @param isRequested True if suggested groceries to the shopping list is wanted and false if not
     * @return All groceries from the shopping list with the shopping list id specified in the parameter
     * @exception NoGroceriesFound Could not find any groceries
     */
    public List<ShoppingListElementDTO> getGroceries(long shoppingListId, boolean isRequested) throws NoGroceriesFound {
        List<GroceryShoppingList> groceries = groceryShoppingListRepository.findByShoppingListId(shoppingListId, isRequested);
        if (groceries.isEmpty()) {
            logger.info("Received no groceries from the database");
            throw new NoGroceriesFound("Could not find any groceries for shopping list id " + shoppingListId);

        }
        return groceries.stream().map(ShoppingListElementDTO::new)
                .sorted(new ShoppingListElementDTOComparator()).collect(Collectors.toList());
    }

    /**
     * Getter for all groceries in the shopping list and the category specified in the parameter
     * @param shoppingListId ID to the shopping list to retrieve groceries from
     * @param categoryId ID to the category to retrieve groceries from
     * @param isRequested True if suggested groceries to the shopping list is wanted and false if not
     * @return All groceries from the shopping list with the shopping list id and category id specified in the parameter
     * @exception NoGroceriesFound Could not find any groceries
     */
    public List<ShoppingListElementDTO> getGroceries(long shoppingListId, long categoryId, boolean isRequested) throws NoGroceriesFound {
        List<GroceryShoppingList> groceries = groceryShoppingListRepository.findByShoppingListIdAndCategoryId(shoppingListId, categoryId, isRequested);
        if (groceries.isEmpty()) {
            logger.info("Received no groceries from the database");
            throw new NoGroceriesFound("Could not find any groceries for shopping list id " + shoppingListId);
        }

        return groceries.stream().map(ShoppingListElementDTO::new)
                .sorted(new ShoppingListElementDTOComparator()).collect(Collectors.toList());
    }

    /**
     * Getter for all groceries from the suggested refrigerator shopping list belonging to the shopping list specified in the parameter
     * @param shoppingListId ID to the shopping list related to the refrigerator shopping list
     * @return All groceries from the shopping list with the shopping list id specified in the parameter
     * @exception NoGroceriesFound Could not find any groceries
     */
    public List<ShoppingCartElementDTO> getGroceriesFromRefrigeratorShoppingList(long shoppingListId) throws NoGroceriesFound {
        List<RefrigeratorShoppingList> groceries = refrigeratorShoppingListRepository.findByShoppingListId(shoppingListId);
        if (groceries.isEmpty()) {
            logger.info("Received no groceries from the database");
            throw new NoGroceriesFound("Could not find any groceries for shopping list id " + shoppingListId);

        }
        return groceries.stream().map(ShoppingCartElementDTO::new)
                .sorted(new ShoppingCartElementDTOComparator()).collect(Collectors.toList());
    }

    /**
     * Getter for all categories of groceries in shopping list
     * @param shoppingListId ID to the shopping list to retrieve categories from
     * @return All categories from the shopping list with the shopping list id specified in the parameter
     * @exception CategoryNotFound If no categories found
     */
    public List<Category> getCategories(long shoppingListId) throws CategoryNotFound {
        List<Category> categories = groceryShoppingListRepository.findCategoryByShoppingListId(shoppingListId);
        if (categories.isEmpty()) {
            logger.info("Received no categories from shopping list with id {}", shoppingListId);
            throw new CategoryNotFound("Could not find any categories from shopping list with id " + shoppingListId);
        }
        categories.sort(new CategoryComparator());
        return categories;
    }

    /**
     * Edit the grocery in shopping list. It is only possible for a superuser of the refrigerator assosiated with the shopping list
     * to edit the grocery. When a superuser is editing a suggested grocery is requested set to false
     * @param groceryShoppingListId ID to the grocery item on a shopping list
     * @param quantity Amount of groceries
     * @param httpRequest http request
     * @return The item on the grocery shopping list
     * @throws NoGroceriesFound if no grocery is found for the groceryShoppingListId
     * @throws UserNotFoundException if no user is found
     * @throws UnauthorizedException if the user is not a superuser in the refrigerator
     * @throws SaveException if it was not possible to save the modifications to the database
     */
    public GroceryShoppingList editGrocery(long groceryShoppingListId, int quantity, HttpServletRequest httpRequest) throws NoGroceriesFound, UserNotFoundException, UnauthorizedException, SaveException {
        GroceryShoppingList groceryShoppingList = groceryShoppingListRepository.findById(groceryShoppingListId)
                .orElseThrow(() -> new NoGroceriesFound("Could not find a grocery with the given i"));
        FridgeRole fridgeRole = groceryService.getFridgeRole(groceryShoppingList.getShoppingList().getRefrigerator(), httpRequest);

        if (fridgeRole == FridgeRole.SUPERUSER) {
            groceryShoppingList.setQuantity(quantity);
            groceryShoppingList.setRequest(false);
            groceryShoppingListRepository.save(groceryShoppingList);
            return groceryShoppingList;
        }
        throw new SaveException("Failed to add a edit the grocery item with id " + groceryShoppingListId);
    }

    /**
     * Edit the grocery in refrigerator shopping list . It is only possible for a superuser of the refrigerator
     * assosiated with the shopping list to edit the grocery. The grocery is transferred to the shopping list when a
     * superuser is editing a grocery suggested from the refrigerator
     * @param groceryRefrigeratorShoppingListId ID to the grocery item on a refrigerator shopping list
     * @param quantity Amount of groceries
     * @param httpRequest http request
     * @return The item on the grocery shopping list
     * @throws NoGroceriesFound if no grocery is found for the groceryShoppingListId
     * @throws UserNotFoundException if no user is found
     * @throws UnauthorizedException if the user is not a superuser in the refrigerator
     * @throws SaveException if it was not possible to save the modifications to the database
     */
    public GroceryShoppingList editRefrigeratorGrocery(long groceryRefrigeratorShoppingListId, int quantity, HttpServletRequest httpRequest) throws NoGroceriesFound, UserNotFoundException, UnauthorizedException, SaveException {
        RefrigeratorShoppingList refrigeratorShoppingListItem = refrigeratorShoppingListRepository.findById(groceryRefrigeratorShoppingListId)
                .orElseThrow(() -> new NoGroceriesFound("Could not find a grocery with the given i"));
        FridgeRole fridgeRole = groceryService.getFridgeRole(refrigeratorShoppingListItem.getShoppingList().getRefrigerator(), httpRequest);

        if (fridgeRole == FridgeRole.SUPERUSER) {
            GroceryShoppingList groceryShoppingList = GroceryShoppingList.builder()
                            .shoppingList(refrigeratorShoppingListItem.getShoppingList())
                            .grocery(refrigeratorShoppingListItem.getGrocery())
                            .quantity(quantity)
                            .isRequest(false)
                            .build();
            groceryShoppingListRepository.save(groceryShoppingList);
            deleteRefrigeratorGrocery(groceryRefrigeratorShoppingListId, httpRequest);
            return groceryShoppingList;
        }
        throw new SaveException("Failed to add a edit the refrigerator grocery item");
    }

    /**
     * Adds predefined grocery to the shopping list for groceries
     * @param groceryId ID to the grocery to save to the shopping list
     * @param shoppingListId ID to the shopping list to save to the grocery to
     * @param quantity Quantity of groceries to save in the shopping list
     * @param request The http request
     * @exception ShoppingListNotFound If the shopping list is not found
     * @exception UserNotFoundException If the user is not found
     */
    public void saveGrocery(long groceryId, long shoppingListId, int quantity, HttpServletRequest request) throws ShoppingListNotFound, UserNotFoundException, UnauthorizedException, SaveException {
        ShoppingList shoppingList = getShoppingListById(shoppingListId);
        Grocery grocery = groceryService.getGroceryById(groceryId);

        boolean isRequested = groceryService.getFridgeRole(shoppingList.getRefrigerator(), request) != FridgeRole.SUPERUSER;

        GroceryShoppingList groceryShoppingList = GroceryShoppingList.builder()
                .grocery(grocery)
                .shoppingList(shoppingList)
                .quantity(quantity)
                .isRequest(isRequested).build();

        try {
            groceryShoppingListRepository.save(groceryShoppingList);
        } catch (Exception e) {
            logger.info("Error when saving grocery");
            throw new SaveException("Could not save the grocery");
        }
    }

    /**
     * Adds predefined grocery to the shopping list for suggestions to refrigerator
     * @param groceryId ID to the grocery to save to the shopping list
     * @param refrigeratorId ID to the shopping list to save to the grocery to
     * @exception ShoppingListNotFound If the shopping list is not found
     * @exception UserNotFoundException If the user is not found
     */
    public void saveGroceryToSuggestionForRefrigerator(long groceryId, long refrigeratorId) throws ShoppingListNotFound, SaveException {
        ShoppingList shoppingList = getShoppingListByRefrigeratorId(refrigeratorId);
        Optional<RefrigeratorShoppingList> refrigeratorShoppingListOptional = refrigeratorShoppingListRepository
                .findByGroceryIdAndShoppingListId(groceryId, shoppingList.getId());
        RefrigeratorShoppingList refrigeratorShoppingList;

        if (refrigeratorShoppingListOptional.isPresent()) {
            logger.info("Grocery item exist already in database. Increment quantity with one");
            refrigeratorShoppingListOptional.get().incrementQuantity();
            refrigeratorShoppingList = refrigeratorShoppingListOptional.get();
        } else {
            logger.info("Grocery item does not exit. Saving the grocery to the database");
            Grocery grocery = groceryService.getGroceryById(groceryId);

             refrigeratorShoppingList = RefrigeratorShoppingList.builder()
                    .grocery(grocery)
                    .shoppingList(shoppingList)
                    .quantity(1) //one grocery in the refrigerator is always one quantity
                    .build();
        }

        try {
            refrigeratorShoppingListRepository.save(refrigeratorShoppingList);
        } catch (Exception e) {
            logger.info("Error when saving grocery");
            throw new SaveException("Could not save the grocery");
        }
    }

    /*
    //todo: method to save a custom grocery
    public Optional<GroceryShoppingList> saveGrocery(SaveGroceryRequest groceryRequest, HttpServletRequest httpRequest) {
        String eMail = groceryService.extractEmail(httpRequest);
        logger.info("Saving grocery: {} to shopping list with id {}", groceryRequest.getName(), groceryRequest.getForeignKey());

        Optional<ShoppingList> shoppingList = shoppingListRepository.findById(groceryRequest.getForeignKey());
        if (shoppingList.isEmpty()) {
            logger.info("Could not find a shopping list with id {}", groceryRequest.getForeignKey());
            return Optional.empty();
        }

        logger.info("Found shopping list for shopping list id {}", shoppingList.get().getId());

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

        boolean isRequested = !isSuperUser(eMail, shoppingList.get().getId());

        GroceryShoppingList groceryShoppingList = new GroceryShoppingList();
        groceryShoppingList.setGrocery(grocery);
        groceryShoppingList.setShoppingList(shoppingList.get());
        groceryShoppingList.setQuantity(groceryRequest.getQuantity());
        groceryShoppingList.setRequest(isRequested);

        logger.info("Saved new grocery to the grocery list");

        return Optional.of(groceryShoppingListRepository.save(groceryShoppingList));
    }

     */

    /**
     * Deletes a grocery from the shopping list
     * @param groceryListId ID to the grocery to delete
     * @param httpRequest http request
     * @throws UnauthorizedException If not authorized
     * @throws NoGroceriesFound If the grocery item not is found in the shopping list
     * @throws UserNotFoundException If the user is not found
     */
    public void deleteGrocery(long groceryListId, HttpServletRequest httpRequest) throws UnauthorizedException, NoGroceriesFound, UserNotFoundException {
        GroceryShoppingList groceryShoppingList = groceryShoppingListRepository.findById(groceryListId)
                .orElseThrow(() -> new NoGroceriesFound("Could not find grocery item"));
        FridgeRole fridgeRole = groceryService.getFridgeRole(groceryShoppingList.getShoppingList().getRefrigerator(), httpRequest);

        if (fridgeRole == FridgeRole.SUPERUSER) {
            groceryShoppingListRepository.deleteById(groceryListId);
        } else {
            throw new UnauthorizedException("The user is not authorized to delete a grocery list item");
        }
    }

    /**
     * Deletes a grocery from the refrigerator shopping list
     * @param refrigeratorShoppingListId ID to the grocery to delete
     * @param httpRequest http request
     * @throws UnauthorizedException If not authorized
     * @throws NoGroceriesFound If the grocery item not is found in the shopping list
     * @throws UserNotFoundException If the user is not found
     */
    public void deleteRefrigeratorGrocery(long refrigeratorShoppingListId, HttpServletRequest httpRequest) throws UnauthorizedException, NoGroceriesFound, UserNotFoundException {
        RefrigeratorShoppingList refrigeratorShoppingListItem = refrigeratorShoppingListRepository.findById(refrigeratorShoppingListId)
                .orElseThrow(() -> new NoGroceriesFound("Could not find grocery item"));
        FridgeRole fridgeRole = groceryService.getFridgeRole(refrigeratorShoppingListItem.getShoppingList().getRefrigerator(), httpRequest);

        if (fridgeRole == FridgeRole.SUPERUSER) {
            refrigeratorShoppingListRepository.deleteById(refrigeratorShoppingListId);
        } else {
            throw new UnauthorizedException("The user is not authorized to delete a grocery list item");
        }
    }

    /**
     * Transfer one grocery from shopping list to shopping cart
     * @param groceryListId ID to the grocery in shopping list to transfer to the shopping cart
     * @param httpRequest http request
     * @throws UnauthorizedException If not authorized
     * @throws NoGroceriesFound If the grocery item not is found in the shopping list
     * @throws UserNotFoundException If the user is not found
     */
    public void transferGroceryToCart(long groceryListId, HttpServletRequest httpRequest) throws UnauthorizedException, NoGroceriesFound, UserNotFoundException {
        GroceryShoppingList groceryShoppingList = groceryShoppingListRepository.findById(groceryListId)
                .orElseThrow(() -> new NoGroceriesFound("Could not find grocery item"));
        FridgeRole fridgeRole = groceryService.getFridgeRole(groceryShoppingList.getShoppingList().getRefrigerator(), httpRequest);

        if (fridgeRole == FridgeRole.SUPERUSER) {
            SaveGroceryRequest saveGroceryRequest = new SaveGroceryRequest(groceryShoppingList);
            groceryShoppingListRepository.deleteById(groceryListId);
            logger.info("The grocery is deleted from shopping list");
            shoppingCartService.saveGrocery(saveGroceryRequest, httpRequest);
            logger.info("The grocery is saved in shopping cart");
        } else {
            throw new UnauthorizedException("The user is not authorized to delete a grocery list item");
        }
    }

    /**
     * Transfer one grocery from refrigerator shopping list to shopping cart
     * @param refrigeratorShoppingListId ID to the grocery in refrigerator shopping list to transfer to the shopping list
     * @param httpRequest http request
     * @throws UnauthorizedException If not authorized
     * @throws NoGroceriesFound If the grocery item not is found in the shopping list
     * @throws UserNotFoundException If the user is not found
     */
    public void transferRefrigeratorGroceryToCart(long refrigeratorShoppingListId, HttpServletRequest httpRequest) throws UnauthorizedException, NoGroceriesFound, UserNotFoundException {
        RefrigeratorShoppingList refrigeratorShoppingListItem = refrigeratorShoppingListRepository.findById(refrigeratorShoppingListId)
                .orElseThrow(() -> new NoGroceriesFound("Could not find grocery item"));
        FridgeRole fridgeRole = groceryService.getFridgeRole(refrigeratorShoppingListItem.getShoppingList().getRefrigerator(), httpRequest);

        if (fridgeRole == FridgeRole.SUPERUSER) {
            SaveGroceryRequest saveGroceryRequest = new SaveGroceryRequest(refrigeratorShoppingListItem);
            refrigeratorShoppingListRepository.deleteById(refrigeratorShoppingListId);
            logger.info("The grocery is deleted from shopping list");
            shoppingCartService.saveGrocery(saveGroceryRequest, httpRequest);
            logger.info("The grocery is saved in shopping cart");
        } else {
            throw new UnauthorizedException("The user is not authorized to delete a grocery list item");
        }
    }
}
