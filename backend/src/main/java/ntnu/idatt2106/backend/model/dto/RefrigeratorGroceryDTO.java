package ntnu.idatt2106.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefrigeratorGroceryDTO {
    private long id;
    private Date physicalExpireDate;
    private GroceryDTO grocery;
}
