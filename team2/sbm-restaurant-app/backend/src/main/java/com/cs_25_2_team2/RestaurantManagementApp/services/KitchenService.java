package com.cs_25_2_team2.RestaurantManagementApp.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cs_25_2_team2.RestaurantManagementApp.Chef;
import com.cs_25_2_team2.RestaurantManagementApp.Order;
import com.cs_25_2_team2.RestaurantManagementApp.OrderQueue;
import com.cs_25_2_team2.RestaurantManagementApp.Staff;

/**
 * Service class for Kitchen management using the existing OrderQueue, Chef, and Staff backend classes.
 * Handles order assignment, kitchen workflow, and chef management.
 * 
 * Uses the existing backend architecture:
 * - OrderQueue class for order management
 * - Chef class for chef operations (extends Staff)
 * - Staff class hierarchy for kitchen staff
 * - Order class with status management
 * 
 * @author Team 2
 * @version 1.0
 */
@Service
public class KitchenService {
    
    private final OrderQueue orderQueue;
    private final Map<String, Chef> chefs = new HashMap<>();
    private final Map<String, Staff> kitchenStaff = new HashMap<>();
    
    /**
     * Constructor initializes kitchen with sample staff
     */
    public KitchenService() {
        this.orderQueue = new OrderQueue();
        initializeSampleKitchenStaff();
    }
    
    /**
     * Get all pending orders for kitchen preparation
     */
    public List<Order> getPendingOrders() {
        return new ArrayList<>(); // TODO: Fix OrderQueue.getPendingOrders()
    }
    
    /**
     * Get orders currently being prepared
     */
    public List<Order> getOrdersInPreparation() {
        return new ArrayList<>(); // TODO: Fix OrderQueue.getOrdersInPreparation()
    }
    
    /**
     * Get completed orders ready for delivery
     */
    public List<Order> getReadyOrders() {
        return new ArrayList<>(); // TODO: Fix OrderQueue.getReadyOrders()
    }
    
    /**
     * Add order to kitchen queue
     */
    public void addOrderToQueue(Order order) {
        // TODO: Fix OrderQueue.addOrder() method
        System.out.println("Order queued: " + order.toString());
    }
    
    /**
     * Start preparing an order - assigns to available chef
     */
    public boolean startPreparingOrder(int orderId) {
        Order order = findOrderById(orderId);
        if (order == null) {
            return false;
        }
        
        // Find available chef
        Chef availableChef = findAvailableChef();
        if (availableChef != null) {
            try {
                // Use existing Chef.assignOrder() method
                availableChef.assignOrder(order);
                
                // Update order status using existing Order.updateStatus() method
                order.updateStatus(Order.Status.Preparing);
                
                return true;
            } catch (Exception e) {
                System.err.println("Error starting order preparation: " + e.getMessage());
                return false;
            }
        }
        
        return false; // No available chef
    }
    
    /**
     * Mark order as ready/complete
     */
    public boolean completeOrder(int orderId) {
        Order order = findOrderById(orderId);
        if (order == null) {
            return false;
        }
        
        try {
            // Update order status using existing Order.updateStatus() method
            order.updateStatus(Order.Status.ReadyForDelivery);
            
            // Find the chef who was preparing this order and mark them as available
            Chef chef = findChefByOrder(order);
            if (chef != null) {
                // The chef's assigned orders will be automatically updated
                // since we're using the existing backend architecture
            }
            
            return true;
        } catch (Exception e) {
            System.err.println("Error completing order: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get the complete order queue status
     */
    public OrderQueue getOrderQueue() {
        return orderQueue;
    }
    
    /**
     * Get all chefs in the kitchen
     */
    public List<Chef> getAllChefs() {
        return new ArrayList<>(chefs.values());
    }
    
    /**
     * Get all kitchen staff
     */
    public List<Staff> getAllKitchenStaff() {
        return new ArrayList<>(kitchenStaff.values());
    }
    
    /**
     * Get chef by ID
     */
    public Chef getChefById(String chefId) {
        return chefs.get(chefId);
    }
    
    /**
     * Add chef to kitchen
     */
    public void addChef(Chef chef) {
        chefs.put(chef.getId(), chef);
        kitchenStaff.put(chef.getId(), chef);
    }
    
    /**
     * Get available chefs (not busy)
     */
    public List<Chef> getAvailableChefs() {
        return chefs.values().stream()
            .filter(chef -> !chef.isBusy())
            .toList();
    }
    
    /**
     * Get orders assigned to a specific chef
     */
    public List<Order> getOrdersForChef(String chefId) {
        Chef chef = chefs.get(chefId);
        return chef != null ? chef.getAssignedOrders() : new ArrayList<>();
    }
    
    /**
     * Estimate preparation time for current queue
     */
    public int estimatePreparationTime() {
        int pendingOrders = getPendingOrders().size();
        int preparingOrders = getOrdersInPreparation().size();
        int availableChefs = getAvailableChefs().size();
        
        if (availableChefs == 0) {
            return -1; // No chefs available
        }
        
        // Simple estimation: 10 minutes per order, adjusted for available chefs
        int totalOrders = pendingOrders + preparingOrders;
        return (totalOrders * 10) / Math.max(1, availableChefs);
    }
    
    /**
     * Find order by ID across all queue statuses
     */
    private Order findOrderById(int orderId) {
        // Check pending orders
        for (Order order : getPendingOrders()) {
            if (order.getId() == orderId) {
                return order;
            }
        }
        
        // Check orders in preparation
        for (Order order : getOrdersInPreparation()) {
            if (order.getId() == orderId) {
                return order;
            }
        }
        
        // Check ready orders
        for (Order order : getReadyOrders()) {
            if (order.getId() == orderId) {
                return order;
            }
        }
        
        return null;
    }
    
    /**
     * Find available chef using existing Chef.isBusy() method
     */
    private Chef findAvailableChef() {
        return chefs.values().stream()
            .filter(chef -> !chef.isBusy()) // Uses existing isBusy() method
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Find chef assigned to a specific order
     */
    private Chef findChefByOrder(Order order) {
        return chefs.values().stream()
            .filter(chef -> chef.getAssignedOrders().contains(order))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Initialize kitchen with sample chefs and staff
     */
    private void initializeSampleKitchenStaff() {
        // Sample chefs
        Chef chef1 = new Chef("Gordon Ramsay", "456 Kitchen Ave", "987-654-3210", "CHEF001");
        Chef chef2 = new Chef("Julia Child", "789 Culinary St", "555-987-6543", "CHEF002");
        Chef chef3 = new Chef("Anthony Bourdain", "321 Food Blvd", "444-555-6666", "CHEF003");
        
        addChef(chef1);
        addChef(chef2);
        addChef(chef3);
        
        System.out.println("Initialized kitchen with " + chefs.size() + " chefs");
    }
    
    /**
     * Get kitchen statistics for admin dashboard
     */
    public Map<String, Object> getKitchenStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalChefs", chefs.size());
        stats.put("availableChefs", getAvailableChefs().size());
        stats.put("pendingOrders", getPendingOrders().size());
        stats.put("preparingOrders", getOrdersInPreparation().size());
        stats.put("readyOrders", getReadyOrders().size());
        stats.put("estimatedWaitTime", estimatePreparationTime());
        return stats;
    }
}