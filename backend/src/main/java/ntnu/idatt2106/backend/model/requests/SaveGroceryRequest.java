package ntnu.idatt2106.backend.model.requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingCart;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingList;

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

    public SaveGroceryRequest(GroceryShoppingList listItem) {
        this.name = listItem.getGrocery().getName();
        this.groceryExpiryDays = listItem.getGrocery().getGroceryExpiryDays();
        this.description = listItem.getGrocery().getDescription();
        this.subCategoryId = listItem.getGrocery().getSubCategory().getId();
        this.foreignKey = listItem.getShoppingList().getId();
        this.quantity = listItem.getQuantity();
    }

    public SaveGroceryRequest(GroceryShoppingCart listItem) {
        this.name = listItem.getGrocery().getName();
        this.groceryExpiryDays = listItem.getGrocery().getGroceryExpiryDays();
        this.description = listItem.getGrocery().getDescription();
        this.subCategoryId = listItem.getGrocery().getSubCategory().getId();
        this.foreignKey = listItem.getShoppingCart().getId();
        this.quantity = listItem.getQuantity();
    }
}
