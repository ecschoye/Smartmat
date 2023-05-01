package ntnu.idatt2106.backend.model.refrigerator;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewRefrigeratorDTO {

    private String userEmail;
    private String name;
    private String address;
}
