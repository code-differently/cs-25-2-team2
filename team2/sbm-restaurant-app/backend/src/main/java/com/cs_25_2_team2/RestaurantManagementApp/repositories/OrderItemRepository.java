package com.cs_25_2_team2.RestaurantManagementApp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cs_25_2_team2.RestaurantManagementApp.entities.MenuItemEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.OrderEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.OrderItemEntity;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
    
    // Find order items by order
    List<OrderItemEntity> findByOrder(OrderEntity order);
    
    // Find order items by order ID
    @Query("SELECT oi FROM OrderItemEntity oi WHERE oi.order.orderId = :orderId")
    List<OrderItemEntity> findByOrderId(@Param("orderId") Long orderId);
    
    // Find order items by menu item
    List<OrderItemEntity> findByMenuItem(MenuItemEntity menuItem);
    
    // Get popular menu items with order counts
    @Query("SELECT oi.menuItem, COUNT(oi) " +
           "FROM OrderItemEntity oi " +
           "GROUP BY oi.menuItem " +
           "ORDER BY COUNT(oi) DESC")
    List<Object[]> findPopularMenuItems(@Param("limit") int limit);
    
    // Count order items for a specific menu item
    @Query("SELECT COUNT(oi) FROM OrderItemEntity oi WHERE oi.menuItem.dishId = :menuItemId")
    Long countByMenuItemId(@Param("menuItemId") Long menuItemId);
}
