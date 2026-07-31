package com.ai.agent.service.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class McpClientConfig {

    @Bean
    public ChatClient chatClient(ChatModel chatModel, List<ToolCallbackProvider> toolCallbackProviders) {
        ChatClient.Builder builder = ChatClient.builder(chatModel);
        
        // Register all available tool callback providers (MCP tools, local functions, etc.)
        for (ToolCallbackProvider provider : toolCallbackProviders) {
            builder.defaultTools(provider);
        }

        return builder.build();
    }
}