package com.cs_25_2_team2.RestaurantManagementApp.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class OrderEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private CustomerEntity customer;
    
    @Column(name = "restaurant_id")
    private Long restaurantId;
    
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.Placed;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_chef_id", referencedColumnName = "staff_id")
    private StaffEntity assignedChef;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_delivery_id", referencedColumnName = "staff_id")
    private StaffEntity assignedDelivery;
    
    // Payment Information
    @Column(name = "credit_card_last_four", length = 4, nullable = false)
    private String creditCardLastFour;
    
    @Column(name = "credit_card_token", nullable = false)
    private String creditCardToken;
    
    @Column(name = "card_expiry_month", nullable = false)
    private Integer cardExpiryMonth;
    
    @Column(name = "card_expiry_year", nullable = false)
    private Integer cardExpiryYear;
    
    @Column(name = "cardholder_name", nullable = false)
    private String cardholderName;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // One-to-Many relationship with order items
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItemEntity> orderItems;
    
    // One-to-One relationship with order queue
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private OrderQueueEntity orderQueue;
    
    public enum OrderStatus {
        Placed, Preparing, ReadyForDelivery, OutForDelivery, Delivered
    }
    
    // Constructors
    public OrderEntity() {}
    
    public OrderEntity(CustomerEntity customer, BigDecimal totalPrice) {
        orderId = new Random().nextLong(99999999999L);
        this.customer = customer;
        this.totalPrice = totalPrice;
    }
    
    // Getters and Setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    
    public CustomerEntity getCustomer() { return customer; }
    public void setCustomer(CustomerEntity customer) { this.customer = customer; }
    
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
    
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    
    public StaffEntity getAssignedChef() { return assignedChef; }
    public void setAssignedChef(StaffEntity assignedChef) { this.assignedChef = assignedChef; }
    
    public StaffEntity getAssignedDelivery() { return assignedDelivery; }
    public void setAssignedDelivery(StaffEntity assignedDelivery) { this.assignedDelivery = assignedDelivery; }
    
    public String getCreditCardLastFour() { return creditCardLastFour; }
    public void setCreditCardLastFour(String creditCardLastFour) { this.creditCardLastFour = creditCardLastFour; }
    
    public String getCreditCardToken() { return creditCardToken; }
    public void setCreditCardToken(String creditCardToken) { this.creditCardToken = creditCardToken; }
    
    public Integer getCardExpiryMonth() { return cardExpiryMonth; }
    public void setCardExpiryMonth(Integer cardExpiryMonth) { this.cardExpiryMonth = cardExpiryMonth; }
    
    public Integer getCardExpiryYear() { return cardExpiryYear; }
    public void setCardExpiryYear(Integer cardExpiryYear) { this.cardExpiryYear = cardExpiryYear; }
    
    public String getCardholderName() { return cardholderName; }
    public void setCardholderName(String cardholderName) { this.cardholderName = cardholderName; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<OrderItemEntity> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItemEntity> orderItems) { this.orderItems = orderItems; }
    
    public OrderQueueEntity getOrderQueue() { return orderQueue; }
    public void setOrderQueue(OrderQueueEntity orderQueue) { this.orderQueue = orderQueue; }
}