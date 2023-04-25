package ntnu.idatt2106.backend.model.dto;

import ntnu.idatt2106.backend.model.GroceryShoppingCart;
import ntnu.idatt2106.backend.model.GroceryShoppingList;

public class ShoppingListElementDTO {
    private long id;
    private long groceryId;
    private String name;
    private int quantity;
    private String categoryName;
    private boolean requested;
    public ShoppingListElementDTO(GroceryShoppingList element) {
        this.id = element.getId();
        this.groceryId = element.getGrocery().getId();
        this.name = element.getGrocery().getName();
        this.quantity = element.getQuantity();
        this.categoryName = element.getGrocery().getSubCategory().getCategory().getName();
        this.requested = element.isRequest();
    }
    public ShoppingListElementDTO(GroceryShoppingCart element) {
        this.id = element.getId();
        this.groceryId = element.getGrocery().getId();
        this.name = element.getGrocery().getName();
        this.quantity = element.getQuantity();
        this.categoryName = element.getGrocery().getSubCategory().getCategory().getName();
        this.requested = false; //TODO: Must either create an own dto or implement requested for groceries in cart
    }
    public long getId() {
        return id;
    }

    public long getGroceryId() {
        return groceryId;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public boolean isRequested() {
        return requested;
    }
}
