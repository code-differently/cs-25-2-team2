package com.cs_25_2_team2.RestaurantManagementApp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cs_25_2_team2.RestaurantManagementApp.entities.StaffEntity;

@Repository
public interface StaffRepository extends JpaRepository<StaffEntity, String> {
    
    // Find staff by username
    Optional<StaffEntity> findByUsername(String username);
    
    // Find staff by role
    List<StaffEntity> findByRole(StaffEntity.StaffRole role);
    
    // Find all chefs
    List<StaffEntity> findByRoleOrderByName(StaffEntity.StaffRole role);
    
    // Check if username exists
    boolean existsByUsername(String username);
    
    // Find staff by phone number
    Optional<StaffEntity> findByPhoneNumber(String phoneNumber);
    
    // Find chefs with their assigned orders
    @Query("SELECT s FROM StaffEntity s LEFT JOIN FETCH s.ordersAsChef WHERE s.role = 'Chef'")
    List<StaffEntity> findChefsWithOrders();
    
    // Find delivery staff with their assigned orders
    @Query("SELECT s FROM StaffEntity s LEFT JOIN FETCH s.ordersAsDelivery WHERE s.role = 'Delivery'")
    List<StaffEntity> findDeliveryStaffWithOrders();
    
    // Find available chefs (not assigned to any preparing orders)
    @Query("SELECT s FROM StaffEntity s WHERE s.role = 'Chef' AND s.staffId NOT IN " +
           "(SELECT DISTINCT o.assignedChef.staffId FROM OrderEntity o WHERE o.status = 'Preparing' AND o.assignedChef IS NOT NULL)")
    List<StaffEntity> findAvailableChefs();
    
    // Find available delivery staff
    @Query("SELECT s FROM StaffEntity s WHERE s.role = 'Delivery' AND s.staffId NOT IN " +
           "(SELECT DISTINCT o.assignedDelivery.staffId FROM OrderEntity o WHERE o.status = 'OutForDelivery' AND o.assignedDelivery IS NOT NULL)")
    List<StaffEntity> findAvailableDeliveryStaff();
    
    // Count staff by role
    @Query("SELECT COUNT(s) FROM StaffEntity s WHERE s.role = :role")
    Long countByRole(@Param("role") StaffEntity.StaffRole role);
}