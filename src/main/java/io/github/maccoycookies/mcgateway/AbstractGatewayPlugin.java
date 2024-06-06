package io.github.maccoycookies.mcgateway;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public abstract class AbstractGatewayPlugin implements GatewayPlugin {

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean support(ServerWebExchange exchange) {
        return doSupport(exchange);
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {
        boolean isSupported = support(exchange);
        System.out.println(" ===> plugin [" + this.getName() + "], support = " + isSupported);
        return isSupported ? doHandle(exchange) : Mono.empty();
    }

    public abstract boolean doSupport(ServerWebExchange exchange);

    public abstract Mono<Void> doHandle(ServerWebExchange exchange);


}
