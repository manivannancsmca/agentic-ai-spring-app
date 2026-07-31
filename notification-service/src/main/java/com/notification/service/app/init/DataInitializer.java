package com.notification.service.app.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.notification.service.app.entity.Notification;
import com.notification.service.app.repository.NotificationRepository;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (notificationRepository.count() > 0) {
            log.info("Notification data already exists, skipping initialization");
            return;
        }

        log.info("Seeding default notification data...");

        Notification notification1 = Notification.builder()
                .userId(1L)
                .type(Notification.NotificationType.EMAIL)
                .message("Welcome to the platform! Your account has been successfully created.")
                .status(Notification.NotificationStatus.SENT)
                .createdAt(LocalDateTime.now().minusDays(5))
                .build();

        Notification notification2 = Notification.builder()
                .userId(1L)
                .type(Notification.NotificationType.EMAIL)
                .message("Your order #1 has been confirmed. Total: $1,299.99. Transaction: TXN-ABC123.")
                .status(Notification.NotificationStatus.SENT)
                .createdAt(LocalDateTime.now().minusDays(2))
                .build();

        Notification notification3 = Notification.builder()
                .userId(2L)
                .type(Notification.NotificationType.SMS)
                .message("Payment received for order #2. Amount: $89.97. Thank you!")
                .status(Notification.NotificationStatus.SENT)
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();

        Notification notification4 = Notification.builder()
                .userId(3L)
                .type(Notification.NotificationType.PUSH)
                .message("Special offer: 20% off on all accessories this weekend!")
                .status(Notification.NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        Notification notification5 = Notification.builder()
                .userId(1L)
                .type(Notification.NotificationType.EMAIL)
                .message("Your order #3 is awaiting payment confirmation.")
                .status(Notification.NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now().minusHours(2))
                .build();

        notificationRepository.save(notification1);
        notificationRepository.save(notification2);
        notificationRepository.save(notification3);
        notificationRepository.save(notification4);
        notificationRepository.save(notification5);

        log.info("Inserted {} sample notifications", notificationRepository.count());
    }
}
