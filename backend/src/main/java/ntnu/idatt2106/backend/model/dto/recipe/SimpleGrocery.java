package ntnu.idatt2106.backend.model.dto.recipe;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleGrocery {
    private long id;
    private String name;
}