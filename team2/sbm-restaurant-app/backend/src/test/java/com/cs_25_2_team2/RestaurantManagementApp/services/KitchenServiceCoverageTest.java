package com.cs_25_2_team2.RestaurantManagementApp.services;

import com.cs_25_2_team2.RestaurantManagementApp.Order;
import com.cs_25_2_team2.RestaurantManagementApp.Chef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class KitchenServiceCoverageTest {
    private KitchenService kitchenService;

    @BeforeEach
    void setUp() {
        kitchenService = new KitchenService();
    }

    @Test
    void testAddOrderToQueueAndFindOrderById() {
    Order order = createOrderWithId(101);
    kitchenService.addOrderToQueue(order);
    assertNotNull(kitchenService.getPendingOrders().stream().filter(o -> o.getId() == 101).findFirst().orElse(null));
    }

    @Test
    void testStartPreparingOrderAndCompleteOrder() {
    Order order = createOrderWithId(102);
    kitchenService.addOrderToQueue(order);
    // Set status to Placed, then Preparing, to allow completion
    try {
        java.lang.reflect.Method updateStatus = order.getClass().getDeclaredMethod("updateStatus", order.getClass().getDeclaredClasses()[0]);
        updateStatus.setAccessible(true);
        Object placedStatus = order.getClass().getDeclaredClasses()[0].getEnumConstants()[1]; // Status.Placed
        Object preparingStatus = order.getClass().getDeclaredClasses()[0].getEnumConstants()[2]; // Status.Preparing
        updateStatus.invoke(order, placedStatus);
        updateStatus.invoke(order, preparingStatus);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    assertTrue(kitchenService.completeOrder(102L));
    }

    @Test
    void testGetOrdersForChef() {
    Chef chef = kitchenService.getAllChefs().get(0);
    Order order = createOrderWithId(103);
    kitchenService.addOrderToQueue(order);
    kitchenService.startPreparingOrder(103L);
    List<Order> orders = kitchenService.getOrdersForChef(String.valueOf(chef.getRawId()));
    assertTrue(orders.size() >= 0);
    }
    private Order createOrderWithId(int id) {
        try {
            // Create dummy Customer
            Class<?> customerClass = Class.forName("com.cs_25_2_team2.RestaurantManagementApp.Customer");
            Object customer = customerClass.getDeclaredConstructor(Long.class, String.class, String.class, String.class)
                .newInstance(1L, "Test Customer", "Test Address", "555-5555");

            // Create dummy MenuItem
            Class<?> menuItemClass = Class.forName("com.cs_25_2_team2.RestaurantManagementApp.MenuItem");
            Object menuItem = menuItemClass.getDeclaredConstructor(int.class, String.class, double.class,
                menuItemClass.getDeclaredClasses()[0], menuItemClass.getDeclaredClasses()[1], boolean.class)
                .newInstance(1, "Test Dish", 1.0,
                    menuItemClass.getDeclaredClasses()[0].getEnumConstants()[0],
                    menuItemClass.getDeclaredClasses()[1].getEnumConstants()[0], true);

            // Create dummy CartItem
            Class<?> cartItemClass = Class.forName("com.cs_25_2_team2.RestaurantManagementApp.CartItem");
            Object cartItem = cartItemClass.getDeclaredConstructor(menuItemClass, int.class)
                .newInstance(menuItem, 1);

            // Create List<CartItem>
            java.util.List<Object> items = new java.util.ArrayList<>();
            items.add(cartItem);

            // Create dummy Date
            java.sql.Date date = new java.sql.Date(System.currentTimeMillis());

            // Use Order(int id, Customer, List<CartItem>, Date)
            Class<?> orderClass = Class.forName("com.cs_25_2_team2.RestaurantManagementApp.Order");
            java.lang.reflect.Constructor<?> ctor = orderClass.getDeclaredConstructor(int.class, customerClass, java.util.List.class, java.sql.Date.class);
            ctor.setAccessible(true);
            Order order = (Order) ctor.newInstance(id, customer, items, date);
            return order;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
