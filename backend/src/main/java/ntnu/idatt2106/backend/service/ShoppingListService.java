package ntnu.idatt2106.backend.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.UnauthorizedException;
import ntnu.idatt2106.backend.model.*;
import ntnu.idatt2106.backend.model.dto.ShoppingListElementDTO;
import ntnu.idatt2106.backend.model.enums.Role;
import ntnu.idatt2106.backend.model.requests.EditGroceryRequest;
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
    private final RefrigeratorRepository refrigeratorRepository;
    private final RefrigeratorUserRepository refrigeratorUserRepository;
    private final GroceryShoppingListRepository groceryShoppingListRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final GroceryRepository groceryRepository;
    private final UserRepository userRepository;

    private final SessionStorageService sessionStorageService;
    private final JwtService jwtService;

    private Logger logger = LoggerFactory.getLogger(ShoppingListService.class);

    public long createShoppingList(long refrigeratorId) {
        logger.info("Creating shopping list for refrigerator with id {}", refrigeratorId);
        Optional<Refrigerator> refrigerator = refrigeratorRepository.findById(refrigeratorId);

        if (refrigerator.isEmpty()) {
            logger.info("Could not find a matching refrigerator for refrigerator id {}", refrigeratorId);
            return -1;
        }

        logger.info("Found refrigerator for refrigerator id {}", refrigeratorId);

        Optional<ShoppingList> shoppingList = shoppingListRepository.findByRefrigeratorId(refrigeratorId);
        if (shoppingList.isPresent()) {
            logger.info("Shopping list already exists for refrigerator id {}", refrigeratorId);
            return shoppingList.get().getId();
        }

        ShoppingList newShoppingList = new ShoppingList();
        newShoppingList.setRefrigerator(refrigerator.get());

        shoppingListRepository.save(newShoppingList);
        logger.info("Created shopping list with id {}", newShoppingList.getId());
        return newShoppingList.getId();
    }

    public List<ShoppingListElementDTO> getGroceries(long shoppingListId) {
        logger.info("Retrieving groceries from the database");
        List<GroceryShoppingList> groceries = groceryShoppingListRepository.findByShoppingListId(shoppingListId);
        if (groceries.isEmpty()) {
            logger.info("Received no groceries from the database");
        }
        List<ShoppingListElementDTO> dtos = groceries.stream().map(ShoppingListElementDTO::new).collect(Collectors.toList());
        logger.info("Received groceries from the database");
        return dtos;
    }

    public List<Grocery> getRequestedGroceries(long shoppingListId) {
        logger.info("Retrieving suggested groceries from the database");
        List<Grocery> groceries = groceryShoppingListRepository.findRequestedGroceriesByShoppingListId(shoppingListId);
        if (groceries.isEmpty()) {
            logger.info("Received no groceries from the database");
        }
        logger.info("Received groceries from the database");
        return groceries;
    }

    public List<Grocery> getGroceries(long shoppingListId, long subCategoryId) {
        logger.info("Retrieving groceries from the database");
        List<Grocery> groceries = groceryShoppingListRepository.findByShoppingListIdAndSubCategoryId(shoppingListId, subCategoryId);
        if (groceries.isEmpty()) {
            logger.info("Received no groceries from the database");
        }
        logger.info("Received groceries from the database");
        return groceries;
    }

    public List<Category> getCategories(long shoppingListId) {
        logger.info("Retrieving categories from shopping list with id {}", shoppingListId);
        List<Category> categories = groceryShoppingListRepository.findCategoryByShoppingListId(shoppingListId);
        if (categories.isEmpty()) {
            logger.info("Received no categories from shopping list with id {}", shoppingListId);
        }
        logger.info("Received categories from the database for shopping list with id {}", shoppingListId);
        return categories;
    }

    public Optional<GroceryShoppingList> editGrocery(EditGroceryRequest groceryRequest, HttpServletRequest httpRequest) {
        String eMail = extractEmail(httpRequest);
        logger.info("Editing grocery with id: {} to shopping list with id {}", groceryRequest.getId(), groceryRequest.getShoppingListId());

        Optional<GroceryShoppingList> groceryShoppingList = groceryShoppingListRepository.findById(groceryRequest.getId());
        if (groceryShoppingList.isPresent()) {
            logger.info("Found grocery in the shopping list");
            boolean isRequested = !isSuperUser(eMail, groceryRequest.getShoppingListId());

            groceryShoppingList.get().setRequest(isRequested);
            groceryShoppingList.get().setQuantity(groceryRequest.getQuantity());

            logger.info("Edit grocery in the grocery list");
            return Optional.of(groceryShoppingListRepository.save(groceryShoppingList.get()));
        }
        logger.info("Could not find the grocery in the shopping list");
        return Optional.empty();
    }

    private String extractEmail(HttpServletRequest httpRequest) {
        String token = sessionStorageService.extractTokenFromAuthorizationHeader(httpRequest);
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

        logger.info("isUserSuper user {}", refrigeratorUser.get().getRole() == Role.SUPERUSER);
        return refrigeratorUser.get().getRole() == Role.SUPERUSER;
    }


    public Optional<GroceryShoppingList> saveGrocery(SaveGroceryRequest groceryRequest, HttpServletRequest httpRequest) {
        String eMail = extractEmail(httpRequest);
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

    public void deleteGrocery(long groceryListId, HttpServletRequest httpRequest) throws UnauthorizedException {
        String eMail = extractEmail(httpRequest);
        Optional<GroceryShoppingList> groceryShoppingList = groceryShoppingListRepository.findById(groceryListId);

        if (groceryShoppingList.isPresent() && isSuperUser(eMail, groceryShoppingList.get().getShoppingList().getId())) {
            groceryShoppingListRepository.deleteById(groceryListId);
        } else {
            throw new UnauthorizedException("The user is not authorized to delete a grocery list item");
        }
    }
}
