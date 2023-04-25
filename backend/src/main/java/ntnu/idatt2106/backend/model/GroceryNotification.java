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
@Table(name = "GroceryNotification")
@Schema(description = "A notification regarding a grocery entity, for example a grocery entity is expiring soon")
@Entity
public class GroceryNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id of a grocery notification")
    private long id;

    @ManyToOne
    @Column(name = "user")
    @Schema(description = "User which owns the notification")
    private User user;

    @ManyToOne
    @Column(name = "groceryEntity")
    @Schema(description = "The grocery entity which the notification regards")
    private RefrigeratorGrocery groceryEntity;

}
