package ntnu.idatt2106.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "GroceryShoppingList")
@Schema(description = "Connection table between groceries and the shopping list")
@Entity
public class GroceryShoppingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id for the connection between shopping list and grocery, automatically generated")
    private long id;

    @Column(name = "isRequested")
    @Schema(description = "Boolean value as represents if a limited user or a super user has added the grocery to the list.")
    private boolean isRequest;

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
}
