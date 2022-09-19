package cloud.order.model;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    private int id;

    public Product() {}

    public Product(int id) {
        this.id = id;
    }

}
