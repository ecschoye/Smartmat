package ntnu.idatt2106.backend.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditGroceryRequest {
    private long id;
    private int quantity;
    private long shoppingListId;
}
