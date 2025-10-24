package com.cs_25_2_team2.RestaurantManagementApp.controllers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs_25_2_team2.RestaurantManagementApp.entities.OrderEntity;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.OrderRepository;
import com.cs_25_2_team2.RestaurantManagementApp.auth.RequiredRole;
import com.cs_25_2_team2.RestaurantManagementApp.services.RestaurantService;

import jakarta.servlet.http.HttpSession;

/**
 * REST Controller for managing restaurant orders.
 * Provides endpoints for creating, retrieving, updating, and canceling orders.
 * 
 * Base URL: /api/orders
 * CORS enabled for: http://localhost:3000 (Next.js development server)
 * 
 * @author Team 2
 * @version 1.0
 */
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000") // Next.js dev server URL
public class OrderController {
    
    private final OrderRepository orderRepository;
    private final RestaurantService restaurantService;
    
    /**
     * Constructor for OrderController with dependency injection.
     * Properly injects dependencies for database operations and business logic.
     * 
     * @param orderRepository Repository for order data persistence
     * @param restaurantService Service for business logic operations
     */
    public OrderController(OrderRepository orderRepository, RestaurantService restaurantService) {
        this.orderRepository = orderRepository;
        this.restaurantService = restaurantService;
    }

    // Endpoint to return all orders
    @GetMapping
    @RequiredRole({"CHEF", "ADMIN"})
    public List<OrderEntity> getAllOrders() {
        return (List<OrderEntity>) orderRepository.findAll();
    }

    // Endpoint to get a specific order
    @GetMapping("/{id}")
    @RequiredRole({"CUSTOMER", "CHEF", "DELIVERY", "ADMIN"})
    public ResponseEntity<OrderEntity> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }



    // Endpoint to create a new order
    @PostMapping
    @RequiredRole({"CUSTOMER", "ADMIN"})
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> orderData, HttpSession session) {
        try {
            // Get user info from session
            Long userId = (Long) session.getAttribute("userId");
            String userType = (String) session.getAttribute("userType");
            String username = (String) session.getAttribute("username");
            
            if (userId == null || userType == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "User information not found in session"));
            }
            
            // Create a simple order
            OrderEntity newOrder = new OrderEntity();
            
            // Set totalPrice from the request
            if (orderData.containsKey("totalPrice")) {
                String priceStr = orderData.get("totalPrice").toString();
                newOrder.setTotalPrice(new BigDecimal(priceStr));
            } else {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Total price is required"));
            }
            
            // Set order details
            newOrder.setStatus(OrderEntity.OrderStatus.Placed);
            newOrder.setCreatedAt(LocalDateTime.now());
            
            // For customers, could associate with their account in future enhancement
            // Currently creating orders with basic information
            
            // Save the order
            OrderEntity savedOrder = orderRepository.save(newOrder);
            
            // Return success response
            Map<String, Object> response = Map.of(
                "message", "Order created successfully",
                "orderId", savedOrder.getOrderId(),
                "status", savedOrder.getStatus().toString(),
                "totalPrice", savedOrder.getTotalPrice(),
                "orderBy", username
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Order creation failed: " + e.getMessage()));
        }
    }
    
    // Update order status
    @PutMapping("/{id}")
    @RequiredRole({"CHEF", "DELIVERY", "ADMIN"})
    public ResponseEntity<OrderEntity> updateStatus(@PathVariable Long id, @RequestBody Map<String, Object> statusData) {
        if (!statusData.containsKey("status")) {
            return ResponseEntity.badRequest().build();
        }
        
        return orderRepository.findById(id)
            .map(order -> {
                try {
                    String statusStr = statusData.get("status").toString();
                    OrderEntity.OrderStatus newStatus = OrderEntity.OrderStatus.valueOf(statusStr);
                    order.setStatus(newStatus);
                    OrderEntity savedOrder = orderRepository.save(order);
                    return ResponseEntity.ok(savedOrder);
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().<OrderEntity>build();
                }
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    // Cancel an order
    @DeleteMapping("/{id}")
    @RequiredRole({"CUSTOMER", "ADMIN"})
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
