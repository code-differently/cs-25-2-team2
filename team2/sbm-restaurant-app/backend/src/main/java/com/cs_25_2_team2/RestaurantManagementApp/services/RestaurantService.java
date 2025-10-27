package com.cs_25_2_team2.RestaurantManagementApp.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cs_25_2_team2.RestaurantManagementApp.entities.CartEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.CartItemEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.CustomerEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.MenuItemEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.OrderEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.OrderItemEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.OrderQueueEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.StaffEntity;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.CartItemRepository;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.CartRepository;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.CustomerRepository;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.MenuItemRepository;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.OrderItemRepository;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.OrderQueueRepository;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.OrderRepository;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.StaffRepository;

/**
 * Comprehensive restaurant business logic service for Phase 3.
 * Coordinates complex operations between multiple entities and repositories.
 * 
 * Features:
 * - Advanced cart management with item persistence
 * - Complete order lifecycle with kitchen queue integration  
 * - Staff assignment and workload balancing
 * - Menu management with availability tracking
 * - Restaurant analytics and reporting
 * 
 * @author Team 2 - Phase 3
 * @version 3.0
 */
@Service
@Transactional
public class RestaurantService {

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private MenuItemRepository menuItemRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private OrderQueueRepository orderQueueRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private StaffRepository staffRepository;

    // ===== ADVANCED CART MANAGEMENT =====

