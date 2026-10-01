package com.ashwitha.ecommerce_backend.product.repository;

import com.ashwitha.ecommerce_backend.product.model.Order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository
        extends JpaRepository<Order, Long> {
    List<Order> findByEmailIgnoreCase(String email);
    List<Order> findByEmail(String email);
}
