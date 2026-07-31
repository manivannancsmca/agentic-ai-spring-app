package com.order.service.app.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.order.service.app.entity.Order;
import com.order.service.app.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (orderRepository.count() > 0) {
            log.info("Order data already exists, skipping initialization");
            return;
        }

        log.info("Seeding default order data directly into repository...");

        Order order1 = Order.builder()
                .userId(1L)          // john_doe
                .productId(1L)       // Laptop Pro
                .quantity(1)
                .totalAmount(new BigDecimal("1299.99"))
                .status(Order.OrderStatus.COMPLETED)
                .createdAt(LocalDateTime.now().minusDays(2))
                .build();

        Order order2 = Order.builder()
                .userId(2L)          // jane_smith
                .productId(2L)       // Wireless Mouse
                .quantity(3)
                .totalAmount(new BigDecimal("89.97"))
                .status(Order.OrderStatus.COMPLETED)
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();

        Order order3 = Order.builder()
                .userId(1L)          // john_doe
                .productId(3L)       // USB-C Hub
                .quantity(2)
                .totalAmount(new BigDecimal("119.98"))
                .status(Order.OrderStatus.PAID)
                .createdAt(LocalDateTime.now().minusHours(5))
                .build();

        Order order4 = Order.builder()
                .userId(3L)          // bob_wilson
                .productId(1L)       // Laptop Pro
                .quantity(2)
                .totalAmount(new BigDecimal("2599.98"))
                .status(Order.OrderStatus.CANCELLED)
                .createdAt(LocalDateTime.now().minusHours(3))
                .build();

        Order order5 = Order.builder()
                .userId(2L)          // jane_smith
                .productId(3L)       // USB-C Hub
                .quantity(1)
                .totalAmount(new BigDecimal("59.99"))
                .status(Order.OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);
        orderRepository.save(order4);
        orderRepository.save(order5);

        log.info("Inserted {} sample orders", orderRepository.count());
    }
}