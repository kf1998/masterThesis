package cloud.order.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.sql.Timestamp;


@Getter
@Setter
@AllArgsConstructor
@Document(collection = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String username;
    private Integer[] productsId;
    private Timestamp orderedAt;
    private String paymentMethod;

    public Order() {
    }

    public Order(String username, Integer[] productsId, Timestamp orderedAt, String paymentMethod) {
        this.username = username;
        this.productsId = productsId;
        this.orderedAt = orderedAt;
        this.paymentMethod = paymentMethod;
    }
}
