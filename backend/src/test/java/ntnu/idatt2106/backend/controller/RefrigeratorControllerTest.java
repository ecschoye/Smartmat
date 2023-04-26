package ntnu.idatt2106.backend.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import ntnu.idatt2106.backend.exceptions.RefrigeratorNotFoundException;
import ntnu.idatt2106.backend.exceptions.SaveException;
import ntnu.idatt2106.backend.exceptions.UnauthorizedException;
import ntnu.idatt2106.backend.exceptions.UserNotFoundException;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.RefrigeratorUser;
import ntnu.idatt2106.backend.model.User;
import ntnu.idatt2106.backend.model.dto.MemberDTO;
import ntnu.idatt2106.backend.model.dto.RefrigeratorDTO;
import ntnu.idatt2106.backend.model.dto.response.SuccessResponse;
import ntnu.idatt2106.backend.model.enums.FridgeRole;
import ntnu.idatt2106.backend.model.requests.MemberRequest;
import ntnu.idatt2106.backend.model.requests.RemoveMemberRequest;
import ntnu.idatt2106.backend.repository.RefrigeratorRepository;
import ntnu.idatt2106.backend.repository.RefrigeratorUserRepository;
import ntnu.idatt2106.backend.repository.UserRepository;
import ntnu.idatt2106.backend.service.CookieService;
import ntnu.idatt2106.backend.service.JwtService;
import ntnu.idatt2106.backend.service.RefrigeratorService;
import ntnu.idatt2106.backend.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class RefrigeratorControllerTest {

    @Mock
    private RefrigeratorService refrigeratorService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefrigeratorUserRepository refrigeratorUserRepository;

    @InjectMocks
    private RefrigeratorController refrigeratorController;

    private Refrigerator refrigerator;
    private User user;

    @Mock
    private CookieService cookieService;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        refrigeratorController = new RefrigeratorController(refrigeratorService, cookieService, userService, jwtService);
        user = new User();
        user.setId("testUserId");
        user.setEmail("testuser@test.com");

        refrigerator = new Refrigerator();
        refrigerator.setId(1L);
        refrigerator.setName("Test Refrigerator");
    }
