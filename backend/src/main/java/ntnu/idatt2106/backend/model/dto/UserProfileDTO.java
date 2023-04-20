package ntnu.idatt2106.backend.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User profile data transfer object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {

    private String name;
    private String email;
}
