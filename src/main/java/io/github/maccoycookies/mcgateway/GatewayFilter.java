package io.github.maccoycookies.mcgateway;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * gateway filter
 */
public interface GatewayFilter {

    Mono<Void> filter(ServerWebExchange exchange);

}
