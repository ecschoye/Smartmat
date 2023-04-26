package ntnu.idatt2106.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ntnu.idatt2106.backend.model.Category;
import ntnu.idatt2106.backend.model.Grocery;

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
    private int groceryExpiryDays;
    private Category category;
    private boolean custom;

    public GroceryDTO(Grocery grocery) {
        this.id = grocery.getId();
        this.name = grocery.getName();
        this.description = grocery.getDescription();
        this.groceryExpiryDays = grocery.getGroceryExpiryDays();
        this.category = grocery.getSubCategory().getCategory();
    }
}
