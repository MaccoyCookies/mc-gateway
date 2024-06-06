package io.github.maccoycookies.mcgateway.plugin;

import com.maccoy.mcrpc.core.api.LoadBalancer;
import com.maccoy.mcrpc.core.api.RegisterCenter;
import com.maccoy.mcrpc.core.cluster.RoundLoadBalancer;
import com.maccoy.mcrpc.core.meta.InstanceMeta;
import com.maccoy.mcrpc.core.meta.ServiceMeta;
import io.github.maccoycookies.mcgateway.AbstractGatewayPlugin;
import io.github.maccoycookies.mcgateway.GatewayPluginChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * mc rpc gateway plugin.
 */
@Component("mcRpc")
public class McRpcPlugin extends AbstractGatewayPlugin {

    public static final String NAME = "mcRpc";
    private static final String PREFIX = GATEWAY_PREFIX + "/" + NAME + "/";

    @Autowired
    private RegisterCenter registerCenter;

    LoadBalancer loadBalancer = new RoundLoadBalancer();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean doSupport(ServerWebExchange exchange) {
        return exchange.getRequest().getPath().value().startsWith(PREFIX);
    }

    @Override
    public Mono<Void> doHandle(ServerWebExchange exchange, GatewayPluginChain chain) {
        System.out.println("===> [McRpcPlugin] ... ");
        String service = exchange.getRequest().getPath().value().substring(PREFIX.length());
        // 1. 通过请求路径或者服务名
        ServiceMeta serviceMeta = new ServiceMeta();
        serviceMeta.setApp("app1");
        serviceMeta.setEnv("env");
        serviceMeta.setNamespace("public");
        serviceMeta.setName(service);
        // 2. 通过rc拿到所有活着的服务实例
        List<InstanceMeta> instanceMetas = registerCenter.fetchAll(serviceMeta);
        InstanceMeta instanceMeta = loadBalancer.choose(instanceMetas);
        // 3. 拿到请求url
        String url = instanceMeta.toUrl();
        // 4. 拿到请求的报文
        Flux<DataBuffer> requestBody = exchange.getRequest().getBody();
        // 5. 通过webClient发送post请求
        WebClient client = WebClient.create(url);
        Mono<ResponseEntity<String>> entity = client.post()
                .header("Content-Type", "application/json")
                .body(requestBody, DataBuffer.class).retrieve().toEntity(String.class);
        // 6. 通过entity获取响应报文
        Mono<String> body = entity.map(ResponseEntity::getBody);
        body.subscribe(source -> System.out.println("response: " + source));
        // 7. 组装响应报文

        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        exchange.getResponse().getHeaders().add("mc.gateway.version", "V1.0.0");
        exchange.getResponse().getHeaders().add("mc.gateway.plugin", getName());
        return body.flatMap(x -> exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(x.getBytes()))))
                .then(chain.handle(exchange));

    }
}
