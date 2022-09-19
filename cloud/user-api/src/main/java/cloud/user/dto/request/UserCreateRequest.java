package cloud.user.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserCreateRequest {

    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
    @NotBlank(message = "Username can not be empty")
    private String username;

    @Size(min = 5, message = "Password must be at least 5 characters long")
    @NotBlank(message = "Password can not be empty")
    private String password;

    @NotBlank(message = "First name can not be empty")
    private String firstName;

    @NotBlank(message = "Last name can not be empty")
    private String lastName;
}
