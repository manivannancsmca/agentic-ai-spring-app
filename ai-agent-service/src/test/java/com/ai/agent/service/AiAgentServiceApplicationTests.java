package com.ai.agent.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
    "spring.ai.mcp.client.enabled=false" // Prevents MCP client connection attempts
})
@ActiveProfiles("test")
class AiAgentServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
