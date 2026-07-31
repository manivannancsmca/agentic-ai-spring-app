package com.notification.service.app.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.notification.service.app.entity.Notification;
import com.notification.service.app.service.NotificationService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Notification> sendNotification(@RequestBody SendNotificationRequest request) {
        log.info("REST: Sending {} notification to user {}", request.getType(), request.getUserId());
        Notification notification = notificationService.sendNotification(
                request.getUserId(),
                request.getType(),
                request.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(notification);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotification(@PathVariable Long id) {
        log.info("REST: Fetching notification {}", id);
        // Assuming service has getById or repository is accessible
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }

    @GetMapping
    public ResponseEntity<List<Notification>> listNotifications() {
        log.info("REST: Listing all notifications");
        return ResponseEntity.ok(notificationService.listNotifications());
    }

    @Data
    public static class SendNotificationRequest {
        private Long userId;
        private String type;   // EMAIL, SMS, PUSH
        private String message;
    }
}