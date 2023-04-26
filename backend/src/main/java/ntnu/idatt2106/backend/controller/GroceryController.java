package ntnu.idatt2106.backend.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.RefrigeratorNotFoundException;
import ntnu.idatt2106.backend.exceptions.UnauthorizedException;
import ntnu.idatt2106.backend.exceptions.UserNotFoundException;
import ntnu.idatt2106.backend.model.Grocery;
import ntnu.idatt2106.backend.model.User;
import ntnu.idatt2106.backend.model.dto.RefrigeratorGroceryDTO;
import ntnu.idatt2106.backend.model.dto.response.ErrorResponse;
import ntnu.idatt2106.backend.model.dto.response.SuccessResponse;
import ntnu.idatt2106.backend.service.CookieService;
import ntnu.idatt2106.backend.service.GroceryService;
import ntnu.idatt2106.backend.service.JwtService;
import ntnu.idatt2106.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.NoGroceriesFound;
import ntnu.idatt2106.backend.exceptions.UnauthorizedException;
import ntnu.idatt2106.backend.model.dto.GroceryDTO;
import ntnu.idatt2106.backend.service.GroceryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller regarding the groceries inside a refrigerator
 */
@RestController
@RequestMapping("/api/refrigerator/grocery")
@RequiredArgsConstructor
public class GroceryController {

    private final GroceryService groceryService;
    private final CookieService cookieService;
    private final UserService userService;
    private final JwtService jwtService;
    Logger logger = LoggerFactory.getLogger(GroceryController.class);

    @GetMapping("/{refrigeratorId}")
    public ResponseEntity<List<RefrigeratorGroceryDTO>> getGroceriesByRefrigerator(@Valid @PathVariable long refrigeratorId, HttpServletRequest httpServletRequest) throws UserNotFoundException, UnauthorizedException, RefrigeratorNotFoundException {
        logger.info("Received request for groceries by refrigerator with id: {}", refrigeratorId);
        return new ResponseEntity<>(groceryService.getGroceriesByRefrigerator(refrigeratorId, httpServletRequest), HttpStatus.OK);
    }

    @DeleteMapping("/remove/{refrigeratorGroceryId}")
    public ResponseEntity<SuccessResponse> removeRefrigeratorGrocery(@Valid @PathVariable long refrigeratorGroceryId, HttpServletRequest httpServletRequest) throws UserNotFoundException, UnauthorizedException, EntityNotFoundException {
        logger.info("Received request to remove refrigeratorGrocery with id: {}",refrigeratorGroceryId);
        groceryService.removeRefrigeratorGrocery(refrigeratorGroceryId, httpServletRequest);
        return new ResponseEntity<>(new SuccessResponse("Grocery removed successfully", HttpStatus.OK.value()), HttpStatus.OK);
    }

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
