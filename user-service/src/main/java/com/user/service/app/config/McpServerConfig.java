package com.user.service.app.config;

import com.user.service.app.mcp.UserMcpTools;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpServerConfig {

    @Bean
    public MethodToolCallbackProvider userServiceTools(UserMcpTools userMcpTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(userMcpTools)
                .build();
    }

    /**
     * Extends Tomcat's async timeout to 1 hour so SSE connections
     * (used by MCP sessions) don't drop before tool results return.
     */
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatAsyncTimeout() {
        return factory -> factory.addConnectorCustomizers(connector -> {
            connector.setAsyncTimeout(3600000L); // 1 hour in milliseconds
        });
    }
}