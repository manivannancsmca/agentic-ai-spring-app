package com.notification.service.app.mcp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import com.notification.service.app.entity.Notification;
import com.notification.service.app.service.NotificationService;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationMcpTools {
    
    private final NotificationService notificationService;
    
    @Tool(name = "send_notification",
          description = "Send a notification to a user via EMAIL, SMS, or PUSH.")
    public Notification sendNotification(
            @ToolParam(description = "Target user ID") Long userId,
            @ToolParam(description = "Type: EMAIL, SMS, or PUSH") String type,
            @ToolParam(description = "Message content") String message) {
        log.info("MCP Tool: send_notification({}, {}, {})", userId, type, message);
        return notificationService.sendNotification(userId, type, message);
    }
}
