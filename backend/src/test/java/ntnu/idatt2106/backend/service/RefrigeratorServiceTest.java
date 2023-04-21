package ntnu.idatt2106.backend.service;

import ntnu.idatt2106.backend.exceptions.UserNotFoundException;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.User;
import ntnu.idatt2106.backend.model.requests.RefrigeratorRequest;
import ntnu.idatt2106.backend.repository.RefrigeratorRepository;
import ntnu.idatt2106.backend.repository.RefrigeratorUserRepository;
import ntnu.idatt2106.backend.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
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
    @DisplayName("Test saving a refrigerator with an invalid user")
    public void testSaveRefrigeratorWithInvalidUser() throws Exception {
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
    public void testSaveRefrigeratorWithEmptyName() throws Exception {
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
    public void testSaveRefrigeratorWithNullName() throws Exception {
        // Arrange
        RefrigeratorRequest request = new RefrigeratorRequest();
        request.setRefrigerator(new Refrigerator());
        request.setUsername("user@example.com");
        request.getRefrigerator().setName(null);

        when(userRepository.findByEmail(request.getUsername())).thenReturn(Optional.of(new User()));

        // Act and assert
        Assertions.assertThrows(Exception.class, () -> refrigeratorService.save(request));
    }

}