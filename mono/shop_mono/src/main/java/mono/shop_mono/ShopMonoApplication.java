package mono.shop_mono;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.metrics.export.prometheus.EnablePrometheusMetrics;

@EnableJpaRepositories
@SpringBootApplication
//@EnablePrometheusMetrics
public class ShopMonoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopMonoApplication.class, args);
    }

}
