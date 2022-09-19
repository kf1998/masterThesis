package cloud.user.dto.request;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UserUpdateRequest {
    @Size(min = 5, message = "Password must be at least 5 characters long")
    private String password;

    private String firstName;

    private String lastName;
}
