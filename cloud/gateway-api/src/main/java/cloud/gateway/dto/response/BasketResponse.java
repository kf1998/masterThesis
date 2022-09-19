package cloud.gateway.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
@AllArgsConstructor
public class BasketResponse {
    private Integer[] productsId;

    @Nullable
    private List<ProductResponse> products;
}
