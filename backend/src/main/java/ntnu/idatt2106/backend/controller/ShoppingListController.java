package ntnu.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.*;
import ntnu.idatt2106.backend.model.Category;
import ntnu.idatt2106.backend.model.Grocery;
import ntnu.idatt2106.backend.model.GroceryShoppingList;
import ntnu.idatt2106.backend.model.dto.ShoppingListElementDTO;
import ntnu.idatt2106.backend.model.dto.response.SuccessResponse;
import ntnu.idatt2106.backend.model.requests.EditGroceryRequest;
import ntnu.idatt2106.backend.model.requests.SaveGroceryListRequest;
import ntnu.idatt2106.backend.model.requests.SaveGroceryRequest;
import ntnu.idatt2106.backend.service.ShoppingListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/shopping-list")
@RequiredArgsConstructor
@Tag(name = "ShoppingList Controller", description = "Controller to handle the shopping list")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    Logger logger = LoggerFactory.getLogger(ShoppingListController.class);

    @PostMapping("/create/{refrigeratorId}")
    public ResponseEntity<Long> createShoppingList(@PathVariable(name = "refrigeratorId") long refrigeratorId) throws SaveException {
        logger.info("Received request to create shopping list for refrigerator with id {}", refrigeratorId);
        long shoppingListId = shoppingListService.createShoppingList(refrigeratorId);
        if (shoppingListId == -1) {
            logger.info("Failed to create shopping list for refrigerator id {}", refrigeratorId);
            throw new SaveException("Failed to create shopping list for refrigerator id " + refrigeratorId);
        }
        logger.info("Returning shopping list id {}", shoppingListId);
        return new ResponseEntity<>(shoppingListId, HttpStatus.OK);
    }

    @GetMapping("/groceries/{shoppingListId}")
    public ResponseEntity<List<ShoppingListElementDTO>> getGroceriesFromShoppingList(@PathVariable(name="shoppingListId") long shoppingListId) throws NullPointerException {
        logger.info("Received request to get groceries from shopping list with id {}", shoppingListId);
        List<ShoppingListElementDTO> groceries = shoppingListService.getGroceries(shoppingListId);
        if (groceries.isEmpty()) {
            logger.info("Received no groceries. Return status NO_CONTENT");
            throw new NullPointerException("Received no groceries");
        }
        logger.info("Returns groceries and status OK");
        return new ResponseEntity<>(groceries, HttpStatus.OK);
    }

    @GetMapping("/category/groceries/{shoppingListId}/{categoryId}")
    public ResponseEntity<List<ShoppingListElementDTO>> getGroceriesFromCategorizedShoppingList(@PathVariable(name="shoppingListId") long shoppingListId,
                                                                                 @PathVariable(name="categoryId") long categoryId) throws NullPointerException {
        logger.info("Received request to get groceries with category id {} from shopping list with id {}", categoryId, shoppingListId);
        List<ShoppingListElementDTO> groceries = shoppingListService.getGroceries(shoppingListId, categoryId);
        if (groceries.isEmpty()) {
            logger.info("Received no groceries with category id {}. Return status NO_CONTENT", categoryId);
            throw new NullPointerException("Received no groceries with given category");
        }
        logger.info("Returns groceries with category id {} and status OK", categoryId);
        return new ResponseEntity<>(groceries, HttpStatus.OK);
    }

    @GetMapping("/categories/{shoppingListId}")
    public ResponseEntity<List<Category>> getCategoriesFromShoppingList(@PathVariable(name="shoppingListId") long shoppingListId) throws NullPointerException {
        logger.info("Received request to get categories from shopping list with id {}", shoppingListId);
        List<Category> categories = shoppingListService.getCategories(shoppingListId);
        if (categories.isEmpty()) {
            logger.info("Received no categories. Return status NO_CONTENT");
            throw new NullPointerException("Received no categories");
        }
        logger.info("Returns categories and status OK");
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    //todo: edit format of response?
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

    //todo: edit format of response?
    @PostMapping("/edit-grocery")
    public ResponseEntity<GroceryShoppingList> editGroceryQuantity(@RequestBody EditGroceryRequest groceryRequest, HttpServletRequest httpRequest) throws SaveException{
        logger.info("Received request to edit grocery with id to {} in shopping list with id {}", groceryRequest.getId(), groceryRequest.getShoppingListId());
        Optional<GroceryShoppingList> grocery = shoppingListService.editGrocery(groceryRequest, httpRequest);
        if (grocery.isEmpty()) {
            logger.info("No registered changes to grocery");
            throw new SaveException("Failed to add a edit the grocery in the shopping list");
        }
        logger.info("Returns edited grocery and status OK");
        return new ResponseEntity<>(grocery.get(), HttpStatus.OK);
    }

    @DeleteMapping("/delete-grocery/{groceryListId}")
    public ResponseEntity<Boolean> removeGroceryFromShoppingList(@PathVariable(name="groceryListId") long groceryListId, HttpServletRequest httpRequest) throws UnauthorizedException {
        logger.info("Received request to delete grocery list item with id {}", groceryListId);
        boolean deleteStatus = false;
        try {
            shoppingListService.deleteGrocery(groceryListId, httpRequest);
            deleteStatus = true;
        } catch (UnauthorizedException e) {
            logger.info("Failed to delete grocery item with id {}", groceryListId);
            throw new UnauthorizedException(e.getMessage());
        }

        logger.info("Returns deleteStatus and status OK");
        return new ResponseEntity<>(deleteStatus, HttpStatus.OK);
    }

    @GetMapping("requested/groceries/{shoppingListId}")
    public ResponseEntity<List<ShoppingListElementDTO>> getRequestedGroceries(@PathVariable("shoppingListId") long shoppingListId) {
        logger.info("Received request to get groceries requested to the shopping list with id {}", shoppingListId);
        List<ShoppingListElementDTO> groceries = shoppingListService.getRequestedGroceries(shoppingListId);
        if (groceries.isEmpty()) {
            logger.info("Received no groceries. Return status NO_CONTENT");
            throw new NullPointerException("Received no groceries");
        }
        logger.info("Returns groceries and status OK");
        return new ResponseEntity<>(groceries, HttpStatus.OK);
    }

    @PostMapping("transfer-shopping-cart/{groceryShoppingListId}")
    public ResponseEntity<Boolean> transferToShoppingCart(@PathVariable("groceryShoppingListId") long groceryShoppingListId, HttpServletRequest httpRequest) throws UnauthorizedException, ShoppingCartNotFound, SubCategoryNotFound {
        logger.info("Received request to transfer grocery item with id {} in shopping list to shopping cart", groceryShoppingListId);
        boolean transferStatus = false;
        try {
            shoppingListService.transferGrocery(groceryShoppingListId, httpRequest);
            transferStatus = true;
        } catch (UnauthorizedException e) {
            logger.info("Failed to transfer grocery item with id {}", groceryShoppingListId);
            throw new UnauthorizedException(e.getMessage());
        } catch (ShoppingCartNotFound e) {
            logger.info("Could not find shopping cart");
            throw new ShoppingCartNotFound(e.getMessage());
        } catch (SubCategoryNotFound e) {
            logger.info("Could not find shopping list");
            throw new SubCategoryNotFound(e.getMessage());
        }

        logger.info("Returns transferStatus and status OK");
        return new ResponseEntity<>(transferStatus, HttpStatus.OK);
    }
}
