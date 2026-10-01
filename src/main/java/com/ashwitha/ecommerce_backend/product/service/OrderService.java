package com.ashwitha.ecommerce_backend.product.service;

import com.ashwitha.ecommerce_backend.product.model.Order;
import com.ashwitha.ecommerce_backend.product.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Place a new order
    public Order placeOrder(Order order) {
        return orderRepository.save(order);
    }

    // Get all orders (Admin)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Get orders belonging to a particular user
    public List<Order> getOrdersByEmail(String email) {
        return orderRepository.findByEmailIgnoreCase(email);
    }

    // Update the status of an existing order
    public Order updateOrderStatus(Long id, String status) {

        // Find the order using its ID
        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found with ID: " + id)
                );

        // Update the status
        order.setStatus(status);

        // Save the updated order
        return orderRepository.save(order);
    }
}