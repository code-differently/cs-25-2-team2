package com.cs_25_2_team2.RestaurantManagementApp.controllers;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
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

import com.cs_25_2_team2.RestaurantManagementApp.entities.MenuItemEntity;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.MenuItemRepository;

public class MenuControllerTest {

    private MenuController controller;
    private MenuItemRepository proxyRepo;
    private InMemoryHandler handler;

    @BeforeEach
    void setup() throws Exception {
        controller = new MenuController();

        // Create dynamic proxy repository backed by in-memory handler
        handler = new InMemoryHandler();
        proxyRepo = (MenuItemRepository) Proxy.newProxyInstance(
            MenuItemRepository.class.getClassLoader(),
            new Class<?>[]{MenuItemRepository.class},
            handler
        );

        // Inject proxyRepo into controller via reflection
        Field repoField = MenuController.class.getDeclaredField("menuItemRepository");
        repoField.setAccessible(true);
        repoField.set(controller, proxyRepo);
    }

    @Test
    void createMenuItem_persists_and_sets_defaults() {
        Map<String, Object> body = new HashMap<>();
        body.put("dishName", "Test Dish");
        body.put("price", "15.50");
        body.put("category", "Main");

        ResponseEntity<MenuItemEntity> resp = controller.createMenuItem(body);

        assertEquals(201, resp.getStatusCode().value(), "Should return 201 Created");
        MenuItemEntity created = resp.getBody();
        assertNotNull(created, "Created menu item body should not be null");
        
        // Check dishName
        assertEquals("Test Dish", created.getDishName(), "Dish name should be set");
        
        // Check price
        assertNotNull(created.getPrice(), "Price should be set");
        assertEquals(0, new BigDecimal("15.50").compareTo(created.getPrice()), "Price must match");
        
        // Check category
        assertEquals(MenuItemEntity.Category.MAIN_DISH, created.getCategory(), "Category should be set");
        
        // Check default values
        assertTrue(created.getIsAvailable(), "Should be available by default");
        assertNotNull(created.getCreatedAt(), "createdAt should be set");
        
        // id assigned by repository proxy
        Long id = handler.getIdFromEntity(created);
        assertNotNull(id, "Saved menu item should have an id assigned");
    }

    @Test
    void getAllMenuItems_returns_saved_items() {
        Map<String, Object> a = new HashMap<>();
        a.put("dishName", "Burger");
        a.put("price", "12.00");
        a.put("category", "Main");
        
        Map<String, Object> b = new HashMap<>();
        b.put("dishName", "Fries");
        b.put("price", "5.50");
        b.put("category", "Side");

        controller.createMenuItem(a);
        controller.createMenuItem(b);

        List<MenuItemEntity> all = controller.getAllMenuItems();
        assertNotNull(all);
        assertEquals(2, all.size(), "Should have 2 menu items");
        
        List<String> dishNames = all.stream()
            .map(MenuItemEntity::getDishName)
            .collect(Collectors.toList());
        
        assertTrue(dishNames.contains("Burger"), "Should contain Burger");
        assertTrue(dishNames.contains("Fries"), "Should contain Fries");
    }

    @Test
    void getMenuItemById_found_and_not_found() {
        Map<String, Object> body = new HashMap<>();
        body.put("dishName", "Pizza");
        body.put("price", "18.25");
        body.put("category", "Main");

        ResponseEntity<MenuItemEntity> createdResp = controller.createMenuItem(body);
        MenuItemEntity created = createdResp.getBody();
        assertNotNull(created);

        Long id = handler.getIdFromEntity(created);
        assertNotNull(id);

        ResponseEntity<MenuItemEntity> getResp = controller.getMenuItemById(id);
        assertEquals(200, getResp.getStatusCode().value());
        MenuItemEntity found = getResp.getBody();
        assertNotNull(found);
        assertEquals("Pizza", found.getDishName());

        ResponseEntity<MenuItemEntity> notFound = controller.getMenuItemById(999999L);
        assertEquals(404, notFound.getStatusCode().value());
    }

    @Test
    void updateMenuItem_success_badRequest_notFound() {
        Map<String, Object> body = new HashMap<>();
        body.put("dishName", "Salad");
        body.put("price", "8.00");
        body.put("category", "Side");

        MenuItemEntity created = controller.createMenuItem(body).getBody();
        Long id = handler.getIdFromEntity(created);
        assertNotNull(id);

        Map<String, Object> updateBody = new HashMap<>();
        updateBody.put("dishName", "Caesar Salad");
        updateBody.put("price", "9.50");

        ResponseEntity<MenuItemEntity> updated = controller.updateMenuItem(id, updateBody);
        assertEquals(200, updated.getStatusCode().value());
        MenuItemEntity updatedItem = updated.getBody();
        assertNotNull(updatedItem);
        assertEquals("Caesar Salad", updatedItem.getDishName());
        assertEquals(0, new BigDecimal("9.50").compareTo(updatedItem.getPrice()));

        // Bad request when no valid fields provided
        ResponseEntity<MenuItemEntity> badReq = controller.updateMenuItem(id, new HashMap<>());
        assertEquals(400, badReq.getStatusCode().value());

        // Not found when non-existing id
        ResponseEntity<MenuItemEntity> notFound = controller.updateMenuItem(888888L, updateBody);
        assertEquals(404, notFound.getStatusCode().value());
    }

