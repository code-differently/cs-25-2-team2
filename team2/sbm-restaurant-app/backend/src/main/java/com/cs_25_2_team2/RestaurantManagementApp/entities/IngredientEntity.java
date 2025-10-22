package com.cs_25_2_team2.RestaurantManagementApp.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ingredients")
public class IngredientEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id")
    private Long ingredientId;
    
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    
    @Column(name = "is_additional_topping")
    private Boolean isAdditionalTopping = false;
    
    @Column(name = "is_optional")
    private Boolean isOptional = false;
    
    @Column(name = "extra_cost", precision = 8, scale = 2)
    private BigDecimal extraCost = BigDecimal.ZERO;
    
    @Column(name = "is_vegetarian")
    private Boolean isVegetarian = true;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // One-to-Many relationship with menu item ingredients
    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MenuItemIngredientEntity> menuItemIngredients;
    
    // One-to-Many relationship with customizations
    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItemCustomizationEntity> customizations;
    
    // Constructors
    public IngredientEntity() {}
    
    public IngredientEntity(String name) {
        this.name = name;
    }
    
    // Getters and Setters
    public Long getIngredientId() { return ingredientId; }
    public void setIngredientId(Long ingredientId) { this.ingredientId = ingredientId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Boolean getIsAdditionalTopping() { return isAdditionalTopping; }
    public void setIsAdditionalTopping(Boolean isAdditionalTopping) { this.isAdditionalTopping = isAdditionalTopping; }
    
    public Boolean getIsOptional() { return isOptional; }
    public void setIsOptional(Boolean isOptional) { this.isOptional = isOptional; }
    
    public BigDecimal getExtraCost() { return extraCost; }
    public void setExtraCost(BigDecimal extraCost) { this.extraCost = extraCost; }
    
    public Boolean getIsVegetarian() { return isVegetarian; }
    public void setIsVegetarian(Boolean isVegetarian) { this.isVegetarian = isVegetarian; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<MenuItemIngredientEntity> getMenuItemIngredients() { return menuItemIngredients; }
    public void setMenuItemIngredients(List<MenuItemIngredientEntity> menuItemIngredients) { this.menuItemIngredients = menuItemIngredients; }
    
    public List<OrderItemCustomizationEntity> getCustomizations() { return customizations; }
    public void setCustomizations(List<OrderItemCustomizationEntity> customizations) { this.customizations = customizations; }
}