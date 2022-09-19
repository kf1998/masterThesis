package mono.shop_mono.dto.request;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class UserRequest {
    @Size(min = 5, message = "Password must be at least 5 characters long")
    private String password;

    private String firstName;

    private String lastName;
}