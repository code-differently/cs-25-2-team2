package com.cs_25_2_team2.RestaurantManagementApp.services;

import com.cs_25_2_team2.RestaurantManagementApp.entities.CustomerEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.StaffEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.OrderEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.CartEntity;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.CustomerRepository;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.StaffRepository;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.OrderRepository;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.CartRepository;
// Import domain classes for API layer
import com.cs_25_2_team2.RestaurantManagementApp.Customer;
import com.cs_25_2_team2.RestaurantManagementApp.Staff;
import com.cs_25_2_team2.RestaurantManagementApp.Chef;
import com.cs_25_2_team2.RestaurantManagementApp.Order;
import com.cs_25_2_team2.RestaurantManagementApp.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

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
     * Get customer by ID - accepts int parameter and returns Customer domain object
     */
    public Customer getCustomerById(int customerId) {
        String entityId = convertToEntityCustomerId(customerId);
        CustomerEntity entity = customerRepository.findById(entityId).orElse(null);
        return convertToCustomer(entity);
    }
    
    /**
     * Get staff by ID - returns Staff domain object
     */
    public Staff getStaffById(String staffId) {
        StaffEntity entity = staffRepository.findById(staffId).orElse(null);
        return convertToStaff(entity);
    }
    
    /**
     * Update customer profile - accepts int customerId and returns Customer domain object
     */
    public Customer updateCustomerProfile(int customerId, String name, String address, String phoneNumber) {
        String entityId = convertToEntityCustomerId(customerId);
        Optional<CustomerEntity> customerOpt = customerRepository.findById(entityId);
        if (customerOpt.isPresent()) {
            CustomerEntity customer = customerOpt.get();
            customer.setName(name);
            customer.setAddress(address);
            customer.setPhoneNumber(phoneNumber);
            CustomerEntity savedEntity = customerRepository.save(customer);
            return convertToCustomer(savedEntity);
        }
        return null;
    }
    
    /**
     * Customer checkout - accepts int customerId and returns Order domain object
     */
    @Transactional
    public Order customerCheckout(int customerId) {
        String entityId = convertToEntityCustomerId(customerId);
        // Get customer with cart
        Optional<CustomerEntity> customerOpt = customerRepository.findByIdWithCart(entityId);
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
        
        // Save order
        OrderEntity savedOrder = orderRepository.save(order);
        
        // Clear cart (delete cart items)
        cart.getCartItems().clear();
        cartRepository.save(cart);
        
        // Convert to domain object (simplified - would need proper Order conversion)
        Customer domainCustomer = convertToCustomer(customer);
        return domainCustomer.checkout(); // Use existing domain logic
    }
    
    /**
     * Get customer's cart - accepts int customerId and returns Cart domain object
     */
    public Cart getCustomerCart(int customerId) {
        String entityId = convertToEntityCustomerId(customerId);
        CustomerEntity customer = customerRepository.findById(entityId).orElse(null);
        return convertToCart(customer);
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
    
    // ===== CONVERTER METHODS BETWEEN ENTITIES AND DOMAIN OBJECTS =====
    
    /**
     * Convert CustomerEntity to Customer domain object
     */
    private Customer convertToCustomer(CustomerEntity entity) {
        if (entity == null) return null;

        Customer customer = new Customer(entity.getCustomerId(), entity.getName(), entity.getAddress(), entity.getPhoneNumber());

        // Note: Customer domain class doesn't have email field, so we skip it
        
        return customer;
    }
    
    /**
     * Convert StaffEntity to Staff domain object (Chef subclass)
     */
    private Staff convertToStaff(StaffEntity entity) {
        if (entity == null) return null;
        
        // For now, create Chef objects since that's what the controllers expect
        // StaffEntity doesn't have address field, so we'll use empty string
        return new Chef(entity.getName(), "", entity.getPhoneNumber(), entity.getStaffId());
    }
    
    /**
     * Convert int customerId to String format for entities
     */
    private String convertToEntityCustomerId(int customerId) {
        return String.format("CUST%06d", customerId);
    }
    
    /**
     * Convert String customerId from entity to int for domain
     */
    private Long convertToDomainCustomerId(Long entityCustomerId) {
        return entityCustomerId;
    }
    
    /**
     * Create a Cart domain object from customer
     */
    private Cart convertToCart(CustomerEntity customerEntity) {
        if (customerEntity == null) return null;
        
        Long customerId = convertToDomainCustomerId(customerEntity.getCustomerId());
        Cart cart = new Cart(customerId);
        
        // In a full implementation, you'd load cart items from CartEntity and CartItemEntity
        // For now, return empty cart
        
        return cart;
    }
}