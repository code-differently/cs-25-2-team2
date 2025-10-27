package com.cs_25_2_team2.RestaurantManagementApp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cs_25_2_team2.RestaurantManagementApp.entities.OrderQueueEntity;

@Repository
public interface OrderQueueRepository extends JpaRepository<OrderQueueEntity, Long> {
    
    /**
     * Find queue entry by order ID
     */
    Optional<OrderQueueEntity> findByOrderOrderId(Long orderId);
    
    /**
     * Find all queue entries that are in preparation (started but not ready)
     */
    @Query("SELECT oq FROM OrderQueueEntity oq WHERE oq.startedPreparingAt IS NOT NULL AND oq.readyForDeliveryAt IS NULL")
    List<OrderQueueEntity> findOrdersInPreparation();
    
    /**
     * Find all queue entries that are ready for delivery
     */
    @Query("SELECT oq FROM OrderQueueEntity oq WHERE oq.readyForDeliveryAt IS NOT NULL AND oq.outForDeliveryAt IS NULL")
    List<OrderQueueEntity> findOrdersReadyForDelivery();
    
    /**
     * Find all pending orders (added to queue but not started preparing)
     */
    @Query("SELECT oq FROM OrderQueueEntity oq WHERE oq.startedPreparingAt IS NULL ORDER BY oq.addedToQueueAt ASC")
    List<OrderQueueEntity> findPendingOrders();
    
    /**
     * Find all orders out for delivery
     */
    @Query("SELECT oq FROM OrderQueueEntity oq WHERE oq.outForDeliveryAt IS NOT NULL AND oq.deliveredAt IS NULL")
    List<OrderQueueEntity> findOrdersOutForDelivery();
    
    /**
     * Find all completed/delivered orders
     */
    @Query("SELECT oq FROM OrderQueueEntity oq WHERE oq.deliveredAt IS NOT NULL")
    List<OrderQueueEntity> findCompletedOrders();
}
