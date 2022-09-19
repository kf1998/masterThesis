package cloud.order.controller;

import cloud.order.dal.OrderDAL;
import cloud.order.dto.request.OrderRequest;
import cloud.order.dto.request.ProductRequest;
import cloud.order.dto.response.OrderResponse;
import cloud.order.model.Order;
import cloud.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private final OrderRepository orderRepository;
    private final OrderDAL orderDAL;

    public OrderController(OrderRepository orderRepository, OrderDAL orderDAL){
        this.orderRepository = orderRepository;
        this.orderDAL = orderDAL;
    }

    @GetMapping
    public List<OrderResponse> getAll() {
        return orderRepository.findAll().stream().map(OrderResponse::new).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrderResponse getById(@PathVariable int id) {
        return new OrderResponse(orderDAL.findById(id));
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public OrderResponse create(@RequestBody @Valid OrderRequest request) {
        Integer[] productsId = request.getProducts()
                .stream()
                .map(ProductRequest::getId)
                .toArray(Integer[]::new);

        Timestamp time = new Timestamp(System.currentTimeMillis());

        Order order = new Order(request.getUser().getUsername(), productsId, time, request.getPaymentMethod());
        Order createdOrder = orderDAL.create(order);

        return new OrderResponse(createdOrder);
    }

    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public OrderResponse update(@PathVariable int id, @RequestBody OrderRequest request) {
        Order order = orderDAL.findById(id);
        if (request.getPaymentMethod() != null) {
            order.setPaymentMethod(request.getPaymentMethod());
        }

        return new OrderResponse(orderDAL.create(order));
    }
}
