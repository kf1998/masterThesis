package cloud.basket.dto.response;

import cloud.basket.model.Basket;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasketResponse {
    private Integer[] productsId;

    public BasketResponse(Basket basket) {
        productsId = basket.getProductsId();
    }
}
