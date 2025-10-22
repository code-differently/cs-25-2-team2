package com.cs_25_2_team2.RestaurantManagementApp.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cs_25_2_team2.RestaurantManagementApp.entities.MenuItemEntity;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItemEntity, String> {
    
    // Find available menu items
    List<MenuItemEntity> findByIsAvailableTrue();
    
    // Find menu items by category
    List<MenuItemEntity> findByCategoryAndIsAvailableTrue(MenuItemEntity.Category category);
    
    // Find menu items by cooked type
    List<MenuItemEntity> findByCookedTypeAndIsAvailableTrue(MenuItemEntity.CookedType cookedType);
    
    // Find menu items by potato type
    List<MenuItemEntity> findByPotatoTypeAndIsAvailableTrue(MenuItemEntity.PotatoType potatoType);
    
    // Find menu items by price range
    List<MenuItemEntity> findByPriceBetweenAndIsAvailableTrue(BigDecimal minPrice, BigDecimal maxPrice);
    
    // Search menu items by name (case insensitive)
    @Query("SELECT m FROM MenuItemEntity m WHERE LOWER(m.dishName) LIKE LOWER(CONCAT('%', :name, '%')) AND m.isAvailable = true")
    List<MenuItemEntity> findByDishNameContainingIgnoreCaseAndIsAvailableTrue(@Param("name") String name);
    
    // Find menu item with ingredients
    @Query("SELECT m FROM MenuItemEntity m LEFT JOIN FETCH m.ingredients WHERE m.dishId = :dishId")
    Optional<MenuItemEntity> findByIdWithIngredients(@Param("dishId") String dishId);
    
    // Get all categories in use
    @Query("SELECT DISTINCT m.category FROM MenuItemEntity m WHERE m.isAvailable = true")
    List<MenuItemEntity.Category> findDistinctCategories();
    
    // Get all cooked types in use
    @Query("SELECT DISTINCT m.cookedType FROM MenuItemEntity m WHERE m.isAvailable = true")
    List<MenuItemEntity.CookedType> findDistinctCookedTypes();
    
    // Get all potato types in use
    @Query("SELECT DISTINCT m.potatoType FROM MenuItemEntity m WHERE m.isAvailable = true")
    List<MenuItemEntity.PotatoType> findDistinctPotatoTypes();
}