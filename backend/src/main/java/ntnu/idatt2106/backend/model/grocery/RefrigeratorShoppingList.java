package ntnu.idatt2106.backend.model.grocery;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ntnu.idatt2106.backend.model.ShoppingList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RefrigeratorShoppingList")
@Schema(description = "Connection table between groceries and the shopping list. The entity is intended to contain" +
        " groceries which is removed from the refrigerator")
@Entity
public class RefrigeratorShoppingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id for the connection between shopping list and grocery, automatically generated")
    private long id;

    @Column(name = "quantity")
    @Schema(description = "The number of groceries to buy")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "groceryId")
    @Schema(description = "Grocery object in the list")
    private Grocery grocery;

    @ManyToOne
    @JoinColumn(name = "shoppingListId")
    @Schema(description = "The shopping list connected to the grocery")
    private ShoppingList shoppingList;

    /**
     * Increments the quantity
     * @return New quantity
     */
    public int incrementQuantity() {
        return ++quantity;
    }
}
