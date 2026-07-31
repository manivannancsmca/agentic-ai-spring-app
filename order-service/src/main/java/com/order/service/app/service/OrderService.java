package com.order.service.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.order.service.app.dto.NotificationRequest;
import com.order.service.app.dto.PaymentDto;
import com.order.service.app.dto.PaymentRequest;
import com.order.service.app.dto.ProductDto;
import com.order.service.app.dto.StockUpdateRequest;
import com.order.service.app.dto.UserDto;
import com.order.service.app.entity.Order;
import com.order.service.app.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    @Value("${service.urls.user}")
    private String userServiceUrl;

    @Value("${service.urls.product}")
    private String productServiceUrl;

    @Value("${service.urls.payment}")
    private String paymentServiceUrl;

    @Value("${service.urls.notification}")
    private String notificationServiceUrl;

    @Transactional
    @Retry(name = "serviceRetry")
    @CircuitBreaker(name = "serviceCircuit", fallbackMethod = "createOrderFallback")
    public Order createOrder(Long userId, Long productId, Integer quantity) {
        log.info("Creating order for user {} product {} qty {}", userId, productId, quantity);

        // 1. Validate User
        UserDto user = fetchUser(userId);
        if (user == null || !Boolean.TRUE.equals(user.getActive())) {
            throw new RuntimeException("Invalid or inactive user: " + userId);
        }

        // 2. Validate Product & Stock
        ProductDto product = fetchProduct(productId);
        if (product == null || product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock for product: " + productId);
        }

        // 3. Calculate Total
        BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(quantity));

        // 4. Create Order
        Order order = Order.builder()
                .userId(userId)
                .productId(productId)
                .quantity(quantity)
                .totalAmount(total)
                .status(Order.OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();
        order = orderRepository.save(order);

        // 5. Process Payment
        PaymentDto payment = processPayment(order.getId(), total);
        if (!"COMPLETED".equals(payment.getStatus())) {
            order.setStatus(Order.OrderStatus.CANCELLED);
            return orderRepository.save(order);
        }

        order.setStatus(Order.OrderStatus.PAID);
        order = orderRepository.save(order);

        // 6. Send Notification
        sendNotification(userId, "Order " + order.getId() + " placed successfully. Total: $" + total);

        // 7. Update Stock (call product service to decrement)
        updateProductStock(productId, product.getStockQuantity() - quantity);

        order.setStatus(Order.OrderStatus.COMPLETED);
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Order> listOrders() {
        return orderRepository.findAll();
    }

    private UserDto fetchUser(Long userId) {
        try {
            ResponseEntity<UserDto> response = restTemplate.getForEntity(
                    userServiceUrl + "/api/users/" + userId, UserDto.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to fetch user {}: {}", userId, e.getMessage());
            throw new RuntimeException("User service unavailable");
        }
    }

    private ProductDto fetchProduct(Long productId) {
        try {
            ResponseEntity<ProductDto> response = restTemplate.getForEntity(
                    productServiceUrl + "/api/products/" + productId, ProductDto.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to fetch product {}: {}", productId, e.getMessage());
            throw new RuntimeException("Product service unavailable");
        }
    }

    private PaymentDto processPayment(Long orderId, BigDecimal amount) {
        try {
            // Call payment service REST API directly (not MCP, for inter-service)
            PaymentRequest request = new PaymentRequest(orderId, amount, "CREDIT_CARD");
            ResponseEntity<PaymentDto> response = restTemplate.postForEntity(
                    paymentServiceUrl + "/api/payments", request, PaymentDto.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Payment failed for order {}: {}", orderId, e.getMessage());
            throw new RuntimeException("Payment processing failed");
        }
    }

    private void sendNotification(Long userId, String message) {
        try {
            NotificationRequest request = new NotificationRequest(userId, "EMAIL", message);
            restTemplate.postForEntity(notificationServiceUrl + "/api/notifications",
                    request, Void.class);
        } catch (Exception e) {
            log.error("Notification failed: {}", e.getMessage());
            // Non-critical: don't throw
        }
    }

    private void updateProductStock(Long productId, Integer newStock) {
        try {
            restTemplate.put(productServiceUrl + "/api/products/" + productId + "/stock",
                    new StockUpdateRequest(newStock));
        } catch (Exception e) {
            log.error("Failed to update stock: {}", e.getMessage());
        }
    }

    // Fallback method for circuit breaker
    private Order createOrderFallback(Long userId, Long productId, Integer quantity, Exception ex) {
        log.error("Circuit breaker open for createOrder. Fallback triggered.");
        throw new RuntimeException("Service temporarily unavailable. Please try again later.");
    }

}