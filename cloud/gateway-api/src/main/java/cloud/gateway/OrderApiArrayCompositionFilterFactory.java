package cloud.gateway;

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
import java.util.Map;

@Component
public class OrderApiArrayCompositionFilterFactory extends
        AbstractGatewayFilterFactory<OrderApiArrayCompositionFilterFactory.Config> {

    public OrderApiArrayCompositionFilterFactory() {
        super(Config.class);
    }

    @Autowired
    ModifyResponseBodyGatewayFilterFactory modifyResponseBodyFilter;

    @Override
    public GatewayFilter apply(final Config config) {
        return modifyResponseBodyFilter.apply((c) -> c.setRewriteFunction(List.class, List.class, (filterExchange, input) -> {
            List<Map<String, Object>> castedInput = (List<Map<String, Object>>) input;

            List<String> baseFieldValues = castedInput.stream()
                    .map(bodyMap -> (String) bodyMap.get(config.getUserBaseField())).toList();

            RestTemplate rest = new RestTemplate();

            int i = 0;
            for(var elem : castedInput) {
                ResponseEntity<UserResponse> exchange = rest.exchange(
                        config.getUserUriBasePath() + config.getUserGatewayPath() + baseFieldValues.get(i),
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        UserResponse.class);
                elem.put(config.getUserComposeField(), exchange.getBody());
                i++;

                List<ProductResponse> products = new ArrayList<>();
                for (var prod : (List<Integer>) (elem.get(config.getProductsBaseField())))
                {
                    ResponseEntity<ProductResponse> ex = rest.exchange(
                            config.getProductsUriBasePath() + config.getProductsGatewayPath() + prod,
                            HttpMethod.GET,
                            HttpEntity.EMPTY,
                            ProductResponse.class);

                    products.add(ex.getBody());
                }
                elem.put(config.getProductsComposeField(), products);
            }

            return Mono.just(castedInput);
        }));
    }

    @AllArgsConstructor
    public static class Config {

        @Getter
        private String userBaseField;
        @Getter
        private String userUriBasePath;
        @Getter
        private String userGatewayPath;
        @Getter
        private String userComposeField;

        @Getter
        private String productsBaseField;
        @Getter
        private String productsUriBasePath;
        @Getter
        private String productsGatewayPath;
        @Getter
        private String productsComposeField;
    }
}
