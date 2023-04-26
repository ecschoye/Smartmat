package ntnu.idatt2106.backend.model.requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveGroceryRequest {
    private String name;
    private int groceryExpiryDays;
    private String description;
    private long subCategoryId;
    private long foreignKey; //can be used for both shoppingListId and shoppingCartId
    private int quantity;

}
