package cloud.order.dto.response;

import cloud.order.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class OrderResponse {

    private Integer id;
    private String userName;
    private Integer[] productsId;
    private Timestamp orderedAt;
    private String paymentMethod;

    public OrderResponse(Order order) {
        id = order.getId();
        userName = order.getUsername();
        productsId = order.getProductsId();
        orderedAt = order.getOrderedAt();
        paymentMethod = order.getPaymentMethod();
    }
}
