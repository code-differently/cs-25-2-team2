package com.cs_25_2_team2.RestaurantManagementApp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cs_25_2_team2.RestaurantManagementApp.entities.IngredientEntity;

/**
 * Repository interface for IngredientEntity
 * Provides CRUD operations and custom queries for ingredient management
 */
@Repository
public interface IngredientRepository extends JpaRepository<IngredientEntity, String> {
    
    /**
     * Find ingredient by name (case-insensitive)
     */
    @Query("SELECT i FROM IngredientEntity i WHERE LOWER(i.name) = LOWER(:name)")
    Optional<IngredientEntity> findByNameIgnoreCase(@Param("name") String name);
    
    /**
     * Find all ingredients by availability status
     */
    List<IngredientEntity> findByIsAvailable(boolean isAvailable);
    
    /**
     * Find ingredients by name containing search term (case-insensitive)
     */
    @Query("SELECT i FROM IngredientEntity i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<IngredientEntity> findByNameContainingIgnoreCase(@Param("searchTerm") String searchTerm);
    
    /**
     * Count available ingredients
     */
    @Query("SELECT COUNT(i) FROM IngredientEntity i WHERE i.isAvailable = true")
    long countAvailableIngredients();
    
    /**
     * Find ingredients by multiple IDs
     */
    @Query("SELECT i FROM IngredientEntity i WHERE i.ingredientId IN :ingredientIds")
    List<IngredientEntity> findByIngredientIds(@Param("ingredientIds") List<String> ingredientIds);
}