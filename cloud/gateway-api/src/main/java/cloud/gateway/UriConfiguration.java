package cloud.gateway;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties
public class UriConfiguration {
    private String userUri = "http://user-api:8081";
    private String productUri = "http://product-api:8082";
    private String orderUri = "http://order-api:8083";
    private String basketUri = "http://basket-api:8084";
}
