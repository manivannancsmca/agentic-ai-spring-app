
package com.ai.agent.service.dto;

public class ChatResponse {

    private String response;

    // No-argument constructor
    public ChatResponse() {
    }

    // All-arguments constructor
    public ChatResponse(String response) {
        this.response = response;
    }

    // Getter
    public String getResponse() {
        return response;
    }

    // Setter
    public void setResponse(String response) {
        this.response = response;
    }

    // toString()
    @Override
    public String toString() {
        return "ChatResponse{" +
                "response='" + response + '\'' +
                '}';
    }
}
