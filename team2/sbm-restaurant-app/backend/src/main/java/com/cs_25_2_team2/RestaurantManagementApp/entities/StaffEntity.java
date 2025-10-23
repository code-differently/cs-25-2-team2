package com.cs_25_2_team2.RestaurantManagementApp.entities;

import java.time.LocalDateTime;
import java.util.List;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "staff")
public class StaffEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private Long staffId;
    
    @Column(name = "username", length = 12, unique = true, nullable = false)
    private String username;
    
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private StaffRole role;
    
    @Column(name = "restaurant_id")
    private Long restaurantId;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // One-to-Many relationship with orders as chef
    @OneToMany(mappedBy = "assignedChef", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderEntity> ordersAsChef;
    
    // One-to-Many relationship with orders as delivery staff
    @OneToMany(mappedBy = "assignedDelivery", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderEntity> ordersAsDelivery;
    
    public enum StaffRole {
        Chef, Delivery
    }
    
    // Constructors
    public StaffEntity() {}
    
    public StaffEntity( String username, String name, String phoneNumber, StaffRole role) {
        // Let Hibernate generate the ID automatically
        this.username = username;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
    
    // Getters and Setters
    public Long getStaffId() { return staffId; }
    public void setStaffId(Long staffId) { this.staffId = staffId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public StaffRole getRole() { return role; }
    public void setRole(StaffRole role) { this.role = role; }
    
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<OrderEntity> getOrdersAsChef() { return ordersAsChef; }
    public void setOrdersAsChef(List<OrderEntity> ordersAsChef) { this.ordersAsChef = ordersAsChef; }
    
    public List<OrderEntity> getOrdersAsDelivery() { return ordersAsDelivery; }
    public void setOrdersAsDelivery(List<OrderEntity> ordersAsDelivery) { this.ordersAsDelivery = ordersAsDelivery; }
}