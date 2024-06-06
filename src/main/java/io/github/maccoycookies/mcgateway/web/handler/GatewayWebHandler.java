package io.github.maccoycookies.mcgateway.web.handler;

import com.maccoy.mcrpc.core.api.LoadBalancer;
import com.maccoy.mcrpc.core.api.RegisterCenter;
import com.maccoy.mcrpc.core.cluster.RoundLoadBalancer;
import com.maccoy.mcrpc.core.meta.InstanceMeta;
import com.maccoy.mcrpc.core.meta.ServiceMeta;
import io.github.maccoycookies.mcgateway.DefaultGatewayPluginChain;
import io.github.maccoycookies.mcgateway.GatewayFilter;
import io.github.maccoycookies.mcgateway.GatewayPlugin;
import io.github.maccoycookies.mcgateway.GatewayPluginChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Maccoy
 * @date 2024/6/2 18:21
 * Description
 */
@Component("gatewayWebHandler")
public class GatewayWebHandler implements WebHandler {

    @Autowired
    List<GatewayPlugin> plugins;

    @Autowired
    List<GatewayFilter> filters;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {
        System.out.println(" ===> Mc Gateway web handler ... ");
        if (plugins == null || plugins.isEmpty()) {
            String mockStr = """
                    {
                        "result": "no plugin"
                    }
                    """;
            return exchange.getResponse()
                    .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(mockStr.getBytes())));
        }

        for (GatewayFilter filter : filters) {
            filter.filter(exchange);
        }

        return new DefaultGatewayPluginChain(plugins).handle(exchange);
        // for (GatewayPlugin plugin : plugins) {
        //     if (plugin.support(exchange)) {
        //         return plugin.handle(exchange);
        //     }
        // }
        // String mockStr = """
        //             {
        //                 "result": "no supported plugin"
        //             }
        //             """;
        // return exchange.getResponse()
        //         .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(mockStr.getBytes())));
    }

}
