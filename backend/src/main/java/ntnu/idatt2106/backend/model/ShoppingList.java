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
@Table(name = "ShoppingList")
@Schema(description = "A registered fridge of the application")
@Entity
public class ShoppingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id to the fridge, automatically generated")
    private int id;

    @OneToOne
    @JoinColumn(name = "fridgeId")
    @Schema(description = "The fridge connected to the shopping list")
    private Fridge fridge;
}
