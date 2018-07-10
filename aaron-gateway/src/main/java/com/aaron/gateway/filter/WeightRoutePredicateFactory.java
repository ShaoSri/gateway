package com.aaron.gateway.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.event.WeightDefinedEvent;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.support.WeightConfig;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_PREDICATE_ROUTE_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.WEIGHT_ATTR;

/**
 * @ClassName: WeightRoutePredicateFactory
 * @Description : 权重路由源码
 * @Author Aaron
 * @Date 2018/7/10
 * @Version 1.0
 */

public class WeightRoutePredicateFactory extends AbstractRoutePredicateFactory<WeightConfig> implements ApplicationEventPublisherAware {
    private static final Log log = LogFactory.getLog(WeightRoutePredicateFactory.class);

    public static final String GROUP_KEY = WeightConfig.CONFIG_PREFIX + ".group";
    public static final String WEIGHT_KEY = WeightConfig.CONFIG_PREFIX + ".weight";
    private ApplicationEventPublisher publisher;

    public WeightRoutePredicateFactory() {
        super(WeightConfig.class);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(GROUP_KEY, WEIGHT_KEY);
    }

    @Override
    public String shortcutFieldPrefix() {
        return WeightConfig.CONFIG_PREFIX;
    }

    @Override
    public void beforeApply(WeightConfig config) {
        if (publisher != null) {
            publisher.publishEvent(new WeightDefinedEvent(this, config));
        }
    }


    @Override
    public Predicate<ServerWebExchange> apply(WeightConfig config) {
        return exchange -> {
            Map<String, String> weights = exchange.getAttributeOrDefault(WEIGHT_ATTR,
                    Collections.emptyMap());
            String routeId = exchange.getAttribute(GATEWAY_PREDICATE_ROUTE_ATTR);
            // all calculations and comparison against random num happened in
            // WeightCalculatorWebFilter
            String group = config.getGroup();

            if (weights.containsKey(group)) {
                String chosenRoute = weights.get(group);
                if (log.isTraceEnabled()) {
                    log.info("in group weight: " + group + ", current route: " + routeId + ", chosen route: " + chosenRoute);
                }
                return routeId.equals(chosenRoute);
            }
            return false;
        };
    }
}