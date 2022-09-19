package cloud.gateway;

import cloud.gateway.dto.response.OrderResponse;
import cloud.gateway.dto.response.ProductResponse;
import cloud.gateway.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderApiObjectCompositionFilterFactory extends
        AbstractGatewayFilterFactory<OrderApiObjectCompositionFilterFactory.Config> {

    public OrderApiObjectCompositionFilterFactory() {
        super(Config.class);
    }

    @Autowired
    ModifyResponseBodyGatewayFilterFactory modifyResponseBodyFilter;

    @Override
    public GatewayFilter apply(final Config config) {
        return modifyResponseBodyFilter.apply((c) -> c.setRewriteFunction(OrderResponse.class, OrderResponse.class, (filterExchange, input) -> {
            OrderResponse castedInput = (OrderResponse) input;

            String baseFieldValue = castedInput.getUserName();

            RestTemplate rest = new RestTemplate();

            ResponseEntity<UserResponse> exchange = rest.exchange(
                    config.getUserUriBasePath() + config.getUserGatewayPath() + baseFieldValue,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    UserResponse.class);
            castedInput.setUser(exchange.getBody());

            List<ProductResponse> products = new ArrayList<>();
            for (var prod : castedInput.getProductsId())
            {
                ResponseEntity<ProductResponse> ex = rest.exchange(
                        config.getProductsUriBasePath() + config.getProductsGatewayPath() + prod,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        ProductResponse.class);

                products.add(ex.getBody());
            }
            castedInput.setProducts(products);

            return Mono.just(castedInput);
        }));
    }

    @AllArgsConstructor
    public static class Config {
        @Getter
        private String userUriBasePath;
        @Getter
        private String userGatewayPath;

        @Getter
        private String productsUriBasePath;
        @Getter
        private String productsGatewayPath;
    }
}
