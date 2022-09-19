package cloud.product.service.search;

import cloud.product.model.Size;
import cloud.product.model.Product;
import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Builder
@Value
public class ProductSearchParameters {
    String name;
    String author;
    Size size;
    Float priceFrom;
    Float priceTo;

    public Product toProductExample() {
        return new Product(
                name,
                author,
                null,
                size
        );
    }

    public Predicate<Product> toNumberPredicates() {
        List<Predicate<Product>> predicates = new ArrayList<>();

        if (priceFrom != null) predicates.add(p -> p.getPrice() >= priceFrom);
        if (priceTo != null) predicates.add(p -> p.getPrice() <= priceTo);

        return predicates.stream().reduce(p -> true, Predicate::and);
    }

}