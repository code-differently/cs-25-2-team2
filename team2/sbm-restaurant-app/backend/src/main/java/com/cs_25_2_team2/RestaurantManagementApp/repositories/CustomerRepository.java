package com.cs_25_2_team2.RestaurantManagementApp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cs_25_2_team2.RestaurantManagementApp.entities.CustomerEntity;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, String> {
    
    // Find customer by username
    Optional<CustomerEntity> findByUsername(String username);
    
    // Find customer by email
    Optional<CustomerEntity> findByEmail(String email);
    
    // Check if username exists
    boolean existsByUsername(String username);
    
    // Check if email exists
    boolean existsByEmail(String email);
    
    // Find customers by phone number
    Optional<CustomerEntity> findByPhoneNumber(String phoneNumber);
    
    // Custom query to find customers with orders
    @Query("SELECT DISTINCT c FROM CustomerEntity c LEFT JOIN FETCH c.orders WHERE c.customerId = :customerId")
    Optional<CustomerEntity> findByIdWithOrders(@Param("customerId") String customerId);
    
    // Custom query to find customer with cart
    @Query("SELECT c FROM CustomerEntity c LEFT JOIN FETCH c.cart WHERE c.customerId = :customerId")
    Optional<CustomerEntity> findByIdWithCart(@Param("customerId") String customerId);
}