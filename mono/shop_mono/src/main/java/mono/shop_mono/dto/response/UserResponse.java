package mono.shop_mono.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import mono.shop_mono.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@AllArgsConstructor
public class UserResponse {
    @JsonInclude(NON_NULL)
    private String token;

    private String username;
    private String firstName;
    private String lastName;
    private boolean isAdmin;


    public UserResponse(User user) {
        username = user.getUsername();
        firstName = user.getFirstname();
        lastName = user.getLastname();
        isAdmin = user.isAdmin();
    }

}