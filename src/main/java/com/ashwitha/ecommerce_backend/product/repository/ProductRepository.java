package com.ashwitha.ecommerce_backend.product.repository;
import com.ashwitha.ecommerce_backend.product.model.Product;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository
        extends JpaRepository<@NonNull Product,@NonNull Long> {
}