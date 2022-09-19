package mono.shop_mono.repository;

import mono.shop_mono.model.Order;
import mono.shop_mono.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    public List<Order> findByUser(User user);
}