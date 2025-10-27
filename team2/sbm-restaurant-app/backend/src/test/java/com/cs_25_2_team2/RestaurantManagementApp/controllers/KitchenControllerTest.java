package com.cs_25_2_team2.RestaurantManagementApp.controllers;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cs_25_2_team2.RestaurantManagementApp.entities.CustomerEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.OrderEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.OrderQueueEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.StaffEntity;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.OrderQueueRepository;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.OrderRepository;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.StaffRepository;

public class KitchenControllerTest {

    private KitchenController kitchenController;
    private OrderRepository mockOrderRepository;
    private OrderQueueRepository mockOrderQueueRepository;
    private StaffRepository mockStaffRepository;
    
    // In-memory storage for testing
    private final Map<Long, OrderEntity> orderStorage = new HashMap<>();
    private final Map<Long, OrderQueueEntity> queueStorage = new HashMap<>();
    private final Map<Long, StaffEntity> staffStorage = new HashMap<>();
    private long nextOrderId = 1L;
    private long nextQueueId = 1L;
    private long nextStaffId = 1L;

    @BeforeEach
    public void setUp() {
        // Clear storage before each test
        orderStorage.clear();
        queueStorage.clear();
        staffStorage.clear();
        nextOrderId = 1L;
        nextQueueId = 1L;
        nextStaffId = 1L;
        
        setupMockRepositories();
        kitchenController = new KitchenController(mockOrderRepository, mockOrderQueueRepository, mockStaffRepository);
    }

    private void setupMockRepositories() {
        // Create dynamic proxy for OrderRepository
        mockOrderRepository = (OrderRepository) Proxy.newProxyInstance(
                OrderRepository.class.getClassLoader(),
                new Class[]{OrderRepository.class},
                (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "save":
                            OrderEntity orderToSave = (OrderEntity) args[0];
                            if (orderToSave.getOrderId() == null) {
                                orderToSave.setOrderId(nextOrderId++);
                            }
                            orderStorage.put(orderToSave.getOrderId(), orderToSave);
                            return orderToSave;
                            
                        case "findById":
                            Long orderId = (Long) args[0];
                            return Optional.ofNullable(orderStorage.get(orderId));
                            
                        case "findAll":
                            return new ArrayList<>(orderStorage.values());
                            
                        case "deleteById":
                            Long orderIdToDelete = (Long) args[0];
                            orderStorage.remove(orderIdToDelete);
                            return null;
                            
                        case "existsById":
                            Long orderIdToCheck = (Long) args[0];
                            return orderStorage.containsKey(orderIdToCheck);
                            
                        default:
                            throw new UnsupportedOperationException("Mock method not implemented: " + method.getName());
                    }
                }
        );

