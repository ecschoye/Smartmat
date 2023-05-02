package ntnu.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.*;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorGrocery;
import ntnu.idatt2106.backend.model.User;
import ntnu.idatt2106.backend.model.dto.GroceryDTO;
import ntnu.idatt2106.backend.model.dto.RefrigeratorGroceryDTO;
import ntnu.idatt2106.backend.model.dto.response.ErrorResponse;
import ntnu.idatt2106.backend.model.dto.response.SuccessResponse;
import ntnu.idatt2106.backend.model.requests.SaveGroceryListRequest;
import ntnu.idatt2106.backend.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ntnu.idatt2106.backend.exceptions.UnauthorizedException;
import ntnu.idatt2106.backend.service.GroceryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller regarding the groceries inside a refrigerator
 */
@RestController
@RequestMapping("/api/refrigerator/grocery")
@RequiredArgsConstructor
@Tag(name = "Grocery Controller", description = "Controller to handle the groceries in a refrigerator")
public class GroceryController {

    private final GroceryService groceryService;
    private final CookieService cookieService;
    private final UserService userService;
    private final JwtService jwtService;
    private final NotificationService notificationService;
    Logger logger = LoggerFactory.getLogger(GroceryController.class);

    @Operation(summary = "Get all groceries by refrigerator id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of groceries fetched successfully", content = @Content(schema = @Schema(implementation = RefrigeratorGroceryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Refrigerator not found"),
            @ApiResponse(responseCode = "401", description = "User is not authorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{refrigeratorId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<RefrigeratorGroceryDTO>> getGroceriesByRefrigerator(@Valid @PathVariable long refrigeratorId, HttpServletRequest httpServletRequest) throws UserNotFoundException, UnauthorizedException, RefrigeratorNotFoundException {
        logger.info("Received request for groceries by refrigerator with id: {}", refrigeratorId);
        return new ResponseEntity<>(groceryService.getGroceriesByRefrigerator(refrigeratorId, httpServletRequest), HttpStatus.OK);
    }

    @Operation(summary = "Remove a refrigerator grocery by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refrigerator grocery removed successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "404", description = "Refrigerator grocery not found"),
            @ApiResponse(responseCode = "401", description = "User is unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/remove/{refrigeratorGroceryId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SuccessResponse> removeRefrigeratorGrocery(@Valid @PathVariable long refrigeratorGroceryId, HttpServletRequest httpServletRequest) throws UserNotFoundException, UnauthorizedException, EntityNotFoundException, NotificationException {
        logger.info("Received request to remove refrigeratorGrocery with id: {}",refrigeratorGroceryId);
        RefrigeratorGrocery refrigeratorGrocery = groceryService.getRefrigeratorGroceryById(refrigeratorGroceryId);
        notificationService.deleteNotificationsByRefrigeratorGrocery(refrigeratorGrocery);
        groceryService.removeRefrigeratorGrocery(refrigeratorGroceryId, httpServletRequest);
        return new ResponseEntity<>(new SuccessResponse("Grocery removed successfully", HttpStatus.OK.value()), HttpStatus.OK);
    }

    @Operation(summary = "Get all groceries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of groceries fetched successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Grocery.class)))),
            @ApiResponse(responseCode = "204", description = "No content", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllGroceries(HttpServletRequest request){
        String jwt = cookieService.extractTokenFromCookie(request); // Extract the token from the cookie
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Unauthorized"));
        }
        List<Grocery> list = groceryService.getAllGroceries();

        if(list.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ErrorResponse("No groceries found"));
        }
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Create a new grocery")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grocery created successfully", content = @Content(schema = @Schema(implementation = GroceryDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/new/{refrigeratorId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createNewGrocery(HttpServletRequest request, @RequestBody GroceryDTO grocery, @Valid @PathVariable long refrigeratorId) throws Exception {
        try{
            String jwt = cookieService.extractTokenFromCookie(request);
            User user = userService.findByEmail(jwtService.extractUsername(jwt)); // Pass the JWT token instead of the request
            logger.info("Received request to create grocery from user: "+ user.getEmail() + ".");
            System.out.println(grocery);
            List<GroceryDTO> dtos = new ArrayList<>();
            dtos.add(grocery);
            groceryService.addGrocery(new SaveGroceryListRequest(refrigeratorId, dtos), request);
            return ResponseEntity.ok(grocery);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    @Operation(summary = "Get all grocery DTOs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of grocery DTOs fetched successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = GroceryDTO.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "204", description = "No groceries found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/allDTOs")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<GroceryDTO>> getAllGroceriesDTOs(HttpServletRequest request) throws UnauthorizedException, NoGroceriesFound{
        String token = cookieService.extractTokenFromCookie(request);
        if (token == null) {
            throw new UnauthorizedException("Unauthorized");
        }
        List<GroceryDTO> groceries = groceryService.getAllGroceriesDTO();

        if(groceries.isEmpty()){
            throw new NoGroceriesFound("No groceries found");
        }
        return new ResponseEntity<>(groceries, HttpStatus.OK);
    }

}
