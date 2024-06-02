package io.github.maccoycookies.mcgateway;

import com.maccoy.mcrpc.core.api.RegisterCenter;
import com.maccoy.mcrpc.core.registry.mc.McRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;

import java.util.Map;
import java.util.Properties;

/**
 * @author Maccoy
 * @date 2024/5/25 11:55
 * Description gateway config
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RegisterCenter registerCenter() {
        return new McRegistryCenter();
    }

    @Bean
    ApplicationRunner runner(@Autowired ApplicationContext applicationContext) {
        return args -> {
            SimpleUrlHandlerMapping simpleUrlHandlerMapping = applicationContext.getBean(SimpleUrlHandlerMapping.class);
            Properties mappings = new Properties();
            mappings.put("/ga/**", "gatewayWebHandler");
            simpleUrlHandlerMapping.setMappings(mappings);
            simpleUrlHandlerMapping.initApplicationContext();

        };
    }
}
