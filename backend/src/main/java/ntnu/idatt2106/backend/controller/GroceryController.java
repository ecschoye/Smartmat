package ntnu.idatt2106.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.RefrigeratorNotFoundException;
import ntnu.idatt2106.backend.exceptions.UnauthorizedException;
import ntnu.idatt2106.backend.exceptions.UserNotFoundException;
import ntnu.idatt2106.backend.model.dto.RefrigeratorGroceryDTO;
import ntnu.idatt2106.backend.service.GroceryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller regarding the groceries inside a refrigerator
 */
@RestController
@RequestMapping("/api/refrigerator/grocery")
@RequiredArgsConstructor
public class GroceryController {

    private final GroceryService groceryService;
    Logger logger = LoggerFactory.getLogger(GroceryController.class);

    @GetMapping("/{refrigeratorId}")
    public ResponseEntity<List<RefrigeratorGroceryDTO>> getGroceriesByRefrigerator(@Valid @PathVariable long refrigeratorId, HttpServletRequest httpServletRequest) throws UserNotFoundException, UnauthorizedException, RefrigeratorNotFoundException {
        logger.info("Received request for groceries by refrigerator with id: {}", refrigeratorId);
        return new ResponseEntity<>(groceryService.getGroceriesByRefrigerator(refrigeratorId, httpServletRequest), HttpStatus.OK);
    }


















}
