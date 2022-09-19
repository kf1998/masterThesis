package cloud.order.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
public class OrderRequest {

    @NotNull(message = "Products can not be empty")
    private Collection<ProductRequest> products;

    @NotNull(message = "User can not be empty")
    private UserRequest user;

    private String paymentMethod;
}
