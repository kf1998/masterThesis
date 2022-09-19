package cloud.gateway;

import cloud.gateway.dto.response.BasketResponse;
import cloud.gateway.dto.response.ProductResponse;
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
public class BasketApiCompositionFilterFactory extends
        AbstractGatewayFilterFactory<BasketApiCompositionFilterFactory.Config> {

    public BasketApiCompositionFilterFactory() {
        super(Config.class);
    }

    @Autowired
    ModifyResponseBodyGatewayFilterFactory modifyResponseBodyFilter;

    @Override
    public GatewayFilter apply(final Config config) {
        return modifyResponseBodyFilter.apply((c) -> c.setRewriteFunction(BasketResponse.class, BasketResponse.class, (filterExchange, input) -> {
            BasketResponse castedInput = (BasketResponse) input;

            RestTemplate rest = new RestTemplate();

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
        private String productsUriBasePath;
        @Getter
        private String productsGatewayPath;
    }
}
