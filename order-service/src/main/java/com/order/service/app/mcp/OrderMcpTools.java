package com.order.service.app.mcp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import com.order.service.app.entity.Order;
import com.order.service.app.service.OrderService;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderMcpTools {
    
    private final OrderService orderService;
    
    @Tool(name = "create_order",
          description = "Create a new order for a user purchasing a product. Validates user, checks stock, processes payment, and sends confirmation.")
    public Order createOrder(
            @ToolParam(description = "User ID placing the order") Long userId,
            @ToolParam(description = "Product ID to purchase") Long productId,
            @ToolParam(description = "Quantity to purchase") Integer quantity) {
        log.info("MCP Tool: create_order({}, {}, {})", userId, productId, quantity);
        return orderService.createOrder(userId, productId, quantity);
    }
    
    @Tool(name = "get_order_by_id",
          description = "Retrieve order details by order ID.")
    public Order getOrderById(
            @ToolParam(description = "Order ID") Long orderId) {
        return orderService.getOrder(orderId);
    }
    
    @Tool(name = "list_user_orders",
          description = "List all orders in the system.")
    public List<Order> listAllOrders() {
        return orderService.listOrders();
    }
}