        // Create dynamic proxy for OrderQueueRepository
        mockOrderQueueRepository = (OrderQueueRepository) Proxy.newProxyInstance(
                OrderQueueRepository.class.getClassLoader(),
                new Class[]{OrderQueueRepository.class},
                (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "save":
                            OrderQueueEntity queueToSave = (OrderQueueEntity) args[0];
                            if (queueToSave.getQueueId() == null) {
                                queueToSave.setQueueId(nextQueueId++);
                            }
                            queueStorage.put(queueToSave.getQueueId(), queueToSave);
                            return queueToSave;
                            
                        case "findById":
                            Long queueId = (Long) args[0];
                            return Optional.ofNullable(queueStorage.get(queueId));
                            
                        case "findAll":
                            return new ArrayList<>(queueStorage.values());
                            
                        case "findByOrderOrderId":
                            Long orderId = (Long) args[0];
                            return queueStorage.values().stream()
                                    .filter(q -> q.getOrder().getOrderId().equals(orderId))
                                    .findFirst();
                                    
                        case "findPendingOrders":
                            return queueStorage.values().stream()
                                    .filter(q -> q.getStartedPreparingAt() == null)
                                    .sorted(Comparator.comparing(OrderQueueEntity::getAddedToQueueAt))
                                    .toList();
                                    
                        case "findOrdersInPreparation":
                            return queueStorage.values().stream()
                                    .filter(q -> q.getStartedPreparingAt() != null && q.getReadyForDeliveryAt() == null)
                                    .toList();
                                    
                        case "findOrdersReadyForDelivery":
                            return queueStorage.values().stream()
                                    .filter(q -> q.getReadyForDeliveryAt() != null && q.getOutForDeliveryAt() == null)
                                    .toList();
                                    
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
                            
                        default:
                            throw new UnsupportedOperationException("Mock method not implemented: " + method.getName());
                    }
                }
        );
    }

    // ============ Queue Management Tests ============
    
    @Test
    public void testGetPendingOrdersEmpty() {
        ResponseEntity<Map<String, Object>> response = kitchenController.getPendingOrders();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("success", responseBody.get("status"));
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> orders = (List<Map<String, Object>>) responseBody.get("orders");
        assertEquals(0, orders.size());
    }

    @Test
    public void testGetPendingOrdersWithData() {
        // Setup: Create pending orders
        OrderEntity order1 = createTestOrder("Order 1", OrderEntity.OrderStatus.Placed);
        OrderEntity order2 = createTestOrder("Order 2", OrderEntity.OrderStatus.Placed);
        createTestQueueEntry(order1, null, null); // Pending
        createTestQueueEntry(order2, null, null); // Pending
        
        ResponseEntity<Map<String, Object>> response = kitchenController.getPendingOrders();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("success", responseBody.get("status"));
        assertEquals(2, responseBody.get("count"));
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> orders = (List<Map<String, Object>>) responseBody.get("orders");
        assertEquals(2, orders.size());
    }

    @Test
    public void testStartPreparingOrderSuccess() {
        // Setup: Create order and chef
        OrderEntity order = createTestOrder("Test Order", OrderEntity.OrderStatus.Placed);
        StaffEntity chef = createTestChef("Chef Mario", StaffEntity.StaffRole.Chef);
        createTestQueueEntry(order, null, null); // Pending
        
        ResponseEntity<Map<String, Object>> response = kitchenController.startPreparingOrder(order.getOrderId());
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("success", responseBody.get("status"));
        assertEquals("Order preparation started successfully", responseBody.get("message"));
        assertEquals(order.getOrderId(), responseBody.get("orderId"));
        
        // Verify order status was updated
        OrderEntity updatedOrder = orderStorage.get(order.getOrderId());
        assertEquals(OrderEntity.OrderStatus.Preparing, updatedOrder.getStatus());
        assertNotNull(updatedOrder.getAssignedChef());
    }

    @Test
    public void testStartPreparingOrderNotFound() {
        ResponseEntity<Map<String, Object>> response = kitchenController.startPreparingOrder(999L);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testStartPreparingOrderNoChefs() {
        // Setup: Create order but no chefs
        OrderEntity order = createTestOrder("Test Order", OrderEntity.OrderStatus.Placed);
        createTestQueueEntry(order, null, null);
        
        ResponseEntity<Map<String, Object>> response = kitchenController.startPreparingOrder(order.getOrderId());
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("error", responseBody.get("status"));
        assertEquals("No available chefs", responseBody.get("message"));
    }

    @Test
    public void testCompleteOrderSuccess() {
        // Setup: Create order in preparation
        OrderEntity order = createTestOrder("Test Order", OrderEntity.OrderStatus.Preparing);
        StaffEntity chef = createTestChef("Chef Mario", StaffEntity.StaffRole.Chef);
        order.setAssignedChef(chef);
        LocalDateTime startTime = LocalDateTime.now().minusMinutes(15);
        createTestQueueEntry(order, startTime, null); // In preparation
        
        ResponseEntity<Map<String, Object>> response = kitchenController.completeOrder(order.getOrderId());
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("success", responseBody.get("status"));
        assertEquals("Order marked as ready for delivery", responseBody.get("message"));
        
        // Verify order status was updated
        OrderEntity updatedOrder = orderStorage.get(order.getOrderId());
        assertEquals(OrderEntity.OrderStatus.ReadyForDelivery, updatedOrder.getStatus());
    }

    @Test
    public void testCompleteOrderNotFound() {
        ResponseEntity<Map<String, Object>> response = kitchenController.completeOrder(999L);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetOrderQueue() {
        // Setup: Create orders in different states
        OrderEntity pendingOrder = createTestOrder("Pending Order", OrderEntity.OrderStatus.Placed);
        OrderEntity preparingOrder = createTestOrder("Preparing Order", OrderEntity.OrderStatus.Preparing);
        OrderEntity readyOrder = createTestOrder("Ready Order", OrderEntity.OrderStatus.ReadyForDelivery);
        
        LocalDateTime now = LocalDateTime.now();
        createTestQueueEntry(pendingOrder, null, null); // Pending
        createTestQueueEntry(preparingOrder, now.minusMinutes(15), null); // Preparing
        createTestQueueEntry(readyOrder, now.minusMinutes(30), now.minusMinutes(5)); // Ready
        
        ResponseEntity<Map<String, Object>> response = kitchenController.getOrderQueue();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("success", responseBody.get("status"));
        assertEquals(3, responseBody.get("totalOrders"));
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> pending = (List<Map<String, Object>>) responseBody.get("pending");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> preparing = (List<Map<String, Object>>) responseBody.get("preparing");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> ready = (List<Map<String, Object>>) responseBody.get("ready");
        
        assertEquals(1, pending.size());
        assertEquals(1, preparing.size());
        assertEquals(1, ready.size());
    }

    // ============ Chef Management Tests ============
    
    @Test
    public void testGetAllChefs() {
        // Setup: Create chefs and other staff
        createTestChef("Chef Mario", StaffEntity.StaffRole.Chef);
        createTestChef("Chef Luigi", StaffEntity.StaffRole.Chef);
        createTestStaff("Delivery Guy", StaffEntity.StaffRole.Delivery); // Not a chef
        
        ResponseEntity<Map<String, Object>> response = kitchenController.getAllChefs();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("success", responseBody.get("status"));
        assertEquals(2, responseBody.get("count")); // Only chefs, not delivery staff
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> chefs = (List<Map<String, Object>>) responseBody.get("chefs");
        assertEquals(2, chefs.size());
    }

    @Test
    public void testGetAvailableChefs() {
        // Setup: Create chefs
        createTestChef("Chef Mario", StaffEntity.StaffRole.Chef);
        createTestChef("Chef Luigi", StaffEntity.StaffRole.Chef);
        
        ResponseEntity<Map<String, Object>> response = kitchenController.getAvailableChefs();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("success", responseBody.get("status"));
        assertEquals(2, responseBody.get("count"));
    }

    @Test
    public void testGetOrdersForChef() {
        // Setup: Create chef and assign orders
        StaffEntity chef = createTestChef("Chef Mario", StaffEntity.StaffRole.Chef);
        OrderEntity assignedOrder = createTestOrder("Assigned Order", OrderEntity.OrderStatus.Preparing);
        assignedOrder.setAssignedChef(chef);
        orderStorage.put(assignedOrder.getOrderId(), assignedOrder);
        
        // Create another order not assigned to this chef
        createTestOrder("Other Order", OrderEntity.OrderStatus.Preparing);
        
        ResponseEntity<Map<String, Object>> response = kitchenController.getOrdersForChef(chef.getStaffId());
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("success", responseBody.get("status"));
        assertEquals(1, responseBody.get("count"));
        assertEquals(chef.getStaffId(), responseBody.get("chefId"));
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> orders = (List<Map<String, Object>>) responseBody.get("orders");
        assertEquals(1, orders.size());
    }

    // ============ Statistics and Utilities Tests ============
    
    @Test
    public void testGetEstimatedTime() {
        // Setup: Create pending orders and chefs
        createTestOrder("Order 1", OrderEntity.OrderStatus.Placed);
        createTestOrder("Order 2", OrderEntity.OrderStatus.Placed);
        createTestChef("Chef Mario", StaffEntity.StaffRole.Chef);
        
        ResponseEntity<Map<String, Object>> response = kitchenController.getEstimatedTime();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("success", responseBody.get("status"));
        assertTrue((Integer) responseBody.get("estimatedMinutes") >= 0);
        assertEquals(1, responseBody.get("availableChefs"));
    }

    @Test
    public void testGetEstimatedTimeNoChefs() {
        // Setup: Create orders but no chefs
        createTestOrder("Order 1", OrderEntity.OrderStatus.Placed);
        
        ResponseEntity<Map<String, Object>> response = kitchenController.getEstimatedTime();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("error", responseBody.get("status"));
        assertEquals(-1, responseBody.get("estimatedMinutes"));
    }

    @Test
    public void testGetKitchenStatistics() {
        // Setup: Create orders in various states
        createTestOrder("Pending", OrderEntity.OrderStatus.Placed);
        createTestOrder("Preparing", OrderEntity.OrderStatus.Preparing);
        createTestOrder("Ready", OrderEntity.OrderStatus.ReadyForDelivery);
        createTestChef("Chef Mario", StaffEntity.StaffRole.Chef);
        
        ResponseEntity<Map<String, Object>> response = kitchenController.getKitchenStatistics();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("success", responseBody.get("status"));
        assertNotNull(responseBody.get("pendingOrdersCount"));
        assertNotNull(responseBody.get("totalChefsCount"));
    }

    @Test
    public void testAddOrderToQueue() {
        // Setup: Create an order
        OrderEntity order = createTestOrder("New Order", OrderEntity.OrderStatus.Placed);
        
        Map<String, Object> orderData = Map.of("orderId", order.getOrderId());
        
        ResponseEntity<Map<String, Object>> response = kitchenController.addOrderToQueue(orderData);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("success", responseBody.get("status"));
        assertEquals("Order added to kitchen queue", responseBody.get("message"));
        assertEquals(order.getOrderId(), responseBody.get("orderId"));
    }

    @Test
    public void testAddOrderToQueueNotFound() {
        Map<String, Object> orderData = Map.of("orderId", 999L);
        
        ResponseEntity<Map<String, Object>> response = kitchenController.addOrderToQueue(orderData);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // ============ Helper Methods ============
    
    private OrderEntity createTestOrder(String customerName, OrderEntity.OrderStatus status) {
        CustomerEntity customer = new CustomerEntity();
        customer.setCustomerId(1L);
        customer.setName(customerName);
        customer.setUsername("customer" + nextOrderId);
        
        OrderEntity order = new OrderEntity();
        order.setOrderId(nextOrderId++);
        order.setCustomer(customer);
        order.setTotalPrice(new BigDecimal("25.99"));
        order.setStatus(status);
        order.setCreatedAt(LocalDateTime.now());
        
        orderStorage.put(order.getOrderId(), order);
        return order;
    }
    
    private StaffEntity createTestChef(String name, StaffEntity.StaffRole role) {
        StaffEntity staff = new StaffEntity();
        staff.setStaffId(nextStaffId++);
        staff.setName(name);
        staff.setUsername("chef" + staff.getStaffId());
        staff.setRole(role);
        
        staffStorage.put(staff.getStaffId(), staff);
        return staff;
    }
    
    private StaffEntity createTestStaff(String name, StaffEntity.StaffRole role) {
        StaffEntity staff = new StaffEntity();
        staff.setStaffId(nextStaffId++);
        staff.setName(name);
        staff.setUsername("staff" + staff.getStaffId());
        staff.setRole(role);
        
        staffStorage.put(staff.getStaffId(), staff);
        return staff;
    }
    
    private OrderQueueEntity createTestQueueEntry(OrderEntity order, LocalDateTime startedAt, LocalDateTime readyAt) {
        OrderQueueEntity queueEntry = new OrderQueueEntity(order);
        queueEntry.setQueueId(nextQueueId++);
        queueEntry.setAddedToQueueAt(LocalDateTime.now().minusMinutes(30));
        queueEntry.setStartedPreparingAt(startedAt);
        queueEntry.setReadyForDeliveryAt(readyAt);
        
        queueStorage.put(queueEntry.getQueueId(), queueEntry);
        return queueEntry;
    }
}
