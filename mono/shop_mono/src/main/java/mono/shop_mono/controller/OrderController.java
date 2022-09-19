package mono.shop_mono.controller;

import lombok.RequiredArgsConstructor;
import mono.shop_mono.dto.request.OrderRequest;
import mono.shop_mono.dto.response.OrderResponse;
import mono.shop_mono.model.Order;
import mono.shop_mono.model.Product;
import mono.shop_mono.model.User;
import mono.shop_mono.service.OrderService;
import mono.shop_mono.service.ProductService;
import mono.shop_mono.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;
    private final UserService userService;

    @GetMapping
    public List<OrderResponse> getAll() {
        return orderService.findAll().stream().map(OrderResponse::new).collect(Collectors.toList());
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public OrderResponse create(@RequestBody @Valid OrderRequest request) {
        List<Product> productsId = request.getProductsId().stream().map(productService::findById).collect(Collectors.toList());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();

        User user = userService.findByUsername(username);

        Timestamp time = new Timestamp(System.currentTimeMillis());

        String paymentMethod = request.getPaymentMethod();

        Order order = new Order(user, productsId, time, paymentMethod);

        return new OrderResponse(orderService.create(order));
    }

    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public OrderResponse update(@PathVariable int id, @RequestBody OrderRequest request) {
        Order order = orderService.findById(id, getUser());
        if (request.getPaymentMethod() != null) {
            order.setPaymentMethod(request.getPaymentMethod());
        }
        return new OrderResponse(orderService.create(order));
    }

    @GetMapping("/{id}")
    public OrderResponse getById(@PathVariable int id) {
        return new OrderResponse(orderService.findById(id, getUser()));
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}