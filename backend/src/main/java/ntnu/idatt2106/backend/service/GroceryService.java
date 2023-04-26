package ntnu.idatt2106.backend.service;

import lombok.AllArgsConstructor;
import ntnu.idatt2106.backend.model.Grocery;
import ntnu.idatt2106.backend.model.dto.GroceryDTO;
import ntnu.idatt2106.backend.repository.GroceryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GroceryService {
    private final GroceryRepository groceryRepository;

    public List<GroceryDTO> getAllGroceries() {
        List<Grocery> groceries = groceryRepository.findAll();
        return groceries.stream().map(GroceryDTO::new).collect(Collectors.toList());
    }
}
