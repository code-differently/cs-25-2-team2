package com.cs_25_2_team2.RestaurantManagementApp.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

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
import jakarta.persistence.Table;

@Entity
@Table(name = "order_item_customizations")
public class OrderItemCustomizationEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customization_id")
    private Long customizationId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", referencedColumnName = "order_item_id")
    private OrderItemEntity orderItem;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", referencedColumnName = "ingredient_id")
    private IngredientEntity ingredient;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "customization_type", nullable = false)
    private CustomizationType customizationType;
    
    @Column(name = "quantity", precision = 8, scale = 2)
    private BigDecimal quantity = BigDecimal.ONE;
    
    @Column(name = "additional_cost", precision = 8, scale = 2)
    private BigDecimal additionalCost = BigDecimal.ZERO;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    public enum CustomizationType {
        Add, Remove, Extra
    }
    
    // Constructors
    public OrderItemCustomizationEntity() {}
    
    public OrderItemCustomizationEntity(OrderItemEntity orderItem, IngredientEntity ingredient, 
                                       CustomizationType customizationType) {
        this.orderItem = orderItem;
        this.ingredient = ingredient;
        this.customizationType = customizationType;
    }
    
    // Getters and Setters
    public Long getCustomizationId() { return customizationId; }
    public void setCustomizationId(Long customizationId) { this.customizationId = customizationId; }
    
    public OrderItemEntity getOrderItem() { return orderItem; }
    public void setOrderItem(OrderItemEntity orderItem) { this.orderItem = orderItem; }
    
    public IngredientEntity getIngredient() { return ingredient; }
    public void setIngredient(IngredientEntity ingredient) { this.ingredient = ingredient; }
    
    public CustomizationType getCustomizationType() { return customizationType; }
    public void setCustomizationType(CustomizationType customizationType) { this.customizationType = customizationType; }
    
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    
    public BigDecimal getAdditionalCost() { return additionalCost; }
    public void setAdditionalCost(BigDecimal additionalCost) { this.additionalCost = additionalCost; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}