package ntnu.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.*;
import ntnu.idatt2106.backend.model.dto.CreateRefrigeratorGroceryDTO;
import ntnu.idatt2106.backend.model.dto.RefrigeratorGroceryDTO;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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

    @Operation(summary = "Get all groceries by refrigerator id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of groceries fetched successfully",
                    content = @Content(schema = @Schema(implementation = RefrigeratorGroceryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Refrigerator not found"),
            @ApiResponse(responseCode = "401", description = "User is not authorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/create/{shoppingListId}")
    public ResponseEntity<Long> createShoppingCartId(@PathVariable(name = "shoppingListId") long shoppingListId) throws ShoppingListNotFound {
        logger.info("Received request to create shopping cart for shopping list with id {}", shoppingListId);
        long shoppingCartId = shoppingCartService.createShoppingCart(shoppingListId);

        logger.info("Returning shopping cart id {}", shoppingCartId);
        return new ResponseEntity<>(shoppingCartId, HttpStatus.OK);
    }

    @Operation(summary = "Get all groceries from shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of groceries fetched successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ShoppingCartElementDTO.class)))),
            @ApiResponse(responseCode = "404", description = "No groceries found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/groceries/{shoppingCartId}")
    public ResponseEntity<List<ShoppingCartElementDTO>> getGroceriesFromShoppingCart(@PathVariable(name="shoppingCartId") long shoppingCartId) throws NullPointerException, NoGroceriesFound {
        logger.info("Received request to get groceries from shopping cart with id {}", shoppingCartId);
        List<ShoppingCartElementDTO> groceries = shoppingCartService.getGroceries(shoppingCartId);

        logger.info("Returns groceries and status OK");
        return new ResponseEntity<>(groceries, HttpStatus.OK);
    }

    @Operation(summary = "Save a grocery to a shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grocery added successfully",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "User is not authorized"),
            @ApiResponse(responseCode = "404", description = "Shopping cart not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/add-grocery")
    public ResponseEntity<SuccessResponse> saveGroceryToShoppingCart(@RequestBody SaveGroceryRequest groceryRequest, HttpServletRequest request) throws UnauthorizedException, ShoppingCartNotFound, UserNotFoundException, SaveException, NoSuchElementException {
        logger.info("Received request to save grocery with id {} to shopping cart with id {}", groceryRequest.getGroceryId(), groceryRequest.getForeignKey());
        shoppingCartService.saveGrocery(groceryRequest, request);
        return new ResponseEntity<>(new SuccessResponse("The grocery was added successfully", HttpStatus.OK.value()), HttpStatus.OK);
    }

    @Operation(summary = "Transfer grocery from shopping cart to shopping list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grocery transferred successfully", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "User is not authorized"),
            @ApiResponse(responseCode = "404", description = "Grocery or shopping cart not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/transfer-shoppingList/{shoppingCartItemId}")
    @Transactional(propagation =  Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResponseEntity<Boolean> transferToShoppingList(@PathVariable(name = "shoppingCartItemId") long shoppingCartItemId,
                                                          HttpServletRequest httpRequest) throws NoGroceriesFound, UserNotFoundException, SaveException, UnauthorizedException, RefrigeratorNotFoundException, ShoppingListNotFound, NoSuchElementException {
        logger.info("Received request to transfer grocery from shopping cart to shopping list");
        GroceryShoppingCart deletedGroceryItem = shoppingCartService.deleteGrocery(shoppingCartItemId, httpRequest);
        shoppingListService.saveGrocery(new SaveGroceryRequest(deletedGroceryItem), httpRequest);
        logger.info("Returns transferStatus and status OK");
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @Operation(summary = "Transfer grocery from shopping cart to refrigerator")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grocery transferred successfully", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "User is not authorized"),
            @ApiResponse(responseCode = "404", description = "Grocery or shopping cart not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/transfer-refrigerator/{shoppingCartItemId}")
    public ResponseEntity<Boolean> transferToRefrigerator(@PathVariable(name = "shoppingCartItemId") long shoppingCartItemId, @RequestBody CreateRefrigeratorGroceryDTO dto,
                                                             HttpServletRequest httpRequest) throws NoGroceriesFound, UserNotFoundException, SaveException, UnauthorizedException, RefrigeratorNotFoundException {
        logger.info("Received request to transfer grocery to refrigerator");
        shoppingCartService.transferGroceryToRefrigerator(shoppingCartItemId, httpRequest, dto);//throws error if the transfer was unsuccessful

        logger.info("Returns transferStatus and status OK");
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @Operation(summary = "Transfer all groceries to refrigerator")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All groceries transferred successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "User is not authorized"),
            @ApiResponse(responseCode = "404", description = "No groceries found in the shopping cart"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("all/transfer-refrigerator/")
    public ResponseEntity<Boolean> transferAllToRefrigerator(@RequestBody SaveGroceryRequest[] request, HttpServletRequest httpRequest) throws UserNotFoundException, NoGroceriesFound, SaveException, UnauthorizedException, RefrigeratorNotFoundException {
        logger.info("Received request to transfer groceries to refrigerator");
        shoppingCartService.transferAllGroceriesToRefrigerator(request, httpRequest);

        logger.info("Returns transferStatus and status OK");
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
