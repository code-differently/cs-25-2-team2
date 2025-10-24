package com.cs_25_2_team2.RestaurantManagementApp;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Proxy;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cs_25_2_team2.RestaurantManagementApp.controller.UserController;
import com.cs_25_2_team2.RestaurantManagementApp.entity.CustomerEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entity.StaffEntity;
import com.cs_25_2_team2.RestaurantManagementApp.repository.CustomerRepository;
import com.cs_25_2_team2.RestaurantManagementApp.repository.StaffRepository;

public class UserControllerTest {

  private UserController userController;
  private CustomerRepository mockCustomerRepository;
  private StaffRepository mockStaffRepository;

  // In-memory storage for testing
  private Map<Long, CustomerEntity> customerStorage = new HashMap<>();
  private Map<Long, StaffEntity> staffStorage = new HashMap<>();
  private long nextCustomerId = 1L;
  private long nextStaffId = 1L;

  @BeforeEach
  public void setUp() {
    // Clear storage before each test
    customerStorage.clear();
    staffStorage.clear();
    nextCustomerId = 1L;
    nextStaffId = 1L;

    setupMockRepositories();
    userController = new UserController(mockCustomerRepository, mockStaffRepository);
  }

  private void setupMockRepositories() {
    // Create dynamic proxy for CustomerRepository
    mockCustomerRepository =
        (CustomerRepository)
            Proxy.newProxyInstance(
                CustomerRepository.class.getClassLoader(),
                new Class[] {CustomerRepository.class},
                (proxy, method, args) -> {
                  switch (method.getName()) {
                    case "save":
                      CustomerEntity customerToSave = (CustomerEntity) args[0];
                      if (customerToSave.getId() == null) {
                        customerToSave.setId(nextCustomerId++);
                      }
                      customerStorage.put(customerToSave.getId(), customerToSave);
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

                    case "findByUsername":
                      String username = (String) args[0];
                      return customerStorage.values().stream()
                          .filter(c -> c.getUsername().equals(username))
                          .findFirst();

                    default:
                      throw new UnsupportedOperationException(
                          "Mock method not implemented: " + method.getName());
                  }
                });

    // Create dynamic proxy for StaffRepository
    mockStaffRepository =
        (StaffRepository)
            Proxy.newProxyInstance(
                StaffRepository.class.getClassLoader(),
                new Class[] {StaffRepository.class},
                (proxy, method, args) -> {
                  switch (method.getName()) {
                    case "save":
                      StaffEntity staffToSave = (StaffEntity) args[0];
                      if (staffToSave.getId() == null) {
                        staffToSave.setId(nextStaffId++);
                      }
                      staffStorage.put(staffToSave.getId(), staffToSave);
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

                    case "findByUsername":
                      String username = (String) args[0];
                      return staffStorage.values().stream()
                          .filter(s -> s.getUsername().equals(username))
                          .findFirst();

                    default:
                      throw new UnsupportedOperationException(
                          "Mock method not implemented: " + method.getName());
                  }
                });
  }

  // ============ Authentication Tests ============

  @Test
  public void testLoginCustomerSuccess() {
    // Setup: Create a customer
    CustomerEntity customer = new CustomerEntity();
    customer.setUsername("testuser");
    customer.setPassword("testpass");
    customer.setName("Test User");
    customer.setEmail("test@example.com");
    customer.setPhoneNumber("123-456-7890");
    customerStorage.put(nextCustomerId, customer);
    customer.setId(nextCustomerId++);

    // Test login
    Map<String, Object> loginRequest =
        Map.of(
            "username", "testuser",
            "password", "testpass");

    ResponseEntity<Map<String, Object>> response = userController.login(loginRequest);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    Map<String, Object> responseBody = response.getBody();
    assertEquals("success", responseBody.get("status"));
    assertEquals("customer", responseBody.get("userType"));
    assertNotNull(responseBody.get("user"));
  }

  @Test
  public void testLoginStaffSuccess() {
    // Setup: Create a staff member
    StaffEntity staff = new StaffEntity();
    staff.setUsername("staffuser");
    staff.setPassword("staffpass");
    staff.setName("Staff User");
    staff.setRole("CHEF");
    staffStorage.put(nextStaffId, staff);
    staff.setId(nextStaffId++);

    // Test login
    Map<String, Object> loginRequest =
        Map.of(
            "username", "staffuser",
            "password", "staffpass");

    ResponseEntity<Map<String, Object>> response = userController.login(loginRequest);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    Map<String, Object> responseBody = response.getBody();
    assertEquals("success", responseBody.get("status"));
    assertEquals("staff", responseBody.get("userType"));
    assertNotNull(responseBody.get("user"));
  }

