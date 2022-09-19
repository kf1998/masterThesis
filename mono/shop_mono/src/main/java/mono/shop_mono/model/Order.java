package mono.shop_mono.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "products_id", referencedColumnName = "id")
    private Collection<Product> products = new HashSet<>();

    @Column(name = "ordered_at", nullable = false)
    private Timestamp orderedAt;

    @Column(name = "payment_method")
    private String paymentMethod;

    public Order() {}

    public Order(User user, Collection<Product> products, Timestamp orderedAt, String paymentMethod) {
        this.user = user;
        this.products = products;
        this.orderedAt = orderedAt;
        this.paymentMethod = paymentMethod;
    }
}