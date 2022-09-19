package mono.shop_mono.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import mono.shop_mono.model.Order;
import mono.shop_mono.model.Product;

import java.sql.Timestamp;
import java.util.Collection;

@Data
@AllArgsConstructor
public class OrderResponse {

    private Integer id;
    private String userName;
    private Collection<Product> products;
    private Timestamp orderedAt;
    private String paymentMethod;

    public OrderResponse(Order order) {
        id = order.getId();
        userName = order.getUser().getUsername();
        products = order.getProducts();
        orderedAt = order.getOrderedAt();
        paymentMethod = order.getPaymentMethod();
    }
}