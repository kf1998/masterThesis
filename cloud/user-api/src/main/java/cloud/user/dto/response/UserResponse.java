package cloud.user.dto.response;

import cloud.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {

    private String username;
    private String firstName;
    private String lastName;
    private boolean isAdmin;

    public static UserResponse of(User user) {
        return new UserResponse(
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.isAdmin()
        );
    }
}
