package com.ashwitha.ecommerce_backend.product.controller;

import com.ashwitha.ecommerce_backend.product.model.Order;
import com.ashwitha.ecommerce_backend.product.service.OrderService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Allowed order statuses
    private static final Set<String> ALLOWED_STATUSES = Set.of(
            "PLACED",
            "PROCESSING",
            "SHIPPED",
            "DELIVERED",
            "CANCELLED"
    );

    // Place a new order using the authenticated user's email
    @PostMapping
    public ResponseEntity<Order> placeOrder(
            @RequestBody Order order,
            Principal principal) {

        String email = principal.getName();

        // Never trust the email supplied in the request body
        order.setEmail(email);

        Order savedOrder = orderService.placeOrder(order);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedOrder);
    }

    // Get all orders (Admin only)
    @GetMapping("/admin/all")
    public ResponseEntity<List<Order>> getAllOrders() {

        List<Order> orders = orderService.getAllOrders();

        return ResponseEntity.ok(orders);
    }

    // Get only the authenticated user's orders
    @GetMapping("/my-orders")
    public ResponseEntity<List<Order>> getMyOrders(
            Principal principal) {

        String email = principal.getName();

        List<Order> orders =
                orderService.getOrdersByEmail(email);

        return ResponseEntity.ok(orders);
    }

    // Update order status (Admin only)
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        String status = request.get("status");

        // Check whether status was provided
        if (status == null || status.isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message", "Status is required"
                    ));
        }

        // Convert status to uppercase
        status = status.trim().toUpperCase();

        // Validate status
        if (!ALLOWED_STATUSES.contains(status)) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message",
                            "Invalid status. Allowed values: "
                                    + ALLOWED_STATUSES
                    ));
        }

        // Update status through the service
        Order updatedOrder =
                orderService.updateOrderStatus(id, status);

        return ResponseEntity.ok(updatedOrder);
    }
}