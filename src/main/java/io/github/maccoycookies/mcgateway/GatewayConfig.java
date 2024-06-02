package io.github.maccoycookies.mcgateway;

import com.maccoy.mcrpc.core.api.RegisterCenter;
import com.maccoy.mcrpc.core.registry.mc.McRegistryCenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

}
