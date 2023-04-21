package ntnu.idatt2106.backend.service;

import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.RefrigeratorUser;
import ntnu.idatt2106.backend.model.User;
import ntnu.idatt2106.backend.model.requests.RefrigeratorRequest;
import ntnu.idatt2106.backend.repository.RefrigeratorRepository;
import ntnu.idatt2106.backend.repository.RefrigeratorUserRepository;
import ntnu.idatt2106.backend.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefrigeratorServiceTests {

    @Mock
    private RefrigeratorRepository refrigeratorRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefrigeratorUserRepository refrigeratorUserRepository;

    @InjectMocks
    private RefrigeratorService refrigeratorService;

    private RefrigeratorRequest refrigeratorRequest;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("example@example.com");

        refrigeratorRequest = new RefrigeratorRequest();
        refrigeratorRequest.setRefrigerator(new Refrigerator());
        refrigeratorRequest.getRefrigerator().setName("Example Name");
        refrigeratorRequest.getRefrigerator().setAddress("Example Address");
        refrigeratorRequest.setUsername("example@example.com");
    }

    @Test
    public void testSaveRefrigeratorSuccess() throws Exception {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(refrigeratorRepository.save(any(Refrigerator.class))).thenReturn(new Refrigerator());
        when(refrigeratorUserRepository.save(any(RefrigeratorUser.class))).thenReturn(new RefrigeratorUser());

        Refrigerator result = refrigeratorService.save(refrigeratorRequest);

        assertNotNull(result);
    }

    @Test
    public void testSaveRefrigeratorFailure() throws Exception {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Refrigerator result = refrigeratorService.save(refrigeratorRequest);

        assertNull(result);
    }

    @Test
    void testSaveRefrigeratorWithNullName() throws Exception {
        // Create user
        User user = new User();
        user.setEmail("user@example.com");
        user.setName("John");
        user.setPassword("password");

        // Save user
        userRepository.save(user);

        // Create refrigerator with null name
        Refrigerator refrigerator = new Refrigerator();
        refrigerator.setAddress("Address");

        // Create request
        RefrigeratorRequest request = new RefrigeratorRequest();
        request.setRefrigerator(refrigerator);
        request.setUsername(user.getEmail());

        // Test save refrigerator with null name
        Assertions.assertNull(refrigeratorService.save(request));
    }

    @Test
    void testSaveRefrigeratorWithNullAddress() throws Exception {
        // Create user
        User user = new User();
        user.setEmail("user@example.com");
        user.setName("John");
        user.setPassword("password");

        // Save user
        userRepository.save(user);

        // Create refrigerator with null address
        Refrigerator refrigerator = new Refrigerator();
        refrigerator.setName("Refrigerator");

        // Create request
        RefrigeratorRequest request = new RefrigeratorRequest();
        request.setRefrigerator(refrigerator);
        request.setUsername(user.getEmail());

        // Test save refrigerator with null address
        Assertions.assertNull(refrigeratorService.save(request));
    }

    @Test
    void testSaveRefrigeratorWithNonExistingUser() throws Exception {
        // Create user
        User user = new User();
        user.setEmail("user@example.com");
        user.setName("John");
        user.setPassword("password");

        // Do not save user

        // Create refrigerator
        Refrigerator refrigerator = new Refrigerator();
        refrigerator.setName("Refrigerator");
        refrigerator.setAddress("Address");

        // Create request
        RefrigeratorRequest request = new RefrigeratorRequest();
        request.setRefrigerator(refrigerator);
        request.setUsername(user.getEmail());

        // Test save refrigerator with non-existing user
        Assertions.assertNull(refrigeratorService.save(request));
    }

    @Test
    void testDeleteNonExistingRefrigerator() {
        // Test deleting a non-existing refrigerator
        Assertions.assertFalse(refrigeratorService.deleteById(1));
    }
}
