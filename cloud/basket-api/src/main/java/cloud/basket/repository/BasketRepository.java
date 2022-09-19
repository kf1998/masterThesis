package cloud.basket.repository;

import cloud.basket.model.Basket;
import org.springframework.data.repository.CrudRepository;

public interface BasketRepository extends CrudRepository<Basket, String> {

}