/**
    @Test
    @DisplayName("Test new refrigerator success")
    void testNewRefrigeratorSuccess() throws Exception {
        // Prepare test data
        RefrigeratorDTO refrigeratorDTO = new RefrigeratorDTO();
        refrigeratorDTO.setId(1L);
        refrigeratorDTO.setName(null);
        refrigeratorDTO.setAddress("Test Address");
        String userEmail = "test@example.com";

        // Mock HttpServletRequest
        HttpServletRequest request = mock(HttpServletRequest.class);

        // Set up mocks
        when(cookieService.extractTokenFromCookie(request)).thenReturn("dummy_jwt");
        when(jwtService.extractUsername("dummy_jwt")).thenReturn(userEmail);
        when(refrigeratorService.convertToEntity(refrigeratorDTO)).thenReturn(refrigerator);
        when(refrigeratorService.save(eq(refrigerator), eq(userEmail))).thenReturn(refrigerator);

        // Inject mocks into refrigeratorController
        ReflectionTestUtils.setField(refrigeratorController, "cookieService", cookieService);
        ReflectionTestUtils.setField(refrigeratorController, "jwtService", jwtService);

        // Test newRefrigerator method
        ResponseEntity<Refrigerator> response = refrigeratorController.newRefrigerator(refrigeratorDTO, request);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(refrigerator, response.getBody());
    }

    @Test
    @DisplayName("Test new refrigerator fail")
    void testNewRefrigeratorFail() throws Exception {
        RefrigeratorDTO refrigeratorDTO = new RefrigeratorDTO();
        refrigeratorDTO.setId(1L);
        refrigeratorDTO.setName(null);
        refrigeratorDTO.setAddress("Test Address");
        String userEmail = "test@example.com";

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(cookieService.extractTokenFromCookie(request)).thenReturn("dummy_jwt");
        when(jwtService.extractUsername("dummy_jwt")).thenReturn(userEmail);
        when(refrigeratorService.convertToEntity(refrigeratorDTO)).thenReturn(refrigerator);
        when(refrigeratorService.save(eq(refrigerator), eq(userEmail))).thenReturn(null);

        Assertions.assertThrows(SaveException.class, () -> refrigeratorController.newRefrigerator(refrigeratorDTO, request));
    }


    @Test
    @DisplayName("Test adding new member")
    void testNewMemberSuccess() throws Exception {
        // Create a mock MemberRequest object
        MemberRequest request = new MemberRequest();
        request.setRefrigeratorId(1);
        request.setUserName(user.getUsername());

        // Create a mock RefrigeratorUser object to return from the service
        RefrigeratorUser refrigeratorUser = new RefrigeratorUser();
        refrigeratorUser.setId(1);
        refrigeratorUser.setUser(user);
        refrigeratorUser.setRefrigerator(refrigerator);
        MemberDTO MemberDTO = new MemberDTO();
        MemberDTO.setUsername(user.getUsername());
        MemberDTO.setRefrigeratorId(refrigerator.getId());
        MemberDTO.setFridgeRole(FridgeRole.USER);

        when(refrigeratorService.addMember(request)).thenReturn(MemberDTO);

        // Call the method being tested
        ResponseEntity<MemberDTO> response = refrigeratorController.newMember(request);

        // Verify that the service method was called with the correct arguments
        Mockito.verify(refrigeratorService).addMember(request);

        // Verify that the response status is OK
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verify that the response body is the expected RefrigeratorUser object
        Assertions.assertEquals(MemberDTO, response.getBody());
    }

    @Test
    @DisplayName("Test get all should succeed")
    void testGetAllSuccess() throws UserNotFoundException {
        List<Refrigerator> refrigerators = new ArrayList<>();
        Refrigerator refrigerator = new Refrigerator();
        refrigerator.setId(1);
        refrigerators.add(refrigerator);

        when(refrigeratorService.getAllByUser(user.getUsername())).thenReturn(refrigerators);

        ResponseEntity<List<Refrigerator>> response = refrigeratorController.getAllByUser(user.getUsername());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(refrigerators, response.getBody());
    }

    @Test
    @DisplayName("Test get by ID endpoint with valid ID")
    public void testGetByIdWithValidId() throws EntityNotFoundException, RefrigeratorNotFoundException {
        // Arrange
        long refrigeratorId = 1L;
        RefrigeratorDTO expectedResponse = new RefrigeratorDTO();

        when(refrigeratorService.getRefrigeratorDTOById(anyLong())).thenReturn(expectedResponse);

        // Act
        ResponseEntity<RefrigeratorDTO> responseEntity = refrigeratorController.getById(refrigeratorId);

        // Assert
        Assertions.assertEquals(expectedResponse, responseEntity.getBody());
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteRefrigerator() throws Exception {
        // Arrange
        int refrigeratorId = 1;
        String username = "user1";
        SuccessResponse expectedResponse = new SuccessResponse("Member removed successfully", HttpStatus.OK.value());

        Mockito.doNothing().when(refrigeratorService).forceDeleteRefrigerator(username, refrigeratorId);

        // Act
        ResponseEntity<SuccessResponse> responseEntity = refrigeratorController.deleteRefrigerator(refrigeratorId, username);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    public void testDeleteRefrigeratorUnauthorized() throws Exception {
        // Arrange
        int refrigeratorId = 1;
        String username = "user1";

        Mockito.doThrow(new AccessDeniedException("")).when(refrigeratorService).forceDeleteRefrigerator(username, refrigeratorId);

        //Act & Assert
        Assertions.assertThrows(AccessDeniedException.class, () -> refrigeratorController.deleteRefrigerator(refrigeratorId, username));
    }

    @Test
    public void testDeleteRefrigeratorBadRequest() throws Exception {
        // Arrange
        int refrigeratorId = 1;
        String username = "user1";

        Mockito.doThrow(new EntityNotFoundException()).when(refrigeratorService).forceDeleteRefrigerator(username, refrigeratorId);

        // Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> refrigeratorController.deleteRefrigerator(refrigeratorId, username));
    }

    @Test
    public void testRemoveMember() throws Exception {
        // Arrange
        RemoveMemberRequest removeMemberRequest = new RemoveMemberRequest();
        removeMemberRequest.setUserName("user1@example.com");
        removeMemberRequest.setSuperName("superuser@example.com");
        removeMemberRequest.setRefrigeratorId(1L);
        removeMemberRequest.setForceDelete(false);

        SuccessResponse successResponse = new SuccessResponse("Member removed successfully",HttpStatus.OK.value());

        // Act
        ResponseEntity<SuccessResponse> responseEntity = refrigeratorController.removeMember(removeMemberRequest);

        // Assert
        Mockito.verify(refrigeratorService, Mockito.times(1)).removeUserFromRefrigerator(removeMemberRequest);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(successResponse, responseEntity.getBody());
    }

    @Test
    @DisplayName("Test editRole with invalid input")
    public void testEditRoleWithInvalidInput() throws SaveException, UserNotFoundException {
        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setFridgeRole(null);
        memberRequest.setRefrigeratorId(0);
        memberRequest.setUserName(null);


        ResponseEntity<MemberDTO> response = refrigeratorController.editRole(memberRequest);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    @DisplayName("Test editRole with null response from service")
    public void testEditRoleWithNullResponse() throws SaveException, UserNotFoundException, UnauthorizedException, RefrigeratorNotFoundException {
        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setFridgeRole(FridgeRole.SUPERUSER);
        memberRequest.setRefrigeratorId(1L);
        memberRequest.setUserName("test_user");

        when(refrigeratorService.setFridgeRole(memberRequest)).thenReturn(null);

        ResponseEntity<MemberDTO> response = refrigeratorController.editRole(memberRequest);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    @DisplayName("Test editRole with valid input")
    public void testEditRoleWithValidInput() throws SaveException, UserNotFoundException, UnauthorizedException, RefrigeratorNotFoundException {
        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setFridgeRole(FridgeRole.SUPERUSER);
        memberRequest.setRefrigeratorId(1L);
        memberRequest.setUserName("test_user");

        MemberDTO expectedResult = new MemberDTO();
        expectedResult.setFridgeRole(FridgeRole.SUPERUSER);
        expectedResult.setRefrigeratorId(1L);
        expectedResult.setUsername("test_user");

        when(refrigeratorService.setFridgeRole(memberRequest)).thenReturn(expectedResult);

        ResponseEntity<MemberDTO> response = refrigeratorController.editRole(memberRequest);
        MemberDTO actualResult = response.getBody();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expectedResult, actualResult);
    }
    **/
}