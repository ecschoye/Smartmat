package ntnu.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.*;
import ntnu.idatt2106.backend.model.category.Category;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingList;
import ntnu.idatt2106.backend.model.dto.shoppingListElement.ShoppingListElementDTO;
import ntnu.idatt2106.backend.model.dto.response.SuccessResponse;
import ntnu.idatt2106.backend.service.ShoppingListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shopping-list")
@RequiredArgsConstructor
@Tag(name = "ShoppingList Controller", description = "Controller to handle the shopping list")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    Logger logger = LoggerFactory.getLogger(ShoppingListController.class);

    @PostMapping("/create/{refrigeratorId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> createShoppingList(@PathVariable(name = "refrigeratorId") long refrigeratorId) throws RefrigeratorNotFoundException {
        logger.info("Received request to create shopping list for refrigerator with id {}", refrigeratorId);
        long shoppingListId = shoppingListService.createShoppingList(refrigeratorId);

        logger.info("Returning shopping list id {}", shoppingListId);
        return new ResponseEntity<>(shoppingListId, HttpStatus.OK);
    }

    @GetMapping("/groceries/{shoppingListId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ShoppingListElementDTO>> getGroceriesFromShoppingList(@PathVariable(name="shoppingListId") long shoppingListId) throws NoGroceriesFound {
        logger.info("Received request to get groceries from shopping list with id {}", shoppingListId);
        List<ShoppingListElementDTO> groceries = shoppingListService.getGroceries(shoppingListId, false);

        logger.info("Returns groceries and status OK");
        return new ResponseEntity<>(groceries, HttpStatus.OK);
    }

    @GetMapping("/category/groceries/{shoppingListId}/{categoryId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ShoppingListElementDTO>> getGroceriesFromCategorizedShoppingList(@PathVariable(name="shoppingListId") long shoppingListId,
                                                                                                @PathVariable(name="categoryId") long categoryId) throws NoGroceriesFound {
        logger.info("Received request to get groceries with category id {} from shopping list with id {}", categoryId, shoppingListId);
        List<ShoppingListElementDTO> groceries = shoppingListService.getGroceries(shoppingListId, categoryId, false);

        logger.info("Returns groceries with category id {} and status OK", categoryId);
        return new ResponseEntity<>(groceries, HttpStatus.OK);
    }

    @GetMapping("/categories/{shoppingListId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Category>> getCategoriesFromShoppingList(@PathVariable(name="shoppingListId") long shoppingListId) throws CategoryNotFound {
        logger.info("Received request to get categories from shopping list with id {}", shoppingListId);
        List<Category> categories = shoppingListService.getCategories(shoppingListId);

        logger.info("Returns categories and status OK");
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    //todo: method for creating custom groceries...
    /*
    @PostMapping("/add-grocery")
    public ResponseEntity<GroceryShoppingList> saveGroceryToShoppingList(@RequestBody SaveGroceryRequest groceryRequest, HttpServletRequest request) throws SaveException{
        logger.info("Received request to save grocery {} to shopping list with id {}", groceryRequest.getName(), groceryRequest.getForeignKey());
        Optional<GroceryShoppingList> groceryListItem = shoppingListService.saveGrocery(groceryRequest, request);
        if (groceryListItem.isEmpty()) {
            logger.info("No registered changes to grocery is saved");
            throw new SaveException("Failed to add a new grocery to shopping list");
        }
        logger.info("Returns groceries and status OK");
        return new ResponseEntity<>(groceryListItem.get(), HttpStatus.OK);
    }

     */

    @PostMapping("/add-grocery/{shoppingListId}/{groceryId}/{quantity}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SuccessResponse> saveGroceryToShoppingList(@PathVariable(name = "shoppingListId") long shoppingListId,
                                                                     @PathVariable(name = "groceryId") long groceryId,
                                                                     @PathVariable(name = "quantity") int quantity,
                                                                     HttpServletRequest request) throws SaveException{
        logger.info("Received request to save grocery with id {} to shopping list with id {}", groceryId, shoppingListId);
        try {
            shoppingListService.saveGrocery(groceryId, shoppingListId, quantity, request);
            return new ResponseEntity<>(new SuccessResponse("The grocery was added successfully", HttpStatus.OK.value()), HttpStatus.OK);
        } catch (Exception e) {
            logger.info("No registered changes to grocery is saved");
            throw new SaveException("Failed to add a new grocery to shopping list");
        }
    }

    @PostMapping("/edit-grocery/{groceryShoppingListId}/{quantity}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GroceryShoppingList> editGroceryQuantity(@PathVariable("groceryShoppingListId") long groceryShoppingListId,
                                                                   @PathVariable("quantity") int quantity,
                                                                   HttpServletRequest httpRequest) throws SaveException{
        logger.info("Received request to edit grocery item with id {}", groceryShoppingListId);
        try {
            GroceryShoppingList grocery = shoppingListService.editGrocery(groceryShoppingListId, quantity, httpRequest);
            return new ResponseEntity<>(grocery, HttpStatus.OK);
        } catch (Exception e) {
            logger.info("No registered changes to grocery");
            throw new SaveException("Failed to add a edit the grocery item with id " + groceryShoppingListId);
        }
    }

    @DeleteMapping("/delete-grocery/{groceryListId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> removeGroceryFromShoppingList(@PathVariable(name="groceryListId") long groceryListId, HttpServletRequest httpRequest) throws UnauthorizedException, NoGroceriesFound, UserNotFoundException {
        logger.info("Received request to delete grocery list item with id {}", groceryListId);
        shoppingListService.deleteGrocery(groceryListId, httpRequest); //throws error if the deletion was unsuccessful

        logger.info("Returns deleteStatus and status OK");
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("requested/groceries/{shoppingListId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ShoppingListElementDTO>> getRequestedGroceries(@PathVariable("shoppingListId") long shoppingListId) throws NoGroceriesFound {
        logger.info("Received request to get groceries requested to the shopping list with id {}", shoppingListId);
        List<ShoppingListElementDTO> groceries = shoppingListService.getGroceries(shoppingListId, true);

        logger.info("Returns groceries and status OK");
        return new ResponseEntity<>(groceries, HttpStatus.OK);
    }

    @GetMapping("/requested/groceries/{shoppingListId}/{categoryId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ShoppingListElementDTO>> getRequestedGroceriesFromCategorizedShoppingList(@PathVariable("shoppingListId") long shoppingListId,
                                                                                                         @PathVariable("categoryId") long categoryId) throws NoGroceriesFound {
        logger.info("Received request to get groceries requested to the shopping list with id {}", shoppingListId);
        List<ShoppingListElementDTO> groceries = shoppingListService.getGroceries(shoppingListId, categoryId, true);
        if (groceries.isEmpty()) {
            logger.info("Received no groceries. Return status NO_CONTENT");
            throw new NullPointerException("Received no groceries");
        }
        logger.info("Returns groceries and status OK");
        return new ResponseEntity<>(groceries, HttpStatus.OK);
    }


    @PostMapping("transfer-shopping-cart/{groceryShoppingListId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> transferToShoppingCart(@PathVariable("groceryShoppingListId") long groceryShoppingListId, HttpServletRequest httpRequest) throws UnauthorizedException, NoGroceriesFound, UserNotFoundException, ShoppingCartNotFound, SubCategoryNotFound {
        logger.info("Received request to transfer grocery item with id {} in shopping list to shopping cart", groceryShoppingListId);

        shoppingListService.transferGrocery(groceryShoppingListId, httpRequest); //throws error if the transfer was unsuccessful
        logger.info("Returns transferStatus and status OK");
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
