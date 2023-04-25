package ntnu.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.SaveException;
import ntnu.idatt2106.backend.exceptions.UnauthorizedException;
import ntnu.idatt2106.backend.model.Grocery;
import ntnu.idatt2106.backend.model.GroceryShoppingCart;
import ntnu.idatt2106.backend.model.dto.ShoppingListElementDTO;
import ntnu.idatt2106.backend.model.requests.SaveGroceryRequest;
import ntnu.idatt2106.backend.service.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/shopping-cart")
@RequiredArgsConstructor
@Tag(name = "ShoppingCart Controller", description = "Controller to handle the shopping cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);

    @PostMapping("/create/{shoppingListId}")
    public ResponseEntity<Long> createShoppingCartId(@PathVariable(name = "shoppingListId") long shoppingListId) throws SaveException {
        logger.info("Received request to create shopping cart for shopping list with id {}", shoppingListId);
        long shoppingCartId = shoppingCartService.createShoppingCart(shoppingListId);
        if (shoppingCartId == -1) {
            logger.info("Failed to create shopping cart for shopping list id {}", shoppingListId);
            throw new SaveException("Failed to create shopping cart for shopping list with id " + shoppingListId);
        }
        logger.info("Returning shopping cart id {}", shoppingCartId);
        return new ResponseEntity<>(shoppingCartId, HttpStatus.OK);
    }

    //todo: test the method underneath
    @GetMapping("/groceries/{shoppingCartId}")
    public ResponseEntity<List<ShoppingListElementDTO>> getGroceriesFromShoppingCart(@PathVariable(name="shoppingCartId") long shoppingCartId) throws NullPointerException {
        logger.info("Received request to get groceries from shopping cart with id {}", shoppingCartId);
        List<ShoppingListElementDTO> groceries = shoppingCartService.getGroceries(shoppingCartId);
        if (groceries.isEmpty()) {
            logger.info("Received no groceries. Return status NO_CONTENT");
            throw new NullPointerException("Received no groceries");
        }
        logger.info("Returns groceries and status OK");
        return new ResponseEntity<>(groceries, HttpStatus.OK);
    }

    //todo: endpoint add product to shopping cart
    @PostMapping("/add-grocery")
    public ResponseEntity<GroceryShoppingCart> saveGroceryToShoppingCart(@RequestBody SaveGroceryRequest groceryRequest, HttpServletRequest request) throws SaveException, UnauthorizedException {
        logger.info("Received request to save grocery {} to shopping cart with id {}", groceryRequest.getName(), groceryRequest.getForeignKey());
        Optional<GroceryShoppingCart> groceryListItem = shoppingCartService.saveGrocery(groceryRequest, request);
        if (groceryListItem.isEmpty()) {
            logger.info("No registered changes to grocery is saved");
            throw new SaveException("Failed to add a new grocery to shopping cart");
        }
        logger.info("Returns groceries and status OK");
        return new ResponseEntity<>(groceryListItem.get(), HttpStatus.OK);
    }

    //todo: endpoint remove a product to shopping cart
}
