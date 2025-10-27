package com.cs_25_2_team2.RestaurantManagementApp.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.*;
import java.time.LocalDateTime;

import com.cs_25_2_team2.RestaurantManagementApp.entities.CustomerEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.StaffEntity;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.CustomerRepository;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.StaffRepository;

/**
 * REST Controller for managing restaurant users (customers and staff).
 * Uses CustomerEntity and StaffEntity for user management operations.
 * Provides endpoints for user authentication, profile management, and user operations.
 * 
 * Base URL: /api/users
 * CORS enabled for: http://localhost:3000 (Next.js development server)
 * 
 * @author Team 2
 * @version 2.0 - Modernized to use JPA entities directly
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    
    private CustomerRepository customerRepository;
    private StaffRepository staffRepository;

    /**
     * Default constructor for UserController.
     */
    public UserController() {
    }
    
    /**
     * Get user count for health check
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> getUserCount() {
        long customerCount = customerRepository.count();
        long staffCount = staffRepository.count();
        
        Map<String, Integer> counts = new HashMap<>();
        counts.put("customers", (int) customerCount);
        counts.put("staff", (int) staffCount);
        counts.put("total", (int) (customerCount + staffCount));
        
        return ResponseEntity.ok(counts);
    }
    
    /**
     * User login endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> credentials) {
        String username = (String) credentials.get("username");
        String password = (String) credentials.get("password");
        
        if (username == null || password == null) {
            return ResponseEntity.badRequest().build();
        }
        
        // Try to find customer first
        Optional<CustomerEntity> customer = customerRepository.findByUsername(username);
        if (customer.isPresent() && checkPassword(customer.get().getPasswordHash(), password)) {
            Map<String, Object> response = mapCustomerToResponse(customer.get());
            response.put("role", "CUSTOMER");
            return ResponseEntity.ok(response);
        }
        
        // Try to find staff
        Optional<StaffEntity> staff = staffRepository.findByUsername(username);
        if (staff.isPresent() && checkPassword(staff.get().getPasswordHash(), password)) {
            Map<String, Object> response = mapStaffToResponse(staff.get());
            response.put("role", staff.get().getRole().toString());
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    
    /**
     * Register new customer
     */
    @PostMapping("/register")
    public ResponseEntity<CustomerEntity> registerCustomer(@RequestBody Map<String, Object> userData) {
        if (!userData.containsKey("username") || !userData.containsKey("password")) {
            return ResponseEntity.badRequest().build();
        }
        
        String username = (String) userData.get("username");
        String password = (String) userData.get("password");
        
        // Check if username already exists
        if (customerRepository.existsByUsername(username) || staffRepository.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        CustomerEntity newCustomer = new CustomerEntity();
        newCustomer.setUsername(username);
        newCustomer.setPasswordHash(hashPassword(password)); // In real implementation, use proper password hashing
        newCustomer.setCreatedAt(LocalDateTime.now());
        
        // Set optional fields
        if (userData.containsKey("name")) {
            newCustomer.setName((String) userData.get("name"));
        }
        if (userData.containsKey("email")) {
            newCustomer.setEmail((String) userData.get("email"));
        }
        if (userData.containsKey("phoneNumber")) {
            newCustomer.setPhoneNumber((String) userData.get("phoneNumber"));
        }
        if (userData.containsKey("address")) {
            newCustomer.setAddress((String) userData.get("address"));
        }
        
        CustomerEntity savedCustomer = customerRepository.save(newCustomer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }
    
    /**
     * Get user profile by ID
     */
    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerEntity> getCustomerById(@PathVariable Long id) {
        return customerRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get staff profile by ID
     */
    @GetMapping("/staff/{id}")
    public ResponseEntity<StaffEntity> getStaffById(@PathVariable Long id) {
        return staffRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Update customer profile
     */
    @PutMapping("/customers/{id}")
    public ResponseEntity<CustomerEntity> updateCustomer(@PathVariable Long id, @RequestBody Map<String, Object> customerData) {
        return customerRepository.findById(id)
            .map(existingCustomer -> {
                // Update fields from the map
                if (customerData.containsKey("name")) {
                    existingCustomer.setName((String) customerData.get("name"));
                }
                if (customerData.containsKey("email")) {
                    existingCustomer.setEmail((String) customerData.get("email"));
                }
                if (customerData.containsKey("phoneNumber")) {
                    existingCustomer.setPhoneNumber((String) customerData.get("phoneNumber"));
                }
                if (customerData.containsKey("address")) {
                    existingCustomer.setAddress((String) customerData.get("address"));
                }
                
                existingCustomer.setUpdatedAt(LocalDateTime.now());
                CustomerEntity savedCustomer = customerRepository.save(existingCustomer);
                return ResponseEntity.ok(savedCustomer);
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Update staff profile
     */
    @PutMapping("/staff/{id}")
    public ResponseEntity<StaffEntity> updateStaff(@PathVariable Long id, @RequestBody Map<String, Object> staffData) {
        return staffRepository.findById(id)
            .map(existingStaff -> {
                // Update fields from the map
                if (staffData.containsKey("name")) {
                    existingStaff.setName((String) staffData.get("name"));
                }
                if (staffData.containsKey("phoneNumber")) {
                    existingStaff.setPhoneNumber((String) staffData.get("phoneNumber"));
                }
                
                existingStaff.setUpdatedAt(LocalDateTime.now());
                StaffEntity savedStaff = staffRepository.save(existingStaff);
                return ResponseEntity.ok(savedStaff);
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get all customers (admin only)
     */
    @GetMapping("/customers")
    public List<CustomerEntity> getAllCustomers() {
        return (List<CustomerEntity>) customerRepository.findAll();
    }
    
    /**
     * Get all staff (admin only)
     */
    @GetMapping("/staff")
    public List<StaffEntity> getAllStaff() {
        return (List<StaffEntity>) staffRepository.findAll();
    }
    
    /**
     * Delete customer by ID
     */
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Delete staff by ID
     */
    @DeleteMapping("/staff/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        if (staffRepository.existsById(id)) {
            staffRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Find user by username (for frontend search)
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> findByUsername(@RequestParam String username) {
        Optional<CustomerEntity> customer = customerRepository.findByUsername(username);
        if (customer.isPresent()) {
            Map<String, Object> response = mapCustomerToResponse(customer.get());
            response.put("userType", "customer");
            return ResponseEntity.ok(response);
        }
        
        Optional<StaffEntity> staff = staffRepository.findByUsername(username);
        if (staff.isPresent()) {
            Map<String, Object> response = mapStaffToResponse(staff.get());
            response.put("userType", "staff");
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.notFound().build();
    }
    
    // Helper methods
    private Map<String, Object> mapCustomerToResponse(CustomerEntity customer) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", customer.getCustomerId());
        response.put("username", customer.getUsername());
        response.put("name", customer.getName());
        response.put("email", customer.getEmail());
        response.put("phoneNumber", customer.getPhoneNumber());
        response.put("address", customer.getAddress());
        response.put("createdAt", customer.getCreatedAt());
        return response;
    }
    
    private Map<String, Object> mapStaffToResponse(StaffEntity staff) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", staff.getStaffId());
        response.put("username", staff.getUsername());
        response.put("name", staff.getName());
        response.put("phoneNumber", staff.getPhoneNumber());
        response.put("role", staff.getRole());
        response.put("createdAt", staff.getCreatedAt());
        return response;
    }
    
    // Simple password checking - in production, use proper password hashing library
    private boolean checkPassword(String hashedPassword, String plainPassword) {
        return hashedPassword != null && hashedPassword.equals(hashPassword(plainPassword));
    }
    
    private String hashPassword(String plainPassword) {
        // In production, use BCrypt or similar
        return "hashed_" + plainPassword; // Simplified for this demo
    }
}
