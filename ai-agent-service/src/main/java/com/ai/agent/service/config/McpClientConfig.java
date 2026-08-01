package com.ai.agent.service.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class McpClientConfig {

    /**
     * Spring AI auto-discovers all MCP tools from the configured SSE connections
     * and registers them as ToolCallback beans. We simply inject them here.
     */
    @Bean
    @Primary
    public ChatClient chatClient(ChatClient.Builder builder, List<ToolCallback> toolCallbacks) {
        
        System.out.println("Total MCP tools registered: " + toolCallbacks.size());
        
        return builder
                .defaultTools(toolCallbacks.toArray(new ToolCallback[0]))
                .defaultSystem("""
                    You are an intelligent e-commerce assistant. You have access to tools 
                    for managing users, products, orders, payments, and notifications.
                    
                    When processing orders:
                    1. Always verify the user exists and is active
                    2. Check product stock before creating an order
                    3. Process payment automatically after order creation
                    4. Confirm the final status to the user
                    
                    Be concise but informative. Always report transaction IDs when available.
                    """)
                .build();
    }
}