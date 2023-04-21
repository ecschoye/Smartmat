package ntnu.idatt2106.backend.model.requests;

public class SaveGroceryRequest {
    private String name;
    private int groceryExpiryDays;
    private String description;
    private long subCategoryId;
    private long shoppingListId;
    private int quantity;

    public SaveGroceryRequest() {
    }

    public SaveGroceryRequest(String name, int groceryExpiryDays, String description, long subCategoryId, long shoppingListId, int quantity) {
        this.name = name;
        this.groceryExpiryDays = groceryExpiryDays;
        this.description = description;
        this.subCategoryId = subCategoryId;
        this.shoppingListId = shoppingListId;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGroceryExpiryDays() {
        return groceryExpiryDays;
    }

    public void setGroceryExpiryDays(int groceryExpiryDays) {
        this.groceryExpiryDays = groceryExpiryDays;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public long getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(long shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
