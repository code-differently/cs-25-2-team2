package com.cs_25_2_team2.RestaurantManagementApp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cs_25_2_team2.RestaurantManagementApp.entities.CartEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.CustomerEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.OrderEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.StaffEntity;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.CartRepository;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.CustomerRepository;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.OrderRepository;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.StaffRepository;

/**
 * Service class for User management using Spring Data JPA repositories.
 * Handles customer and staff authentication, registration, and profile management.
 * 
 * Uses Spring Data JPA with PostgreSQL database:
 * - CustomerEntity for customer data persistence
 * - StaffEntity for staff data persistence (Chef, Delivery roles)
 * - Proper database transactions and error handling
 * 
 * @author Team 2
 * @version 2.0
 */
@Service
@Transactional
public class UserService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private StaffRepository staffRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    /**
     * Authenticate user (Customer or Staff) by username and password
     */
    public Object authenticateUser(String username, String password) {
        // First try to find customer
        Optional<CustomerEntity> customer = customerRepository.findByUsername(username);
        if (customer.isPresent()) {
            // In a real app, you'd hash and compare passwords
            // For now, we'll do a simple comparison (NOT SECURE - just for demo)
            if (password.equals("password")) { // Demo password
                return customer.get();
            }
        }
        
        // Then try to find staff
        Optional<StaffEntity> staff = staffRepository.findByUsername(username);
        if (staff.isPresent()) {
            // Same simple password check (NOT SECURE - just for demo)
            if (password.equals("password")) { // Demo password
                return staff.get();
            }
        }
        
        return null; // Authentication failed
    }
    
    /**
     * Register new customer using JPA repository
     */
    public CustomerEntity registerCustomer(String username, String name, String address, String phoneNumber) {
        // Check if username already exists
        if (customerRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
            
        // Create new customer entity
        CustomerEntity customer = new CustomerEntity(username, name, address, phoneNumber);
        customer.setPasswordHash("hashedPassword"); // In real app, hash the password
        
        // Save customer to database
        CustomerEntity savedCustomer = customerRepository.save(customer);
        
        // Create cart for customer
        CartEntity cart = new CartEntity(savedCustomer);
        cartRepository.save(cart);
        
        return savedCustomer;
    }
    
    /**
     * Register new staff member using JPA repository
     */
    public StaffEntity registerStaff(String username, String name, String address, String phoneNumber, String role) {
        // Check if username already exists
        if (staffRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
    

        // Create staff entity with proper role enum
        StaffEntity.StaffRole staffRole = StaffEntity.StaffRole.valueOf(role);
        StaffEntity staff = new StaffEntity(username, name, phoneNumber, staffRole);
        staff.setPasswordHash("hashedPassword"); // In real app, hash the password
        
        // Save staff to database
        return staffRepository.save(staff);
    }
    
    /**
     * Get user by username (Customer or Staff)
     */
    public Object getUserByUsername(String username) {
        // Try customer first
        Optional<CustomerEntity> customer = customerRepository.findByUsername(username);
        if (customer.isPresent()) {
            return customer.get();
        }
        
        // Try staff
        Optional<StaffEntity> staff = staffRepository.findByUsername(username);
        return staff.orElse(null);
    }
    
    /**
     * Get customer by ID - returns CustomerEntity directly (modernized)
     */
    public CustomerEntity getCustomerById(Long customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }
    
    /**
     * Get staff by ID - returns StaffEntity directly (modernized)
     */
    public StaffEntity getStaffById(Long staffId) {
        return staffRepository.findById(staffId).orElse(null);
    }
    
    /**
     * Update customer profile - returns CustomerEntity directly (modernized)
     */
    public CustomerEntity updateCustomerProfile(Long customerId, String name, String address, String phoneNumber) {
        Optional<CustomerEntity> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isPresent()) {
            CustomerEntity customer = customerOpt.get();
            customer.setName(name);
            customer.setAddress(address);
            customer.setPhoneNumber(phoneNumber);
            return customerRepository.save(customer);
        }
        return null;
    }
    
    /**
     * Customer checkout - returns OrderEntity directly (modernized)
     */
    @Transactional
    public OrderEntity customerCheckout(Long customerId) {
        // Get customer with cart
        Optional<CustomerEntity> customerOpt = customerRepository.findByIdWithCart(customerId);
        if (customerOpt.isEmpty()) {
            throw new RuntimeException("Customer not found");
        }
        
        CustomerEntity customer = customerOpt.get();
        CartEntity cart = customer.getCart();
        
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        
        OrderEntity order = new OrderEntity(customer, new java.math.BigDecimal("19.99"));
        
        // Set dummy payment info (in real app, process payment)
        order.setCreditCardLastFour("1234");
        order.setCreditCardToken("token_" + System.currentTimeMillis());
        order.setCardExpiryMonth(12);
        order.setCardExpiryYear(2025);
        order.setCardholderName(customer.getName());
        
        // Save and return order
        OrderEntity savedOrder = orderRepository.save(order);
        
        // Clear cart (delete cart items)
        cart.getCartItems().clear();
        cartRepository.save(cart);
        
        return savedOrder;
    }
    
    /**
     * Get customer's cart - returns CartEntity directly (modernized)
     */
    public CartEntity getCustomerCart(Long customerId) {
        CustomerEntity customer = customerRepository.findById(customerId).orElse(null);
        if (customer != null) {
            return customer.getCart();
        }
        return null;
    }
    
    /**
     * Get all customers using JPA repository
     */
    public List<CustomerEntity> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    /**
     * Get all staff using JPA repository
     */
    public List<StaffEntity> getAllStaff() {
        return staffRepository.findAll();
    }
    
    /**
     * Get staff by role using JPA repository
     */
    public List<StaffEntity> getStaffByRole(String role) {
        StaffEntity.StaffRole staffRole = StaffEntity.StaffRole.valueOf(role);
        return staffRepository.findByRole(staffRole);
    }
    
    /**
     * Get CustomerEntity by username
     */
    public CustomerEntity getCustomerByUsername(String username) {
        Optional<CustomerEntity> customer = customerRepository.findByUsername(username);
        return customer.orElse(null);
    }

    /**
     * Get StaffEntity by username
     */
    public StaffEntity getStaffByUsername(String username) {
        Optional<StaffEntity> staff = staffRepository.findByUsername(username);
        return staff.orElse(null);
    }

    /**
     * Save/update CustomerEntity
     */
    public CustomerEntity saveCustomer(CustomerEntity customer) {
        return customerRepository.save(customer);
    }

    /**
     * Save/update StaffEntity
     */
    public StaffEntity saveStaff(StaffEntity staff) {
        return staffRepository.save(staff);
    }

    // ===== ENTITY-BASED OPERATIONS COMPLETE =====
    // All methods now work directly with JPA entities for consistency with controllers
}