package com.cs_25_2_team2.RestaurantManagementApp.controllers;

import com.cs_25_2_team2.RestaurantManagementApp.entities.CustomerEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.StaffEntity;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.CustomerRepository;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.StaffRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Proxy;
import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private UserController userController;
    private CustomerRepository mockCustomerRepository;
    private StaffRepository mockStaffRepository;
    
    // In-memory storage for testing
    private final Map<Long, CustomerEntity> customerStorage = new HashMap<>();
    private final Map<Long, StaffEntity> staffStorage = new HashMap<>();
    private long nextCustomerId = 1L;
    private long nextStaffId = 1L;

    @BeforeEach
    public void setUp() throws Exception {
        // Clear storage before each test
        customerStorage.clear();
        staffStorage.clear();
        nextCustomerId = 1L;
        nextStaffId = 1L;
        
        setupMockRepositories();
        userController = new UserController();
        
        // Inject mock repositories using reflection (since no constructor injection)
        injectMockRepositories();
    }
    
    private void injectMockRepositories() throws Exception {
        // Use reflection to inject mock repositories
        Field customerRepoField = UserController.class.getDeclaredField("customerRepository");
        customerRepoField.setAccessible(true);
        customerRepoField.set(userController, mockCustomerRepository);
        
        Field staffRepoField = UserController.class.getDeclaredField("staffRepository");
        staffRepoField.setAccessible(true);
        staffRepoField.set(userController, mockStaffRepository);
    }

    private void setupMockRepositories() {
        // Create dynamic proxy for CustomerRepository
        mockCustomerRepository = (CustomerRepository) Proxy.newProxyInstance(
                CustomerRepository.class.getClassLoader(),
                new Class[]{CustomerRepository.class},
                (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "save":
                            CustomerEntity customerToSave = (CustomerEntity) args[0];
                            if (customerToSave.getCustomerId() == null) {
                                customerToSave.setCustomerId(nextCustomerId++);
                            }
                            customerStorage.put(customerToSave.getCustomerId(), customerToSave);
                            return customerToSave;
                            
                        case "findById":
                            Long customerId = (Long) args[0];
                            return Optional.ofNullable(customerStorage.get(customerId));
                            
                        case "findAll":
                            return new ArrayList<>(customerStorage.values());
                            
                        case "deleteById":
                            Long customerIdToDelete = (Long) args[0];
                            customerStorage.remove(customerIdToDelete);
                            return null;
                            
                        case "existsById":
                            Long customerIdToCheck = (Long) args[0];
                            return customerStorage.containsKey(customerIdToCheck);
                            
                        case "existsByUsername":
                            String usernameCheck = (String) args[0];
                            return customerStorage.values().stream()
                                    .anyMatch(c -> c.getUsername().equals(usernameCheck));
                            
                        case "findByUsername":
                            String username = (String) args[0];
                            return customerStorage.values().stream()
                                    .filter(c -> c.getUsername().equals(username))
                                    .findFirst();
                                    
                        case "count":
                            return (long) customerStorage.size();
                            
                        default:
                            throw new UnsupportedOperationException("Mock method not implemented: " + method.getName());
                    }
                }
        );

        // Create dynamic proxy for StaffRepository
        mockStaffRepository = (StaffRepository) Proxy.newProxyInstance(
                StaffRepository.class.getClassLoader(),
                new Class[]{StaffRepository.class},
                (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "save":
                            StaffEntity staffToSave = (StaffEntity) args[0];
                            if (staffToSave.getStaffId() == null) {
                                staffToSave.setStaffId(nextStaffId++);
                            }
                            staffStorage.put(staffToSave.getStaffId(), staffToSave);
                            return staffToSave;
                            
                        case "findById":
                            Long staffId = (Long) args[0];
                            return Optional.ofNullable(staffStorage.get(staffId));
                            
                        case "findAll":
                            return new ArrayList<>(staffStorage.values());
                            
                        case "deleteById":
                            Long staffIdToDelete = (Long) args[0];
                            staffStorage.remove(staffIdToDelete);
                            return null;
                            
                        case "existsById":
                            Long staffIdToCheck = (Long) args[0];
                            return staffStorage.containsKey(staffIdToCheck);
                            
                        case "existsByUsername":
                            String usernameCheckStaff = (String) args[0];
                            return staffStorage.values().stream()
                                    .anyMatch(s -> s.getUsername().equals(usernameCheckStaff));
                            
                        case "findByUsername":
                            String username = (String) args[0];
                            return staffStorage.values().stream()
                                    .filter(s -> s.getUsername().equals(username))
                                    .findFirst();
                                    
                        case "count":
                            return (long) staffStorage.size();
                            
                        default:
                            throw new UnsupportedOperationException("Mock method not implemented: " + method.getName());
                    }
                }
        );
    }

    // ============ Authentication Tests ============
    
    @Test
    public void testLoginCustomerSuccess() {
        // Setup: Create a customer
        CustomerEntity customer = new CustomerEntity();
        customer.setCustomerId(nextCustomerId++);
        customer.setUsername("testuser");
        customer.setPasswordHash("testpass");
        customer.setName("Test User");
        customer.setEmail("test@example.com");
        customer.setPhoneNumber("123-456-7890");
        customerStorage.put(customer.getCustomerId(), customer);
        
        // Test login - Note: UserController.login expects Map<String,Object>
        Map<String, Object> loginRequest = Map.of(
                "username", "testuser",
                "password", "testpass"
        );
        
        ResponseEntity<Map<String, Object>> response = userController.login(loginRequest);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertNotNull(responseBody.get("role"));
    }

    @Test
    public void testLoginStaffSuccess() {
        // Setup: Create a staff member
        StaffEntity staff = new StaffEntity();
        staff.setStaffId(nextStaffId++);
        staff.setUsername("staffuser");
        staff.setPasswordHash("staffpass");
        staff.setName("Staff User");
        staff.setRole(StaffEntity.StaffRole.Chef);
        staffStorage.put(staff.getStaffId(), staff);
        
        // Test login
        Map<String, Object> loginRequest = Map.of(
                "username", "staffuser",
                "password", "staffpass"
        );
        
        ResponseEntity<Map<String, Object>> response = userController.login(loginRequest);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertNotNull(responseBody.get("role"));
    }

    @Test
    public void testLoginInvalidCredentials() {
        Map<String, Object> loginRequest = Map.of(
                "username", "nonexistent",
                "password", "wrongpass"
        );
        
        ResponseEntity<Map<String, Object>> response = userController.login(loginRequest);
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    // ============ Customer CRUD Tests ============
    
    @Test
    public void testRegisterCustomerSuccess() {
        Map<String, Object> registrationData = Map.of(
                "username", "newcustomer",
                "password", "newpass",
                "name", "New Customer",
                "email", "new@example.com",
                "phoneNumber", "987-654-3210"
        );
        
        ResponseEntity<CustomerEntity> response = userController.registerCustomer(registrationData);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        CustomerEntity responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("newcustomer", responseBody.getUsername());
        
        // Verify customer was saved
        assertEquals(1, customerStorage.size());
    }

    @Test
    public void testGetAllCustomers() {
        // Setup: Create multiple customers  
        createTestCustomer("user1", "User One");
        createTestCustomer("user2", "User Two");
        
        List<CustomerEntity> customers = userController.getAllCustomers();
        
        assertEquals(2, customers.size());
    }

    @Test
    public void testGetCustomerByIdSuccess() {
        CustomerEntity customer = createTestCustomer("testuser", "Test User");
        
        ResponseEntity<CustomerEntity> response = userController.getCustomerById(customer.getCustomerId());
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        CustomerEntity responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("testuser", responseBody.getUsername());
    }

    @Test
    public void testGetCustomerByIdNotFound() {
        ResponseEntity<CustomerEntity> response = userController.getCustomerById(999L);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateCustomerSuccess() {
        CustomerEntity customer = createTestCustomer("testuser", "Test User");
        
        Map<String, Object> updateData = Map.of(
                "name", "Updated Name",
                "email", "updated@example.com"
        );
        
        ResponseEntity<CustomerEntity> response = userController.updateCustomer(customer.getCustomerId(), updateData);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        CustomerEntity responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Updated Name", responseBody.getName());
        assertEquals("updated@example.com", responseBody.getEmail());
    }

    @Test
    public void testDeleteCustomerSuccess() {
        CustomerEntity customer = createTestCustomer("testuser", "Test User");
        
        ResponseEntity<Void> response = userController.deleteCustomer(customer.getCustomerId());
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        
        // Verify deletion
        assertFalse(customerStorage.containsKey(customer.getCustomerId()));
    }

    // ============ Staff CRUD Tests ============
    
    @Test
    public void testGetAllStaff() {
        // Setup: Create multiple staff members
        createTestStaff("staff1", "Staff One", StaffEntity.StaffRole.Chef);
        createTestStaff("staff2", "Staff Two", StaffEntity.StaffRole.Delivery);
        
        List<StaffEntity> staff = userController.getAllStaff();
        
        assertEquals(2, staff.size());
    }

    @Test
    public void testGetStaffByIdSuccess() {
        StaffEntity staff = createTestStaff("staffuser", "Staff User", StaffEntity.StaffRole.Chef);
        
        ResponseEntity<StaffEntity> response = userController.getStaffById(staff.getStaffId());
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        StaffEntity responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("staffuser", responseBody.getUsername());
    }

    @Test
    public void testUpdateStaffSuccess() {
        StaffEntity staff = createTestStaff("staffuser", "Staff User", StaffEntity.StaffRole.Chef);
        
        Map<String, Object> updateData = Map.of(
                "name", "Updated Staff Name",
                "phoneNumber", "987-654-3210"
        );
        
        ResponseEntity<StaffEntity> response = userController.updateStaff(staff.getStaffId(), updateData);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        StaffEntity responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Updated Staff Name", responseBody.getName());
        assertEquals("987-654-3210", responseBody.getPhoneNumber());
    }

    @Test
    public void testDeleteStaffSuccess() {
        StaffEntity staff = createTestStaff("staffuser", "Staff User", StaffEntity.StaffRole.Chef);
        
        ResponseEntity<Void> response = userController.deleteStaff(staff.getStaffId());
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        
        // Verify deletion
        assertFalse(staffStorage.containsKey(staff.getStaffId()));
    }

    // ============ Search Tests ============
    
    @Test
    public void testFindByUsernameCustomerFound() {
        createTestCustomer("searchuser", "Search User");
        
        ResponseEntity<Map<String, Object>> response = userController.findByUsername("searchuser");
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("customer", responseBody.get("userType"));
    }

    @Test
    public void testFindByUsernameStaffFound() {
        createTestStaff("searchstaff", "Search Staff", StaffEntity.StaffRole.Chef);
        
        ResponseEntity<Map<String, Object>> response = userController.findByUsername("searchstaff");
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("staff", responseBody.get("userType"));
    }

    @Test
    public void testFindByUsernameNotFound() {
        ResponseEntity<Map<String, Object>> response = userController.findByUsername("nonexistent");
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // ============ Helper Methods ============
    
    private CustomerEntity createTestCustomer(String username, String name) {
        CustomerEntity customer = new CustomerEntity();
        customer.setCustomerId(nextCustomerId++);
        customer.setUsername(username);
        customer.setPasswordHash("testpass");
        customer.setName(name);
        customer.setEmail(username + "@example.com");
        customer.setPhoneNumber("123-456-7890");
        customerStorage.put(customer.getCustomerId(), customer);
        return customer;
    }
    
    private StaffEntity createTestStaff(String username, String name, StaffEntity.StaffRole role) {
        StaffEntity staff = new StaffEntity();
        staff.setStaffId(nextStaffId++);
        staff.setUsername(username);
        staff.setPasswordHash("testpass");
        staff.setName(name);
        staff.setRole(role);
        staffStorage.put(staff.getStaffId(), staff);
        return staff;
    }
}
