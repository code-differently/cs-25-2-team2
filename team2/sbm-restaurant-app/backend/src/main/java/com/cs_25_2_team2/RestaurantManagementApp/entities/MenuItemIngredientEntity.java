package com.cs_25_2_team2.RestaurantManagementApp.entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "menu_item_ingredients")
public class MenuItemIngredientEntity {
    
    @EmbeddedId
    private MenuItemIngredientId id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("menuItemId")
    @JoinColumn(name = "menu_item_id", referencedColumnName = "dish_id")
    private MenuItemEntity menuItem;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id", referencedColumnName = "ingredient_id")
    private IngredientEntity ingredient;
    
    @Column(name = "quantity", precision = 8, scale = 2)
    private BigDecimal quantity = BigDecimal.ONE;
    
    @Column(name = "is_required")
    private Boolean isRequired = true;
    
    // Constructors
    public MenuItemIngredientEntity() {}
    
    public MenuItemIngredientEntity(MenuItemEntity menuItem, IngredientEntity ingredient) {
        this.menuItem = menuItem;
        this.ingredient = ingredient;
        this.id = new MenuItemIngredientId(menuItem.getDishId(), ingredient.getIngredientId());
    }
    
    // Getters and Setters
    public MenuItemIngredientId getId() { return id; }
    public void setId(MenuItemIngredientId id) { this.id = id; }
    
    public MenuItemEntity getMenuItem() { return menuItem; }
    public void setMenuItem(MenuItemEntity menuItem) { this.menuItem = menuItem; }
    
    public IngredientEntity getIngredient() { return ingredient; }
    public void setIngredient(IngredientEntity ingredient) { this.ingredient = ingredient; }
    
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    
    public Boolean getIsRequired() { return isRequired; }
    public void setIsRequired(Boolean isRequired) { this.isRequired = isRequired; }
}

@Embeddable
class MenuItemIngredientId {
    @Column(name = "menu_item_id")
    private String menuItemId;
    
    @Column(name = "ingredient_id")
    private Long ingredientId;
    
    public MenuItemIngredientId() {}
    
    public MenuItemIngredientId(String menuItemId, Long ingredientId) {
        this.menuItemId = menuItemId;
        this.ingredientId = ingredientId;
    }
    
    // Getters and Setters
    public String getMenuItemId() { return menuItemId; }
    public void setMenuItemId(String menuItemId) { this.menuItemId = menuItemId; }
    
    public Long getIngredientId() { return ingredientId; }
    public void setIngredientId(Long ingredientId) { this.ingredientId = ingredientId; }
}