package ntnu.idatt2106.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.NoGroceriesFound;
import ntnu.idatt2106.backend.exceptions.UnauthorizedException;
import ntnu.idatt2106.backend.model.dto.GroceryDTO;
import ntnu.idatt2106.backend.service.GroceryService;
import ntnu.idatt2106.backend.service.SessionStorageService;
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
    private final SessionStorageService sessionStorageService;

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<GroceryDTO>> getAllGroceries(HttpServletRequest request) throws UnauthorizedException, NoGroceriesFound{
        String token = sessionStorageService.extractTokenFromAuthorizationHeader(request);
        if (token == null) {
            throw new UnauthorizedException("Unauthorized");
        }
        List<GroceryDTO> groceries = groceryService.getAllGroceries();

        if(groceries.isEmpty()){
            throw new NoGroceriesFound("No groceries found");
        }
        return new ResponseEntity<>(groceries, HttpStatus.OK);
    }
}
