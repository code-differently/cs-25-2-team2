package com.cs_25_2_team2.RestaurantManagementApp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cs_25_2_team2.RestaurantManagementApp.entities.CartEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.CartItemEntity;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    
    // Find cart items by cart
    List<CartItemEntity> findByCart(CartEntity cart);
    
    // Find cart items by cart ID
    @Query("SELECT ci FROM CartItemEntity ci WHERE ci.cart.cartId = :cartId")
    List<CartItemEntity> findByCartId(@Param("cartId") Long cartId);
    
    // Delete all cart items for a specific cart
    void deleteByCart(CartEntity cart);
    
    // Count items in cart
    @Query("SELECT COUNT(ci) FROM CartItemEntity ci WHERE ci.cart.cartId = :cartId")
    Long countByCartId(@Param("cartId") Long cartId);
}
