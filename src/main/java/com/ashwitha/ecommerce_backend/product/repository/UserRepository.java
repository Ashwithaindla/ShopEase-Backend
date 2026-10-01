package com.ashwitha.ecommerce_backend.product.repository;
import com.ashwitha.ecommerce_backend.product.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface  UserRepository extends
        JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

}
