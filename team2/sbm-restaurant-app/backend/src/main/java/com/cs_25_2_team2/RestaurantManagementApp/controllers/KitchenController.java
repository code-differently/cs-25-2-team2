package com.cs_25_2_team2.RestaurantManagementApp.controllers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs_25_2_team2.RestaurantManagementApp.entities.OrderEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.OrderQueueEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.StaffEntity;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.OrderQueueRepository;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.OrderRepository;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.StaffRepository;

/**
 * REST Controller for managing kitchen operations.
 * Modernized to use JPA entities directly instead of domain classes.
 * Uses OrderEntity, StaffEntity, and OrderQueueEntity with their respective repositories.
 * 
 * Base URL: /api/kitchen
 * CORS enabled for: http://localhost:3000 (Next.js development server)
 * 
 * @author Team 2
 * @version 2.0 - Modernized to use entity classes directly
 */
@RestController
@RequestMapping("/api/kitchen")
@CrossOrigin(origins = "http://localhost:3000")
public class KitchenController {
    
    private final OrderRepository orderRepository;
    private final OrderQueueRepository orderQueueRepository;
    private final StaffRepository staffRepository;
    
    @Autowired
    public KitchenController(OrderRepository orderRepository, 
                           OrderQueueRepository orderQueueRepository,
                           StaffRepository staffRepository) {
        this.orderRepository = orderRepository;
        this.orderQueueRepository = orderQueueRepository;
        this.staffRepository = staffRepository;
    }
    
