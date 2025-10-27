package com.cs_25_2_team2.RestaurantManagementApp.controllers;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.cs_25_2_team2.RestaurantManagementApp.entities.OrderEntity;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.OrderRepository;
import com.cs_25_2_team2.RestaurantManagementApp.services.RestaurantService;

import jakarta.servlet.http.HttpSession;

public class OrderControllerTest {

    private OrderController controller;
    private OrderRepository proxyRepo;
    private InMemoryHandler handler;
    private RestaurantService restaurantService; 
    private HttpSession session;

    @BeforeEach
    void setup() throws Exception {
        controller = new OrderController(proxyRepo, restaurantService);

        // Create dynamic proxy repository backed by in-memory handler
        handler = new InMemoryHandler();
        proxyRepo = (OrderRepository) Proxy.newProxyInstance(
            OrderRepository.class.getClassLoader(),
            new Class<?>[]{OrderRepository.class},
            handler
        );

        // Inject proxyRepo into controller via reflection
        Field repoField = OrderController.class.getDeclaredField("orderRepository");
        repoField.setAccessible(true);
        repoField.set(controller, proxyRepo);

        // Mock HttpSession with basic attributes
            session = new jakarta.servlet.http.HttpSession() {
            private int maxInactiveInterval = 0;
            @Override public int getMaxInactiveInterval() { return maxInactiveInterval; }
            @Override public void setMaxInactiveInterval(int interval) { maxInactiveInterval = interval; }
            @Override public jakarta.servlet.ServletContext getServletContext() { return null; }
            private final Map<String, Object> attributes = new HashMap<>();
            {
                attributes.put("userId", 1L);
                attributes.put("userType", "CUSTOMER");
                attributes.put("username", "testuser");
            }
            @Override public Object getAttribute(String name) { return attributes.get(name); }
            @Override public void setAttribute(String name, Object value) { attributes.put(name, value); }
            @Override public void removeAttribute(String name) { attributes.remove(name); }
            @Override public java.util.Enumeration<String> getAttributeNames() { return java.util.Collections.enumeration(attributes.keySet()); }
            @Override public long getCreationTime() { return 0; }
            @Override public String getId() { return "mockSession"; }
            @Override public long getLastAccessedTime() { return 0; }
            @Override public void invalidate() {}
            @Override public boolean isNew() { return false; }
        };
    }

    @Test
    void createOrder_persists_and_sets_defaults() {
        Map<String, Object> body = new HashMap<>();
        body.put("totalPrice", "12.50");

    ResponseEntity<?> resp = controller.createOrder(body, session);
    Assertions.assertEquals(201, resp.getStatusCodeValue(), "Should return 201 Created");
    Object respBody = resp.getBody();
    assertNotNull(respBody, "Created order body should not be null");
    assertTrue(respBody instanceof Map, "Response should be a Map");
    Map<String, Object> response = (Map<String, Object>) respBody;
    assertNotNull(response.get("status"), "Status should not be null");
    Assertions.assertEquals("Pending", response.get("status").toString(), "Default status should be Pending");
    assertNotNull(response.get("orderId"), "Saved order should have an id assigned");
    assertNotNull(response.get("totalPrice"), "totalPrice should not be null");
    Assertions.assertTrue(new BigDecimal("12.50").compareTo(new BigDecimal(response.get("totalPrice").toString())) == 0, "totalPrice must match");
    }

