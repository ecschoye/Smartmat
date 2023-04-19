package ntnu.idatt2106.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class FridgeGrocery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id for the connection between fridge and grocery, automatically generated")
    private int id;

    @Column(name = "physicalExpireDate")
    @Schema(description = "Expire date for the grocery")
    private Date physicalExpireDate;

    @ManyToOne
    @JoinColumn(name = "fridgeId")
    @Schema(description = "The fridge the grocery is in")
    private Fridge fridge;

    @ManyToOne
    @JoinColumn(name = "groceryId")
    @Schema(description = "Grocery in the fridge")
    private Grocery grocery;
}
