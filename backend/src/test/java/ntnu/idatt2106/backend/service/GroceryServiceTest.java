package ntnu.idatt2106.backend.service;

import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import ntnu.idatt2106.backend.exceptions.*;
import ntnu.idatt2106.backend.model.*;
import ntnu.idatt2106.backend.model.category.Category;
import ntnu.idatt2106.backend.model.dto.GroceryDTO;
import ntnu.idatt2106.backend.model.dto.RefrigeratorGroceryDTO;
import ntnu.idatt2106.backend.model.dto.UnitDTO;
import ntnu.idatt2106.backend.model.enums.FridgeRole;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorGrocery;
import ntnu.idatt2106.backend.model.requests.SaveGroceryListRequest;
import ntnu.idatt2106.backend.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GroceryServiceTest {

    @Mock
    private RefrigeratorGroceryRepository refrigeratorGroceryRepository;

    @Mock
    private GroceryRepository groceryRepository;

    @Mock
    private RefrigeratorRepository refrigeratorRepository;

    @Mock
    private SubCategoryRepository subCategoryRepository;

    @Mock
    private RefrigeratorService refrigeratorService;

    @Mock
    private CookieService cookieService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private GroceryService groceryService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private UnitRepository unitRepository;

    //Testdata
    private Grocery grocery;
    private GroceryDTO customGroceryDTO;
    private GroceryDTO existingGroceryDTO;
    private List<RefrigeratorGrocery> groceryList;
    private List<RefrigeratorGroceryDTO> groceryDTOList;
    private RefrigeratorGrocery refrigeratorGrocery;
    private Refrigerator refrigerator;
    private RefrigeratorUser refrigeratorUser;
    private User user;
    private Unit unit;
    private HttpServletRequest HttpRequest;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId("testUserId");
        user.setEmail("testuser@test.com");

        unit = Unit.builder().name("l")
                .weight(1000)
                .build();

        refrigerator = new Refrigerator();
        refrigerator.setId(1L);
        refrigerator.setName("Test Refrigerator");

        refrigeratorUser = new RefrigeratorUser();
        refrigeratorUser.setUser(user);
        refrigeratorUser.setRefrigerator(refrigerator);
        refrigeratorUser.setFridgeRole(FridgeRole.USER);

        groceryList = new ArrayList<>();
        groceryDTOList = new ArrayList<>();

        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(new Category());
        grocery = new Grocery();
        grocery.setId(1L);
        grocery.setName("Name");
        grocery.setDescription("Desc");
        grocery.setGroceryExpiryDays(2);
        grocery.setSubCategory(subCategory);
        refrigeratorGrocery = new RefrigeratorGrocery();
        refrigeratorGrocery.setGrocery(grocery);
        refrigeratorGrocery.setRefrigerator(refrigerator);
        Unit unit = new Unit(1, "dl", 100);
        refrigeratorGrocery.setUnit(unit);
        refrigeratorGrocery.setQuantity(1);
        customGroceryDTO = new GroceryDTO();
        customGroceryDTO.setName("Name");
        customGroceryDTO.setDescription("Desc");
        customGroceryDTO.setSubCategory(subCategory);
        customGroceryDTO.setGroceryExpiryDays(2);

        existingGroceryDTO = new GroceryDTO();
        existingGroceryDTO.setId(grocery.getId());

        groceryList.add(refrigeratorGrocery);
        groceryDTOList.add(new RefrigeratorGroceryDTO(refrigeratorGrocery));

        HttpRequest = mock(HttpServletRequest.class);
    }

    @Test
    @DisplayName("Test updateRefrigeratorGrocery succeeds")
    public void testUpdateRefrigeratorGrocerySucceeds() throws UserNotFoundException, UnauthorizedException, NotificationException, EntityNotFoundException, ParseException {
        // Setup


        refrigeratorGrocery.setPhysicalExpireDate(new SimpleDateFormat("dd/MM/yyyy").parse("10/05/2023"));

        RefrigeratorGroceryDTO refrigeratorGroceryDTO = new RefrigeratorGroceryDTO();
        refrigeratorGroceryDTO.setPhysicalExpireDate(new SimpleDateFormat("dd/MM/yyyy").parse("20/05/2023"));
        refrigeratorGroceryDTO.setId(1L);

        String expectedEmail = "testuser@test.com";
        String token = "valid_token";

        Mockito.when(refrigeratorGroceryRepository.findById(refrigeratorGroceryDTO.getId())).thenReturn(Optional.of(refrigeratorGrocery));
        Mockito.when(jwtService.extractClaim(token, Claims::getSubject)).thenReturn(expectedEmail);
        Mockito.when(cookieService.extractTokenFromCookie(HttpRequest)).thenReturn(token);
        Mockito.when(groceryService.getFridgeRole(refrigerator, HttpRequest)).thenReturn(FridgeRole.SUPERUSER);

        // Execute
        groceryService.updateRefrigeratorGrocery(mock(User.class), refrigeratorGroceryDTO, HttpRequest);

        // Verify
        verify(notificationService, times(1)).deleteNotificationsByRefrigeratorGrocery(refrigeratorGrocery);
        verify(refrigeratorGroceryRepository, times(1)).save(any(RefrigeratorGrocery.class));
    }

    @Test
    @DisplayName("Test updateRefrigeratorGrocery throws EntityNotFoundException when object not in repo")
    public void testUpdateRefrigeratorGroceryThrowsEntityNotFound() throws UserNotFoundException, UnauthorizedException, NotificationException, EntityNotFoundException, ParseException {
        // Setup

        refrigeratorGrocery.setPhysicalExpireDate(new SimpleDateFormat("dd/MM/yyyy").parse("10/05/2023"));

        RefrigeratorGroceryDTO refrigeratorGroceryDTO = new RefrigeratorGroceryDTO();
        refrigeratorGroceryDTO.setPhysicalExpireDate(new SimpleDateFormat("dd/MM/yyyy").parse("20/05/2023"));
        refrigeratorGroceryDTO.setId(3L);


        String expectedEmail = "testuser@test.com";
        String token = "valid_token";

        Mockito.when(refrigeratorGroceryRepository.findById(refrigeratorGroceryDTO.getId())).thenReturn(Optional.empty());
        Mockito.when(jwtService.extractClaim(token, Claims::getSubject)).thenReturn(expectedEmail);
        Mockito.when(cookieService.extractTokenFromCookie(HttpRequest)).thenReturn(token);
        Mockito.when(groceryService.getFridgeRole(refrigerator, HttpRequest)).thenReturn(FridgeRole.SUPERUSER);

        // Execute and Verify
        assertThrows(EntityNotFoundException.class, () -> {
            groceryService.updateRefrigeratorGrocery(mock(User.class), refrigeratorGroceryDTO, HttpRequest);
        });

        verify(notificationService, times(0)).deleteNotificationsByRefrigeratorGrocery(refrigeratorGrocery);
        verify(refrigeratorGroceryRepository, times(0)).save(any(RefrigeratorGrocery.class));
    }

    @Test
    @DisplayName("Test updateRefrigeratorGrocery throws UnauthorizedException if user is normal user")
    public void testUpdateRefrigeratorGroceryThrowsUnathorized() throws UserNotFoundException, UnauthorizedException, NotificationException, EntityNotFoundException, ParseException {
        // Setup

        refrigeratorGrocery.setPhysicalExpireDate(new SimpleDateFormat("dd/MM/yyyy").parse("10/05/2023"));

        RefrigeratorGroceryDTO refrigeratorGroceryDTO = new RefrigeratorGroceryDTO();
        refrigeratorGroceryDTO.setPhysicalExpireDate(new SimpleDateFormat("dd/MM/yyyy").parse("20/05/2023"));
        refrigeratorGroceryDTO.setId(1L);

        String expectedEmail = "testuser@test.com";
        String token = "valid_token";

        Mockito.when(refrigeratorGroceryRepository.findById(refrigeratorGroceryDTO.getId())).thenReturn(Optional.of(refrigeratorGrocery));
        Mockito.when(jwtService.extractClaim(token, Claims::getSubject)).thenReturn(expectedEmail);
        Mockito.when(cookieService.extractTokenFromCookie(HttpRequest)).thenReturn(token);
        Mockito.when(groceryService.getFridgeRole(refrigerator, HttpRequest)).thenReturn(FridgeRole.USER);

        // Execute and Verify
        assertThrows(UnauthorizedException.class, () -> {
            groceryService.updateRefrigeratorGrocery(mock(User.class), refrigeratorGroceryDTO, HttpRequest);
        });

        verify(notificationService, times(0)).deleteNotificationsByRefrigeratorGrocery(refrigeratorGrocery);
        verify(refrigeratorGroceryRepository, times(0)).save(any(RefrigeratorGrocery.class));
    }

    @Test
    @DisplayName("Test updateRefrigeratorGrocery does not call notificationService if date is same")
    public void testUpdateRefrigeratorGroceryDoesNotCallNotificationService() throws UserNotFoundException, UnauthorizedException, NotificationException, EntityNotFoundException, ParseException {
        // Setup
        refrigeratorGrocery.setPhysicalExpireDate(new SimpleDateFormat("dd/MM/yyyy").parse("10/05/2023"));

        RefrigeratorGroceryDTO refrigeratorGroceryDTO = new RefrigeratorGroceryDTO();
        refrigeratorGroceryDTO.setPhysicalExpireDate(new SimpleDateFormat("dd/MM/yyyy").parse("10/05/2023"));
        refrigeratorGroceryDTO.setId(1L);

        String expectedEmail = "testuser@test.com";
        String token = "valid_token";

        Mockito.when(refrigeratorGroceryRepository.findById(refrigeratorGroceryDTO.getId())).thenReturn(Optional.of(refrigeratorGrocery));
        Mockito.when(jwtService.extractClaim(token, Claims::getSubject)).thenReturn(expectedEmail);
        Mockito.when(cookieService.extractTokenFromCookie(HttpRequest)).thenReturn(token);
        Mockito.when(groceryService.getFridgeRole(refrigerator, HttpRequest)).thenReturn(FridgeRole.SUPERUSER);

        // Execute
        groceryService.updateRefrigeratorGrocery(mock(User.class), refrigeratorGroceryDTO, HttpRequest);

        // Verify

        verify(notificationService, times(0)).deleteNotificationsByRefrigeratorGrocery(refrigeratorGrocery);
        verify(refrigeratorGroceryRepository, times(1)).save(any(RefrigeratorGrocery.class));
    }


    @Test
    @DisplayName("Test getGroceriesByRefrigerator succeeds")
    public void testGetGroceriesByRefrigeratorSucceeds() throws RefrigeratorNotFoundException, UserNotFoundException, UnauthorizedException {
        // Setup
        long refrigeratorId = 1L;
        HttpServletRequest request = mock(HttpServletRequest.class);
        String expectedEmail = "testuser@test.com";
        String token = "valid_token";
        List<RefrigeratorGrocery> groceries = new ArrayList<>();
        groceries.add(refrigeratorGrocery);
        Mockito.when(cookieService.extractTokenFromCookie(request)).thenReturn(token);
        Mockito.when(jwtService.extractClaim(token, Claims::getSubject)).thenReturn(expectedEmail);
        Mockito.when(refrigeratorService.getRefrigerator(refrigeratorId)).thenReturn(refrigerator);
        Mockito.when(refrigeratorService.getFridgeRole(any(), any())).thenReturn(FridgeRole.USER);
        Mockito.when(refrigeratorGroceryRepository.findAllByRefrigeratorId(refrigerator.getId())).thenReturn(groceries);

        // Execute
        List<RefrigeratorGroceryDTO> result = groceryService.getGroceriesByRefrigerator(refrigeratorId, request);

        // Assert
        assertNotNull(result);
        assertEquals(groceries.size(), result.size());
    }

    @Test
    @DisplayName("Test getGroceriesByRefrigerator fails when refrigerator not found")
    public void testGetGroceriesByRefrigeratorFailsWhenRefrigeratorNotFound() throws RefrigeratorNotFoundException {
        // Setup
        long refrigeratorId = 1L;
        HttpServletRequest request = mock(HttpServletRequest.class);
        String expectedEmail = "testuser@test.com";
        String token = "valid_token";
        Mockito.when(cookieService.extractTokenFromCookie(request)).thenReturn(token);
        Mockito.when(jwtService.extractClaim(token, Claims::getSubject)).thenReturn(expectedEmail);
        Mockito.when(refrigeratorService.getRefrigerator(refrigeratorId)).thenThrow(new RefrigeratorNotFoundException("Refrigerator not found"));

        // Execute and Assert
        assertThrows(RefrigeratorNotFoundException.class, () -> groceryService.getGroceriesByRefrigerator(refrigeratorId, request));
    }

    @Test
    @DisplayName("Test getGroceriesByRefrigerator fails when user not a member of the refrigerator")
    public void testGetGroceriesByRefrigeratorFailsWhenUserNotMember() throws RefrigeratorNotFoundException, UserNotFoundException, UnauthorizedException {
        // Setup
        long refrigeratorId = 1L;
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = "valid_token";
        Mockito.when(cookieService.extractTokenFromCookie(request)).thenReturn(token);
        Mockito.when(jwtService.extractClaim(token, Claims::getSubject)).thenReturn(user.getUsername());
        Mockito.when(refrigeratorService.getRefrigerator(refrigeratorId)).thenReturn(refrigerator);
        Mockito.when(refrigeratorService.getFridgeRole(any(), any())).thenThrow(new UnauthorizedException("User not member"));

        // Execute and Assert
        assertThrows(UnauthorizedException.class, () -> groceryService.getGroceriesByRefrigerator(refrigeratorId, request));
    }

    @Test
    @DisplayName("Test getGroceriesByRefrigerator fails when user not found")
    public void testGetGroceriesByRefrigeratorFailsWhenUserNotFound() throws RefrigeratorNotFoundException, UserNotFoundException, UnauthorizedException {
        // Setup
        long refrigeratorId = 1L;
        HttpServletRequest request = mock(HttpServletRequest.class);
        String expectedEmail = "testuser@test.com";
        String token = "valid_token";
        Mockito.when(cookieService.extractTokenFromCookie(request)).thenReturn(token);
        Mockito.when(jwtService.extractClaim(token, Claims::getSubject)).thenReturn(expectedEmail);
        Mockito.when(refrigeratorService.getRefrigerator(refrigeratorId)).thenReturn(refrigerator);
        Mockito.when(refrigeratorService.getFridgeRole(any(),any())).thenThrow(new UserNotFoundException("User not found"));

        // Execute and Assert
        assertThrows(UserNotFoundException.class, () -> groceryService.getGroceriesByRefrigerator(refrigeratorId, request));
    }

    @Test
    public void removeRefrigeratorGrocery_SuccessfulRemoval() throws UserNotFoundException, UnauthorizedException, EntityNotFoundException {
        String email = "user@example.com";

        when(cookieService.extractTokenFromCookie(any(HttpServletRequest.class))).thenReturn("token");
        when(jwtService.extractClaim(eq("token"), any())).thenReturn(email);
        when(refrigeratorGroceryRepository.findById(any())).thenReturn(Optional.of(refrigeratorGrocery));
        when(refrigeratorService.getFridgeRole(any(), any())).thenReturn(FridgeRole.SUPERUSER);

        groceryService.removeRefrigeratorGrocery(1L, HttpRequest);

        verify(refrigeratorGroceryRepository, times(1)).deleteById(1L);
    }

    @Test
    public void removeRefrigeratorGrocery_EntityNotFound() {
        when(refrigeratorGroceryRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> groceryService.removeRefrigeratorGrocery(1L, HttpRequest));

        verify(refrigeratorGroceryRepository, never()).deleteById(1L);
    }

    @Test
    public void removeRefrigeratorGrocery_UnauthorizedUser() throws UserNotFoundException, EntityNotFoundException, UnauthorizedException {
        String email = "user@example.com";

        when(cookieService.extractTokenFromCookie(any(HttpServletRequest.class))).thenReturn("token");
        when(jwtService.extractClaim(eq("token"), any())).thenReturn(email);
        when(refrigeratorGroceryRepository.findById(any())).thenReturn(Optional.of(refrigeratorGrocery));
        when(refrigeratorService.getFridgeRole(any(Refrigerator.class), eq(email))).thenReturn(FridgeRole.USER);

        Assertions.assertThrows(UnauthorizedException.class, () -> groceryService.removeRefrigeratorGrocery(refrigeratorGrocery.getId(), HttpRequest));
    }

    @Test
    @DisplayName("Test adding grocery")
    public void testAddingGrocery() throws UserNotFoundException, SaveException, UnauthorizedException, RefrigeratorNotFoundException {
        SaveGroceryListRequest request = new SaveGroceryListRequest();
        List<GroceryDTO> dtoList = new ArrayList<>();
        dtoList.add(existingGroceryDTO);
        request.setGroceryList(dtoList);
        request.setUnitDTO(UnitDTO.builder().name("dl").id(1L).build());
        request.setQuantity(1);
        request.setRefrigeratorId(refrigerator.getId());

        when(unitRepository.findById(any())).thenReturn(Optional.ofNullable(unit));
        when(groceryService.getFridgeRole(refrigerator, any())).thenReturn(FridgeRole.SUPERUSER);
        when(groceryRepository.findById(existingGroceryDTO.getId())).thenReturn(Optional.ofNullable(grocery));
        when(refrigeratorGroceryRepository.findAllByRefrigeratorId(any())).thenReturn(groceryList);
        when(refrigeratorRepository.findById(any())).thenReturn(Optional.ofNullable(refrigerator));
        when(refrigeratorService.getRefrigerator(refrigerator.getId())).thenReturn(refrigerator);

        groceryService.addGrocery(request, HttpRequest);
        int result = groceryService.getGroceriesByRefrigerator(refrigerator.getId(), HttpRequest).size();

        Assertions.assertEquals(dtoList.size(), result);
    }

    @Test
    public void testAddCustomGrocery() throws SaveException {
        // Given
        Mockito.when(subCategoryRepository.findById(customGroceryDTO.getId())).thenReturn(Optional.of(customGroceryDTO.getSubCategory()));
        Mockito.when(groceryService.saveGrocery(Mockito.any())).thenReturn(grocery);

        // When
        Grocery result = groceryService.addCustomGrocery(customGroceryDTO);

        // Then
        assertNotNull(result);
        assertEquals(customGroceryDTO.getName(), result.getName());
        assertEquals(customGroceryDTO.getDescription(), result.getDescription());
        assertEquals(customGroceryDTO.getGroceryExpiryDays(), result.getGroceryExpiryDays());
        assertEquals(customGroceryDTO.getSubCategory(), result.getSubCategory());
    }

    @Test
    public void testAddCustomGrocery_SubCategoryNotFound() {
        // Given
        Mockito.when(subCategoryRepository.findById(customGroceryDTO.getId())).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> groceryService.addCustomGrocery(customGroceryDTO));
    }

    @Test
    public void testAddCustomGrocery_WithDescriptionNull() throws SaveException {
        // Given
        customGroceryDTO.setDescription(null);
        grocery.setDescription(null);
        Mockito.when(subCategoryRepository.findById(customGroceryDTO.getId())).thenReturn(Optional.of(customGroceryDTO.getSubCategory()));
        Mockito.when(groceryService.saveGrocery(Mockito.any())).thenReturn(grocery);

        // When
        Grocery result = groceryService.addCustomGrocery(customGroceryDTO);

        // Then
        assertNotNull(result);
        assertEquals(customGroceryDTO.getName(), result.getName());
        assertNull(result.getDescription());
        assertEquals(customGroceryDTO.getGroceryExpiryDays(), result.getGroceryExpiryDays());
        assertEquals(customGroceryDTO.getSubCategory(), result.getSubCategory());
    }
}
