package com.cs_25_2_team2.RestaurantManagementApp.services;

import com.cs_25_2_team2.RestaurantManagementApp.Customer;
import com.cs_25_2_team2.RestaurantManagementApp.Staff;
import com.cs_25_2_team2.RestaurantManagementApp.Chef;
import com.cs_25_2_team2.RestaurantManagementApp.Order;
import com.cs_25_2_team2.RestaurantManagementApp.Cart;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service class for User management using the existing Customer and Staff backend classes.
 * Handles user authentication, profile management, and user-specific operations.
 * 
 * Uses the existing backend architecture:
 * - Customer class for customer operations
 * - Staff class hierarchy (Chef, etc.)
 * - Cart class for shopping cart management
 * - Order class for order management
 * 
 * @author Team 2
 * @version 1.0
 */
@Service
public class UserService {
    
    private final Map<Integer, Customer> customers = new ConcurrentHashMap<>();
    private final Map<String, Staff> staffMembers = new ConcurrentHashMap<>();
    private final Map<String, Object> userCredentials = new ConcurrentHashMap<>(); // username -> user object
    private int nextCustomerId = 1;
    private int nextStaffId = 1;
    
    /**
     * Constructor initializes with sample users
     */
    public UserService() {
        initializeSampleUsers();
    }
    
    /**
     * Authenticate user login - Frontend: username, role compatibility
     */
    public Object authenticateUser(String username, String password) {
        Object user = userCredentials.get(username);
        if (user != null) {
            // In a real system, you'd verify the password here
            // For now, we'll use a simple password check
            if (password.equals("password")) { // Simple demo password
                return user;
            }
        }
        return null;
    }
    
    /**
     * Register new customer - maps to Customer class
     */
    public Customer registerCustomer(String username, String name, String address, String phoneNumber) {
        Customer customer = new Customer(nextCustomerId++, name, address, phoneNumber);
        customers.put(customer.getCustomerId(), customer);
        userCredentials.put(username, customer);
        return customer;
    }
    
    /**
     * Register new staff member - maps to Staff hierarchy
     */
    public Staff registerStaff(String username, String name, String address, String phoneNumber, String role) {
        Staff staff;
        String staffId = "STAFF" + nextStaffId++;
        
        // Create appropriate staff type based on role
        switch (role.toUpperCase()) {
            case "CHEF":
                staff = new Chef(name, address, phoneNumber, staffId);
                break;
            case "ADMIN":
            default:
                // For admin and other roles, create a basic Staff implementation
                staff = new Staff(name, address, phoneNumber, staffId, role) {
                    @Override
                    public void assignOrder(Order order) {
                        // Admin doesn't typically get assigned orders
                        getAssignedOrders().add(order);
                    }
                    
                    @Override
                    public boolean isBusy() {
                        return false; // Admin is typically not "busy" in kitchen sense
                    }
                };
                break;
        }
        
        staffMembers.put(staff.getId(), staff);
        userCredentials.put(username, staff);
        return staff;
    }
    
    /**
     * Get customer by ID
     */
    public Customer getCustomerById(int customerId) {
        return customers.get(customerId);
    }
    
    /**
     * Get staff by ID
     */
    public Staff getStaffById(String staffId) {
        return staffMembers.get(staffId);
    }
    
    /**
     * Get user by username (could be Customer or Staff)
     */
    public Object getUserByUsername(String username) {
        return userCredentials.get(username);
    }
    
    /**
     * Update customer profile
     */
    public Customer updateCustomerProfile(int customerId, String name, String address, String phoneNumber) {
        Customer customer = customers.get(customerId);
        if (customer != null) {
            // Since Customer fields are set in Person constructor, we'd need to create a new customer
            // or add setter methods to the Person class
            // For now, return the existing customer
            System.out.println("Profile update requested for customer: " + customerId);
            // In a real implementation, you'd update the customer object
        }
        return customer;
    }
    
    /**
     * Customer checkout - uses the existing Customer.checkout() method
     */
    public Order customerCheckout(int customerId) {
        Customer customer = customers.get(customerId);
        if (customer != null) {
            try {
                return customer.checkout(); // Uses existing backend method
            } catch (IllegalStateException e) {
                throw new RuntimeException("Checkout failed: " + e.getMessage());
            }
        }
        throw new RuntimeException("Customer not found");
    }
    
    /**
     * Get customer's cart
     */
    public Cart getCustomerCart(int customerId) {
        Customer customer = customers.get(customerId);
        return customer != null ? customer.getCart() : null;
    }
    
    /**
     * Get all customers (Admin function)
     */
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }
    
    /**
     * Get all staff members (Admin function)
     */
    public List<Staff> getAllStaff() {
        return new ArrayList<>(staffMembers.values());
    }
    
    /**
     * Get customers by role filter (for frontend compatibility)
     */
    public List<Customer> getCustomers() {
        return getAllCustomers();
    }
    
    /**
     * Get staff by role filter
     */
    public List<Staff> getStaffByRole(String role) {
        return staffMembers.values().stream()
            .filter(staff -> staff.getRole().equalsIgnoreCase(role))
            .toList();
    }
    
    /**
     * Initialize sample users matching frontend authService expectations
     */
    private void initializeSampleUsers() {
        // Sample customer
        Customer customer = new Customer(1, "John Doe", "123 Main St", "123-456-7890");
        customers.put(customer.getCustomerId(), customer);
        userCredentials.put("customer", customer);
        
        // Sample chef
        Chef chef = new Chef("Gordon Ramsay", "456 Kitchen Ave", "987-654-3210", "CHEF001");
        staffMembers.put(chef.getId(), chef);
        userCredentials.put("chef", chef);
        
        // Sample admin
        Staff admin = new Staff("Admin User", "789 Office Blvd", "555-123-4567", "ADMIN001", "ADMIN") {
            @Override
            public void assignOrder(Order order) {
                getAssignedOrders().add(order);
            }
            
            @Override
            public boolean isBusy() {
                return false;
            }
        };
        staffMembers.put(admin.getId(), admin);
        userCredentials.put("admin", admin);
    }
    
    /**
     * Helper method to determine user role for frontend compatibility
     */
    public String getUserRole(Object user) {
        if (user instanceof Customer) {
            return "CUSTOMER";
        } else if (user instanceof Chef) {
            return "CHEF";
        } else if (user instanceof Staff) {
            return ((Staff) user).getRole().toUpperCase();
        }
        return "UNKNOWN";
    }
    
    /**
     * Helper method to get user ID for frontend compatibility
     */
    public String getUserId(Object user) {
        if (user instanceof Customer) {
            return String.valueOf(((Customer) user).getCustomerId());
        } else if (user instanceof Staff) {
            return ((Staff) user).getId();
        }
        return null;
    }
    
    /**
     * Helper method to get user name for frontend compatibility
     */
    public String getUserName(Object user) {
        if (user instanceof Customer) {
            return ((Customer) user).getName();
        } else if (user instanceof Staff) {
            return ((Staff) user).getName();
        }
        return null;
    }
}