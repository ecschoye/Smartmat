package ntnu.idatt2106.backend.model.dto.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ntnu.idatt2106.backend.model.grocery.Grocery;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDTO {
    private SimpleGrocery simpleGrocery;
    private int quantity;
}