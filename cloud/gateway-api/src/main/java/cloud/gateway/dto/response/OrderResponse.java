package cloud.gateway.dto.response;

import io.micrometer.core.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponse {

    private Integer id;
    private String userName;
    private Integer[] productsId;
    private Timestamp orderedAt;
    private String paymentMethod;
    @Nullable
    private List<ProductResponse> products;
    @Nullable UserResponse user;
}
