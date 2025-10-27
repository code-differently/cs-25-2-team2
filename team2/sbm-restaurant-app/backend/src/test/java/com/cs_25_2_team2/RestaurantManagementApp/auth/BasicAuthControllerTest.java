package com.cs_25_2_team2.RestaurantManagementApp.auth;

import com.cs_25_2_team2.RestaurantManagementApp.entities.CustomerEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.StaffEntity;
import com.cs_25_2_team2.RestaurantManagementApp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class BasicAuthControllerTest {
    private BasicAuthController controller;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private HttpSession session;

    @BeforeEach
    void setup() {
        userService = Mockito.mock(UserService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        controller = new BasicAuthController();
        // Inject mocks
        try {
            var userField = BasicAuthController.class.getDeclaredField("userService");
            userField.setAccessible(true);
            userField.set(controller, userService);
            var passField = BasicAuthController.class.getDeclaredField("passwordEncoder");
            passField.setAccessible(true);
            passField.set(controller, passwordEncoder);
        } catch (Exception e) { throw new RuntimeException(e); }
        session = Mockito.mock(HttpSession.class);
    }

    @Test
    void testLoginCustomerSuccess() {
        BasicAuthController.LoginRequest req = new BasicAuthController.LoginRequest();
        req.setUsername("cust"); req.setPassword("pass"); req.setUserType("customer");
        CustomerEntity cust = new CustomerEntity();
        cust.setCustomerId(1L); cust.setUsername("cust"); cust.setPasswordHash("hash"); cust.setName("Cust Name");
        Mockito.when(userService.getCustomerByUsername("cust")).thenReturn(cust);
        Mockito.when(passwordEncoder.matches("pass", "hash")).thenReturn(true);
        ResponseEntity<?> resp = controller.login(req, session);
    assertEquals(200, resp.getStatusCode().value());
    }

    @Test
    void testLoginStaffSuccess() {
        BasicAuthController.LoginRequest req = new BasicAuthController.LoginRequest();
        req.setUsername("staff"); req.setPassword("pass"); req.setUserType("staff");
        StaffEntity staff = new StaffEntity();
        staff.setStaffId(2L); staff.setUsername("staff"); staff.setPasswordHash("hash"); staff.setName("Staff Name"); staff.setRole(StaffEntity.StaffRole.Chef);
        Mockito.when(userService.getStaffByUsername("staff")).thenReturn(staff);
        Mockito.when(passwordEncoder.matches("pass", "hash")).thenReturn(true);
        ResponseEntity<?> resp = controller.login(req, session);
    assertEquals(200, resp.getStatusCode().value());
    }

    @Test
    void testLoginFailure() {
        BasicAuthController.LoginRequest req = new BasicAuthController.LoginRequest();
        req.setUsername("bad"); req.setPassword("bad"); req.setUserType("customer");
        Mockito.when(userService.getCustomerByUsername("bad")).thenReturn(null);
        ResponseEntity<?> resp = controller.login(req, session);
    assertEquals(401, resp.getStatusCode().value());
    }

    @Test
    void testRegisterValidationErrors() {
        BasicAuthController.RegisterRequest req = new BasicAuthController.RegisterRequest();
        req.setUsername(""); req.setPassword(""); req.setEmail("");
        ResponseEntity<?> resp = controller.register(req);
    assertEquals(400, resp.getStatusCode().value());
    }

    @Test
    void testRegisterPasswordTooShort() {
        BasicAuthController.RegisterRequest req = new BasicAuthController.RegisterRequest();
        req.setUsername("user"); req.setPassword("123"); req.setEmail("a@b.com");
        ResponseEntity<?> resp = controller.register(req);
    assertEquals(400, resp.getStatusCode().value());
    }

    @Test
    void testRegisterDuplicateUsername() {
        BasicAuthController.RegisterRequest req = new BasicAuthController.RegisterRequest();
        req.setUsername("user"); req.setPassword("123456"); req.setEmail("a@b.com");
        Mockito.when(userService.getCustomerByUsername("user")).thenReturn(new CustomerEntity());
        ResponseEntity<?> resp = controller.register(req);
    assertEquals(400, resp.getStatusCode().value());
    }

    @Test
    void testRegisterSuccess() {
        BasicAuthController.RegisterRequest req = new BasicAuthController.RegisterRequest();
        req.setUsername("user"); req.setPassword("123456"); req.setEmail("a@b.com");
        Mockito.when(userService.getCustomerByUsername("user")).thenReturn(null);
        Mockito.when(passwordEncoder.encode("123456")).thenReturn("hash");
        CustomerEntity saved = new CustomerEntity(); saved.setCustomerId(1L); saved.setUsername("user");
        Mockito.when(userService.saveCustomer(Mockito.any())).thenReturn(saved);
        ResponseEntity<?> resp = controller.register(req);
    assertEquals(201, resp.getStatusCode().value());
    }

    @Test
    void testLogoutSuccess() {
        ResponseEntity<?> resp = controller.logout(session);
    assertEquals(200, resp.getStatusCode().value());
    }

    @Test
    void testGetProfileUnauthenticated() {
        Mockito.when(session.getAttribute("username")).thenReturn(null);
        Mockito.when(session.getAttribute("userType")).thenReturn(null);
        ResponseEntity<?> resp = controller.getProfile(session);
    assertEquals(401, resp.getStatusCode().value());
    }

    @Test
    void testGetAuthStatusAuthenticated() {
        Mockito.when(session.getAttribute("username")).thenReturn("user");
        Mockito.when(session.getAttribute("userType")).thenReturn("CUSTOMER");
        ResponseEntity<?> resp = controller.getAuthStatus(session);
    assertEquals(200, resp.getStatusCode().value());
    }

    @Test
    void testGetAuthStatusUnauthenticated() {
        Mockito.when(session.getAttribute("username")).thenReturn(null);
        Mockito.when(session.getAttribute("userType")).thenReturn(null);
        ResponseEntity<?> resp = controller.getAuthStatus(session);
    assertEquals(200, resp.getStatusCode().value());
    }
}
