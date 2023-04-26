package ntnu.idatt2106.backend.service;

import jakarta.persistence.EntityNotFoundException;
import ntnu.idatt2106.backend.exceptions.LastSuperuserException;
import ntnu.idatt2106.backend.exceptions.UserNotFoundException;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.RefrigeratorUser;
import ntnu.idatt2106.backend.model.User;
import ntnu.idatt2106.backend.model.dto.response.RefrigeratorResponse;
import ntnu.idatt2106.backend.model.enums.FridgeRole;
import ntnu.idatt2106.backend.model.requests.MemberRequest;
import ntnu.idatt2106.backend.model.requests.RefrigeratorRequest;
import ntnu.idatt2106.backend.model.dto.response.MemberResponse;
import ntnu.idatt2106.backend.model.requests.RemoveMemberRequest;
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

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

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
    private User superuser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        refrigerator = new Refrigerator();
        refrigerator.setId(1L);
        refrigerator.setName("Test Refrigerator");

        user = new User();
        user.setId("testUserId");
        user.setEmail("testuser@test.com");

        superuser = new User();
        superuser.setId("testSuperuserId");
        superuser.setEmail("testSuperuser@test.com");
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
    @DisplayName("getAllRefrigerators should return list of refrigerators")
    public void getAllRefrigerators_shouldReturnListOfRefrigerators() throws UserNotFoundException {
        List<RefrigeratorUser> refrigeratorList = new ArrayList<>();
        RefrigeratorUser ru = new RefrigeratorUser();
        ru.setRefrigerator(refrigerator);
        ru.setUser(user);
        refrigeratorList.add(ru);
        List<Refrigerator> refrigerators = new ArrayList<>();
        refrigerators.add(refrigerator);

        Mockito.when(refrigeratorUserRepository.findByUser(user)).thenReturn(refrigeratorList);
        Mockito.when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.ofNullable(user));

        List<Refrigerator> result = refrigeratorService.getAllByUser(user.getUsername());

        Assertions.assertEquals(refrigerators, result);
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
        request1.setRole(FridgeRole.USER);
        RefrigeratorUser ru = new RefrigeratorUser();
        ru.setUser(user);
        ru.setFridgeRole(FridgeRole.SUPERUSER);
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
    @DisplayName("Test setFridgeRole with valid input")
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
        refrigeratorSuper.setFridgeRole(FridgeRole.SUPERUSER);

        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(superUser.getUsername())).thenReturn(Optional.of(superUser));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id("test_user_id", 1L)).thenReturn(Optional.of(refrigeratorUser));
        when(refrigeratorUserRepository.save(Mockito.any(RefrigeratorUser.class))).thenReturn(refrigeratorUser);
        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.of(user));
        when(refrigeratorRepository.findById(refrigerator.getId())).thenReturn(Optional.of(refrigerator));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(superUser.getId(), refrigerator.getId())).thenReturn(Optional.of(refrigeratorSuper));
        when(refrigeratorUserRepository.findByUserAndRefrigerator(user,refrigerator)).thenReturn(Optional.of(refrigeratorUser));

        FridgeRole newFridgeRole = FridgeRole.SUPERUSER;

        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setRole(newFridgeRole);
        memberRequest.setRefrigeratorId(1L);
        memberRequest.setSuperName(superUser.getUsername());
        memberRequest.setUserName(user.getUsername());
        MemberResponse result = refrigeratorService.setFridgeRole(memberRequest);

        Assertions.assertEquals(result.getRole(), newFridgeRole);
    }

    @Test
    @DisplayName("Test setFridgeRole with invalid user ")
    public void testSetRoleWithInvalidUser() {
        Mockito.when(userRepository.findByEmail("nonexistent_user@test.com")).thenReturn(Optional.empty());

        FridgeRole newFridgeRole = FridgeRole.SUPERUSER;
        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setRole(newFridgeRole);
        memberRequest.setRefrigeratorId(1L);
        memberRequest.setUserName("nonexistent_user@test.com");

        Assertions.assertThrows(UserNotFoundException.class, () -> refrigeratorService.setFridgeRole(memberRequest));
    }

    @Test
    @DisplayName("Test setFridgeRole with invalid refrigerator user")
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
        refrigeratorSuper.setFridgeRole(FridgeRole.SUPERUSER);

        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(superUser.getUsername())).thenReturn(Optional.of(superUser));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id("test_user_id", 1L)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.of(user));
        when(refrigeratorRepository.findById(refrigerator.getId())).thenReturn(Optional.of(refrigerator));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(superUser.getId(), refrigerator.getId())).thenReturn(Optional.of(refrigeratorSuper));
        when(refrigeratorUserRepository.findByUserAndRefrigerator(user,refrigerator)).thenReturn(Optional.of(refrigeratorUser));

        FridgeRole newFridgeRole = FridgeRole.SUPERUSER;

        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setRole(newFridgeRole);
        memberRequest.setRefrigeratorId(1L);
        memberRequest.setSuperName(superUser.getUsername());
        memberRequest.setUserName(user.getUsername());
        MemberResponse result = refrigeratorService.setFridgeRole(memberRequest);

        Assertions.assertNull(result);
    }

    @Test
    @DisplayName("Test removing a user from a refrigerator")
    public void testRemoveUserFromRefrigerator() throws Exception {
        RemoveMemberRequest request = new RemoveMemberRequest();
        request.setRefrigeratorId(refrigerator.getId());
        request.setUserName(user.getEmail());
        request.setSuperName(superuser.getEmail());
        request.setForceDelete(false);

        RefrigeratorUser userRole = new RefrigeratorUser();
        userRole.setId(1L);
        userRole.setRefrigerator(refrigerator);
        userRole.setUser(user);
        userRole.setFridgeRole(FridgeRole.USER);

        RefrigeratorUser superuserRole = new RefrigeratorUser();
        superuserRole.setId(2L);
        superuserRole.setRefrigerator(refrigerator);
        superuserRole.setUser(superuser);
        superuserRole.setFridgeRole(FridgeRole.SUPERUSER);

        List<RefrigeratorUser> superUsers = new ArrayList<>();
        superUsers.add(superuserRole);

        when(refrigeratorRepository.findById(refrigerator.getId())).thenReturn(Optional.of(refrigerator));
        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(superuser.getUsername())).thenReturn(Optional.of(superuser));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(user.getId(), refrigerator.getId())).thenReturn(Optional.of(userRole));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(superuser.getId(), refrigerator.getId())).thenReturn(Optional.of(superuserRole));
        when(refrigeratorUserRepository.findByRefrigeratorIdAndFridgeRole(refrigerator.getId(), FridgeRole.SUPERUSER)).thenReturn(superUsers);

        refrigeratorService.removeUserFromRefrigerator(request);

        Mockito.verify(refrigeratorUserRepository, Mockito.times(1)).delete(userRole);
        Mockito.verify(refrigeratorRepository, Mockito.times(1)).save(refrigerator);
    }

    @Test
    @DisplayName("Test user tries to remove other user")
    public void testUserRemovesUserShouldFail() {
        RemoveMemberRequest request = new RemoveMemberRequest();
        request.setRefrigeratorId(refrigerator.getId());
        request.setUserName(user.getEmail());
        request.setSuperName(superuser.getEmail());
        request.setForceDelete(false);

        RefrigeratorUser userRole = new RefrigeratorUser();
        userRole.setId(1L);
        userRole.setRefrigerator(refrigerator);
        userRole.setUser(user);
        userRole.setFridgeRole(FridgeRole.USER);

        RefrigeratorUser userRole1 = new RefrigeratorUser();
        userRole1.setId(2L);
        userRole1.setRefrigerator(refrigerator);
        userRole1.setUser(superuser);
        userRole1.setFridgeRole(FridgeRole.USER); //Pretend is a normal user

        when(refrigeratorRepository.findById(refrigerator.getId())).thenReturn(Optional.of(refrigerator));
        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(superuser.getUsername())).thenReturn(Optional.of(superuser));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(user.getId(), refrigerator.getId())).thenReturn(Optional.of(userRole));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(superuser.getId(), refrigerator.getId())).thenReturn(Optional.of(userRole1));

        Assertions.assertThrows(AccessDeniedException.class, () -> refrigeratorService.removeUserFromRefrigerator(request));
    }

    @Test
    @DisplayName("Test removing the last superuser without forceDelete")
    public void testRemoveLastSuperuserWithoutForceDeleteShouldFail() throws EntityNotFoundException {
        RemoveMemberRequest request = new RemoveMemberRequest();
        request.setRefrigeratorId(refrigerator.getId());
        request.setUserName(superuser.getEmail());
        request.setSuperName(superuser.getEmail());
        request.setForceDelete(false);

        RefrigeratorUser superuserRole = new RefrigeratorUser();
        superuserRole.setId(2L);
        superuserRole.setRefrigerator(refrigerator);
        superuserRole.setUser(superuser);
        superuserRole.setFridgeRole(FridgeRole.SUPERUSER);

        List<RefrigeratorUser> superUsers = new ArrayList<>();
        superUsers.add(superuserRole);

        when(refrigeratorRepository.findById(refrigerator.getId())).thenReturn(Optional.of(refrigerator));
        when(userRepository.findByEmail(superuser.getUsername())).thenReturn(Optional.of(superuser));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(superuser.getId(), refrigerator.getId())).thenReturn(Optional.of(superuserRole));
        when(refrigeratorUserRepository.findByRefrigeratorIdAndFridgeRole(refrigerator.getId(), FridgeRole.SUPERUSER)).thenReturn(superUsers);

        Assertions.assertThrows(LastSuperuserException.class, () -> refrigeratorService.removeUserFromRefrigerator(request));

        Mockito.verify(refrigeratorUserRepository, Mockito.times(0)).delete(superuserRole);
        Mockito.verify(refrigeratorRepository, Mockito.times(0)).save(refrigerator);
    }

    @Test
    @DisplayName("Test removing a user from a refrigerator when user is not a member")
    public void testRemoveUserFromRefrigeratorUserNotMember() throws EntityNotFoundException {
        RemoveMemberRequest request = new RemoveMemberRequest();
        request.setRefrigeratorId(refrigerator.getId());
        request.setUserName("not_a_member@test.com");
        request.setSuperName(superuser.getEmail());
        request.setForceDelete(false);

        when(refrigeratorRepository.findById(refrigerator.getId())).thenReturn(Optional.of(refrigerator));
        when(userRepository.findByEmail(request.getUserName())).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> refrigeratorService.removeUserFromRefrigerator(request));
    }

    @Test
    @DisplayName("Test removing a user from a refrigerator when superuser is not a member")
    public void testRemoveUserFromRefrigeratorSuperuserNotMember() throws EntityNotFoundException {
        RemoveMemberRequest request = new RemoveMemberRequest();
        request.setRefrigeratorId(refrigerator.getId());
        request.setUserName(user.getEmail());
        request.setSuperName("not_a_member@test.com");
        request.setForceDelete(false);

        RefrigeratorUser userRole = new RefrigeratorUser();
        userRole.setId(1L);
        userRole.setRefrigerator(refrigerator);
        userRole.setUser(user);
        userRole.setFridgeRole(FridgeRole.USER);

        when(refrigeratorRepository.findById(refrigerator.getId())).thenReturn(Optional.of(refrigerator));
        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(request.getSuperName())).thenReturn(Optional.empty());
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(user.getId(), refrigerator.getId())).thenReturn(Optional.of(userRole));

        Assertions.assertThrows(EntityNotFoundException.class, () -> refrigeratorService.removeUserFromRefrigerator(request));
    }

    @Test
    @DisplayName("Test removing a user from a refrigerator when user tries to remove other user")
    public void testRemoveUserFromRefrigeratorUserTriesToRemoveOtherUser() throws EntityNotFoundException {
        RemoveMemberRequest request = new RemoveMemberRequest();
        request.setRefrigeratorId(refrigerator.getId());
        request.setUserName(user.getEmail());
        request.setSuperName(superuser.getEmail());
        request.setForceDelete(false);

        RefrigeratorUser userRole = new RefrigeratorUser();
        userRole.setId(1L);
        userRole.setRefrigerator(refrigerator);
        userRole.setUser(user);
        userRole.setFridgeRole(FridgeRole.USER);

        RefrigeratorUser userRole1 = new RefrigeratorUser();
        userRole1.setId(2L);
        userRole1.setRefrigerator(refrigerator);
        userRole1.setUser(superuser);
        userRole1.setFridgeRole(FridgeRole.USER);

        when(refrigeratorRepository.findById(refrigerator.getId())).thenReturn(Optional.of(refrigerator));
        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(superuser.getUsername())).thenReturn(Optional.of(superuser));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(user.getId(), refrigerator.getId())).thenReturn(Optional.of(userRole));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(superuser.getId(), refrigerator.getId())).thenReturn(Optional.of(userRole1));

        Assertions.assertThrows(AccessDeniedException.class, () -> refrigeratorService.removeUserFromRefrigerator(request));
    }

    @Test
    @DisplayName("Test force deleting a refrigerator when user is not a superuser")
    public void testForceDeleteRefrigeratorUserNotSuperuser() throws EntityNotFoundException {
        // Arrange
        RefrigeratorUser userRole = new RefrigeratorUser();
        userRole.setId(1L);
        userRole.setRefrigerator(refrigerator);
        userRole.setUser(user);
        userRole.setFridgeRole(FridgeRole.USER);

        when(userRepository.findByEmail(superuser.getEmail())).thenReturn(Optional.of(superuser));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(superuser.getId(), refrigerator.getId())).thenReturn(Optional.of(userRole));

        // Act and Assert
        Assertions.assertThrows(AccessDeniedException.class, () -> refrigeratorService.forceDeleteRefrigerator(superuser.getEmail(), refrigerator.getId()));
    }

    @Test
    @DisplayName("Test force deleting a refrigerator when user is a superuser and refrigerator exists with no members")
    public void testForceDeleteRefrigeratorSuperuserNoMembers() throws Exception {
        // Arrange
        RefrigeratorUser superUserRole = new RefrigeratorUser();
        superUserRole.setId(1L);
        superUserRole.setRefrigerator(refrigerator);
        superUserRole.setUser(superuser);
        superUserRole.setFridgeRole(FridgeRole.SUPERUSER);

        when(userRepository.findByEmail(superuser.getEmail())).thenReturn(Optional.of(superuser));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(superuser.getId(), refrigerator.getId())).thenReturn(Optional.of(superUserRole));
        when(refrigeratorRepository.existsById(refrigerator.getId())).thenReturn(true);

        // Act
        refrigeratorService.forceDeleteRefrigerator(superuser.getEmail(), refrigerator.getId());

        // Assert
        Mockito.verify(refrigeratorRepository, times(1)).deleteById(refrigerator.getId());
    }

    @Test
    @DisplayName("Test force deleting a refrigerator when user is a superuser and refrigerator exists with members")
    public void testForceDeleteRefrigeratorSuperuserWithMembers() throws Exception {
        // Arrange
        RefrigeratorUser superUserRole = new RefrigeratorUser();
        superUserRole.setId(1L);
        superUserRole.setRefrigerator(refrigerator);
        superUserRole.setUser(superuser);
        superUserRole.setFridgeRole(FridgeRole.SUPERUSER);

        when(userRepository.findByEmail(superuser.getEmail())).thenReturn(Optional.of(superuser));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(superuser.getId(), refrigerator.getId())).thenReturn(Optional.of(superUserRole));
        when(refrigeratorRepository.existsById(refrigerator.getId())).thenReturn(true);
        when(refrigeratorUserRepository.count()).thenReturn(2L);
        // Act
        refrigeratorService.forceDeleteRefrigerator(superuser.getEmail(), refrigerator.getId());

        // Assert
        Mockito.verify(refrigeratorUserRepository, times(1)).removeByRefrigeratorId(refrigerator.getId());
        Mockito.verify (refrigeratorRepository, times(1)).deleteById(refrigerator.getId());
    }

    @Test
    @DisplayName("Test force deleting a non-existent refrigerator")
    public void testForceDeleteRefrigeratorNonExistent() {
        // Arrange
        String email = superuser.getEmail();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(superuser));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(superuser.getId(), refrigerator.getId())).thenThrow(EntityNotFoundException.class);
        when(refrigeratorRepository.existsById(refrigerator.getId())).thenReturn(false);

        // Act and Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> refrigeratorService.forceDeleteRefrigerator(email, refrigerator.getId()));
    }

    @Test
    @DisplayName("Test getting an existing refrigerator by id")
    public void testGetExistingRefrigeratorById() throws EntityNotFoundException {
        // Arrange
        long id = 1L;
        Refrigerator refrigerator = new Refrigerator();
        refrigerator.setId(id);
        refrigerator.setName("Test Refrigerator");
        when(refrigeratorRepository.existsById(id)).thenReturn(true);
        when(refrigeratorRepository.findById(id)).thenReturn(Optional.of(refrigerator));
        when(refrigeratorUserRepository.findByRefrigeratorId(id)).thenReturn(new ArrayList<>());

        // Act
        RefrigeratorResponse result = refrigeratorService.getRefrigeratorById(id);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals(refrigerator.getName(), result.getName());
        Assertions.assertEquals(new ArrayList<MemberResponse>(), result.getMembers());
    }

    @Test
    @DisplayName("Test getting a non-existing refrigerator by id")
    public void testGetNonExistingRefrigeratorById() {
        // Arrange
        long id = 1L;
        when(refrigeratorRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> refrigeratorService.getRefrigeratorById(id));
    }
}