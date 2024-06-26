// package io.github.maccoycookies.mcgateway;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.stereotype.Component;
// import org.springframework.web.reactive.function.server.RouterFunction;
// import org.springframework.web.reactive.function.server.ServerResponse;
// import reactor.core.publisher.Mono;
//
// import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
// import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
// import static org.springframework.web.reactive.function.server.RouterFunctions.route;
//
// /**
//  * @author Maccoy
//  * @date 2024/5/25 11:16
//  * Description gateway router
//  */
// @Component
// public class GatewayRouter {
//
//     @Autowired
//     HelloHandler helloHandler;
//
//     @Autowired
//     GatewayHandler gatewayHandler;
//
//     @Bean
//     public RouterFunction<?> helloRouterFunction() {
//         return route(GET("/hello"), helloHandler::handle);
//     }
//
//     @Bean
//     public RouterFunction<?> gatwayRouterFunction() {
//         return route(GET("/gw").or(POST("/gw/**")), gatewayHandler::handle);
//     }
//
// }
