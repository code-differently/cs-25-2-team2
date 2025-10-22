package com.cs_25_2_team2.RestaurantManagementApp.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;
import com.cs_25_2_team2.RestaurantManagementApp.Customer;
import com.cs_25_2_team2.RestaurantManagementApp.Staff;
import com.cs_25_2_team2.RestaurantManagementApp.Order;
import com.cs_25_2_team2.RestaurantManagementApp.Cart;
import com.cs_25_2_team2.RestaurantManagementApp.services.UserService;

/**
 * REST Controller for managing restaurant user profiles.
 * Now properly uses the existing Customer and Staff backend classes via UserService.
 * Provides endpoints for user authentication, profile management, and user-specific operations.
 * 
 * Frontend user mapping:
 * - Frontend: username, role (CUSTOMER/CHEF/ADMIN) 
 * - Backend: Customer class for customers, Staff hierarchy for staff
 * 
 * Base URL: /api/users
 * CORS enabled for: http://localhost:3000 (Next.js development server)
 * 
 * @author Team 2
 * @version 1.0
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * Login endpoint - maps frontend username/password to backend Customer/Staff
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        Object user = userService.authenticateUser(username, password);
        if (user != null) {
            Map<String, Object> response = mapUserToFrontendFormat(user);
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    
    /**
     * Register new user - creates Customer or Staff based on role
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> userData) {
        try {
            String username = userData.get("username");
            String name = userData.get("name");
            String address = userData.getOrDefault("address", "");
            String phoneNumber = userData.getOrDefault("phoneNumber", "");
            String role = userData.getOrDefault("role", "CUSTOMER");
            
            Object user;
            if ("CUSTOMER".equalsIgnoreCase(role)) {
                user = userService.registerCustomer(username, name, address, phoneNumber);
            } else {
                user = userService.registerStaff(username, name, address, phoneNumber, role);
            }
            
            Map<String, Object> response = mapUserToFrontendFormat(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get user profile
     */
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getUserProfile(@RequestParam String username) {
        Object user = userService.getUserByUsername(username);
        if (user != null) {
            Map<String, Object> response = mapUserToFrontendFormat(user);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Update user profile
     */
    @PutMapping("/profile")
    public ResponseEntity<Map<String, Object>> updateUserProfile(@RequestBody Map<String, Object> profileData) {
        try {
            String userId = (String) profileData.get("id");
            String name = (String) profileData.get("name");
            String address = (String) profileData.get("address");
            String phoneNumber = (String) profileData.get("phoneNumber");
            
            // Check if it's a customer (numeric ID) or staff (string ID)
            if (userId.matches("\\d+")) {
                int customerId = Integer.parseInt(userId);
                Customer updatedCustomer = userService.updateCustomerProfile(customerId, name, address, phoneNumber);
                if (updatedCustomer != null) {
                    return ResponseEntity.ok(mapUserToFrontendFormat(updatedCustomer));
                }
            } else {
                // Staff profile update would go here
                Staff staff = userService.getStaffById(userId);
                if (staff != null) {
                    return ResponseEntity.ok(mapUserToFrontendFormat(staff));
                }
            }
            
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get user addresses (for customers)
     */
    @GetMapping("/addresses")
    public ResponseEntity<List<Map<String, String>>> getUserAddresses(@RequestParam String userId) {
        try {
            if (userId.matches("\\d+")) {
                int customerId = Integer.parseInt(userId);
                Customer customer = userService.getCustomerById(customerId);
                if (customer != null) {
                    // Since Customer has single address, return it as a list
                    List<Map<String, String>> addresses = new ArrayList<>();
                    Map<String, String> address = new HashMap<>();
                    address.put("id", "1");
                    address.put("address", customer.getAddress());
                    address.put("type", "Primary");
                    addresses.add(address);
                    return ResponseEntity.ok(addresses);
                }
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Add new address (for future expansion)
     */
    @PostMapping("/addresses")
    public ResponseEntity<Map<String, String>> addUserAddress(@RequestBody Map<String, String> addressData) {
        // For now, return the address as-is since Customer has single address
        return ResponseEntity.ok(addressData);
    }
    
    /**
     * Update address
     */
    @PutMapping("/addresses/{id}")
    public ResponseEntity<Map<String, String>> updateUserAddress(@PathVariable String id, @RequestBody Map<String, String> addressData) {
        // For now, return the updated address
        addressData.put("id", id);
        return ResponseEntity.ok(addressData);
    }
    
    /**
     * Delete address
     */
    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<String> deleteUserAddress(@PathVariable String id) {
        return ResponseEntity.ok("Address deleted successfully");
    }
    
    /**
     * Get user order history - uses Customer.getCart() and integrates with orders
     */
    @GetMapping("/order-history")
    public ResponseEntity<List<Map<String, Object>>> getOrderHistory(@RequestParam String userId) {
        try {
            if (userId.matches("\\d+")) {
                int customerId = Integer.parseInt(userId);
                Customer customer = userService.getCustomerById(customerId);
                if (customer != null) {
                    // For now, return empty list since order history requires integration with order management
                    // In full implementation, this would fetch customer's order history
                    List<Map<String, Object>> orderHistory = new ArrayList<>();
                    return ResponseEntity.ok(orderHistory);
                }
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Change password
     */
    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody Map<String, String> passwordData) {
        // For demo purposes, always return success
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password changed successfully");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Customer checkout - uses existing Customer.checkout() method
     */
    @PostMapping("/checkout")
    public ResponseEntity<Map<String, Object>> customerCheckout(@RequestParam String userId) {
        try {
            if (userId.matches("\\d+")) {
                int customerId = Integer.parseInt(userId);
                Order order = userService.customerCheckout(customerId);
                
                // Map order to frontend format
                Map<String, Object> orderData = new HashMap<>();
                orderData.put("id", order.getId());
                orderData.put("customerId", order.getCustomer().getCustomerId());
                orderData.put("totalPrice", order.getTotalPrice());
                orderData.put("status", order.getStatus().toString());
                orderData.put("createdAt", order.getCreatedAt().toString());
                
                return ResponseEntity.ok(orderData);
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Get customer's cart
     */
    @GetMapping("/cart")
    public ResponseEntity<Map<String, Object>> getCustomerCart(@RequestParam String userId) {
        try {
            if (userId.matches("\\d+")) {
                int customerId = Integer.parseInt(userId);
                Cart cart = userService.getCustomerCart(customerId);
                if (cart != null) {
                    Map<String, Object> cartData = new HashMap<>();
                    cartData.put("customerId", cart.getCustomerId());
                    cartData.put("items", cart.getItems());
                    cartData.put("total", cart.calculateTotal());
                    return ResponseEntity.ok(cartData);
                }
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Maps backend user objects (Customer/Staff) to frontend format
     */
    private Map<String, Object> mapUserToFrontendFormat(Object user) {
        Map<String, Object> userMap = new HashMap<>();
        
        if (user instanceof Customer) {
            Customer customer = (Customer) user;
            userMap.put("id", customer.getCustomerId());
            userMap.put("username", "customer"); // This would be stored separately in real system
            userMap.put("name", customer.getName());
            userMap.put("phoneNumber", customer.getPhoneNumber());
            userMap.put("address", customer.getAddress());
            userMap.put("role", "CUSTOMER");
        } else if (user instanceof Staff) {
            Staff staff = (Staff) user;
            userMap.put("id", staff.getId());
            userMap.put("username", staff.getRole().toLowerCase()); // This would be stored separately in real system
            userMap.put("name", staff.getName());
            userMap.put("phoneNumber", staff.getPhoneNumber());
            userMap.put("address", staff.getAddress());
            userMap.put("role", staff.getRole().toUpperCase());
        }
        
        // Add mock token for frontend compatibility
        userMap.put("token", "mock-jwt-token-" + userMap.get("id") + "-" + System.currentTimeMillis());
        
        return userMap;
    }
}