package cloud.order.service;

import cloud.order.dto.exception.NotFoundException;
import cloud.order.repository.OrderRepository;
import cloud.order.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Order findById(Integer orderId) {
        return orderRepository
                .findById(orderId)
                .orElseThrow(() -> new NotFoundException(String.format("Order with id %s does not exist", orderId)));
    }

    @Transactional
    public Order create(Order order) {
        return orderRepository.save(order);
    }

    @Transactional
    public List<Order> findByUsername(String username) {
        return orderRepository.findByUsername(username);
    }
}
