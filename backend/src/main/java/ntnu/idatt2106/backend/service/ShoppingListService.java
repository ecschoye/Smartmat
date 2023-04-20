package ntnu.idatt2106.backend.service;

import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.model.Grocery;
import ntnu.idatt2106.backend.model.GroceryShoppingList;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.ShoppingList;
import ntnu.idatt2106.backend.repository.GroceryListRepository;
import ntnu.idatt2106.backend.repository.RefrigeratorRepository;
import ntnu.idatt2106.backend.repository.ShoppingListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoppingListService {
    private final ShoppingListRepository shoppingListRepository;

    private final RefrigeratorRepository refrigeratorRepository;
    private final GroceryListRepository groceryListRepository;
    private Logger logger = LoggerFactory.getLogger(ShoppingListService.class);

    public int createShoppingList(int refrigeratorId) {
        logger.info("Creating shopping list for refrigerator with id {}", refrigeratorId);
        Optional<Refrigerator> refrigerator = refrigeratorRepository.findById(refrigeratorId);

        if (refrigerator.isPresent()) {
            logger.info("Found refrigerator for refrigerator id {}", refrigeratorId);
            ShoppingList shoppingList = new ShoppingList();
            shoppingList.setRefrigerator(refrigerator.get());

            shoppingListRepository.save(shoppingList);
            logger.info("Created shopping list with id {}", shoppingList.getId());
            return shoppingList.getId();
        }
        logger.info("Could not find a matching refrigerator for refrigerator id {}", refrigeratorId);
        return -1;
    }

    public List<Grocery> getGroceries(int shoppingListId) {
        logger.info("Retrieving groceries from the database");
        List<Grocery> groceries = groceryListRepository.findByShoppingListId(shoppingListId);
        if (groceries.isEmpty()) {
            logger.info("Received no groceries from the database");
        }
        logger.info("Received groceries from the database");
        return groceries;
    }

    public Optional<GroceryShoppingList> saveGrocery(Grocery grocery, int shoppingListId, boolean isRequested) {
        logger.info("Saving grocery: {} to shopping list with id {}", grocery, shoppingListId);
        Optional<ShoppingList> shoppingList = shoppingListRepository.findById(shoppingListId);
        if (shoppingList.isPresent()) {
            logger.info("Found shopping list for shopping list id {}", shoppingListId);

            GroceryShoppingList groceryShoppingList = new GroceryShoppingList();
            groceryShoppingList.setGrocery(grocery);
            groceryShoppingList.setShoppingList(shoppingList.get());
            groceryShoppingList.setRequest(isRequested);

            return Optional.ofNullable(groceryListRepository.save(groceryShoppingList));
        }
        logger.info("Could not find a shopping list with id {}", shoppingListId);
        return Optional.empty();
    }
}
