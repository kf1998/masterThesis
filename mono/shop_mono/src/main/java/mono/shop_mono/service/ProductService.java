package mono.shop_mono.service;

import lombok.RequiredArgsConstructor;
import mono.shop_mono.dto.exception.NotFoundException;
import mono.shop_mono.model.Product;
import mono.shop_mono.repository.ProductRepository;
import mono.shop_mono.service.search.ProductSearchParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product create(Product product) {
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Product findById(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Product with id %d not found", id)));
    }

    @Transactional(readOnly = true)
    public List<Product> findAllByParameters(ProductSearchParameters searchParams) {
        ExampleMatcher matcher = ExampleMatcher
                .matchingAll()
                .withIgnoreNullValues()
                .withIgnorePaths("id", "price")
                .withIgnoreCase()
                .withMatcher("name", contains())
                .withMatcher("author", exact())
                .withMatcher("size", exact());

        Product example = searchParams.toProductExample();
        Predicate<Product> productPredicate = searchParams.toNumberPredicates();

        return productRepository.findAll(Example.of(example, matcher)).stream()
                .filter(productPredicate)
                .collect(Collectors.toList());
    }

    public Product update(Product newProduct){
        return productRepository.save(newProduct);
    }

    @Transactional
    public void delete(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Product with id %s not found", id)));
        productRepository.delete(product);
    }
}
