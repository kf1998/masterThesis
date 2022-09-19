package cloud.product.controller;

import cloud.product.dto.request.ProductRequest;
import cloud.product.model.Size;
import cloud.product.model.Product;
import cloud.product.service.search.ProductSearchParameters;
import cloud.product.dto.response.ProductResponse;
import cloud.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductResponse> getAll(@RequestParam(required = false) String name,
                                        @RequestParam(required = false) Size size,
                                        @RequestParam(required = false) String author,
                                        @RequestParam(required = false) Float priceFrom,
                                        @RequestParam(required = false) Float priceTo) {
        ProductSearchParameters searchParams = ProductSearchParameters.builder()
                .name(name)
                .author(author)
                .size(size)
                .priceFrom(priceFrom)
                .priceTo(priceTo)
                .build();

        return productService.findAllByParameters(searchParams).stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable int id) {
        return new ProductResponse(productService.findById(id));
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ProductResponse create(@ModelAttribute @Valid ProductRequest request) {
        Product product = new Product(
                request.getName(),
                request.getAuthor(),
                request.getPrice(),
                request.getSize()
        );
        return new ProductResponse(productService.create(product));
    }

    @RequestMapping(path = "/{id}", method = {POST, PUT}, consumes ={MediaType.MULTIPART_FORM_DATA_VALUE})
    public ProductResponse edit(@PathVariable int id, @ModelAttribute @Valid ProductRequest request) {
        Product newProduct = new Product(id, request);
        return new ProductResponse(productService.update(newProduct));
    }

    @DeleteMapping(path="/{id}")
    public void delete(@PathVariable Integer id) {
        productService.delete(id);
    }
}
