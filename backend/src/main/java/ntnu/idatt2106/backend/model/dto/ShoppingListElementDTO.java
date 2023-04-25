package ntnu.idatt2106.backend.model.dto;

import ntnu.idatt2106.backend.model.GroceryShoppingList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShoppingListElementDTO {
    private long id;
    private long groceryId;
    private String name;
    private int quantity;
    private String subCategoryName;
    private boolean requested;
    public ShoppingListElementDTO(GroceryShoppingList element) {
        this.id = element.getId();
        this.groceryId = element.getGrocery().getId();
        this.name = element.getGrocery().getName();
        this.quantity = element.getQuantity();
        this.subCategoryName = element.getGrocery().getSubCategory().getName();
        this.requested = element.isRequest();
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

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public boolean isRequested() {
        return requested;
    }
}