    @Test
    void deleteMenuItem_deletes_and_not_found() {
        Map<String, Object> body = new HashMap<>();
        body.put("dishName", "Soup");
        body.put("price", "6.99");
        body.put("category", "Soup");

        MenuItemEntity created = controller.createMenuItem(body).getBody();
        Long id = handler.getIdFromEntity(created);

        ResponseEntity<Void> delResp = controller.deleteMenuItem(id);
        assertEquals(204, delResp.getStatusCode().value());

        // second delete should be 404
        ResponseEntity<Void> second = controller.deleteMenuItem(id);
        assertEquals(404, second.getStatusCode().value());
    }

    @Test
    void getCategories_returns_available_categories() {
        ResponseEntity<List<String>> resp = controller.getCategories();
        assertEquals(200, resp.getStatusCode().value());
        
        List<String> categories = resp.getBody();
        assertNotNull(categories);
        assertTrue(categories.contains("MAIN_DISH"));
        assertTrue(categories.contains("SIDE"));
        assertTrue(categories.contains("SOUP"));
    }

    @Test
    void createMenuItem_with_frontend_data_structure() {
        // Test with data structure similar to frontend menuItems.js
        Map<String, Object> texasLoadedPotato = new HashMap<>();
        texasLoadedPotato.put("dishName", "Texas Loaded Baked Potato");
        texasLoadedPotato.put("category", "Main");
        texasLoadedPotato.put("price", "7.99");

        ResponseEntity<MenuItemEntity> resp = controller.createMenuItem(texasLoadedPotato);
        assertEquals(201, resp.getStatusCode().value());
        
        MenuItemEntity created = resp.getBody();
        assertNotNull(created);
        assertEquals("Texas Loaded Baked Potato", created.getDishName());
        assertEquals(MenuItemEntity.Category.MAIN_DISH, created.getCategory());
        assertEquals(0, new BigDecimal("7.99").compareTo(created.getPrice()));

        // Test soup category
        Map<String, Object> potatoSoup = new HashMap<>();
        potatoSoup.put("dishName", "Loaded Baked Potato Soup");
        potatoSoup.put("category", "Soup");
        potatoSoup.put("price", "5.99");

        ResponseEntity<MenuItemEntity> soupResp = controller.createMenuItem(potatoSoup);
        assertEquals(201, soupResp.getStatusCode().value());
        
        MenuItemEntity soupCreated = soupResp.getBody();
        assertNotNull(soupCreated);
        assertEquals(MenuItemEntity.Category.SOUP, soupCreated.getCategory());
    }

    // ---------- Helper: In-memory InvocationHandler for MenuItemRepository ----------
    private static class InMemoryHandler implements InvocationHandler {
        private final Map<Long, MenuItemEntity> store = new LinkedHashMap<>();
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
                store.put(id, (MenuItemEntity) entity);
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

            if ("findByCategoryAndIsAvailableTrue".equals(name) && args != null && args.length == 1) {
                MenuItemEntity.Category category = (MenuItemEntity.Category) args[0];
                return store.values().stream()
                    .filter(item -> item.getCategory().equals(category) && item.getIsAvailable())
                    .collect(Collectors.toList());
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
                Method m = findMethod(entity.getClass(), "getDishId");
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
                Method m = findMethod(entity.getClass(), "setDishId", Long.class);
                if (m == null) {
                    // try primitive long
                    m = findMethod(entity.getClass(), "setDishId", long.class);
                }
                if (m == null) m = findMethod(entity.getClass(), "setId", Long.class);
                if (m == null) m = findMethod(entity.getClass(), "setId", long.class);

                if (m != null) {
                    m.invoke(entity, id);
                    return;
                }

                // last resort: try to set a field named dishId or id
                Field f = null;
                try {
                    f = entity.getClass().getDeclaredField("dishId");
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

    // Assertion helper methods that delegate to JUnit
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

    private void assertEquals(MenuItemEntity.Category expected, MenuItemEntity.Category actual, String message) {
        org.junit.jupiter.api.Assertions.assertEquals(expected, actual, message);
    }

    private void assertEquals(MenuItemEntity.Category expected, MenuItemEntity.Category actual) {
        org.junit.jupiter.api.Assertions.assertEquals(expected, actual);
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

    private void assertTrue(boolean condition) {
        org.junit.jupiter.api.Assertions.assertTrue(condition);
    }


}
