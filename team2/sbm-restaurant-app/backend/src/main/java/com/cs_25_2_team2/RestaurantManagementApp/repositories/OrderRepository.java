package com.cs_25_2_team2.RestaurantManagementApp.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cs_25_2_team2.RestaurantManagementApp.entities.CustomerEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.OrderEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.StaffEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    
    // Find orders by customer
    List<OrderEntity> findByCustomerOrderByCreatedAtDesc(CustomerEntity customer);
    
    // Find orders by status
    List<OrderEntity> findByStatusOrderByCreatedAtAsc(OrderEntity.OrderStatus status);
    
    // Find orders by assigned chef
    List<OrderEntity> findByAssignedChefOrderByCreatedAtAsc(StaffEntity chef);
    
    // Find orders by assigned delivery staff
    List<OrderEntity> findByAssignedDeliveryOrderByCreatedAtAsc(StaffEntity delivery);
    
    // Find orders created within date range
    List<OrderEntity> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find order with items
    @Query("SELECT o FROM OrderEntity o LEFT JOIN FETCH o.orderItems WHERE o.orderId = :orderId")
    Optional<OrderEntity> findByIdWithItems(@Param("orderId") String orderId);
    
    // Find orders in kitchen queue (pending, placed, or preparing)
    @Query("SELECT o FROM OrderEntity o WHERE o.status IN ('Pending', 'Placed', 'Preparing') ORDER BY o.createdAt ASC")
    List<OrderEntity> findOrdersInKitchenQueue();
    
    // Find orders ready for delivery
    @Query("SELECT o FROM OrderEntity o WHERE o.status = 'ReadyForDelivery' ORDER BY o.createdAt ASC")
    List<OrderEntity> findOrdersReadyForDelivery();
    
    // Count orders by status
    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.status = :status")
    Long countByStatus(@Param("status") OrderEntity.OrderStatus status);
    
    // Find orders with queue information
    @Query("SELECT o FROM OrderEntity o LEFT JOIN FETCH o.orderQueue WHERE o.orderId = :orderId")
    Optional<OrderEntity> findByIdWithQueue(@Param("orderId") String orderId);
    
    // Count orders by assigned chef and status
    Long countByAssignedChefAndStatus(StaffEntity chef, OrderEntity.OrderStatus status);
    
    // Count orders created after a specific date
    Long countByCreatedAtAfter(LocalDateTime date);
    
    // Sum total price of orders created after a specific date
    @Query("SELECT SUM(o.totalPrice) FROM OrderEntity o WHERE o.createdAt > :date")
    BigDecimal sumTotalPriceByCreatedAtAfter(@Param("date") LocalDateTime date);
}