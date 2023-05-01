package ntnu.idatt2106.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorGrocery;

import java.util.Date;

/**
 * RefrigeratorGrocery DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefrigeratorGroceryDTO {
    private long id;
    private Date physicalExpireDate;
    private GroceryDTO grocery;

    public RefrigeratorGroceryDTO(RefrigeratorGrocery refrigeratorGrocery) {
        this.id = refrigeratorGrocery.getId();
        this.physicalExpireDate = refrigeratorGrocery.getPhysicalExpireDate();
        this.grocery = new GroceryDTO(refrigeratorGrocery.getGrocery());
    }
}
