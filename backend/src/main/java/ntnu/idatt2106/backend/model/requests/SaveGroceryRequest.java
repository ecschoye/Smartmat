package ntnu.idatt2106.backend.model.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ntnu.idatt2106.backend.model.dto.GroceryDTO;

/**
 * Request to save a grocery
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveGroceryRequest {

    @NotNull
    private long refrigeratorId;
    @NotNull
    private boolean custom;
    @NotNull
    private GroceryDTO grocery;
}