    @Test
    void getAllOrders_returns_saved_orders() {
        Map<String, Object> a = new HashMap<>();
        a.put("totalPrice", "3.00");
        Map<String, Object> b = new HashMap<>();
        b.put("totalPrice", "4.50");

    controller.createOrder(a, session);
    controller.createOrder(b, session);

        List<OrderEntity> all = controller.getAllOrders();
        assertNotNull(all);
        List<BigDecimal> prices = all.stream().map(o -> {
            try {
                Method m = o.getClass().getMethod("getTotalPrice");
                return (BigDecimal) m.invoke(o);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }).collect(Collectors.toList());

        assertTrue(prices.contains(new BigDecimal("3.00")), "Should contain 3.00 price");
        assertTrue(prices.contains(new BigDecimal("4.50")), "Should contain 4.50 price");
    }

    @Test
    void getOrderById_found_and_not_found() {
        Map<String, Object> body = new HashMap<>();
        body.put("totalPrice", "7.25");

    ResponseEntity<?> createdResp = controller.createOrder(body, session);
    Object respBody = createdResp.getBody();
    assertNotNull(respBody);
    assertTrue(respBody instanceof Map, "Response body should be a Map");
    @SuppressWarnings("unchecked")
    Map<String, Object> response = (Map<String, Object>) respBody;
    assertNotNull(response, "Response map should not be null");
    assertNotNull(response.get("orderId"));
    Long id = Long.valueOf(response.get("orderId").toString());
    assertNotNull(id);

    ResponseEntity<OrderEntity> getResp = controller.getOrderById(id);
    assertEquals(200, getResp.getStatusCode().value());
    assertNotNull(getResp.getBody());

    ResponseEntity<OrderEntity> notFound = controller.getOrderById(999999L);
    assertEquals(404, notFound.getStatusCode().value());
    }

    @Test
    void updateStatus_success_badRequest_notFound() {
        Map<String, Object> body = new HashMap<>();
        body.put("totalPrice", "2.00");

        ResponseEntity<?> createdResp = controller.createOrder(body, session);
        Object respBody = createdResp.getBody();
        assertNotNull(respBody);
    assertTrue(respBody instanceof Map, "Response body should be a Map");
    @SuppressWarnings("unchecked")
    Map<String, Object> response = (Map<String, Object>) respBody;
    assertNotNull(response, "Response map should not be null");
    assertNotNull(response.get("orderId"));
        Long id = Long.valueOf(response.get("orderId").toString());
        assertNotNull(id);

        Map<String, Object> statusBody = new HashMap<>();
        statusBody.put("status", "Placed"); // Use valid enum value

        ResponseEntity<OrderEntity> updated = controller.updateStatus(id, statusBody);
    assertEquals(200, updated.getStatusCode().value());
        assertNotNull(updated.getBody());
        try {
            assertNotNull(updated.getBody(), "Updated order body should not be null");
            Method getStatus = updated.getBody().getClass().getMethod("getStatus");
            assertEquals("Placed", getStatus.invoke(updated.getBody()).toString());
        } catch (Exception ex) {
            fail("OrderEntity should have getStatus method: " + ex.getMessage());
        }

        // Bad request when no status provided
        ResponseEntity<OrderEntity> badReq = controller.updateStatus(id, new HashMap<>());
    assertEquals(400, badReq.getStatusCode().value());

        // Not found when non-existing id
        ResponseEntity<OrderEntity> notFound = controller.updateStatus(888888L, statusBody);
    assertEquals(404, notFound.getStatusCode().value());
    }

    @Test
    void cancelOrder_deletes_and_not_found() {
        Map<String, Object> body = new HashMap<>();
        body.put("totalPrice", "9.99");

    ResponseEntity<?> createdResp = controller.createOrder(body, session);
    Object respBody = createdResp.getBody();
    assertNotNull(respBody);
    assertTrue(respBody instanceof Map, "Response body should be a Map");
    @SuppressWarnings("unchecked")
    Map<String, Object> response = (Map<String, Object>) respBody;
    assertNotNull(response, "Response map should not be null");
    assertNotNull(response.get("orderId"));
    Long id = Long.valueOf(response.get("orderId").toString());

    ResponseEntity<Void> delResp = controller.cancelOrder(id);
    assertEquals(204, delResp.getStatusCode().value());

    // second delete should be 404
    ResponseEntity<Void> second = controller.cancelOrder(id);
    assertEquals(404, second.getStatusCode().value());
    }

    // ---------- Helper: In-memory InvocationHandler for OrderRepository ----------
    private static class InMemoryHandler implements InvocationHandler {
        private final Map<Long, OrderEntity> store = new LinkedHashMap<>();
        private final AtomicLong counter = new AtomicLong(1);

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String name = method.getName();

            if ("findAll".equals(name) && (args == null || args.length == 0)) {
                return new ArrayList<>(store.values());
            }

            if ("findById".equals(name) && args != null && args.length == 1) {
                Long id = toLong(args[0]);
                return Optional.ofNullable(store.get(id));
            }

            if ("save".equals(name) && args != null && args.length == 1) {
                Object entity = args[0];
                if (entity == null) return null;
                Long id = getId(entity);
                if (id == null || id == 0L) {
                    id = counter.getAndIncrement();
                    setId(entity, id);
                }
                // store a shallow copy (store same instance for simplicity)
                store.put(id, (OrderEntity) entity);
                return entity;
            }

            if ("existsById".equals(name) && args != null && args.length == 1) {
                Long id = toLong(args[0]);
                return store.containsKey(id);
            }

            if ("deleteById".equals(name) && args != null && args.length == 1) {
                Long id = toLong(args[0]);
                store.remove(id);
                return null;
            }

            // fallback for other methods used implicitly by JPA interfaces
            // Provide reasonable defaults to avoid failing reflective calls in tests
            if ("getOne".equals(name) || "getById".equals(name)) {
                Long id = args != null && args.length > 0 ? toLong(args[0]) : null;
                return store.get(id);
            }

            throw new UnsupportedOperationException("Method not implemented in proxy: " + method);
        }

        Long getIdFromEntity(Object entity) {
            return getId(entity);
        }

        private static Long toLong(Object o) {
            if (o == null) return null;
            if (o instanceof Long) return (Long) o;
            if (o instanceof Integer) return ((Integer) o).longValue();
            if (o instanceof Short) return ((Short) o).longValue();
            if (o instanceof String) return Long.parseLong((String) o);
            return null;
        }

        private static Long getId(Object entity) {
            try {
                Method m = findMethod(entity.getClass(), "getOrderId");
                if (m == null) m = findMethod(entity.getClass(), "getId");
                if (m != null) {
                    Object v = m.invoke(entity);
                    return toLong(v);
                }
            } catch (Exception ignored) {}
            return null;
        }

        private static void setId(Object entity, Long id) {
            try {
                Method m = findMethod(entity.getClass(), "setOrderId", Long.class);
                if (m == null) {
                    // try primitive long
                    m = findMethod(entity.getClass(), "setOrderId", long.class);
                }
                if (m == null) m = findMethod(entity.getClass(), "setId", Long.class);
                if (m == null) m = findMethod(entity.getClass(), "setId", long.class);

                if (m != null) {
                    m.invoke(entity, id);
                    return;
                }

                // last resort: try to set a field named orderId or id
                Field f = null;
                try {
                    f = entity.getClass().getDeclaredField("orderId");
                } catch (NoSuchFieldException e) {
                    try {
                        f = entity.getClass().getDeclaredField("id");
                    } catch (NoSuchFieldException ignored) {}
                }
                if (f != null) {
                    f.setAccessible(true);
                    if (f.getType() == Long.class) f.set(entity, id);
                    else if (f.getType() == long.class) f.setLong(entity, id);
                }
            } catch (Exception ignored) {}
        }

        private static Method findMethod(Class<?> cls, String name, Class<?>... params) {
            try {
                return cls.getMethod(name, params);
            } catch (NoSuchMethodException ignored) {
                // try declared
                try {
                    return cls.getDeclaredMethod(name, params);
                } catch (NoSuchMethodException e) {
                    return null;
                }
            }
        }
    }

