package ntnu.idatt2106.backend.model.requests;

import ntnu.idatt2106.backend.model.ShoppingCart;
import ntnu.idatt2106.backend.model.ShoppingList;
import ntnu.idatt2106.backend.model.SubCategory;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingCart;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class SaveGroceryRequestTest {

    private final String NAME = "Test Grocery";
    private final int GROCERY_EXPIRY_DAYS = 7;
    private final String DESCRIPTION = "This is a test grocery";
    private final long SUB_CATEGORY_ID = 1L;
    private SubCategory SUB_CATEGORY = new SubCategory();
    private final long FOREIGN_KEY = 1L;
    private final int QUANTITY = 10;

    @BeforeEach
    public void setup(){
        SUB_CATEGORY.setId(SUB_CATEGORY_ID);
    }

    @Test
    public void testBuilder() {
        SaveGroceryRequest request = SaveGroceryRequest.builder()
                .name(NAME)
                .groceryExpiryDays(GROCERY_EXPIRY_DAYS)
                .description(DESCRIPTION)
                .subCategoryId(SUB_CATEGORY_ID)
                .foreignKey(FOREIGN_KEY)
                .quantity(QUANTITY)
                .build();

        assertNotNull(request);
        assertEquals(NAME, request.getName());
        assertEquals(GROCERY_EXPIRY_DAYS, request.getGroceryExpiryDays());
        assertEquals(DESCRIPTION, request.getDescription());
        assertEquals(SUB_CATEGORY_ID, request.getSubCategoryId());
        assertEquals(FOREIGN_KEY, request.getForeignKey());
        assertEquals(QUANTITY, request.getQuantity());
    }

    @Test
    public void testConstructorWithGroceryShoppingList() {
        Grocery grocery = Grocery.builder()
                .name(NAME)
                .groceryExpiryDays(GROCERY_EXPIRY_DAYS)
                .description(DESCRIPTION)
                .subCategory(SUB_CATEGORY)
                .build();
        GroceryShoppingList listItem = GroceryShoppingList.builder()
                .grocery(grocery)
                .quantity(QUANTITY)
                .shoppingList(new ShoppingList())
                .build();

        SaveGroceryRequest request = new SaveGroceryRequest(listItem);

        assertNotNull(request);
        assertEquals(NAME, request.getName());
        assertEquals(GROCERY_EXPIRY_DAYS, request.getGroceryExpiryDays());
        assertEquals(DESCRIPTION, request.getDescription());
        assertEquals(SUB_CATEGORY_ID, request.getSubCategoryId());
        assertEquals(listItem.getShoppingList().getId(), request.getForeignKey());
        assertEquals(QUANTITY, request.getQuantity());
    }

    @Test
    public void testConstructorWithGroceryShoppingCart() {
        Grocery grocery = Grocery.builder()
                .name(NAME)
                .groceryExpiryDays(GROCERY_EXPIRY_DAYS)
                .description(DESCRIPTION)
                .subCategory(SUB_CATEGORY)
                .build();
        GroceryShoppingCart listItem = GroceryShoppingCart.builder()
                .grocery(grocery)
                .shoppingCart(new ShoppingCart())
                .quantity(QUANTITY)
                .build();

        SaveGroceryRequest request = new SaveGroceryRequest(listItem);

        assertNotNull(request);
        assertEquals(NAME, request.getName());
        assertEquals(GROCERY_EXPIRY_DAYS, request.getGroceryExpiryDays());
        assertEquals(DESCRIPTION, request.getDescription());
        assertEquals(SUB_CATEGORY_ID, request.getSubCategoryId());
        assertEquals(listItem.getShoppingCart().getId(), request.getForeignKey());
        assertEquals(QUANTITY, request.getQuantity());
    }

    @Test
    public void testEquals() {
        SaveGroceryRequest cart1 = new SaveGroceryRequest();
        cart1.setName("name");

        SaveGroceryRequest cart2 = new SaveGroceryRequest();
        cart2.setName("name");

        SaveGroceryRequest cart3 = new SaveGroceryRequest();
        cart3.setName("name2");

        assertEquals(cart1, cart2); // Test if two shopping carts with the same ID are equal
        assertNotEquals(cart1, cart3); // Test if two shopping carts with different IDs are not equal
    }

    @Test
    public void testHashCode() {
        SaveGroceryRequest cart1 = new SaveGroceryRequest();
        cart1.setName("name");

        SaveGroceryRequest cart2 = new SaveGroceryRequest();
        cart2.setName("name");

        SaveGroceryRequest cart3 = new SaveGroceryRequest();
        cart3.setName("name2");

        assertEquals(cart1.hashCode(), cart2.hashCode()); // Test if two shopping carts with the same ID have the same hash code
        assertNotEquals(cart1.hashCode(), cart3.hashCode()); // Test if two shopping carts with different IDs have different hash codes
    }

    @Test
    public void testGettersAndSetters() {
        SaveGroceryRequest request = new SaveGroceryRequest();
        request.setName("Milk");
        request.setGroceryExpiryDays(7);
        request.setDescription("Organic whole milk");
        request.setSubCategoryId(2);
        request.setForeignKey(3);
        request.setQuantity(1);

        assertEquals("Milk", request.getName());
        assertEquals(7, request.getGroceryExpiryDays());
        assertEquals("Organic whole milk", request.getDescription());
        assertEquals(2, request.getSubCategoryId());
        assertEquals(3, request.getForeignKey());
        assertEquals(1, request.getQuantity());
    }

    @Test
    public void testToString() {
        SaveGroceryRequest request = SaveGroceryRequest.builder()
                .name("Milk")
                .groceryExpiryDays(7)
                .description("Organic whole milk")
                .subCategoryId(2)
                .foreignKey(3)
                .quantity(1)
                .build();

        String expected = "SaveGroceryRequest(name=Milk, groceryExpiryDays=7, description=Organic whole milk, " +
                "subCategoryId=2, foreignKey=3, quantity=1)";
        assertEquals(expected, request.toString());
    }
}
