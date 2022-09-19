package cloud.product.dto.response;

import cloud.product.model.Size;
import cloud.product.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

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
