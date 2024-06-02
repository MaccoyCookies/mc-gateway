package io.github.maccoycookies.mcgateway;

import com.maccoy.mcrpc.core.api.LoadBalancer;
import com.maccoy.mcrpc.core.api.RegisterCenter;
import com.maccoy.mcrpc.core.cluster.RoundLoadBalancer;
import com.maccoy.mcrpc.core.meta.InstanceMeta;
import com.maccoy.mcrpc.core.meta.ServiceMeta;
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
    private RegisterCenter registerCenter;

    LoadBalancer loadBalancer = new RoundLoadBalancer();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {
        // return exchange.getResponse().writeWith(
        //         Mono.just(exchange.getResponse().bufferFactory().wrap("hello world".getBytes()))
        // );
        // return Mono.empty();
        System.out.println("===> Mc Gateway web handler ... ");
        String service = exchange.getRequest().getPath().value().substring(4);
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
        return body.flatMap(x -> exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(x.getBytes()))));

    }

}
