package ntnu.idatt2106.backend.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRefrigeratorGroceryDTO {

    private GroceryDTO groceryDTO;
    private UnitDTO unitDTO;
    private Integer quantity;

}