  @Test
  public void testLoginInvalidCredentials() {
    Map<String, Object> loginRequest =
        Map.of(
            "username", "nonexistent",
            "password", "wrongpass");

    ResponseEntity<Map<String, Object>> response = userController.login(loginRequest);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    Map<String, Object> responseBody = response.getBody();
    assertEquals("error", responseBody.get("status"));
    assertEquals("Invalid username or password", responseBody.get("message"));
  }

  // ============ Customer CRUD Tests ============

  @Test
  public void testRegisterCustomerSuccess() {
    Map<String, Object> registrationData =
        Map.of(
            "username", "newcustomer",
            "password", "newpass",
            "name", "New Customer",
            "email", "new@example.com",
            "phoneNumber", "987-654-3210");

    ResponseEntity<Map<String, Object>> response =
        userController.registerCustomer(registrationData);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    Map<String, Object> responseBody = response.getBody();
    assertEquals("success", responseBody.get("status"));
    assertEquals("Customer registered successfully", responseBody.get("message"));
    assertNotNull(responseBody.get("customer"));

    // Verify customer was saved
    assertEquals(1, customerStorage.size());
  }

  @Test
  public void testGetAllCustomers() {
    // Setup: Create multiple customers
    CustomerEntity customer1 = createTestCustomer("user1", "User One");
    CustomerEntity customer2 = createTestCustomer("user2", "User Two");

    ResponseEntity<Map<String, Object>> response = userController.getAllCustomers();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    Map<String, Object> responseBody = response.getBody();
    assertEquals("success", responseBody.get("status"));

    @SuppressWarnings("unchecked")
    List<Map<String, Object>> customers = (List<Map<String, Object>>) responseBody.get("customers");
    assertEquals(2, customers.size());
  }

  @Test
  public void testGetCustomerByIdSuccess() {
    CustomerEntity customer = createTestCustomer("testuser", "Test User");

    ResponseEntity<Map<String, Object>> response = userController.getCustomerById(customer.getId());

    assertEquals(HttpStatus.OK, response.getStatusCode());
    Map<String, Object> responseBody = response.getBody();
    assertEquals("success", responseBody.get("status"));
    assertNotNull(responseBody.get("customer"));
  }

