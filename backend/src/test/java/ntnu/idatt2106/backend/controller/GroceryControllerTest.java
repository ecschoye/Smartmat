package ntnu.idatt2106.backend.controller;

import jakarta.persistence.EntityNotFoundException;
import ntnu.idatt2106.backend.exceptions.RefrigeratorNotFoundException;
import ntnu.idatt2106.backend.exceptions.UnauthorizedException;
import ntnu.idatt2106.backend.exceptions.UserNotFoundException;
import ntnu.idatt2106.backend.model.dto.RefrigeratorGroceryDTO;
import ntnu.idatt2106.backend.model.dto.response.SuccessResponse;
import ntnu.idatt2106.backend.service.GroceryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

class GroceryControllerTest {

    @Mock
    private GroceryService groceryService;

    @InjectMocks
    private GroceryController groceryController;

    private MockHttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        httpServletRequest = new MockHttpServletRequest();
    }

    @Test
    void getGroceriesByRefrigerator_validInput_returnsGroceries() throws UserNotFoundException, UnauthorizedException, RefrigeratorNotFoundException {
        // Arrange
        long refrigeratorId = 1L;
        RefrigeratorGroceryDTO groceryDTO = new RefrigeratorGroceryDTO();
        List<RefrigeratorGroceryDTO> expectedList = new ArrayList<>();
        expectedList.add(groceryDTO);
        when(groceryService.getGroceriesByRefrigerator(refrigeratorId, httpServletRequest)).thenReturn(expectedList);

        // Act
        ResponseEntity<List<RefrigeratorGroceryDTO>> responseEntity = groceryController.getGroceriesByRefrigerator(refrigeratorId, httpServletRequest);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(expectedList, responseEntity.getBody());
    }

    @Test
    void getGroceriesByRefrigerator_invalidRefrigeratorId_throwsRefrigeratorNotFoundException() throws UserNotFoundException, UnauthorizedException, RefrigeratorNotFoundException {
        // Arrange
        long refrigeratorId = 1L;
        when(groceryService.getGroceriesByRefrigerator(refrigeratorId, httpServletRequest)).thenThrow(new RefrigeratorNotFoundException("Refrigerator not found"));

        // Act and Assert
        Assertions.assertThrows(RefrigeratorNotFoundException.class, () -> {
            groceryController.getGroceriesByRefrigerator(refrigeratorId, httpServletRequest);
        });
    }

    @Test
    void removeRefrigeratorGrocery_validInput_removesGrocery() throws UserNotFoundException, UnauthorizedException, EntityNotFoundException {
        // Arrange
        long refrigeratorGroceryId = 1L;

        // Act
        ResponseEntity<SuccessResponse> responseEntity = groceryController.removeRefrigeratorGrocery(refrigeratorGroceryId, httpServletRequest);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals("Grocery removed successfully", responseEntity.getBody().getMessage());
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
