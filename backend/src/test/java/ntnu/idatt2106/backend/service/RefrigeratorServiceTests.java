package ntnu.idatt2106.backend.service;

import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.repository.RefrigeratorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefrigeratorServiceTests {

    @Mock
    private RefrigeratorRepository repository;

    @InjectMocks
    private RefrigeratorService service;

    private Refrigerator refrigerator;

    @BeforeEach
    void setUp() {
        refrigerator = new Refrigerator();
        refrigerator.setName("Example Name");
        refrigerator.setAddress("Example Address");
    }

    @Test
    public void testSaveRefrigeratorSuccess() {
        when(repository.save(refrigerator)).thenReturn(refrigerator);

        Refrigerator saved = service.save(refrigerator);

        assertNotNull(saved);
        assertEquals(refrigerator.getName(), saved.getName());
        assertEquals(refrigerator.getAddress(), saved.getAddress());

        verify(repository, times(1)).save(refrigerator);
    }

    @Test
    public void testFindByIdSuccess() {
        when(repository.findById(1L)).thenReturn(Optional.of(refrigerator));

        Optional<Refrigerator> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(refrigerator.getName(), result.get().getName());
        assertEquals(refrigerator.getAddress(), result.get().getAddress());

        verify(repository, times(1)).findById(1L);
    }

    @Test
    public void testGetAllRefrigeratorsSuccess() {
        List<Refrigerator> refrigeratorList = new ArrayList<>();
        refrigeratorList.add(refrigerator);

        when(repository.findAll()).thenReturn(refrigeratorList);

        List<Refrigerator> result = service.getAllRefrigerators();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(refrigerator.getName(), result.get(0).getName());
        assertEquals(refrigerator.getAddress(), result.get(0).getAddress());

        verify(repository, times(1)).findAll();
    }

    @Test
    public void testDeleteRefrigeratorSuccess() {
        when(repository.existsById(1L)).thenReturn(true);

        boolean result = service.delete(1L);

        assertTrue(result);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testSaveRefrigeratorWithoutName() {
        refrigerator.setName(null);
        assertNull(service.save(refrigerator));
    }

    @Test
    void testSaveRefrigeratorWithoutAddress() {
        refrigerator.setAddress(null);
        assertNull(service.save(refrigerator));
    }

    @Test
    void testDeleteNonExistentRefrigerator() {
        Mockito.when(repository.existsById(Mockito.anyLong())).thenReturn(false);
        assertFalse(service.delete(1L));
    }

    @Test
    void testDeleteRefrigeratorThrowsEmptyResultDataAccessException() {
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(Mockito.anyLong());
        Mockito.when(repository.existsById(Mockito.anyLong())).thenReturn(true);
        assertFalse(service.delete(1L));
    }

    @Test
    void testFindByIdReturnsEmptyOptional() {
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        assertTrue(service.findById(1L).isEmpty());
    }

    @Test
    void testGetAllRefrigeratorsReturnsEmptyList() {
        Mockito.when(repository.findAll()).thenReturn(new ArrayList<>());
        assertEquals(new ArrayList<>(), service.getAllRefrigerators());
    }
}
