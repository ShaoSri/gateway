package com.aaron.gateway.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @ClassName: CusomizeGlobalFilter
 * @Description : 自定义全局过滤器
 * @Author Aaron
 * @Date 2018/7/9
 * @Version 1.0
 */

public class CusomizeGlobalFilter implements GlobalFilter, Ordered {

    private static final Log log = LogFactory.getLog(CusomizeGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.debug("进入全局过滤器===================");
        String path = exchange.getRequest().getPath().toString();
  /*      String token = exchange.getRequest().getQueryParams().getFirst("token");
        if (token == null || token.isEmpty()) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }*/
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }

}
