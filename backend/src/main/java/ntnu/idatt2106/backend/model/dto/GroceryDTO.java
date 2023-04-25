package ntnu.idatt2106.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ntnu.idatt2106.backend.model.Category;

/**
 * Grocery DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroceryDTO {
    private long id;
    private String name;
    private String description;
    private int groceryExpiryDate;
    private Category category;
}
