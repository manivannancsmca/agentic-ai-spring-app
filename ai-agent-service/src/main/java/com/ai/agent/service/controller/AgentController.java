package com.ai.agent.service.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ai.agent.service.dto.ChatRequest;
import com.ai.agent.service.dto.ChatResponse;
import com.ai.agent.service.service.AgentService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/agent")
public class AgentController {
    
    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        String response = agentService.processUserRequest(request.getMessage());
        return ResponseEntity.ok(new ChatResponse(response));
    }
    
    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@RequestBody ChatRequest request) {
        return agentService.streamUserRequest(request.getMessage());
    }
}