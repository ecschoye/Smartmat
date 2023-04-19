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
@Table(name = "Fridge")
@Schema(description = "A registered fridge of the application")
@Entity
public class Fridge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id to the fridge, automatically generated")
    private int id;

    @Column(name = "name")
    @Schema(description = "The defined name to the fridge")
    private String name;

    @Column(name = "address")
    @Schema(description = "The address to the fridge")
    private String address;
}
