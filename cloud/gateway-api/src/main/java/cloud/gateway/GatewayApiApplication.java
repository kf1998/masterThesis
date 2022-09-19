package cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

@SpringBootApplication
@EnableConfigurationProperties(UriConfiguration.class)
public class GatewayApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApiApplication.class, args);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder, UriConfiguration uriConfiguration, OrderApiArrayCompositionFilterFactory orderApiCompositionFilterFactory, OrderApiObjectCompositionFilterFactory orderApiObjectCompositionFilterFactory, BasketApiCompositionFilterFactory basketApiCompositionFilterFactory) {
        String userUri = uriConfiguration.getUserUri();
        String productUri = uriConfiguration.getProductUri();
        String orderUri = uriConfiguration.getOrderUri();
        String basketUri = uriConfiguration.getBasketUri();

        return builder.routes()
                .route(p -> p
                        .path("/user")
                        .uri(userUri))
                .route(p -> p
                        .path("/user/register")
                        .and().method(HttpMethod.POST)
                        .filters(f -> f.rewritePath("/user/register", "/user"))
                        .uri(userUri))
                .route(p -> p
                        .path("/user/*")
                        .uri(userUri))
                .route(p -> p
                        .path("/product")
                        .uri(productUri))
                .route(p -> p
                        .path("/product/*")
                        .uri(productUri))
                .route(p -> p
                        .path("/order")
                        .and().method(HttpMethod.GET)
                        .filters(f -> f
                                .filter(orderApiCompositionFilterFactory.apply(
                                    new OrderApiArrayCompositionFilterFactory.Config(
                                            "userName",
                                            userUri,
                                            "/user/",
                                            "user",
                                            "productsId",
                                            productUri,
                                            "/product/",
                                            "products"))))
                        .uri(orderUri))
                .route(p -> p
                        .path("/order")
                        .and().method(HttpMethod.POST)
                        .filters(f -> f
                                .filter(orderApiObjectCompositionFilterFactory.apply(
                                        new OrderApiObjectCompositionFilterFactory.Config(
                                                userUri,
                                                "/user/",
                                                productUri,
                                                "/product/"))))
                        .uri(orderUri))
                .route(p -> p
                        .path("/order/*")
                        .filters(f -> f
                                .filter(orderApiObjectCompositionFilterFactory.apply(
                                        new OrderApiObjectCompositionFilterFactory.Config(
                                                userUri,
                                                "/user/",
                                                productUri,
                                                "/product/"))))
                        .uri(orderUri))
                .route(p -> p
                        .path("/basket/**")
                        .filters(f -> f
                                .filter(basketApiCompositionFilterFactory.apply(
                                        new BasketApiCompositionFilterFactory.Config(
                                                productUri,
                                                "/product/"))))
                        .uri(basketUri))
                .route(p -> p
                        .path("/basket/*")
                        .uri(basketUri))
                .build();
    }
}
