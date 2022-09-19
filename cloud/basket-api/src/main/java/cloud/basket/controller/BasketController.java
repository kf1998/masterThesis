package cloud.basket.controller;

import cloud.basket.dto.request.BasketRequest;
import cloud.basket.dto.request.ProductRequest;
import cloud.basket.dto.response.BasketResponse;
import cloud.basket.model.Basket;
import cloud.basket.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/basket")
@RequiredArgsConstructor
public class BasketController {
    private final BasketService basketService;

    @GetMapping("/{username}")
    public BasketResponse getById(@PathVariable String username) {
        return new BasketResponse(basketService.findByUsername(username));
    }

    @PutMapping(path = "/{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BasketResponse update(@PathVariable String username, @RequestBody BasketRequest request) {
        Integer[] productsId = request.getProducts()
                .stream()
                .map(ProductRequest::getId)
                .toArray(Integer[]::new);

        Basket basket = new Basket(username, productsId);
        return new BasketResponse(basketService.create(basket));
    }
}