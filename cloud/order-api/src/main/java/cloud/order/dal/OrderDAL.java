package cloud.order.dal;

import cloud.order.model.Order;

import java.util.List;

public interface OrderDAL {
    List<Order> findAll();

    Order findById(Integer orderId);

    Order create(Order order);

    List<Order> findByUsername(String username);
}