    private void assertEquals(int expected, int actual) {
        org.junit.jupiter.api.Assertions.assertEquals(expected, actual);
    }

    private void assertEquals(BigDecimal expected, BigDecimal actual, String message) {
        org.junit.jupiter.api.Assertions.assertEquals(expected, actual, message);
    }

    private void assertEquals(String expected, String actual) {
        org.junit.jupiter.api.Assertions.assertEquals(expected, actual);
    }

    private void assertEquals(String expected, String actual, String message) {
        org.junit.jupiter.api.Assertions.assertEquals(expected, actual, message);
    }

    private void assertNotNull(Object obj) {
        org.junit.jupiter.api.Assertions.assertNotNull(obj);
    }

    private void assertNotNull(Object obj, String message) {
        org.junit.jupiter.api.Assertions.assertNotNull(obj, message);
    }

    private void assertTrue(boolean condition, String message) {
        org.junit.jupiter.api.Assertions.assertTrue(condition, message);
    }

    private void fail(String message) {
        org.junit.jupiter.api.Assertions.fail(message);
    }

    // ---------- Additional Tests for Edge Cases ----------
    @Test
void createOrder_missingTotalPrice_returnsBadRequest() {
    Map<String, Object> body = new HashMap<>();
    // No totalPrice
    ResponseEntity<?> resp = controller.createOrder(body, session);
    assertEquals(400, resp.getStatusCode().value());
}

@Test
void createOrder_missingSessionAttributes_returnsBadRequest() {
    Map<String, Object> body = new HashMap<>();
    body.put("totalPrice", "10.00");
    // Session missing userId and userType
    HttpSession badSession = new jakarta.servlet.http.HttpSession() {
        private final Map<String, Object> attributes = new HashMap<>();
        @Override public Object getAttribute(String name) { return attributes.get(name); }
        @Override public void setAttribute(String name, Object value) { attributes.put(name, value); }
        @Override public void removeAttribute(String name) { attributes.remove(name); }
        @Override public java.util.Enumeration<String> getAttributeNames() { return java.util.Collections.enumeration(attributes.keySet()); }
        @Override public long getCreationTime() { return 0; }
        @Override public String getId() { return "badSession"; }
        @Override public long getLastAccessedTime() { return 0; }
        @Override public int getMaxInactiveInterval() { return 0; }
        @Override public void setMaxInactiveInterval(int interval) {}
        @Override public jakarta.servlet.ServletContext getServletContext() { return null; }
        @Override public void invalidate() {}
        @Override public boolean isNew() { return false; }
    };
    ResponseEntity<?> resp = controller.createOrder(body, badSession);
    assertEquals(400, resp.getStatusCode().value());
}

@Test
void updateStatus_invalidStatus_returnsBadRequest() {
    Map<String, Object> body = new HashMap<>();
    body.put("totalPrice", "5.00");
    ResponseEntity<?> createdResp = controller.createOrder(body, session);
    Object respBody = createdResp.getBody();
    assertNotNull(respBody);
    @SuppressWarnings("unchecked")
    Map<String, Object> response = (Map<String, Object>) respBody;
    Long id = Long.valueOf(response.get("orderId").toString());
    Map<String, Object> statusBody = new HashMap<>();
    statusBody.put("status", "NotAStatus");
    ResponseEntity<OrderEntity> resp = controller.updateStatus(id, statusBody);
    assertEquals(400, resp.getStatusCode().value());
}

@Test
void updateStatus_missingStatus_returnsBadRequest() {
    Map<String, Object> body = new HashMap<>();
    body.put("totalPrice", "5.00");
    ResponseEntity<?> createdResp = controller.createOrder(body, session);
    Object respBody = createdResp.getBody();
    assertNotNull(respBody);
    @SuppressWarnings("unchecked")
    Map<String, Object> response = (Map<String, Object>) respBody;
    Long id = Long.valueOf(response.get("orderId").toString());
    Map<String, Object> statusBody = new HashMap<>();
    ResponseEntity<OrderEntity> resp = controller.updateStatus(id, statusBody);
    assertEquals(400, resp.getStatusCode().value());
}

@Test
void updateStatus_nonExistentOrder_returnsNotFound() {
    Map<String, Object> statusBody = new HashMap<>();
    statusBody.put("status", "Placed");
    ResponseEntity<OrderEntity> resp = controller.updateStatus(999999L, statusBody);
    assertEquals(404, resp.getStatusCode().value());
}

@Test
void cancelOrder_nonExistentOrder_returnsNotFound() {
    ResponseEntity<Void> resp = controller.cancelOrder(999999L);
    assertEquals(404, resp.getStatusCode().value());
}

@Test
void getAllOrders_empty_returnsEmptyList() {
    List<OrderEntity> all = controller.getAllOrders();
    assertNotNull(all);
    assertTrue(all.isEmpty(), "Should be empty when no orders exist");
}

@Test
void getOrderById_nonExistent_returnsNotFound() {
    ResponseEntity<OrderEntity> resp = controller.getOrderById(999999L);
    assertEquals(404, resp.getStatusCode().value());
}
}