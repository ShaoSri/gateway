package com.aaron.gateway;

import com.aaron.gateway.filter.CustomizeFilter;
import com.aaron.gateway.filter.RateLimitByCpuGatewayFilter;
import com.aaron.gateway.filter.RateLimitByIpGatewayFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

/**
 * Hello world!
 */
@SpringBootApplication
public class GatewaySampleApplication {


    /**
     * 路由转发
     * @param builder
     * @return
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r
                        .path("/baidu")
                        .filters(f -> f
                                .filter(new CustomizeFilter()).addRequestHeader("X-Response-Default-Foo", "Default-Bar"))
                        .uri("http://baidu.com:80/")
                        .order(0)
                        .id("aaron1")

                )
                .build();
    }



    /**
     * 权重路由转发
     * @param builder
     * @return
     */
    @Bean
    public RouteLocator weightRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r
                        .weight("provide",90)
                        .and()
                        .path("/test1")
                        .uri("http://localhost:10001/getServices")
                )

                .route(r -> r
                        .weight("provide",10)
                        .and()
                        .path("/test1")
                        .uri("http://localhost:10002/getServices")
                )

                .build();
    }


    /**
     * 令牌桶算法限流
     * @param builder
     * @return
     */
    @Bean
    public RouteLocator rateLimitRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/rateLimit")
                        .filters(f -> f
                                .filter(new RateLimitByIpGatewayFilter(10,1,Duration.ofSeconds(100))))
                        .uri("http://baidu.com:80/")
                        .order(10)
                        .id("aaron2")
                )
                .build();
    }


    /**
     * 基于CPU负载的动态限流
     */
    @Autowired
    private RateLimitByCpuGatewayFilter rateLimitByCpuGatewayFilter;

    @Bean
    public RouteLocator rateLimitRouteLocatorByCPU(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/")
                        .filters(f -> f.stripPrefix(2)
                                .filter(rateLimitByCpuGatewayFilter))
                        .uri("http://baidu.com:80/")
                        .order(0)
                        .id("cpu_service")
                )
                .build();
    }



    public static void main(String[] args) {
        SpringApplication.run(GatewaySampleApplication.class, args);
    }


}
