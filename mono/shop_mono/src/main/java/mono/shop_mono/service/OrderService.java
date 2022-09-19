package mono.shop_mono.service;

import lombok.RequiredArgsConstructor;
import mono.shop_mono.dto.exception.ForbiddenException;
import mono.shop_mono.dto.exception.NotFoundException;
import mono.shop_mono.model.Order;
import mono.shop_mono.model.User;
import mono.shop_mono.model.UserRepository;
import mono.shop_mono.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order create(Order order) {
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Order findById(Integer orderId, User user) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new NotFoundException(String.format("Order with id %s does not exist", orderId)));

        if (!order.getUser().getUsername().equals(user.getUsername()) && !user.isAdmin())
            throw new ForbiddenException("User can not access this order");

        return order;
    }

    @Transactional
    public List<Order> findByUsername(String username) {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException(String.format("User with username %s does not exist", username)));
        return orderRepository.findByUser(user);
    }
}