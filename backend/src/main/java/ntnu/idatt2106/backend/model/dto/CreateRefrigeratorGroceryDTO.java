package ntnu.idatt2106.backend.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRefrigeratorGroceryDTO {

    private GroceryDTO groceryDTO;
    private UnitDTO unitDTO;
    private Integer quantity;

}
