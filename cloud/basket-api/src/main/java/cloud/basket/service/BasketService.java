package cloud.basket.service;

import cloud.basket.dto.exception.NotFoundException;
import cloud.basket.model.Basket;
import cloud.basket.repository.BasketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;

    @Transactional(readOnly = true)
    public List<Basket> findAll() {
        return (List<Basket>) basketRepository.findAll();
    }

    @Transactional
    public Basket create(Basket order) {
        return basketRepository.save(order);
    }

    @Cacheable(value = "basketCache")
    public Basket findByUsername(String username) {
        return basketRepository.findById(username).orElseThrow(() -> new NotFoundException(String.format("Basket for %s does not exist", username)));
    }
}