package com.cs_25_2_team2.RestaurantManagementApp.services;

import com.cs_25_2_team2.RestaurantManagementApp.entities.*;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Test
    void testBranchCoverageForRegistrationAndProfileUpdates() {
        // Setup mocks
        CustomerEntity customer = new CustomerEntity();
        StaffEntity staff = new StaffEntity();
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(customer);
        when(staffRepository.save(any(StaffEntity.class))).thenReturn(staff);

        // Register customer
        Object regCustomer = userService.registerCustomer("user", "name", "address", "555-5555");
        assertNotNull(regCustomer);

        // Register staff
    Object regStaff = userService.registerStaff("user", "name", "address", "555-5555", "Chef");
        assertNotNull(regStaff);

        // Update customer profile
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        Object updated = userService.updateCustomerProfile(1L, "newname", "newaddress", "555-0000");
        assertNotNull(updated);

        // Update customer profile with missing customer
        when(customerRepository.findById(2L)).thenReturn(Optional.empty());
        Object updatedMissing = userService.updateCustomerProfile(2L, "x", "y", "z");
        assertNull(updatedMissing);
    }

    @Test
    void testBranchCoverageForCartAndCheckout() {
        CustomerEntity customer = new CustomerEntity();
        CartEntity cart = new CartEntity();
    java.util.List<CartItemEntity> mutableCartItems = new java.util.ArrayList<>();
    mutableCartItems.add(new CartItemEntity());
    cart.setCartItems(mutableCartItems);
        customer.setCart(cart);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(new OrderEntity());

        // Get customer cart
        Object cartObj = userService.getCustomerCart(1L);
        assertNotNull(cartObj);
        Object cartObjMissing = userService.getCustomerCart(2L);
        assertNull(cartObjMissing);

        // Customer checkout
    when(customerRepository.findByIdWithCart(1L)).thenReturn(Optional.of(customer));
    Object checkout = userService.customerCheckout(1L);
    assertNotNull(checkout);

        // Checkout with missing customer
        when(customerRepository.findById(2L)).thenReturn(Optional.empty());
        try {
            userService.customerCheckout(2L);
            fail("Expected RuntimeException for missing customer");
        } catch (RuntimeException e) {
            // Expected
        }
    }

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private StaffRepository staffRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateUser_CustomerSuccess() {
        CustomerEntity customer = new CustomerEntity();
        when(customerRepository.findByUsername("user")).thenReturn(Optional.of(customer));
        Object result = userService.authenticateUser("user", "password");
        assertEquals(customer, result);
    }

    @Test
    void testAuthenticateUser_StaffSuccess() {
        when(customerRepository.findByUsername("user")).thenReturn(Optional.empty());
        StaffEntity staff = new StaffEntity();
        when(staffRepository.findByUsername("user")).thenReturn(Optional.of(staff));
        Object result = userService.authenticateUser("user", "password");
        assertEquals(staff, result);
    }

    @Test
    void testAuthenticateUser_Failure() {
        when(customerRepository.findByUsername("user")).thenReturn(Optional.empty());
        when(staffRepository.findByUsername("user")).thenReturn(Optional.empty());
        Object result = userService.authenticateUser("user", "wrong");
        assertNull(result);
    }

    @Test
    void testRegisterCustomer_Success() {
        when(customerRepository.existsByUsername("newuser")).thenReturn(false);
        CustomerEntity customer = new CustomerEntity("newuser", "Name", "Addr", "123");
        when(customerRepository.save(any())).thenReturn(customer);
        when(cartRepository.save(any())).thenReturn(new CartEntity(customer));
        CustomerEntity result = userService.registerCustomer("newuser", "Name", "Addr", "123");
        assertEquals(customer, result);
    }

    @Test
    void testRegisterCustomer_UsernameExists() {
        when(customerRepository.existsByUsername("existing")).thenReturn(true);
        assertThrows(RuntimeException.class, () -> userService.registerCustomer("existing", "Name", "Addr", "123"));
    }

    @Test
    void testGetCustomerById() {
        CustomerEntity customer = new CustomerEntity();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        assertEquals(customer, userService.getCustomerById(1L));
    }

    @Test
    void testGetStaffById() {
        StaffEntity staff = new StaffEntity();
        when(staffRepository.findById(2L)).thenReturn(Optional.of(staff));
        assertEquals(staff, userService.getStaffById(2L));
    }

    @Test
    void testGetAllCustomers() {
        when(customerRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(userService.getAllCustomers().isEmpty());
    }

    @Test
    void testGetAllStaff() {
        when(staffRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(userService.getAllStaff().isEmpty());
    }
}