package com.ashwitha.ecommerce_backend.product.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import jakarta.persistence.PrePersist;
import java.math.BigDecimal;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String email;

    private String phone;

    @Column(length = 1000)
    private String address;

    private String city;

    private String pinCode;

    private BigDecimal totalAmount;

    // New field
    @Column(nullable = false)
    private String status = "PLACED";

    public void setEmail(String email) {
        this.email = email;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @Column(nullable = false, updatable = false)
    private LocalDateTime orderDate;
    @PrePersist
    protected void onCreate() {
        if (orderDate == null) {
            orderDate = LocalDateTime.now();
        }
    }
}