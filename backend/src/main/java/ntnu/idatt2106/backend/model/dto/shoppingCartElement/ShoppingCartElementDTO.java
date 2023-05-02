package ntnu.idatt2106.backend.model.dto.shoppingCartElement;

import ntnu.idatt2106.backend.model.grocery.GroceryShoppingCart;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingList;

public class ShoppingCartElementDTO {
    private long id;
    private long groceryId;
    private String description;
    private int quantity;
    private String categoryName;
    private boolean requested;

    public ShoppingCartElementDTO(GroceryShoppingCart element) {
        this.id = element.getId();
        this.groceryId = element.getGrocery().getId();
        this.description = element.getGrocery().getDescription();
        this.quantity = element.getQuantity();
        this.categoryName = element.getGrocery().getSubCategory().getCategory().getName();
    }
    public long getId() {
        return id;
    }

    public long getGroceryId() {
        return groceryId;
    }

    public String getDescription() {
        return description;
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
