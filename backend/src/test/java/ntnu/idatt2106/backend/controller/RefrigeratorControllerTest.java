package ntnu.idatt2106.backend.controller;

import ntnu.idatt2106.backend.exceptions.SaveException;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.service.RefrigeratorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class RefrigeratorControllerTest {

    @Mock
    private RefrigeratorService refrigeratorService;

    @InjectMocks
    private RefrigeratorController refrigeratorController;

    @Test
    public void testCreateRefrigerator() throws SaveException {
        Refrigerator refrigerator = new Refrigerator();
        refrigerator.setName("testName");
        refrigerator.setAddress("testAddress");

        when(refrigeratorService.save(refrigerator)).thenReturn(refrigerator);

        ResponseEntity<Refrigerator> responseEntity = refrigeratorController.createRefrigerator(refrigerator);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(refrigerator, responseEntity.getBody());
    }

    @Test
    public void testDeleteRefrigerator() {
        int refrigeratorId = 1;

        when(refrigeratorService.delete(refrigeratorId)).thenReturn(true);

        ResponseEntity<Void> responseEntity = refrigeratorController.deleteRefrigerator(refrigeratorId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testGetAll() {
        List<Refrigerator> refrigerators = Arrays.asList(new Refrigerator(), new Refrigerator());

        when(refrigeratorService.getAllRefrigerators()).thenReturn(refrigerators);

        ResponseEntity<List<Refrigerator>> responseEntity = refrigeratorController.getAll();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(refrigerators, responseEntity.getBody());
    }

    @Test
    public void testGetById() {
        int refrigeratorId = 1;
        Refrigerator refrigerator = new Refrigerator();
        refrigerator.setId(refrigeratorId);

        when(refrigeratorService.findById(refrigeratorId)).thenReturn(Optional.of(refrigerator));

        ResponseEntity<Refrigerator> responseEntity = refrigeratorController.getById(refrigeratorId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(refrigerator, responseEntity.getBody());
    }

    @Test
    public void testCreateRefrigeratorFail() throws SaveException {
        // Create an invalid refrigerator
        Refrigerator refrigerator = new Refrigerator();
        refrigerator.setName(null);
        refrigerator.setAddress("123 Main Street");

        // Expect a SaveException to be thrown when attempting to create the refrigerator
        assertThrows(SaveException.class, () -> {
            refrigeratorController.createRefrigerator(refrigerator);
        });
    }

    @Test
    public void testDeleteRefrigeratorFail() {
        // Attempt to delete a refrigerator that doesn't exist
        ResponseEntity<Void> response = refrigeratorController.deleteRefrigerator(999);

        // Expect a NOT_FOUND status code to be returned
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetByIdFail() {
        // Attempt to get a refrigerator that doesn't exist
        ResponseEntity<Refrigerator> response = refrigeratorController.getById(999);

        // Expect a NOT_FOUND status code to be returned
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}