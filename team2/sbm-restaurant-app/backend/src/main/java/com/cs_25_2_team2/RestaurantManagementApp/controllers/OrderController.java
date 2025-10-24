package com.cs_25_2_team2.RestaurantManagementApp.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.*;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs_25_2_team2.RestaurantManagementApp.Order;
import com.cs_25_2_team2.RestaurantManagementApp.CartItem;
import com.cs_25_2_team2.RestaurantManagementApp.Customer;

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
    private final List<Order> orders = new ArrayList<>();
    
    /**
     * Default constructor for OrderController.
     * Initializes the controller with an empty list of orders.
     * Note: This uses in-memory storage - orders will be lost when the application restarts.
     * In a production environment, this should be replaced with a proper database service.
     */
    public OrderController() {
    }

    // Endpoint to return all orders
    @GetMapping
    public List<Order> getAllOrders() {
        return orders;
    }

    // Endpoint to get a specific order
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orders.stream()
            .filter(o -> o.getId() == id)
            .findFirst()
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/items")
    public ResponseEntity<List<CartItem>> getOrderItems() {
        // Return all cart items across all orders
        List<CartItem> allItems = orders.stream()
            .flatMap(order -> order.getItems().stream())
            .toList();
        return ResponseEntity.ok(allItems);
    }

    // Endpoint to create a new order
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order newOrder) {
        orders.add(newOrder);
        return ResponseEntity.ok(newOrder);
    }
    
    // Update order status
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestBody String status) {
        return orders.stream()
            .filter(o -> o.getId() == id)
            .findFirst()
            .map(order -> {
                try {
                    order.updateStatus(Order.Status.valueOf(status));
                    return ResponseEntity.ok(order);
                } catch (IllegalArgumentException | IllegalStateException e) {
                    return ResponseEntity.badRequest().<Order>build();
                }
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    // Cancel an order
    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id) {
        return orders.stream()
            .filter(o -> o.getId() == id)
            .findFirst()
            .map(order -> {
                try {
                    order.cancelOrder();
                    return ResponseEntity.ok("Order cancelled successfully");
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body("Cannot cancel order: " + e.getMessage());
                }
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    // Get orders by customer
    @GetMapping("/customer/{customerId}")
    public List<Order> getOrdersByCustomer(@PathVariable Long customerId) {
        return orders.stream()
            .filter(o -> o.getCustomer().getCustomerId() == customerId)
            .toList();
    }
    
    // Get orders by status
    @GetMapping("/status/{status}")
    public List<Order> getOrdersByStatus(@PathVariable String status) {
        try {
            Order.Status orderStatus = Order.Status.valueOf(status);
            return orders.stream()
                .filter(o -> o.getStatus() == orderStatus)
                .toList();
        } catch (IllegalArgumentException e) {
            return new ArrayList<>();
        }
    }
}
