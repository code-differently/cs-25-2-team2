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
public interface IngredientRepository extends JpaRepository<IngredientEntity, Long> {
    
    /**
     * Find ingredient by name (case-insensitive)
     */
    @Query("SELECT i FROM IngredientEntity i WHERE LOWER(i.name) = LOWER(:name)")
    Optional<IngredientEntity> findByNameIgnoreCase(@Param("name") String name);
    
    /**
     * Find all vegetarian ingredients
     */
    List<IngredientEntity> findByIsVegetarian(boolean isVegetarian);
    
    /**
     * Find ingredients by name containing search term (case-insensitive)
     */
    @Query("SELECT i FROM IngredientEntity i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<IngredientEntity> findByNameContainingIgnoreCase(@Param("searchTerm") String searchTerm);
    
    /**
     * Count vegetarian ingredients
     */
    @Query("SELECT COUNT(i) FROM IngredientEntity i WHERE i.isVegetarian = true")
    long countVegetarianIngredients();
    
    /**
     * Find ingredients by multiple IDs
     */
    @Query("SELECT i FROM IngredientEntity i WHERE i.ingredientId IN :ingredientIds")
    List<IngredientEntity> findByIngredientIds(@Param("ingredientIds") List<Long> ingredientIds);
}