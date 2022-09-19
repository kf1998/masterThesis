package cloud.product.model;

import java.sql.Date;
import java.util.Collection;
import java.util.HashSet;

import javax.persistence.*;

import cloud.product.dto.request.ProductRequest;
import lombok.*;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "price", nullable = false)
    private Float price;

    @Column(name = "size", nullable = false)
    private Size size;


    public Product() {}

    public Product(int id, ProductRequest productRequest) {
        this.id = id;
        this.name = productRequest.getName();
        this.size = productRequest.getSize();
        this.author = productRequest.getAuthor();
        this.price = productRequest.getPrice();
    }

    public Product(
            String name,
            String author,
            Float price,
            Size size
    ) {
        this.name = name;
        this.size = size;
        this.author = author;
        this.price = price;
    }
}