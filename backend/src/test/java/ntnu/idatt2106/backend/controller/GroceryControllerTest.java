package ntnu.idatt2106.backend.controller;

import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import ntnu.idatt2106.backend.exceptions.NotificationException;
import ntnu.idatt2106.backend.exceptions.RefrigeratorNotFoundException;
import ntnu.idatt2106.backend.exceptions.UnauthorizedException;
import ntnu.idatt2106.backend.exceptions.UserNotFoundException;
import ntnu.idatt2106.backend.model.Grocery;
import ntnu.idatt2106.backend.model.dto.RefrigeratorGroceryDTO;
import ntnu.idatt2106.backend.model.dto.response.ErrorResponse;
import ntnu.idatt2106.backend.model.dto.response.SuccessResponse;
import ntnu.idatt2106.backend.service.CookieService;
import ntnu.idatt2106.backend.service.GroceryService;
import ntnu.idatt2106.backend.service.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

class GroceryControllerTest {

    @Mock
    private GroceryService groceryService;

    @Mock
    private CookieService cookieService;

    @Mock
    private JwtService jwtService;

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
    void removeRefrigeratorGrocery_validInput_removesGrocery() throws UserNotFoundException, UnauthorizedException, EntityNotFoundException, NotificationException {
        // Arrange
        long refrigeratorGroceryId = 1L;

        // Act
        ResponseEntity<SuccessResponse> responseEntity = groceryController.removeRefrigeratorGrocery(refrigeratorGroceryId, httpServletRequest);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals("Grocery removed successfully", responseEntity.getBody().getMessage());
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void getAllGroceries_validInput_returnsListOfGroceries() {
        // Arrange
        List<Grocery> expectedList = new ArrayList<>();
        Grocery grocery = new Grocery();
        grocery.setName("Milk");
        grocery.setId(1);
        Grocery grocery1 = new Grocery();
        grocery1.setName("Eggs");
        grocery1.setId(2);
        expectedList.add(grocery);
        expectedList.add(grocery1);
        when(groceryService.getAllGroceries()).thenReturn(expectedList);
        MockHttpServletRequest request = new MockHttpServletRequest();
        String token = "valid_token";
        Mockito.when(cookieService.extractTokenFromCookie(request)).thenReturn(token);
        Mockito.when(jwtService.extractClaim(token, Claims::getSubject)).thenReturn("test");

        // Act
        ResponseEntity<?> responseEntity = groceryController.getAllGroceries(request);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(expectedList, responseEntity.getBody());
    }

    @Test
    void getAllGroceries_emptyList_returnsNoContent() {
        // Arrange
        List<Grocery> expectedList = new ArrayList<>();
        when(groceryService.getAllGroceries()).thenReturn(expectedList);
        MockHttpServletRequest request = new MockHttpServletRequest();
        String token = "valid_token";
        Mockito.when(cookieService.extractTokenFromCookie(request)).thenReturn(token);
        Mockito.when(jwtService.extractClaim(token, Claims::getSubject)).thenReturn("test");

        // Act
        ResponseEntity<?> responseEntity = groceryController.getAllGroceries(request);

        // Assert
        Assertions.assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        Assertions.assertEquals(new ErrorResponse("No groceries found"), responseEntity.getBody());
    }

    @Test
    void getAllGroceries_unauthorized_returnsUnauthorized() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();

        // Act
        ResponseEntity<?> responseEntity = groceryController.getAllGroceries(request);

        // Assert
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        Assertions.assertEquals(new ErrorResponse("Unauthorized"), responseEntity.getBody());
    }
}
