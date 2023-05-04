package ntnu.idatt2106.backend.model.dto.shoppingListElement;

import ntnu.idatt2106.backend.model.dto.UnitDTO;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingList;

public class ShoppingListElementDTO {
    private long id;
    private long groceryId;
    private String description;
    private int quantity;
    private UnitDTO unitDTO;
    private String categoryName;
    private boolean requested;
    public ShoppingListElementDTO(GroceryShoppingList element) {
        this.id = element.getId();
        this.groceryId = element.getGrocery().getId();
        this.description = element.getGrocery().getDescription();
        this.quantity = element.getQuantity();
        this.unitDTO = new UnitDTO(element.getUnit());
        this.categoryName = element.getGrocery().getSubCategory().getCategory().getName();
        this.requested = element.isRequest();
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

    public UnitDTO getUnitDTO() {
        return unitDTO;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public boolean isRequested() {
        return requested;
    }
}
