package ntnu.idatt2106.backend.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteRefrigeratorGroceryDTO {
    private RefrigeratorGroceryDTO refrigeratorGroceryDTO;
    private Integer quantity;
    private UnitDTO unitDTO;
}
