package com.cs_25_2_team2.RestaurantManagementApp.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_queue")
public class OrderQueueEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "queue_id")
    private Long queueId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", unique = true)
    private OrderEntity order;
    
    @CreationTimestamp
    @Column(name = "added_to_queue_at")
    private LocalDateTime addedToQueueAt;
    
    @Column(name = "started_preparing_at")
    private LocalDateTime startedPreparingAt;
    
    @Column(name = "ready_for_delivery_at")
    private LocalDateTime readyForDeliveryAt;
    
    @Column(name = "out_for_delivery_at")
    private LocalDateTime outForDeliveryAt;
    
    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;
    
    // Constructors
    public OrderQueueEntity() {}
    
    public OrderQueueEntity(OrderEntity order) {
        this.order = order;
    }
    
    // Getters and Setters
    public Long getQueueId() { return queueId; }
    public void setQueueId(Long queueId) { this.queueId = queueId; }
    
    public OrderEntity getOrder() { return order; }
    public void setOrder(OrderEntity order) { this.order = order; }
    
    public LocalDateTime getAddedToQueueAt() { return addedToQueueAt; }
    public void setAddedToQueueAt(LocalDateTime addedToQueueAt) { this.addedToQueueAt = addedToQueueAt; }
    
    public LocalDateTime getStartedPreparingAt() { return startedPreparingAt; }
    public void setStartedPreparingAt(LocalDateTime startedPreparingAt) { this.startedPreparingAt = startedPreparingAt; }
    
    public LocalDateTime getReadyForDeliveryAt() { return readyForDeliveryAt; }
    public void setReadyForDeliveryAt(LocalDateTime readyForDeliveryAt) { this.readyForDeliveryAt = readyForDeliveryAt; }
    
    public LocalDateTime getOutForDeliveryAt() { return outForDeliveryAt; }
    public void setOutForDeliveryAt(LocalDateTime outForDeliveryAt) { this.outForDeliveryAt = outForDeliveryAt; }
    
    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }
}