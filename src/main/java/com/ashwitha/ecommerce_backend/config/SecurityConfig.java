package com.ashwitha.ecommerce_backend.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // Configure Spring Security
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                // Enable CORS
                .cors(Customizer.withDefaults())

                // Disable CSRF for REST APIs
                .csrf(csrf -> csrf.disable())

                // Allow H2 Console frames
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable())
                )

                // Use stateless JWT authentication
                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // Configure endpoint permissions
                .authorizeHttpRequests(auth -> auth

                        // Allow CORS preflight requests
                        .requestMatchers(
                                HttpMethod.OPTIONS, "/**"
                        ).permitAll()

                        // Allow H2 Console
                        .requestMatchers(
                                "/h2-console/**"
                        ).permitAll()

                        // Public registration and login
                        .requestMatchers(
                                "/api/users/register",
                                "/api/users/login"
                        ).permitAll()

                        // Allow everyone to view products
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/products",
                                "/api/products/**"
                        ).permitAll()

                        // Only admins can add products
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/products",
                                "/api/products/**"
                        ).hasRole("ADMIN")

                        // Only admins can update products
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/products",
                                "/api/products/**"
                        ).hasRole("ADMIN")

                        // Only admins can delete products
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/products",
                                "/api/products/**"
                        ).hasRole("ADMIN")

                                // Only admins can view all orders
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/orders/admin/all"
                                ).hasRole("ADMIN")

// Only admins can update order status
                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/orders/*/status"
                                ).hasRole("ADMIN")

// Logged-in customers can place orders
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/orders"
                                ).authenticated()

// Logged-in customers can access their own order endpoints
                                .requestMatchers(
                                        "/api/orders/**"
                                ).authenticated()

// Require authentication for any other unmatched endpoint
                                .anyRequest().authenticated()
                )

                // Disable default login mechanisms
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // Add JWT filter to Spring Security
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    // Prevent the JWT filter from running as a separate
    // servlet filter outside the Spring Security chain
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter>
    jwtAuthenticationFilterRegistration(
            JwtAuthenticationFilter filter) {

        FilterRegistrationBean<JwtAuthenticationFilter> registration =
                new FilterRegistrationBean<>();

        registration.setFilter(filter);
        registration.setEnabled(false);

        return registration;
    }
}