package com.cs_25_2_team2.RestaurantManagementApp.entities;

import java.math.BigDecimal;
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
@Table(name = "menu_items")
public class MenuItemEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dish_id")
    private Long dishId;
    
    @Column(name = "restaurant_id")
    private Long restaurantId;
    
    @Column(name = "dish_name", nullable = false)
    private String dishName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;
    
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "cooked_type", nullable = false)
    private CookedType cookedType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "potato_type", nullable = false)
    private PotatoType potatoType;
    
    @Column(name = "is_available")
    private Boolean isAvailable = true;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Many-to-Many relationship with ingredients
    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MenuItemIngredientEntity> ingredients;
    
    // One-to-Many relationship with order items
    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItemEntity> orderItems;
    
    // One-to-Many relationship with cart items
    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartItemEntity> cartItems;
    
    public enum Category {
        MAIN_DISH("Main Dish"), SIDE("Side"), SOUP("Soup");
        
        private final String value;
        Category(String value) { this.value = value; }
        public String getValue() { return value; }
    }
    
    public enum CookedType {
        Fried, Baked, Grilled, Mashed, Roasted, Boiled, Steamed, Scalloped, Soupped
    }
    
    public enum PotatoType {
        Russet, New, YukonGold, Kennebec, AllBlue, AdirondackBlue, RedBliss, 
        GermanButterball, RedThumb, RussianBanana, PurplePeruvian, JapaneseSweet, 
        HannahSweet, JewelYams
    }
    
    // Constructors
    public MenuItemEntity() {}
    
    public MenuItemEntity(String dishName, Category category, BigDecimal price, 
                         CookedType cookedType, PotatoType potatoType) {
        // Let Hibernate generate the IDs automatically
        this.dishName = dishName;
        this.category = category;
        this.price = price;
        this.cookedType = cookedType;
        this.potatoType = potatoType;
    }
    
    // Getters and Setters
    public Long getDishId() { return dishId; }
    public void setDishId(Long dishId) { this.dishId = dishId; }
    
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
    
    public String getDishName() { return dishName; }
    public void setDishName(String dishName) { this.dishName = dishName; }
    
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public CookedType getCookedType() { return cookedType; }
    public void setCookedType(CookedType cookedType) { this.cookedType = cookedType; }
    
    public PotatoType getPotatoType() { return potatoType; }
    public void setPotatoType(PotatoType potatoType) { this.potatoType = potatoType; }
    
    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<MenuItemIngredientEntity> getIngredients() { return ingredients; }
    public void setIngredients(List<MenuItemIngredientEntity> ingredients) { this.ingredients = ingredients; }
    
    public List<OrderItemEntity> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItemEntity> orderItems) { this.orderItems = orderItems; }
    
    public List<CartItemEntity> getCartItems() { return cartItems; }
    public void setCartItems(List<CartItemEntity> cartItems) { this.cartItems = cartItems; }
}