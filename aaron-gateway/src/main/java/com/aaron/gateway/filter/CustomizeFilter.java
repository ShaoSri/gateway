package com.aaron.gateway.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @ClassName: CustomizeFilter
 * @Description : 自定义过滤器(pre/post)
 * @Author Aaron
 * @Date 2018/7/9
 * @Version 1.0
 */

public class CustomizeFilter implements GatewayFilter, Ordered {

    private static final Log log = LogFactory.getLog(CustomizeFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getAttributes().put("pre", "pre-filter");
        log.info("进入pre_filter------------："+exchange.getApplicationContext());
        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                    log.info("进入filter-------------------------");
                })
        );
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
