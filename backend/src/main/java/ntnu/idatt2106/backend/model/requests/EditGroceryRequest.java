package ntnu.idatt2106.backend.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EditGroceryRequest {
    private long id;
    @JsonProperty("isRequested")
    private boolean requested;
    private int quantity;
    private long shoppingListId;

    public EditGroceryRequest() {
    }

    public EditGroceryRequest(long id, boolean requested, int quantity, long shoppingListId) {
        this.id = id;
        this.requested = requested;
        this.quantity = quantity;
        this.shoppingListId = shoppingListId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isRequested() {
        return requested;
    }

    public void setRequested(boolean requested) {
        this.requested = requested;
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
