package com.cs_25_2_team2.RestaurantManagementApp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cs_25_2_team2.RestaurantManagementApp.entities.CartEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.CustomerEntity;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
    
    // Find cart by customer
    Optional<CartEntity> findByCustomer(CustomerEntity customer);
    
    // Find cart by customer ID
    @Query("SELECT c FROM CartEntity c WHERE c.customer.customerId = :customerId")
    Optional<CartEntity> findByCustomerId(@Param("customerId") String customerId);
    
    // Find cart with items
    @Query("SELECT c FROM CartEntity c LEFT JOIN FETCH c.cartItems WHERE c.customer.customerId = :customerId")
    Optional<CartEntity> findByCustomerIdWithItems(@Param("customerId") String customerId);
    
    // Check if customer has a cart
    boolean existsByCustomer(CustomerEntity customer);
    
    // Delete cart by customer
    void deleteByCustomer(CustomerEntity customer);
}