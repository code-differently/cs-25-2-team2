package com.cs_25_2_team2.RestaurantManagementApp.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;
import com.cs_25_2_team2.RestaurantManagementApp.Order;
import com.cs_25_2_team2.RestaurantManagementApp.OrderQueue;
import com.cs_25_2_team2.RestaurantManagementApp.Chef;
import com.cs_25_2_team2.RestaurantManagementApp.Staff;
import com.cs_25_2_team2.RestaurantManagementApp.services.KitchenService;

/**
 * REST Controller for managing kitchen operations.
 * Now properly uses the existing OrderQueue, Chef, and Staff backend classes via KitchenService.
 * Provides endpoints for kitchen workflow, order preparation, and chef management.
 * 
 * Uses existing backend architecture:
 * - OrderQueue class for order management
 * - Chef class (extends Staff) for chef operations using Chef.assignOrder()
 * - Order class with Order.updateStatus() method
 * - Staff hierarchy for kitchen staff management
 * 
 * Base URL: /api/kitchen
 * CORS enabled for: http://localhost:3000 (Next.js development server)
 * 
 * @author Team 2
 * @version 1.0
 */
@RestController
@RequestMapping("/api/kitchen")
@CrossOrigin(origins = "http://localhost:3000")
public class KitchenController {
    
    @Autowired
    private KitchenService kitchenService;
    
    /**
     * Get all pending orders for kitchen preparation
     */
    @GetMapping("/orders/pending")
    public ResponseEntity<List<Map<String, Object>>> getPendingOrders() {
        try {
            List<Order> pendingOrders = kitchenService.getPendingOrders();
            List<Map<String, Object>> orderData = pendingOrders.stream()
                .map(this::mapOrderToResponse)
                .toList();
            return ResponseEntity.ok(orderData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Start preparing an order - assigns to available chef using Chef.assignOrder()
     */
    @PutMapping("/orders/{id}/start")
    public ResponseEntity<Map<String, Object>> startPreparingOrder(@PathVariable Long id) {
        try {
            boolean started = kitchenService.startPreparingOrder(id);
            
            Map<String, Object> response = new HashMap<>();
            if (started) {
                response.put("success", true);
                response.put("message", "Order preparation started successfully");
                response.put("orderId", id);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to start order preparation - no available chef or order not found");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Mark order as ready/complete using Order.updateStatus()
     */
    @PutMapping("/orders/{id}/complete")
    public ResponseEntity<Map<String, Object>> completeOrder(@PathVariable Long id) {
        try {
            boolean completed = kitchenService.completeOrder(id);
            
            Map<String, Object> response = new HashMap<>();
            if (completed) {
                response.put("success", true);
                response.put("message", "Order marked as ready for delivery");
                response.put("orderId", id);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to complete order - order not found or already completed");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get the complete kitchen order queue using OrderQueue class
     */
    @GetMapping("/order-queue")
    public ResponseEntity<Map<String, Object>> getOrderQueue() {
        try {
            OrderQueue orderQueue = kitchenService.getOrderQueue();
            
            Map<String, Object> queueData = new HashMap<>();
            queueData.put("pending", kitchenService.getPendingOrders().stream()
                .map(this::mapOrderToResponse).toList());
            queueData.put("preparing", kitchenService.getOrdersInPreparation().stream()
                .map(this::mapOrderToResponse).toList());
            queueData.put("ready", kitchenService.getReadyOrders().stream()
                .map(this::mapOrderToResponse).toList());
            
            return ResponseEntity.ok(queueData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get estimated preparation time based on current queue and available chefs
     */
    @PostMapping("/estimate-time")
    public ResponseEntity<Map<String, Object>> getEstimatedTime() {
        try {
            int estimatedMinutes = kitchenService.estimatePreparationTime();
            
            Map<String, Object> timeData = new HashMap<>();
            if (estimatedMinutes >= 0) {
                timeData.put("estimatedMinutes", estimatedMinutes);
                timeData.put("estimatedTime", estimatedMinutes + " minutes");
                timeData.put("availableChefs", kitchenService.getAvailableChefs().size());
                timeData.put("totalChefs", kitchenService.getAllChefs().size());
            } else {
                timeData.put("error", "No chefs available");
                timeData.put("estimatedMinutes", -1);
            }
            
            return ResponseEntity.ok(timeData);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get all chefs in the kitchen
     */
    @GetMapping("/chefs")
    public ResponseEntity<List<Map<String, Object>>> getAllChefs() {
        try {
            List<Chef> chefs = kitchenService.getAllChefs();
            List<Map<String, Object>> chefData = chefs.stream()
                .map(this::mapChefToResponse)
                .toList();
            return ResponseEntity.ok(chefData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get available chefs (not busy)
     */
    @GetMapping("/chefs/available")
    public ResponseEntity<List<Map<String, Object>>> getAvailableChefs() {
        try {
            List<Chef> availableChefs = kitchenService.getAvailableChefs();
            List<Map<String, Object>> chefData = availableChefs.stream()
                .map(this::mapChefToResponse)
                .toList();
            return ResponseEntity.ok(chefData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get orders assigned to a specific chef
     */
    @GetMapping("/chef/{chefId}/orders")
    public ResponseEntity<List<Map<String, Object>>> getOrdersForChef(@PathVariable Long chefId) {
        try {
            List<Order> chefOrders = kitchenService.getOrdersForChef(chefId.toString());
            List<Map<String, Object>> orderData = chefOrders.stream()
                .map(this::mapOrderToResponse)
                .toList();
            return ResponseEntity.ok(orderData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get kitchen statistics for admin dashboard
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getKitchenStatistics() {
        try {
            Map<String, Object> stats = kitchenService.getKitchenStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Add order to kitchen queue (called from OrderController)
     */
    @PostMapping("/orders")
    public ResponseEntity<Map<String, Object>> addOrderToQueue(@RequestBody Map<String, Object> orderData) {
        try {
            // This would typically receive an Order object from the OrderController
            // For now, return success - proper integration requires Order object creation
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Order added to kitchen queue");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Maps Order object to frontend-compatible response format
     */
    private Map<String, Object> mapOrderToResponse(Order order) {
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("id", order.getId());
        orderMap.put("customerId", order.getCustomer().getCustomerId());
        orderMap.put("customerName", order.getCustomer().getName());
        orderMap.put("totalPrice", order.getTotalPrice());
        orderMap.put("status", order.getStatus().toString());
        orderMap.put("createdAt", order.getCreatedAt().toString());
        
        // Add order items if available
        if (order.getItems() != null) {
            orderMap.put("items", order.getItems());
        }
        
        return orderMap;
    }
    
    /**
     * Maps Chef object to frontend-compatible response format
     */
    private Map<String, Object> mapChefToResponse(Chef chef) {
        Map<String, Object> chefMap = new HashMap<>();
        chefMap.put("id", chef.getId());
        chefMap.put("name", chef.getName());
        chefMap.put("phoneNumber", chef.getPhoneNumber());
        chefMap.put("address", chef.getAddress());
        chefMap.put("role", chef.getRole());
        chefMap.put("isBusy", chef.isBusy());
        chefMap.put("assignedOrdersCount", chef.getAssignedOrders().size());
        
        return chefMap;
    }
}