package mono.shop_mono.dto.request;

import lombok.Data;
import mono.shop_mono.model.Size;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProductRequest {

    @NotBlank(message = "Name can not be empty")
    private String name;

    @NotNull(message = "Size can not be empty")
    private Size size;

    @NotBlank(message = "Author can not be empty")
    private String author;

    @NotNull(message = "Price can not be empty")
    private Float price;

}