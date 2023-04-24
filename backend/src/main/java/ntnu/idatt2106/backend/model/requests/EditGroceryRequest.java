package ntnu.idatt2106.backend.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EditGroceryRequest {
    private long id;
    private int quantity;
    private long shoppingListId;

    public EditGroceryRequest() {
    }

    public EditGroceryRequest(long id, int quantity, long shoppingListId) {
        this.id = id;
        this.quantity = quantity;
        this.shoppingListId = shoppingListId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(long shoppingListId) {
        this.shoppingListId = shoppingListId;
    }
}
