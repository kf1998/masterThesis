package mono.shop_mono.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import mono.shop_mono.model.Product;
import mono.shop_mono.model.Size;

@Data
@AllArgsConstructor
public class ProductResponse {

    private int id;
    private String name;
    private Size size;
    private String author;
    private Float price;

    public ProductResponse(Product product) {
        id = product.getId();
        name = product.getName();
        size = product.getSize();
        author = product.getAuthor();
        price = product.getPrice();
    }

}