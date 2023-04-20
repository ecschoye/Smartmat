package ntnu.idatt2106.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    private int id;

    @Column(name = "physicalExpireDate")
    @Schema(description = "Expire date for the grocery")
    private Date physicalExpireDate;

    @ManyToOne
    @JoinColumn(name = "refrigeratorId")
    @Schema(description = "The refrigerator the grocery is in")
    private Refrigerator refrigerator;

    @ManyToOne
    @JoinColumn(name = "groceryId")
    @Schema(description = "Grocery in the refrigerator")
    private Grocery grocery;
}
