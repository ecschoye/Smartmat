package ntnu.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.*;
import ntnu.idatt2106.backend.model.dto.response.SuccessResponse;
import ntnu.idatt2106.backend.model.dto.shoppingCartElement.ShoppingCartElementDTO;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingCart;
import ntnu.idatt2106.backend.model.requests.SaveGroceryRequest;
import ntnu.idatt2106.backend.service.ShoppingCartService;
import ntnu.idatt2106.backend.service.ShoppingListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shopping-cart")
@RequiredArgsConstructor
@Tag(name = "ShoppingCart Controller", description = "Controller to handle the shopping cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final ShoppingListService shoppingListService;
    private Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);

    @PostMapping("/create/{shoppingListId}")
    public ResponseEntity<Long> createShoppingCartId(@PathVariable(name = "shoppingListId") long shoppingListId) throws ShoppingListNotFound {
        logger.info("Received request to create shopping cart for shopping list with id {}", shoppingListId);
        long shoppingCartId = shoppingCartService.createShoppingCart(shoppingListId);

        logger.info("Returning shopping cart id {}", shoppingCartId);
        return new ResponseEntity<>(shoppingCartId, HttpStatus.OK);
    }

    @GetMapping("/groceries/{shoppingCartId}")
    public ResponseEntity<List<ShoppingCartElementDTO>> getGroceriesFromShoppingCart(@PathVariable(name="shoppingCartId") long shoppingCartId) throws NullPointerException, NoGroceriesFound {
        logger.info("Received request to get groceries from shopping cart with id {}", shoppingCartId);
        List<ShoppingCartElementDTO> groceries = shoppingCartService.getGroceries(shoppingCartId);

        logger.info("Returns groceries and status OK");
        return new ResponseEntity<>(groceries, HttpStatus.OK);
    }


    @PostMapping("/add-grocery")
    public ResponseEntity<SuccessResponse> saveGroceryToShoppingCart(@RequestBody SaveGroceryRequest groceryRequest, HttpServletRequest request) throws UnauthorizedException, ShoppingCartNotFound, UserNotFoundException, SaveException {
        logger.info("Received request to save grocery with id {} to shopping cart with id {}", groceryRequest.getGroceryId(), groceryRequest.getForeignKey());
        shoppingCartService.saveGrocery(groceryRequest, request);
        return new ResponseEntity<>(new SuccessResponse("The grocery was added successfully", HttpStatus.OK.value()), HttpStatus.OK);
    }

    @PostMapping("/transfer-shoppingList/{shoppingCartItemId}")
    public ResponseEntity<Boolean> transferToShoppingList(@PathVariable(name = "shoppingCartItemId") long shoppingCartItemId,
                                                          HttpServletRequest httpRequest) throws NoGroceriesFound, UserNotFoundException, SaveException, UnauthorizedException, RefrigeratorNotFoundException, ShoppingListNotFound {
        logger.info("Received request to transfer grocery from shopping cart to shopping list");
        GroceryShoppingCart deletedGroceryItem = shoppingCartService.deleteGrocery(shoppingCartItemId, httpRequest);
        shoppingListService.saveGrocery(new SaveGroceryRequest(deletedGroceryItem), httpRequest);
        logger.info("Returns transferStatus and status OK");
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PostMapping("/transfer-refrigerator/{shoppingCartItemId}")
    public ResponseEntity<Boolean> transferToRefrigerator(@PathVariable(name = "shoppingCartItemId") long shoppingCartItemId,
                                                             HttpServletRequest httpRequest) throws NoGroceriesFound, UserNotFoundException, SaveException, UnauthorizedException, RefrigeratorNotFoundException {
        logger.info("Received request to transfer grocery to refrigerator");
        shoppingCartService.transferGroceryToRefrigerator(shoppingCartItemId, httpRequest);//throws error if the transfer was unsuccessful

        logger.info("Returns transferStatus and status OK");
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PostMapping("all/transfer-refrigerator/{groceryIds}")
    public ResponseEntity<Boolean> transferAllToRefrigerator(@PathVariable(name = "groceryIds") long[] groceryIds,
                                                          HttpServletRequest httpRequest) throws UserNotFoundException, NoGroceriesFound, SaveException, UnauthorizedException, RefrigeratorNotFoundException {
        logger.info("Received request to transfer groceries to refrigerator");
        shoppingCartService.transferAllGroceriesToRefrigerator(groceryIds, httpRequest);

        logger.info("Returns transferStatus and status OK");
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
