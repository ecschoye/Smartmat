package ntnu.idatt2106.backend.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ntnu.idatt2106.backend.model.enums.AuthenticationState;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStatusResponse {

    private AuthenticationState state;
    private String role;
}
