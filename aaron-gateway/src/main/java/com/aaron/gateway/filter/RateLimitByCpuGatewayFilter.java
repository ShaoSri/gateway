package com.aaron.gateway.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @ClassName: RateLimitByCpuGatewayFilter
 * @Description : 基于CPU系统负载的动态限流
 * @Author Aaron
 * @Date 2018/7/10
 * @Version 1.0
 */
@Component
public class RateLimitByCpuGatewayFilter implements GatewayFilter, Ordered {
    private static final Log log = LogFactory.getLog(RateLimitByCpuGatewayFilter.class);

    @Autowired
    private MetricsEndpoint metricsEndpoint;

    private static final String METRIC_NAME = "system.cpu.usage";
    private static final double MAX_USAGE = 0.50D;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Double systemCpuUsage = metricsEndpoint.metric(METRIC_NAME, null)
                .getMeasurements()
                .stream()
                .filter(Objects::nonNull)
                .findFirst()
                .map(MetricsEndpoint.Sample::getValue)
                .filter(Double::isFinite)
                .orElse(0.0D);

        boolean flag = systemCpuUsage < MAX_USAGE;

        log.info("系统 CPU 使用率=================: " + systemCpuUsage + " ok: " + flag);

        if (!flag) {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        } else {
            return chain.filter(exchange);
        }
    }



    @Override
    public int getOrder() {
        return 0;
    }

}
