// package io.github.maccoycookies.mcgateway;
//
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Component;
// import org.springframework.web.reactive.function.client.WebClient;
// import org.springframework.web.reactive.function.server.ServerRequest;
// import org.springframework.web.reactive.function.server.ServerResponse;
// import reactor.core.publisher.Mono;
//
// /**
//  * @author Maccoy
//  * @date 2024/5/25 11:26
//  * Description
//  */
// @Component
// public class HelloHandler {
//
//     Mono<ServerResponse> handle(ServerRequest request) {
//
//         String url = "http://localhost:7001/";
//         String requestJson = """
//                 {
//                   "service": "com.maccoy.mcrpc.demo.api.IUserService",
//                   "methodSign": "selectUserById@1_java.lang.Integer",
//                   "args": [100]
//                 }
//                 """;
//
//         WebClient client = WebClient.create(url);
//         Mono<ResponseEntity<String>> entity = client.post()
//                 .header("Content-Type", "application/json")
//                 .bodyValue(requestJson).retrieve().toEntity(String.class);
//
//         Mono<String> body = entity.map(ResponseEntity::getBody);
//         body.subscribe(source -> System.out.println("response: " + source));
//
//         return ServerResponse.ok()
//                 .header("Content-Type", "application/json")
//                 .header("mc.gateway.version", "V1.0.0")
//                 .body(body, String.class);
//     }
//
// }
