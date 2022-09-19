package mono.shop_mono.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
public class OrderRequest {

    @NotNull(message = "Product id can not be empty")
    private Collection<Integer> productsId;

    private String paymentMethod;

}