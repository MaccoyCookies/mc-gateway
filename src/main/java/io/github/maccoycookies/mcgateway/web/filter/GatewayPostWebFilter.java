package io.github.maccoycookies.mcgateway.web.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author Maccoy
 * @date 2024/6/2 19:10
 * Description
 */
@Component
public class GatewayPostWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange).doFinally(s -> {
            System.out.println("post filter");

            exchange.getAttributes().forEach((k, v) -> System.out.println("k: " + k + ", v: "+ v));
        });
    }
}
