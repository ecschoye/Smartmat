package ntnu.idatt2106.backend.model.dto.shoppingCartElement;

import ntnu.idatt2106.backend.model.grocery.GroceryShoppingCart;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingList;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorShoppingList;

public class ShoppingCartElementDTO {
    private long id;
    private long groceryId;
    private String name;
    private int quantity;
    private String categoryName;
    public ShoppingCartElementDTO(GroceryShoppingCart element) {
        this.id = element.getId();
        this.groceryId = element.getGrocery().getId();
        this.name = element.getGrocery().getName();
        this.quantity = element.getQuantity();
        this.categoryName = element.getGrocery().getSubCategory().getCategory().getName();
    }

    public ShoppingCartElementDTO(RefrigeratorShoppingList element) {
        this.id = element.getId();
        this.groceryId = element.getGrocery().getId();
        this.name = element.getGrocery().getName();
        this.quantity = element.getQuantity();
        this.categoryName = element.getGrocery().getSubCategory().getCategory().getName();
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
}