  @Test
  public void testGetCustomerByIdNotFound() {
    ResponseEntity<Map<String, Object>> response = userController.getCustomerById(999L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    Map<String, Object> responseBody = response.getBody();
    assertEquals("error", responseBody.get("status"));
    assertEquals("Customer not found", responseBody.get("message"));
  }

  @Test
  public void testUpdateCustomerSuccess() {
    CustomerEntity customer = createTestCustomer("testuser", "Test User");

    Map<String, Object> updateData =
        Map.of(
            "name", "Updated Name",
            "email", "updated@example.com");

    ResponseEntity<Map<String, Object>> response =
        userController.updateCustomer(customer.getId(), updateData);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    Map<String, Object> responseBody = response.getBody();
    assertEquals("success", responseBody.get("status"));
    assertEquals("Customer updated successfully", responseBody.get("message"));

    // Verify update
    CustomerEntity updatedCustomer = customerStorage.get(customer.getId());
    assertEquals("Updated Name", updatedCustomer.getName());
    assertEquals("updated@example.com", updatedCustomer.getEmail());
  }

  @Test
  public void testDeleteCustomerSuccess() {
    CustomerEntity customer = createTestCustomer("testuser", "Test User");

    ResponseEntity<Map<String, Object>> response = userController.deleteCustomer(customer.getId());

    assertEquals(HttpStatus.OK, response.getStatusCode());
    Map<String, Object> responseBody = response.getBody();
    assertEquals("success", responseBody.get("status"));
    assertEquals("Customer deleted successfully", responseBody.get("message"));

    // Verify deletion
    assertFalse(customerStorage.containsKey(customer.getId()));
  }

  // ============ Staff CRUD Tests ============

  @Test
  public void testCreateStaffSuccess() {
    Map<String, Object> staffData =
        Map.of(
            "username", "newstaff",
            "password", "staffpass",
            "name", "New Staff",
            "role", "WAITER");

    ResponseEntity<Map<String, Object>> response = userController.createStaff(staffData);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    Map<String, Object> responseBody = response.getBody();
    assertEquals("success", responseBody.get("status"));
    assertEquals("Staff member created successfully", responseBody.get("message"));
    assertNotNull(responseBody.get("staff"));

    // Verify staff was saved
    assertEquals(1, staffStorage.size());
  }

  @Test
  public void testGetAllStaff() {
    // Setup: Create multiple staff members
    StaffEntity staff1 = createTestStaff("staff1", "Staff One", "CHEF");
    StaffEntity staff2 = createTestStaff("staff2", "Staff Two", "WAITER");

    ResponseEntity<Map<String, Object>> response = userController.getAllStaff();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    Map<String, Object> responseBody = response.getBody();
    assertEquals("success", responseBody.get("status"));

    @SuppressWarnings("unchecked")
    List<Map<String, Object>> staff = (List<Map<String, Object>>) responseBody.get("staff");
    assertEquals(2, staff.size());
  }

  @Test
  public void testGetStaffByIdSuccess() {
    StaffEntity staff = createTestStaff("staffuser", "Staff User", "CHEF");

    ResponseEntity<Map<String, Object>> response = userController.getStaffById(staff.getId());

    assertEquals(HttpStatus.OK, response.getStatusCode());
    Map<String, Object> responseBody = response.getBody();
    assertEquals("success", responseBody.get("status"));
    assertNotNull(responseBody.get("staff"));
  }

  @Test
  public void testUpdateStaffSuccess() {
    StaffEntity staff = createTestStaff("staffuser", "Staff User", "CHEF");

    Map<String, Object> updateData =
        Map.of(
            "name", "Updated Staff Name",
            "role", "MANAGER");

    ResponseEntity<Map<String, Object>> response =
        userController.updateStaff(staff.getId(), updateData);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    Map<String, Object> responseBody = response.getBody();
    assertEquals("success", responseBody.get("status"));
    assertEquals("Staff member updated successfully", responseBody.get("message"));

    // Verify update
    StaffEntity updatedStaff = staffStorage.get(staff.getId());
    assertEquals("Updated Staff Name", updatedStaff.getName());
    assertEquals("MANAGER", updatedStaff.getRole());
  }

  @Test
  public void testDeleteStaffSuccess() {
    StaffEntity staff = createTestStaff("staffuser", "Staff User", "CHEF");

    ResponseEntity<Map<String, Object>> response = userController.deleteStaff(staff.getId());

    assertEquals(HttpStatus.OK, response.getStatusCode());
    Map<String, Object> responseBody = response.getBody();
    assertEquals("success", responseBody.get("status"));
    assertEquals("Staff member deleted successfully", responseBody.get("message"));

    // Verify deletion
    assertFalse(staffStorage.containsKey(staff.getId()));
  }

  // ============ Search Tests ============

  @Test
  public void testSearchByUsernameCustomerFound() {
    CustomerEntity customer = createTestCustomer("searchuser", "Search User");

    ResponseEntity<Map<String, Object>> response = userController.searchByUsername("searchuser");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    Map<String, Object> responseBody = response.getBody();
    assertEquals("success", responseBody.get("status"));
    assertEquals("customer", responseBody.get("userType"));
    assertNotNull(responseBody.get("user"));
  }

  @Test
  public void testSearchByUsernameStaffFound() {
    StaffEntity staff = createTestStaff("searchstaff", "Search Staff", "CHEF");

    ResponseEntity<Map<String, Object>> response = userController.searchByUsername("searchstaff");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    Map<String, Object> responseBody = response.getBody();
    assertEquals("success", responseBody.get("status"));
    assertEquals("staff", responseBody.get("userType"));
    assertNotNull(responseBody.get("user"));
  }

  @Test
  public void testSearchByUsernameNotFound() {
    ResponseEntity<Map<String, Object>> response = userController.searchByUsername("nonexistent");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    Map<String, Object> responseBody = response.getBody();
    assertEquals("error", responseBody.get("status"));
    assertEquals("User not found", responseBody.get("message"));
  }

  // ============ Helper Methods ============

  private CustomerEntity createTestCustomer(String username, String name) {
    CustomerEntity customer = new CustomerEntity();
    customer.setId(nextCustomerId++);
    customer.setUsername(username);
    customer.setPassword("testpass");
    customer.setName(name);
    customer.setEmail(username + "@example.com");
    customer.setPhoneNumber("123-456-7890");
    customerStorage.put(customer.getId(), customer);
    return customer;
  }

  private StaffEntity createTestStaff(String username, String name, String role) {
    StaffEntity staff = new StaffEntity();
    staff.setId(nextStaffId++);
    staff.setUsername(username);
    staff.setPassword("testpass");
    staff.setName(name);
    staff.setRole(role);
    staffStorage.put(staff.getId(), staff);
    return staff;
  }
}
