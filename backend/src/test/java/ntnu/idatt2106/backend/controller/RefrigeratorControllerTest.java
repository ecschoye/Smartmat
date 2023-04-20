package ntnu.idatt2106.backend.controller;

import ntnu.idatt2106.backend.exceptions.SaveException;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.requests.RefrigeratorRequest;
import ntnu.idatt2106.backend.service.RefrigeratorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class RefrigeratorControllerTest {

    @Mock
    private RefrigeratorService refrigeratorService;

    private RefrigeratorController refrigeratorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        refrigeratorController = new RefrigeratorController(refrigeratorService);
    }

    @Test
    void testCreateRefrigeratorSuccess() throws Exception {
        RefrigeratorRequest request = new RefrigeratorRequest();
        Refrigerator refrigerator = new Refrigerator();
        refrigerator.setId(1);

        Mockito.when(refrigeratorService.save(request)).thenReturn(refrigerator);

        ResponseEntity<Refrigerator> response = refrigeratorController.createRefrigerator(request);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(refrigerator, response.getBody());
    }

    @Test
    void testCreateRefrigeratorFail() throws Exception {
        RefrigeratorRequest request = new RefrigeratorRequest();

        Mockito.when(refrigeratorService.save(request)).thenReturn(null);

        Assertions.assertThrows(SaveException.class, () -> {
            refrigeratorController.createRefrigerator(request);
        });
    }


    @Test
    void testDeleteRefrigeratorSuccess() {
        int id = 1;

        Mockito.when(refrigeratorService.delete(id)).thenReturn(true);

        ResponseEntity<Void> response = refrigeratorController.deleteRefrigerator(id);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteRefrigeratorFail() {
        int id = 1;

        Mockito.when(refrigeratorService.delete(id)).thenReturn(false);

        ResponseEntity<Void> response = refrigeratorController.deleteRefrigerator(id);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllSuccess() {
        List<Refrigerator> refrigerators = new ArrayList<>();
        Refrigerator refrigerator = new Refrigerator();
        refrigerator.setId(1);
        refrigerators.add(refrigerator);

        Mockito.when(refrigeratorService.getAllRefrigerators()).thenReturn(refrigerators);

        ResponseEntity<List<Refrigerator>> response = refrigeratorController.getAll();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(refrigerators, response.getBody());
    }

    @Test
    void testGetByIdSuccess() {
        int id = 1;
        Refrigerator refrigerator = new Refrigerator();
        refrigerator.setId(id);

        Mockito.when(refrigeratorService.findById(id)).thenReturn(Optional.of(refrigerator));

        ResponseEntity<Refrigerator> response = refrigeratorController.getById(id);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(refrigerator, response.getBody());
    }

    @Test
    void testGetByIdFail() {
        int id = 1;

        Mockito.when(refrigeratorService.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Refrigerator> response = refrigeratorController.getById(id);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}