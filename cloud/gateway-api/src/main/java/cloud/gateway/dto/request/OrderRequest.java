package cloud.gateway.dto.request;

import cloud.gateway.dto.response.UserResponse;
import cloud.gateway.dto.response.ProductResponse;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
public class OrderRequest {

    @NotNull(message = "Products can not be empty")
    private Collection<ProductResponse> products;

    @NotNull(message = "User can not be empty")
    private UserResponse user;

    private String paymentMethod;
}
