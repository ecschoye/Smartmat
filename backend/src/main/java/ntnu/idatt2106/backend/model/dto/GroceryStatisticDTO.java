package ntnu.idatt2106.backend.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroceryStatisticDTO {
    private String monthName;
    private Integer foodEaten;
    private Integer foodWaste;
}
