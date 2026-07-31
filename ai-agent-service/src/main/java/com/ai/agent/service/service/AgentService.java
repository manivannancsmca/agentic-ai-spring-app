package com.ai.agent.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.agent.service.config.McpClientConfig;

import reactor.core.publisher.Flux;

@Service
public class AgentService {

    private static final Logger log = LoggerFactory.getLogger(AgentService.class);

    private final ChatClient chatClient;

    public AgentService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String processUserRequest(String userMessage) {
        log.info("Processing user request: {}", userMessage);

        try {
            String response = chatClient.prompt()
                    .user(userMessage)
                    .call()
                    .content();

            log.info("Agent response generated successfully");
            return response;
        } catch (Exception e) {
            log.error("Agent processing failed: {}", e.getMessage(), e);
            return "I apologize, but I encountered an error processing your request. " +
                    "Please try again or contact support if the issue persists.";
        }
    }

    public Flux<String> streamUserRequest(String userMessage) {
        log.info("Streaming response for: {}", userMessage);

        return chatClient.prompt()
                .user(userMessage)
                .stream()
                .content();
    }
}