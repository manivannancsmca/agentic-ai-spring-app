package com.ai.agent.service.dto;

public class ChatRequest {

    private String message;

    // No-argument constructor
    public ChatRequest() {
    }

    // All-arguments constructor
    public ChatRequest(String message) {
        this.message = message;
    }

    // Getter
    public String getMessage() {
        return message;
    }

    // Setter
    public void setMessage(String message) {
        this.message = message;
    }

    // toString()
    @Override
    public String toString() {
        return "ChatRequest{" +
                "message='" + message + '\'' +
                '}';
    }
}
