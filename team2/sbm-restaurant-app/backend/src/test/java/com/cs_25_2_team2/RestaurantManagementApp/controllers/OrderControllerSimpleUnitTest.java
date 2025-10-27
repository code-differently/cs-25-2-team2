package com.cs_25_2_team2.RestaurantManagementApp.controllers;

import com.cs_25_2_team2.RestaurantManagementApp.entities.OrderEntity;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.OrderRepository;
import com.cs_25_2_team2.RestaurantManagementApp.services.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import jakarta.servlet.http.HttpSession;

class OrderControllerSimpleUnitTest {
    private OrderRepository orderRepository;
    private RestaurantService restaurantService;
    private OrderController orderController;
    private HttpSession session;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        restaurantService = mock(RestaurantService.class);
        orderController = new OrderController(orderRepository, restaurantService);
        session = mock(HttpSession.class);
        when(session.getAttribute("userId")).thenReturn(1L);
    }

    @Test
    void testGetAllOrdersReturnsList() {
        when(orderRepository.findAll()).thenReturn(java.util.List.of(new OrderEntity()));
        assertEquals(1, orderController.getAllOrders().size());
    }

    @Test
    void testGetOrderByIdReturnsEntity() {
        OrderEntity order = new OrderEntity();
        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(order));
        ResponseEntity<OrderEntity> response = orderController.getOrderById(1L);
    assertEquals(order, response.getBody());
    assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testGetOrderByIdNotFound() {
        when(orderRepository.findById(2L)).thenReturn(java.util.Optional.empty());
        ResponseEntity<OrderEntity> response = orderController.getOrderById(2L);
    assertNull(response.getBody());
    assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testCreateOrderHandlesSession() {
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("totalPrice", 10.0);
        ResponseEntity<?> response = orderController.createOrder(orderData, session);
    int code = response.getStatusCode().value();
    assertTrue(code == 200 || code == 400);
    }
}
