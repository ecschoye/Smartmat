package ntnu.idatt2106.backend.model.grocery;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.Unit;
import ntnu.idatt2106.backend.model.grocery.Grocery;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RefrigeratorGrocery")
@Schema(description = "Connection between the groceries and refrigerators")
@Entity
public class RefrigeratorGrocery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id for the connection between refrigerator and grocery, automatically generated")
    private long id;

    @NotNull
    @Column(name = "physicalExpireDate")
    @Schema(description = "Expire date for the grocery")
    private LocalDate physicalExpireDate;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "refrigeratorId")
    @Schema(description = "The refrigerator the grocery is in")
    private Refrigerator refrigerator;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "groceryId")
    @Schema(description = "Grocery in the refrigerator")
    private Grocery grocery;


    @ManyToOne
    @JoinColumn(name = "unitId")
    @Schema(description = "The unit of a grocery")
    private Unit unit;

    @Column(name = "quantity")
    @Schema(description = "THe quantity of specified units")
    private int quantity;




    @OneToMany(mappedBy = "groceryEntity", cascade = CascadeType.REMOVE)
    private Set<GroceryNotification> groceryNotifications = new HashSet<>();
}