    /**
     * Get all pending orders for kitchen preparation
     */
    @GetMapping("/orders/pending")
    public ResponseEntity<Map<String, Object>> getPendingOrders() {
        try {
            List<OrderQueueEntity> pendingQueue = orderQueueRepository.findPendingOrders();
            List<Map<String, Object>> orderData = pendingQueue.stream()
                .map(queueEntry -> mapOrderToResponse(queueEntry.getOrder()))
                .toList();
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("orders", orderData);
            response.put("count", orderData.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Start preparing an order - assigns to available chef
     */
    @PutMapping("/orders/{id}/start")
    public ResponseEntity<Map<String, Object>> startPreparingOrder(@PathVariable Long id) {
        try {
            // Find the order
            Optional<OrderEntity> orderOpt = orderRepository.findById(id);
            if (orderOpt.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Order not found");
                return ResponseEntity.notFound().build();
            }
            
            OrderEntity order = orderOpt.get();
            
            // Find available chef (Staff with Chef role)
            List<StaffEntity> availableChefs = staffRepository.findAll().stream()
                .filter(staff -> staff.getRole() == StaffEntity.StaffRole.Chef)
                .toList();
            
            if (availableChefs.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "No available chefs");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Assign to first available chef
            StaffEntity chef = availableChefs.get(0);
            order.setAssignedChef(chef);
            order.setStatus(OrderEntity.OrderStatus.Preparing);
            orderRepository.save(order);
            
            // Update queue entry
            Optional<OrderQueueEntity> queueOpt = orderQueueRepository.findByOrderOrderId(id);
            if (queueOpt.isPresent()) {
                OrderQueueEntity queueEntry = queueOpt.get();
                queueEntry.setStartedPreparingAt(LocalDateTime.now());
                orderQueueRepository.save(queueEntry);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Order preparation started successfully");
            response.put("orderId", id);
            response.put("assignedChef", mapStaffToResponse(chef));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Mark order as ready for delivery
     */
    @PutMapping("/orders/{id}/complete")
    public ResponseEntity<Map<String, Object>> completeOrder(@PathVariable Long id) {
        try {
            Optional<OrderEntity> orderOpt = orderRepository.findById(id);
            if (orderOpt.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Order not found");
                return ResponseEntity.notFound().build();
            }
            
            OrderEntity order = orderOpt.get();
            order.setStatus(OrderEntity.OrderStatus.ReadyForDelivery);
            orderRepository.save(order);
            
            // Update queue entry
            Optional<OrderQueueEntity> queueOpt = orderQueueRepository.findByOrderOrderId(id);
            if (queueOpt.isPresent()) {
                OrderQueueEntity queueEntry = queueOpt.get();
                queueEntry.setReadyForDeliveryAt(LocalDateTime.now());
                orderQueueRepository.save(queueEntry);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Order marked as ready for delivery");
            response.put("orderId", id);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get the complete kitchen order queue
     */
    @GetMapping("/order-queue")
    public ResponseEntity<Map<String, Object>> getOrderQueue() {
        try {
            List<OrderQueueEntity> pendingQueue = orderQueueRepository.findPendingOrders();
            List<OrderQueueEntity> preparingQueue = orderQueueRepository.findOrdersInPreparation();
            List<OrderQueueEntity> readyQueue = orderQueueRepository.findOrdersReadyForDelivery();
            
            Map<String, Object> queueData = new HashMap<>();
            queueData.put("pending", pendingQueue.stream()
                .map(qe -> mapOrderToResponse(qe.getOrder())).toList());
            queueData.put("preparing", preparingQueue.stream()
                .map(qe -> mapOrderToResponse(qe.getOrder())).toList());
            queueData.put("ready", readyQueue.stream()
                .map(qe -> mapOrderToResponse(qe.getOrder())).toList());
            
            queueData.put("status", "success");
            queueData.put("totalOrders", pendingQueue.size() + preparingQueue.size() + readyQueue.size());
            
            return ResponseEntity.ok(queueData);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get estimated preparation time based on current queue and available chefs
     */
    @GetMapping("/estimate-time")
    public ResponseEntity<Map<String, Object>> getEstimatedTime() {
        try {
            List<OrderQueueEntity> pendingOrders = orderQueueRepository.findPendingOrders();
            List<StaffEntity> availableChefs = staffRepository.findAll().stream()
                .filter(staff -> staff.getRole() == StaffEntity.StaffRole.Chef)
                .toList();
            
            Map<String, Object> timeData = new HashMap<>();
            
            if (availableChefs.isEmpty()) {
                timeData.put("status", "error");
                timeData.put("message", "No chefs available");
                timeData.put("estimatedMinutes", -1);
            } else {
                // Simple estimation: 10 minutes per order per chef
                int estimatedMinutes = (pendingOrders.size() * 10) / availableChefs.size();
                timeData.put("status", "success");
                timeData.put("estimatedMinutes", estimatedMinutes);
                timeData.put("estimatedTime", estimatedMinutes + " minutes");
                timeData.put("availableChefs", availableChefs.size());
                timeData.put("pendingOrders", pendingOrders.size());
            }
            
            return ResponseEntity.ok(timeData);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get all chefs in the kitchen
     */
    @GetMapping("/chefs")
    public ResponseEntity<Map<String, Object>> getAllChefs() {
        try {
            List<StaffEntity> chefs = staffRepository.findAll().stream()
                .filter(staff -> staff.getRole() == StaffEntity.StaffRole.Chef)
                .toList();
            
            List<Map<String, Object>> chefData = chefs.stream()
                .map(this::mapStaffToResponse)
                .toList();
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("chefs", chefData);
            response.put("count", chefData.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get available chefs (those with Chef role)
     */
    @GetMapping("/chefs/available")
    public ResponseEntity<Map<String, Object>> getAvailableChefs() {
        try {
            List<StaffEntity> availableChefs = staffRepository.findAll().stream()
                .filter(staff -> staff.getRole() == StaffEntity.StaffRole.Chef)
                .toList();
            
            List<Map<String, Object>> chefData = availableChefs.stream()
                .map(this::mapStaffToResponse)
                .toList();
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("chefs", chefData);
            response.put("count", chefData.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get orders assigned to a specific chef
     */
    @GetMapping("/chef/{chefId}/orders")
    public ResponseEntity<Map<String, Object>> getOrdersForChef(@PathVariable Long chefId) {
        try {
            // Find orders where assignedChef.staffId = chefId and status is Preparing
            List<OrderEntity> chefOrders = orderRepository.findAll().stream()
                .filter(order -> order.getAssignedChef() != null && 
                               order.getAssignedChef().getStaffId().equals(chefId) &&
                               order.getStatus() == OrderEntity.OrderStatus.Preparing)
                .toList();
            
            List<Map<String, Object>> orderData = chefOrders.stream()
                .map(this::mapOrderToResponse)
                .toList();
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("orders", orderData);
            response.put("count", orderData.size());
            response.put("chefId", chefId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get kitchen statistics for admin dashboard
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getKitchenStatistics() {
        try {
            List<OrderQueueEntity> pendingOrders = orderQueueRepository.findPendingOrders();
            List<OrderQueueEntity> preparingOrders = orderQueueRepository.findOrdersInPreparation();
            List<OrderQueueEntity> readyOrders = orderQueueRepository.findOrdersReadyForDelivery();
            List<StaffEntity> chefs = staffRepository.findAll().stream()
                .filter(staff -> staff.getRole() == StaffEntity.StaffRole.Chef)
                .toList();
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("status", "success");
            stats.put("pendingOrdersCount", pendingOrders.size());
            stats.put("preparingOrdersCount", preparingOrders.size());
            stats.put("readyOrdersCount", readyOrders.size());
            stats.put("totalChefsCount", chefs.size());
            stats.put("averagePreparationTime", "10 minutes"); // Placeholder
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Add order to kitchen queue (called when new order is placed)
     */
    @PostMapping("/orders")
    public ResponseEntity<Map<String, Object>> addOrderToQueue(@RequestBody Map<String, Object> orderData) {
        try {
            Long orderId = Long.valueOf(orderData.get("orderId").toString());
            
            Optional<OrderEntity> orderOpt = orderRepository.findById(orderId);
            if (orderOpt.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Order not found");
                return ResponseEntity.notFound().build();
            }
            
            OrderEntity order = orderOpt.get();
            
            // Create queue entry if it doesn't exist
            Optional<OrderQueueEntity> existingQueue = orderQueueRepository.findByOrderOrderId(orderId);
            if (existingQueue.isEmpty()) {
                OrderQueueEntity queueEntry = new OrderQueueEntity(order);
                orderQueueRepository.save(queueEntry);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Order added to kitchen queue");
            response.put("orderId", orderId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    // ============ Helper Methods ============
    
    /**
     * Maps OrderEntity to frontend-compatible response format
     */
    private Map<String, Object> mapOrderToResponse(OrderEntity order) {
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("id", order.getOrderId());
        orderMap.put("customerId", order.getCustomer().getCustomerId());
        orderMap.put("customerName", order.getCustomer().getName());
        orderMap.put("totalPrice", order.getTotalPrice());
        orderMap.put("status", order.getStatus().toString());
        orderMap.put("createdAt", order.getCreatedAt().toString());
        
        // Add assigned chef if available
        if (order.getAssignedChef() != null) {
            orderMap.put("assignedChef", mapStaffToResponse(order.getAssignedChef()));
        }
        
        // Add order items count if available
        if (order.getOrderItems() != null) {
            orderMap.put("itemCount", order.getOrderItems().size());
        }
        
        return orderMap;
    }
    
    /**
     * Maps StaffEntity to frontend-compatible response format
     */
    private Map<String, Object> mapStaffToResponse(StaffEntity staff) {
        Map<String, Object> staffMap = new HashMap<>();
        staffMap.put("id", staff.getStaffId());
        staffMap.put("name", staff.getName());
        staffMap.put("username", staff.getUsername());
        staffMap.put("phoneNumber", staff.getPhoneNumber());
        staffMap.put("role", staff.getRole().toString());
        
        return staffMap;
    }
}
