// package io.github.maccoycookies.mcgateway;
//
// import com.maccoy.mcrpc.core.api.LoadBalancer;
// import com.maccoy.mcrpc.core.api.RegisterCenter;
// import com.maccoy.mcrpc.core.cluster.RoundLoadBalancer;
// import com.maccoy.mcrpc.core.meta.InstanceMeta;
// import com.maccoy.mcrpc.core.meta.ServiceMeta;
// import org.jetbrains.annotations.NotNull;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Component;
// import org.springframework.web.reactive.function.client.WebClient;
// import org.springframework.web.reactive.function.server.ServerRequest;
// import org.springframework.web.reactive.function.server.ServerResponse;
// import reactor.core.publisher.Mono;
//
// import java.util.List;
//
// /**
//  * @author Maccoy
//  * @date 2024/5/25 11:26
//  * Description
//  */
// @Component
// public class GatewayHandler {
//
//     @Autowired
//     private RegisterCenter registerCenter;
//
//     LoadBalancer loadBalancer = new RoundLoadBalancer();
//
//     Mono<ServerResponse> handle(ServerRequest request) {
//         // 1. 通过请求路径或者服务名
//         String service = request.path().substring(1);
//
//         ServiceMeta serviceMeta = new ServiceMeta();
//         serviceMeta.setApp("app1");
//         serviceMeta.setEnv("env");
//         serviceMeta.setNamespace("public");
//         serviceMeta.setName(service);
//         // 2. 通过rc拿到所有活着的服务实例
//         List<InstanceMeta> instanceMetas = registerCenter.fetchAll(serviceMeta);
//         InstanceMeta instanceMeta = loadBalancer.choose(instanceMetas);
//         // 3. 拿到请求url
//         String url = instanceMeta.toUrl();
//         // 4. 拿到请求的报文
//         Mono<String> requestMono = request.bodyToMono(String.class);
//         return requestMono.flatMap(x -> invokeFromRegistry(x, url));
//     }
//
//     private static Mono<ServerResponse> invokeFromRegistry(String x, String url) {
//         // 5. 通过webClient发送post请求
//         WebClient client = WebClient.create(url);
//         Mono<ResponseEntity<String>> entity = client.post()
//                 .header("Content-Type", "application/json")
//                 .bodyValue(x).retrieve().toEntity(String.class);
//         // 6. 通过entity获取响应报文
//         Mono<String> body = entity.map(ResponseEntity::getBody);
//         body.subscribe(source -> System.out.println("response: " + source));
//
//         // 7. 组装响应报文
//         return ServerResponse.ok()
//                 .header("Content-Type", "application/json")
//                 .header("mc.gateway.version", "V1.0.0")
//                 .body(body, String.class);
//     }
//
// }
