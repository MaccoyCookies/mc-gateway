package io.github.maccoycookies.mcgateway.filter;

import io.github.maccoycookies.mcgateway.GatewayFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component("demoFilter")
public class DemoFilter implements GatewayFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange) {
        System.out.println("===> filters: demo filter ...");
        exchange.getRequest().getHeaders().toSingleValueMap().forEach((k, v) -> System.out.println(k + ": " + v));
        return Mono.empty();
    }
}
