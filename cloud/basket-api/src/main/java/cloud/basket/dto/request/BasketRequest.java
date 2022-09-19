package cloud.basket.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
public class BasketRequest {
    @NotNull(message = "Products can not be empty")
    private Collection<ProductRequest> products;
}