    /**
     * Add item to customer's cart with quantity
     */
    public CartEntity addItemToCart(Long customerId, Long menuItemId, Integer quantity) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        MenuItemEntity menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));
        
        if (!menuItem.getIsAvailable()) {
            throw new RuntimeException("Menu item is not available");
        }
        
        CartEntity cart = customer.getCart();
        if (cart == null) {
            cart = new CartEntity(customer);
            cart = cartRepository.save(cart);
        }
        
        // Check if item already exists in cart
        Optional<CartItemEntity> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getMenuItem().getDishId().equals(menuItemId))
                .findFirst();
        
        if (existingItem.isPresent()) {
            // Update quantity
            CartItemEntity cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            // Create new cart item
            CartItemEntity cartItem = new CartItemEntity(cart, menuItem, quantity);
            cartItemRepository.save(cartItem);
        }
        
        return cartRepository.findById(cart.getCartId()).orElse(cart);
    }

    /**
     * Remove item from cart or decrease quantity
     */
    public CartEntity removeItemFromCart(Long customerId, Long menuItemId, Integer quantityToRemove) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        CartEntity cart = customer.getCart();
        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }
        
        CartItemEntity cartItem = cart.getCartItems().stream()
                .filter(item -> item.getMenuItem().getDishId().equals(menuItemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));
        
        if (cartItem.getQuantity() <= quantityToRemove) {
            // Remove item completely
            cartItemRepository.delete(cartItem);
        } else {
            // Decrease quantity
            cartItem.setQuantity(cartItem.getQuantity() - quantityToRemove);
            cartItemRepository.save(cartItem);
        }
        
        return cartRepository.findById(cart.getCartId()).orElse(cart);
    }

    /**
     * Clear entire cart
     */
    public void clearCart(Long customerId) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        CartEntity cart = customer.getCart();
        if (cart != null && !cart.getCartItems().isEmpty()) {
            cartItemRepository.deleteAll(cart.getCartItems());
            cart.getCartItems().clear();
            cartRepository.save(cart);
        }
    }

    // ===== ADVANCED ORDER PROCESSING =====

    /**
     * Complete order checkout with full business logic
     */
    @Transactional
    public OrderEntity processOrderCheckout(Long customerId, String cardNumber, String cardholderName, 
                                          int expiryMonth, int expiryYear, String cvv) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        CartEntity cart = customer.getCart();
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        
        // Calculate total price
        BigDecimal totalPrice = cart.getCartItems().stream()
                .map(item -> item.getMenuItem().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Create order
        OrderEntity order = new OrderEntity(customer, totalPrice);
        
        // Set payment information (in production, this would be tokenized)
        order.setCreditCardLastFour(cardNumber.substring(cardNumber.length() - 4));
        order.setCreditCardToken("token_" + System.currentTimeMillis());
        order.setCardExpiryMonth(expiryMonth);
        order.setCardExpiryYear(expiryYear);
        order.setCardholderName(cardholderName);
        
        // Save order first to get ID
        order = orderRepository.save(order);
        
        // Create order items from cart items
        for (CartItemEntity cartItem : cart.getCartItems()) {
            BigDecimal unitPrice = cartItem.getMenuItem().getPrice();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            
            OrderItemEntity orderItem = new OrderItemEntity(order, cartItem.getMenuItem(), 
                                                           cartItem.getQuantity(), unitPrice, subtotal);
            orderItemRepository.save(orderItem);
        }
        
        // Create order queue entry
        OrderQueueEntity queueEntry = new OrderQueueEntity(order);
        orderQueueRepository.save(queueEntry);
        
        // Clear cart after successful order
        clearCart(customerId);
        
        // Assign chef if available
        assignChefToOrder(order.getOrderId());
        
        return order;
    }

    /**
     * Assign available chef to order
     */
    public OrderEntity assignChefToOrder(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        if (order.getAssignedChef() != null) {
            return order; // Already assigned
        }
        
        // Find chefs and assign to one with least current orders
        List<StaffEntity> availableChefs = staffRepository.findByRole(StaffEntity.StaffRole.Chef);
        
        if (!availableChefs.isEmpty()) {
            // Simple load balancing - assign to chef with fewest current orders
            StaffEntity selectedChef = availableChefs.stream()
                    .min((chef1, chef2) -> {
                        long count1 = orderRepository.countByAssignedChefAndStatus(
                                chef1, OrderEntity.OrderStatus.Preparing);
                        long count2 = orderRepository.countByAssignedChefAndStatus(
                                chef2, OrderEntity.OrderStatus.Preparing);
                        return Long.compare(count1, count2);
                    })
                    .orElse(availableChefs.get(0));
            
            order.setAssignedChef(selectedChef);
            orderRepository.save(order);
        }
        
        return order;
    }

    /**
     * Update order status with business logic
     */
    public OrderEntity updateOrderStatus(Long orderId, OrderEntity.OrderStatus newStatus) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        OrderEntity.OrderStatus currentStatus = order.getStatus();
        
        // Validate status transition
        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new RuntimeException("Invalid status transition from " + currentStatus + " to " + newStatus);
        }
        
        order.setStatus(newStatus);
        
        // Handle status-specific logic
        switch (newStatus) {
            case Pending:
                // Pending is the initial state, no special handling needed
                break;
            case Placed:
                // Placed after Pending, no special handling needed
                break;
            case Preparing:
                if (order.getAssignedChef() == null) {
                    assignChefToOrder(orderId);
                }
                break;
            case ReadyForDelivery:
                assignDeliveryToOrder(orderId);
                break;
            case OutForDelivery:
                // Update delivery timestamp
                break;
            case Delivered:
                // Complete order - in full implementation, could track completion metrics
                break;
        }
        
        return orderRepository.save(order);
    }

    /**
     * Assign delivery staff to order
     */
    private void assignDeliveryToOrder(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        List<StaffEntity> availableDelivery = staffRepository.findByRole(StaffEntity.StaffRole.Delivery);
        
        if (!availableDelivery.isEmpty()) {
            StaffEntity selectedDelivery = availableDelivery.get(0);
            order.setAssignedDelivery(selectedDelivery);
            orderRepository.save(order);
        }
    }

    /**
     * Validate order status transitions
     */
    private boolean isValidStatusTransition(OrderEntity.OrderStatus from, OrderEntity.OrderStatus to) {
        switch (from) {
            case Pending:
                return to == OrderEntity.OrderStatus.Placed;
            case Placed:
                return to == OrderEntity.OrderStatus.Preparing;
            case Preparing:
                return to == OrderEntity.OrderStatus.ReadyForDelivery;
            case ReadyForDelivery:
                return to == OrderEntity.OrderStatus.OutForDelivery;
            case OutForDelivery:
                return to == OrderEntity.OrderStatus.Delivered;
            case Delivered:
                return false; // No transitions from delivered
            default:
                return false;
        }
    }

    // ===== RESTAURANT ANALYTICS =====

    /**
     * Get restaurant performance metrics
     */
    public RestaurantMetrics getRestaurantMetrics() {
        RestaurantMetrics metrics = new RestaurantMetrics();
        
        // Order statistics
        metrics.totalOrders = orderRepository.count();
        metrics.ordersToday = orderRepository.countByCreatedAtAfter(LocalDateTime.now().minusDays(1));
        metrics.pendingOrders = orderRepository.countByStatus(OrderEntity.OrderStatus.Placed);
        metrics.preparingOrders = orderRepository.countByStatus(OrderEntity.OrderStatus.Preparing);
        
        // Staff statistics  
        metrics.totalChefs = staffRepository.countByRole(StaffEntity.StaffRole.Chef);
        metrics.availableChefs = metrics.totalChefs; // Simplified - assume all available
        metrics.totalDelivery = staffRepository.countByRole(StaffEntity.StaffRole.Delivery);
        metrics.availableDelivery = metrics.totalDelivery; // Simplified - assume all available
        
        // Revenue statistics (today)
        metrics.revenueToday = orderRepository.sumTotalPriceByCreatedAtAfter(
                LocalDateTime.now().minusDays(1));
        if (metrics.revenueToday == null) {
            metrics.revenueToday = BigDecimal.ZERO;
        }
        
        return metrics;
    }

    /**
     * Get popular menu items
     */
    public List<MenuItemPopularity> getPopularMenuItems(int limit) {
        List<Object[]> results = orderItemRepository.findPopularMenuItems(limit);
        return results.stream()
            .map(result -> new MenuItemPopularity((MenuItemEntity) result[0], (Long) result[1]))
            .toList();
    }

    // ===== DATA TRANSFER OBJECTS =====

    /**
     * Restaurant performance metrics DTO
     */
    public static class RestaurantMetrics {
        public Long totalOrders;
        public Long ordersToday;
        public Long pendingOrders;
        public Long preparingOrders;
        public Long totalChefs;
        public Long availableChefs;
        public Long totalDelivery;
        public Long availableDelivery;
        public BigDecimal revenueToday;
    }

    /**
     * Menu item popularity DTO
     */
    public static class MenuItemPopularity {
        public MenuItemEntity menuItem;
        public Long orderCount;
        
        public MenuItemPopularity(MenuItemEntity menuItem, Long orderCount) {
            this.menuItem = menuItem;
            this.orderCount = orderCount;
        }
    }
}
