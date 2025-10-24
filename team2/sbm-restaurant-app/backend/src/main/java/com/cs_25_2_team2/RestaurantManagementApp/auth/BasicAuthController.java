package com.cs_25_2_team2.RestaurantManagementApp.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cs_25_2_team2.RestaurantManagementApp.services.UserService;
import com.cs_25_2_team2.RestaurantManagementApp.entities.CustomerEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.StaffEntity;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Basic Authentication Controller for restaurant management system.
 * Provides simple session-based authentication without complex JWT dependencies.
 * 
 * Features:
 * - User login/logout with session management
 * - Role-based authentication (CUSTOMER, CHEF, MANAGER, ADMIN)
 * - Password validation and user registration
 * - Profile management and session tracking
 * 
 * @author Team 2 - Phase 3 Authentication
 * @version 3.1
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class BasicAuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Login request data class
     */
    public static class LoginRequest {
        private String username;
        private String password;
        private String userType; // "customer" or "staff"
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getUserType() { return userType; }
        public void setUserType(String userType) { this.userType = userType; }
    }

    /**
     * Registration request data class
     */
    public static class RegisterRequest {
        private String username;
        private String password;
        private String email;
        private String firstName;
        private String lastName;
        private String phone;
        private String userType; // "customer" only for registration
        
        // Getters and setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getUserType() { return userType; }
        public void setUserType(String userType) { this.userType = userType; }
    }

    /**
     * User login endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session) {
        try {
            if ("customer".equalsIgnoreCase(request.getUserType())) {
                CustomerEntity customer = userService.getCustomerByUsername(request.getUsername());
                if (customer != null && passwordEncoder.matches(request.getPassword(), customer.getPasswordHash())) {
                    // Set session attributes
                    session.setAttribute("userId", customer.getCustomerId());
                    session.setAttribute("username", customer.getUsername());
                    session.setAttribute("userType", "CUSTOMER");
                    session.setAttribute("fullName", customer.getName());
                    
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Login successful");
                    response.put("userType", "CUSTOMER");
                    response.put("username", customer.getUsername());
                    response.put("fullName", customer.getName());
                    
                    return ResponseEntity.ok(response);
                }
            } else if ("staff".equalsIgnoreCase(request.getUserType())) {
                StaffEntity staff = userService.getStaffByUsername(request.getUsername());
                if (staff != null && passwordEncoder.matches(request.getPassword(), staff.getPasswordHash())) {
                    // Set session attributes
                    session.setAttribute("userId", staff.getStaffId());
                    session.setAttribute("username", staff.getUsername());
                    session.setAttribute("userType", staff.getRole().name().toUpperCase());
                    session.setAttribute("fullName", staff.getName());
                    
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Login successful");
                    response.put("userType", staff.getRole().name().toUpperCase());
                    response.put("username", staff.getUsername());
                    response.put("fullName", staff.getName());
                    response.put("role", staff.getRole());
                    
                    return ResponseEntity.ok(response);
                }
            }
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid username, password, or user type"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Login failed: " + e.getMessage()));
        }
    }

    /**
     * User registration endpoint (customers only)
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // Validate input
            if (request.getUsername() == null || request.getUsername().trim().isEmpty() ||
                request.getPassword() == null || request.getPassword().trim().isEmpty() ||
                request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Username, password, and email are required"));
            }
            
            // Check password strength
            if (request.getPassword().length() < 6) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Password must be at least 6 characters long"));
            }
            
            // Check if username already exists
            if (userService.getCustomerByUsername(request.getUsername()) != null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Username already exists"));
            }
            
            // Create new customer
            CustomerEntity customer = new CustomerEntity();
            customer.setUsername(request.getUsername());
            customer.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            customer.setEmail(request.getEmail());
            customer.setName((request.getFirstName() != null ? request.getFirstName() : "") + 
                           (request.getLastName() != null ? " " + request.getLastName() : ""));
            customer.setPhoneNumber(request.getPhone() != null ? request.getPhone() : "");
            
            CustomerEntity savedCustomer = userService.saveCustomer(customer);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Registration successful");
            response.put("username", savedCustomer.getUsername());
            response.put("customerId", savedCustomer.getCustomerId());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }

    /**
     * User logout endpoint
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        try {
            session.invalidate();
            return ResponseEntity.ok(Map.of("message", "Logout successful"));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("message", "Logout completed"));
        }
    }

    /**
     * Get current user profile
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpSession session) {
        try {
            String username = (String) session.getAttribute("username");
            String userType = (String) session.getAttribute("userType");
            
            if (username == null || userType == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
            }
            
            Map<String, Object> profile = new HashMap<>();
            profile.put("username", username);
            profile.put("userType", userType);
            profile.put("fullName", session.getAttribute("fullName"));
            profile.put("userId", session.getAttribute("userId"));
            
            if ("CUSTOMER".equals(userType)) {
                CustomerEntity customer = userService.getCustomerByUsername(username);
                if (customer != null) {
                    profile.put("email", customer.getEmail());
                    profile.put("phone", customer.getPhoneNumber());
                }
            } else {
                StaffEntity staff = userService.getStaffByUsername(username);
                if (staff != null) {
                    profile.put("role", staff.getRole());
                    profile.put("phone", staff.getPhoneNumber());
                }
            }
            
            return ResponseEntity.ok(profile);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to retrieve profile: " + e.getMessage()));
        }
    }

    /**
     * Check authentication status
     */
    @GetMapping("/status")
    public ResponseEntity<?> getAuthStatus(HttpSession session) {
        String username = (String) session.getAttribute("username");
        String userType = (String) session.getAttribute("userType");
        
        if (username != null && userType != null) {
            Map<String, Object> status = new HashMap<>();
            status.put("authenticated", true);
            status.put("username", username);
            status.put("userType", userType);
            status.put("fullName", session.getAttribute("fullName"));
            return ResponseEntity.ok(status);
        } else {
            return ResponseEntity.ok(Map.of("authenticated", false));
        }
    }
}
