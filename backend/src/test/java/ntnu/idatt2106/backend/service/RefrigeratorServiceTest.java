package ntnu.idatt2106.backend.service;

import ntnu.idatt2106.backend.exceptions.UserNotFoundException;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.RefrigeratorUser;
import ntnu.idatt2106.backend.model.User;
import ntnu.idatt2106.backend.model.enums.Role;
import ntnu.idatt2106.backend.model.requests.MemberRequest;
import ntnu.idatt2106.backend.model.requests.RefrigeratorRequest;
import ntnu.idatt2106.backend.model.responses.MemberResponse;
import ntnu.idatt2106.backend.repository.RefrigeratorRepository;
import ntnu.idatt2106.backend.repository.RefrigeratorUserRepository;
import ntnu.idatt2106.backend.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class RefrigeratorServiceTest {

    @Mock
    private RefrigeratorRepository refrigeratorRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private RefrigeratorUserRepository refrigeratorUserRepository;

    @InjectMocks
    private RefrigeratorService refrigeratorService;

    private Refrigerator refrigerator;
    private User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        refrigerator = new Refrigerator();
        refrigerator.setId(1L);
        refrigerator.setName("Test Refrigerator");

        user = new User();
        user.setId("testUserId");
        user.setEmail("testuser@test.com");
    }

    @Test
    @DisplayName("Test saving a refrigerator with a valid user")
    public void testSaveRefrigeratorWithValidUser() throws Exception {
        // Arrange
        RefrigeratorRequest request = new RefrigeratorRequest();
        request.setRefrigerator(new Refrigerator());
        request.setUsername("user@example.com");
        request.getRefrigerator().setName("Refrigerator name");
        Refrigerator refrigerator = request.getRefrigerator();

        when(userRepository.findByEmail(request.getUsername())).thenReturn(Optional.of(new User()));
        when(refrigeratorRepository.save(refrigerator)).thenReturn(refrigerator);

        // Act
        Refrigerator result = refrigeratorService.save(request);

        // Assert
        Assertions.assertNotNull(result);
    }

    @Test
    @DisplayName("Test getUser returns existing user ")
    public void testGetUserReturnsExistingUser() throws UserNotFoundException {
        // Arrange
        String existingUserEmail = "john.doe@example.com";
        User existingUser = new User();
        existingUser.setEmail(existingUserEmail);
        Mockito.when(userRepository.findByEmail(existingUserEmail)).thenReturn(Optional.of(existingUser));

        // Act
        User result = refrigeratorService.getUser(existingUserEmail);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingUserEmail, result.getEmail());
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(existingUserEmail);
    }

    @Test
    @DisplayName("Test getUser returns existing user ")
    public void testGetUserThrowsNonExistingUser() {
        // Arrange
        String nonExistingUserEmail = "john.doe@example.com";

        // Act
        Assert.assertThrows(UserNotFoundException.class,() -> refrigeratorService.getUser(nonExistingUserEmail));
    }

    @Test
    @DisplayName("Test saving a refrigerator with an invalid user")
    public void testSaveRefrigeratorWithInvalidUser() {
        // Arrange
        RefrigeratorRequest request = new RefrigeratorRequest();
        request.setRefrigerator(new Refrigerator());
        request.setUsername("invalid@example.com");

        when(userService.findByEmail(request.getUsername())).thenThrow(new UsernameNotFoundException("User not found with email: invalid@example.com"));

        // Act and assert
        Assertions.assertThrows(Exception.class, () -> refrigeratorService.save(request));
    }

    @Test
    @DisplayName("Test saving a refrigerator with an empty name")
    public void testSaveRefrigeratorWithEmptyName() {
        // Arrange
        // Arrange
        RefrigeratorRequest request = new RefrigeratorRequest();
        request.setRefrigerator(new Refrigerator());
        request.setUsername("user@example.com");
        request.getRefrigerator().setName("");

        when(userRepository.findByEmail(request.getUsername())).thenReturn(Optional.of(new User()));

        // Act and assert
        Assertions.assertThrows(Exception.class, () -> refrigeratorService.save(request));
    }

    @Test
    @DisplayName("Test saving a refrigerator with a null name")
    public void testSaveRefrigeratorWithNullName() {
        // Arrange
        RefrigeratorRequest request = new RefrigeratorRequest();
        request.setRefrigerator(new Refrigerator());
        request.setUsername("user@example.com");
        request.getRefrigerator().setName(null);

        when(userRepository.findByEmail(request.getUsername())).thenReturn(Optional.of(new User()));

        // Act and assert
        Assertions.assertThrows(Exception.class, () -> refrigeratorService.save(request));
    }

    @Test
    public void deleteById_shouldReturnTrue() {
        Mockito.when(refrigeratorRepository.existsById(1L)).thenReturn(true);
        Mockito.doNothing().when(refrigeratorRepository).deleteById(1L);

        boolean result = refrigeratorService.deleteById(1L);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("deleteById should return false")
    public void deleteByIdShouldReturnFalse() {
        Mockito.when(refrigeratorRepository.existsById(1L)).thenReturn(false);

        boolean result = refrigeratorService.deleteById(1L);

        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("getAllRefrigerators should return list of refrigerators")
    public void getAllRefrigerators_shouldReturnListOfRefrigerators() {
        List<Refrigerator> refrigeratorList = new ArrayList<>();
        refrigeratorList.add(refrigerator);

        Mockito.when(refrigeratorRepository.findAll()).thenReturn(refrigeratorList);

        List<Refrigerator> result = refrigeratorService.getAllRefrigerators();

        Assertions.assertEquals(refrigeratorList, result);
    }

    @Test
    @DisplayName("findById should return refrigerator")
    public void findByIdShouldReturnRefrigerator() {
        Optional<Refrigerator> optionalRefrigerator = Optional.of(refrigerator);

        Mockito.when(refrigeratorRepository.findById(1L)).thenReturn(optionalRefrigerator);

        Optional<Refrigerator> result = refrigeratorService.findById(1L);

        Assertions.assertEquals(optionalRefrigerator, result);
    }

    @Test
    @DisplayName("findById should return empty optional")
    public void findByIdShouldReturnEmptyOptional() {
        Optional<Refrigerator> optionalRefrigerator = Optional.empty();

        Mockito.when(refrigeratorRepository.findById(1L)).thenReturn(optionalRefrigerator);

        Optional<Refrigerator> result = refrigeratorService.findById(1L);

        Assertions.assertEquals(optionalRefrigerator, result);
    }

    @Test
    @DisplayName("Test adding a member to a refrigerator")
    public void testAddMemberToRefrigerator() {
        // Arrange
        User newUser = new User();
        newUser.setEmail("user1@example.com");
        MemberRequest request1 = new MemberRequest();
        request1.setRefrigeratorId(refrigerator.getId());
        request1.setSuperName(user.getUsername());
        request1.setUserName(newUser.getUsername());
        request1.setRole(Role.USER);
        RefrigeratorUser ru = new RefrigeratorUser();
        ru.setUser(user);
        ru.setRole(Role.SUPERUSER);
        ru.setRefrigerator(refrigerator);

        when(userRepository.findByEmail(request1.getUserName())).thenReturn(Optional.of(new User()));
        when(refrigeratorRepository.findById(request1.getRefrigeratorId())).thenReturn(Optional.of(refrigerator));
        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(newUser.getUsername())).thenReturn(Optional.of(newUser));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(user.getId(), refrigerator.getId())).thenReturn(Optional.of(ru));

        //Act
        Assertions.assertDoesNotThrow(() -> refrigeratorService.addMember(request1));
    }

    @Test
    @DisplayName("Test setRole with valid input")
    public void testSetRoleWithValidInput() throws UserNotFoundException {
        User user = new User();
        user.setId("test_user_id");
        user.setEmail("test_user@test.com");
        user.setPassword("test_password");
        User superUser = new User();
        superUser.setId("super_user_id");
        superUser.setEmail("super_user@test.com");
        superUser.setPassword("super_password");

        Refrigerator refrigerator = new Refrigerator();
        refrigerator.setId(1L);
        refrigerator.setName("test_refrigerator");

        RefrigeratorUser refrigeratorUser = new RefrigeratorUser();
        refrigeratorUser.setId(1L);
        refrigeratorUser.setUser(user);
        refrigeratorUser.setRefrigerator(refrigerator);

        RefrigeratorUser refrigeratorSuper = new RefrigeratorUser();
        refrigeratorSuper.setId(1L);
        refrigeratorSuper.setUser(superUser);
        refrigeratorSuper.setRefrigerator(refrigerator);
        refrigeratorSuper.setRole(Role.SUPERUSER);

        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(superUser.getUsername())).thenReturn(Optional.of(superUser));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id("test_user_id", 1L)).thenReturn(Optional.of(refrigeratorUser));
        when(refrigeratorUserRepository.save(Mockito.any(RefrigeratorUser.class))).thenReturn(refrigeratorUser);
        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.of(user));
        when(refrigeratorRepository.findById(refrigerator.getId())).thenReturn(Optional.of(refrigerator));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(superUser.getId(), refrigerator.getId())).thenReturn(Optional.of(refrigeratorSuper));
        when(refrigeratorUserRepository.findByUserAndRefrigerator(user,refrigerator)).thenReturn(Optional.of(refrigeratorUser));

        Role newRole = Role.SUPERUSER;

        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setRole(newRole);
        memberRequest.setRefrigeratorId(1L);
        memberRequest.setSuperName(superUser.getUsername());
        memberRequest.setUserName(user.getUsername());
        MemberResponse result = refrigeratorService.setRole(memberRequest);

        Assertions.assertEquals(result.getRole(), newRole);
    }

    @Test
    @DisplayName("Test setRole with invalid user ")
    public void testSetRoleWithInvalidUser() throws UserNotFoundException {
        Mockito.when(userRepository.findByEmail("nonexistent_user@test.com")).thenReturn(Optional.empty());

        Role newRole = Role.SUPERUSER;
        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setRole(newRole);
        memberRequest.setRefrigeratorId(1L);
        memberRequest.setUserName("nonexistent_user@test.com");

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            refrigeratorService.setRole(memberRequest);
        });
    }

    @Test
    @DisplayName("Test setRole with invalid refrigerator user")
    public void testSetRoleWithInvalidRefrigeratorUser() throws UserNotFoundException {
        User user = new User();
        user.setId("test_user_id");
        user.setEmail("test_user@test.com");
        user.setPassword("test_password");
        User superUser = new User();
        superUser.setId("super_user_id");
        superUser.setEmail("super_user@test.com");
        superUser.setPassword("super_password");

        Refrigerator refrigerator = new Refrigerator();
        refrigerator.setId(1L);
        refrigerator.setName("test_refrigerator");

        RefrigeratorUser refrigeratorUser = new RefrigeratorUser();
        refrigeratorUser.setId(1L);
        refrigeratorUser.setUser(user);
        refrigeratorUser.setRefrigerator(refrigerator);

        RefrigeratorUser refrigeratorSuper = new RefrigeratorUser();
        refrigeratorSuper.setId(1L);
        refrigeratorSuper.setUser(superUser);
        refrigeratorSuper.setRefrigerator(refrigerator);
        refrigeratorSuper.setRole(Role.SUPERUSER);

        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(superUser.getUsername())).thenReturn(Optional.of(superUser));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id("test_user_id", 1L)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.of(user));
        when(refrigeratorRepository.findById(refrigerator.getId())).thenReturn(Optional.of(refrigerator));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(superUser.getId(), refrigerator.getId())).thenReturn(Optional.of(refrigeratorSuper));
        when(refrigeratorUserRepository.findByUserAndRefrigerator(user,refrigerator)).thenReturn(Optional.of(refrigeratorUser));

        Role newRole = Role.SUPERUSER;

        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setRole(newRole);
        memberRequest.setRefrigeratorId(1L);
        memberRequest.setSuperName(superUser.getUsername());
        memberRequest.setUserName(user.getUsername());
        MemberResponse result = refrigeratorService.setRole(memberRequest);

        Assertions.assertNull(result);
    }
}