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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.cs_25_2_team2.RestaurantManagementApp.entities.OrderEntity;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.OrderRepository;

public class OrderControllerTest {

    private OrderController controller;
    private OrderRepository proxyRepo;
    private InMemoryHandler handler;

    @BeforeEach
    void setup() throws Exception {
        controller = new OrderController();

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
    }

    @Test
    void createOrder_persists_and_sets_defaults() {
        Map<String, Object> body = new HashMap<>();
        body.put("totalPrice", "12.50");

        ResponseEntity<OrderEntity> resp = controller.createOrder(body);

        assertEquals(201, resp.getStatusCodeValue(), "Should return 201 Created");
        OrderEntity created = resp.getBody();
        assertNotNull(created, "Created order body should not be null");
        // status default set to "Placed" by controller
        try {
            Method getStatus = created.getClass().getMethod("getStatus");
            Object status = getStatus.invoke(created);
            assertEquals("Placed", status.toString(), "Default status should be Placed");
        } catch (Exception ex) {
            fail("OrderEntity should have getStatus() method: " + ex.getMessage());
        }

        // createdAt set
        try {
            Method getCreatedAt = created.getClass().getMethod("getCreatedAt");
            Object createdAt = getCreatedAt.invoke(created);
            assertNotNull(createdAt, "createdAt should be set");
            assertTrue(createdAt instanceof LocalDateTime, "createdAt should be LocalDateTime");
        } catch (Exception ex) {
            fail("OrderEntity should have getCreatedAt() method: " + ex.getMessage());
        }

        // totalPrice check
        try {
            Method getTotalPrice = created.getClass().getMethod("getTotalPrice");
            Object tp = getTotalPrice.invoke(created);
            assertNotNull(tp, "totalPrice should be set on created order");
            assertTrue(tp instanceof BigDecimal, "totalPrice should be BigDecimal");
            assertEquals(0, new BigDecimal("12.50").compareTo((BigDecimal) tp), "totalPrice must match");
        } catch (Exception ex) {
            fail("OrderEntity should have getTotalPrice() method: " + ex.getMessage());
        }

        // id assigned by repository proxy
        Long id = handler.getIdFromEntity(created);
        assertNotNull(id, "Saved order should have an id assigned");
    }

    @Test
    void getAllOrders_returns_saved_orders() {
        Map<String, Object> a = new HashMap<>();
        a.put("totalPrice", "3.00");
        Map<String, Object> b = new HashMap<>();
        b.put("totalPrice", "4.50");

        controller.createOrder(a);
        controller.createOrder(b);

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

        ResponseEntity<OrderEntity> createdResp = controller.createOrder(body);
        OrderEntity created = createdResp.getBody();
        assertNotNull(created);

        Long id = handler.getIdFromEntity(created);
        assertNotNull(id);

        ResponseEntity<OrderEntity> getResp = controller.getOrderById(id);
        assertEquals(200, getResp.getStatusCodeValue());
        assertNotNull(getResp.getBody());

        ResponseEntity<OrderEntity> notFound = controller.getOrderById(999999L);
        assertEquals(404, notFound.getStatusCodeValue());
    }

    @Test
    void updateStatus_success_badRequest_notFound() {
        Map<String, Object> body = new HashMap<>();
        body.put("totalPrice", "2.00");

        OrderEntity created = controller.createOrder(body).getBody();
        Long id = handler.getIdFromEntity(created);
        assertNotNull(id);

        Map<String, Object> statusBody = new HashMap<>();
        statusBody.put("status", "Paid");

        ResponseEntity<OrderEntity> updated = controller.updateStatus(id, statusBody);
        assertEquals(200, updated.getStatusCodeValue());
        try {
            Method getStatus = updated.getBody().getClass().getMethod("getStatus");
            assertEquals("Paid", getStatus.invoke(updated.getBody()).toString());
        } catch (Exception ex) {
            fail("OrderEntity should have getStatus method: " + ex.getMessage());
        }

        // Bad request when no status provided
        ResponseEntity<OrderEntity> badReq = controller.updateStatus(id, new HashMap<>());
        assertEquals(400, badReq.getStatusCodeValue());

        // Not found when non-existing id
        ResponseEntity<OrderEntity> notFound = controller.updateStatus(888888L, statusBody);
        assertEquals(404, notFound.getStatusCodeValue());
    }

    @Test
    void cancelOrder_deletes_and_not_found() {
        Map<String, Object> body = new HashMap<>();
        body.put("totalPrice", "9.99");

        OrderEntity created = controller.createOrder(body).getBody();
        Long id = handler.getIdFromEntity(created);

        ResponseEntity<Void> delResp = controller.cancelOrder(id);
        assertEquals(204, delResp.getStatusCodeValue());

        // second delete should be 404
        ResponseEntity<Void> second = controller.cancelOrder(id);
        assertEquals(404, second.getStatusCodeValue());
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

    private void assertEquals(int expected, int actual, String message) {
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
}