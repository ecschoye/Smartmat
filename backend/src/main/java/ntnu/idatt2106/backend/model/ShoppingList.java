package ntnu.idatt2106.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ShoppingList")
@Schema(description = "A registered refrigerator of the application")
@Entity
public class ShoppingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id to the refrigerator, automatically generated")
    private long id;

    @OneToOne()
    @JoinColumn(name = "refrigeratorId")
    @Schema(description = "The refrigerator connected to the shopping list")
    private Refrigerator refrigerator;

    /**TODO Test if this causes groceries to be deleted when deleting ShoppingList
    @OneToMany(mappedBy = "shoppingList", cascade = CascadeType.REMOVE)
    @Schema(description = "The grocery items on the shopping list")
    private Set<GroceryShoppingList> groceryItems;*/
}

