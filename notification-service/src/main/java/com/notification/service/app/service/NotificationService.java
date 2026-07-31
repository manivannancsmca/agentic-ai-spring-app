package com.notification.service.app.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.notification.service.app.entity.Notification;
import com.notification.service.app.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public Notification sendNotification(Long userId, String type, String message) {
        log.info("Sending {} notification to user {}: {}", type, userId, message);
        
        Notification.NotificationType notifType = Notification.NotificationType.valueOf(type);
        
        // Simulate sending
        boolean sent = simulateSend();
        
        Notification notification = Notification.builder()
                .userId(userId)
                .type(notifType)
                .message(message)
                .status(sent ? Notification.NotificationStatus.SENT : Notification.NotificationStatus.FAILED)
                .createdAt(LocalDateTime.now())
                .build();
        
        return notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Notification> listNotifications() {
        return notificationRepository.findAll();
    }
    
    private boolean simulateSend() {
        return Math.random() > 0.05; // 95% success
    }
}
