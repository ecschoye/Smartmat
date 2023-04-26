package ntnu.idatt2106.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Grocery")
@Schema(description = "A registered grocery of the application")
@Entity
public class Grocery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id of the grocery, automatically generated")
    private long id;

    @NotNull
    @Column(name = "name")
    @Schema(description = "Name of the grocery")
    private String name;

    @NotNull
    @Column(name = "groceryExpiryDays")
    @Schema(description = "Number of days until expected expiry")
    private int groceryExpiryDays;

    @Column(name = "description")
    @Schema(description = "Description of the grocery")
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "subCategoryId")
    @Schema(description = "The sub category to the grocery")
    private SubCategory subCategory;

}
