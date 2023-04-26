package ntnu.idatt2106.backend.model.dto.refrigerator;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefrigeratorDTO {



    private long id;
    private String name;
    private String address;

}
