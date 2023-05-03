package ntnu.idatt2106.backend.integration.refrigerator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.RefrigeratorUser;
import ntnu.idatt2106.backend.model.User;
import ntnu.idatt2106.backend.model.dto.GroceryDTO;
import ntnu.idatt2106.backend.model.dto.recipe.RecipeDTO;
import ntnu.idatt2106.backend.model.authentication.AuthenticationRequest;
import ntnu.idatt2106.backend.model.authentication.RegisterRequest;
import ntnu.idatt2106.backend.model.dto.response.AuthenticationResponse;
import ntnu.idatt2106.backend.model.dto.response.RegisterResponse;
import ntnu.idatt2106.backend.model.enums.FridgeRole;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorGrocery;
import ntnu.idatt2106.backend.model.refrigerator.NewRefrigeratorDTO;
import ntnu.idatt2106.backend.repository.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@EnableTransactionManagement
@Transactional
public class RefrigeratorTest {


    @Autowired
    private TestRestTemplate restTemplate;
    private static HttpHeaders headers;
    private static HttpEntity<?> entity;

    @Autowired
    private RefrigeratorUserRepository refrigeratorUserRepository;

    @Autowired
    private RefrigeratorRepository refrigeratorRepository;

    @Autowired
    private RefrigeratorGroceryRepository refrigeratorGroceryRepository;

    @Autowired
    private ShoppingListRepository shoppingListRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void resetAutoIncrement() {
        Query query = entityManager.createNativeQuery("ALTER TABLE refrigerator ALTER COLUMN id RESTART WITH 1;");
        query.executeUpdate();
    }

    @BeforeAll
    public static void setUpTestData(@Autowired TestRestTemplate restTemplate) {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("create_new_fridge2_test@email.com")
                .name("Test User")
                .password("testpassword")
                .build();

        ResponseEntity<RegisterResponse> registerResponse = restTemplate.postForEntity(
                "/api/auth/register",
                registerRequest,
                RegisterResponse.class
        );

        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                "create_new_fridge2_test@email.com",
                "testpassword"
        );

        ResponseEntity<AuthenticationResponse> authenticationResponse = restTemplate.postForEntity(
                "/api/auth/login",
                authenticationRequest,
                AuthenticationResponse.class
        );

        assertThat(authenticationResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        headers = new HttpHeaders();
        headers.set(HttpHeaders.COOKIE, authenticationResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
        headers.setContentType(MediaType.APPLICATION_JSON);

        entity = new HttpEntity<>(headers);
    }

    @BeforeEach
    public void setUp() {
        shoppingCartRepository.deleteAll();
        shoppingListRepository.deleteAll();
        refrigeratorGroceryRepository.deleteAll();
        refrigeratorUserRepository.deleteAll();
        refrigeratorRepository.deleteAll();
        resetAutoIncrement();
    }

    @Test
    public void createNewFridge (){
        NewRefrigeratorDTO newRefrigeratorDTO = NewRefrigeratorDTO.builder()
                .userEmail("create_new_fridge2_test@email.com")
                .name("Test Refrigerator")
                .address("Test Address")
                .build();

        HttpEntity<NewRefrigeratorDTO> request = new HttpEntity<>(newRefrigeratorDTO, headers);
        ResponseEntity<Refrigerator> response = restTemplate.exchange(
                "/api/refrigerator/new",
                HttpMethod.POST,
                request,
                Refrigerator.class
        );
        System.out.println(refrigeratorUserRepository.findByRefrigeratorId(1));



        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Test Refrigerator");
        assertThat(response.getBody().getAddress()).isEqualTo("Test Address");
    }

    @Test
    public void insertGroceryToRefrigerator(){
        Refrigerator refrigerator = createRefrigerator();
        System.out.println(refrigeratorUserRepository.findByRefrigeratorId(refrigerator.getId()));
        GroceryDTO groceryDTO = GroceryDTO.builder()
                .id(1)
                .name("Egg")
                .groceryExpiryDays(7)
                .build();

        // Create a new grocery and add it to the refrigerator
        HttpEntity<GroceryDTO> newGroceryRequest = new HttpEntity<>(groceryDTO, headers);
        ResponseEntity<GroceryDTO> newGroceryResponse = restTemplate.exchange(
                "/api/refrigerator/grocery/create",
                HttpMethod.POST,
                newGroceryRequest,
                GroceryDTO.class
        );

        assertThat(newGroceryResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(newGroceryResponse.getBody().getId()).isEqualTo(1);
        assertThat(newGroceryResponse.getBody().getName()).isEqualTo("Egg");
        assertThat(newGroceryResponse.getBody().getGroceryExpiryDays()).isEqualTo(7);


        // Create a new grocery and add it to the refrigerator
        HttpEntity<GroceryDTO> insertGroceryRequest = new HttpEntity<>(groceryDTO, headers);
        ResponseEntity<GroceryDTO> insertGroceryResponse = restTemplate.exchange(
                "/api/refrigerator/grocery/new/" + refrigerator.getId(),
                HttpMethod.POST,
                insertGroceryRequest,
                GroceryDTO.class
        );

        assertThat(insertGroceryResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(insertGroceryResponse.getBody().getId()).isEqualTo(1);
        assertThat(insertGroceryResponse.getBody().getName()).isEqualTo("Egg");
        assertThat(insertGroceryResponse.getBody().getGroceryExpiryDays()).isEqualTo(7);
    }

    private Refrigerator createRefrigerator() {
        NewRefrigeratorDTO newRefrigeratorDTO = NewRefrigeratorDTO.builder()
                .userEmail("create_new_fridge2_test@email.com")
                .name("Refrigerator Integration Test")
                .address("Test Address")
                .build();

        HttpEntity<NewRefrigeratorDTO> request = new HttpEntity<>(newRefrigeratorDTO, headers);
        ResponseEntity<Refrigerator> response = restTemplate.exchange(
                "/api/refrigerator/new",
                HttpMethod.POST,
                request,
                Refrigerator.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Refrigerator Integration Test");
        assertThat(response.getBody().getAddress()).isEqualTo("Test Address");

        return response.getBody();
    }

}
