package com.cs_25_2_team2.RestaurantManagementApp;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTest {
    private Customer customer;
    private CartItem cartItem1;
    private CartItem cartItem2;
    private Order order;
    private Date orderDate;
    private List<CartItem> cartItems;

    @BeforeEach
    void setUp(){
        customer = new Customer
        (1, 
        "Trishtan", 
        "67 Your Moms St",
         "555-555-5555");

         cartItem1 = new CartItem
         (new MenuItem
         (1,
          "Fries",
           3.99, 
           MenuItem.CookedType.Fried,
           MenuItem.PotatoType.Russet, 
            true), 2);

        cartItem2 = new CartItem
        (new MenuItem(
            2,
            "Loaded Baked Potato",
            4.99,
            MenuItem.CookedType.Baked,
            MenuItem.PotatoType.YukonGold,
            true), 1
        );

        cartItems = new ArrayList<>();
        cartItems.add(cartItem1);
        cartItems.add(cartItem2);
        orderDate = new Date(System.currentTimeMillis());
        order = new Order(1, customer, cartItems, orderDate);
    }

    @Test
    @DisplayName("Test Order initialization")
    void testOrderInitialization(){
        assertNotNull(order, "Order should be initialized");
        assertEquals(customer, order.getCustomer(), "Order should have the correct Customer");
        assertEquals(cartItems.size(), order.getItems().size(), "Order should have the correct number of CartItems");
        assertEquals(Order.Status.Placed, order.getStatus(), "Order status should be 'Placed' upon initialization");
        assertEquals(orderDate, order.getCreatedAt(), "Order should have the correct order date");
    }

     @Test
    @DisplayName("Test total price calculation")
    void testTotalPriceCalculation() {
        double expectedTotal = cartItem1.getSubtotal() + cartItem2.getSubtotal();
        assertEquals(expectedTotal, order.getTotalPrice(), 0.01, "Order total price should match sum of item subtotals");
    }

     @Test
    @DisplayName("Test status update")
    void testStatusUpdate() {
        order.updateStatus(Order.Status.Preparing);
        assertEquals(Order.Status.Preparing, order.getStatus(), "Order status should be updated to 'Preparing'");

        order.updateStatus(Order.Status.Delivered);
        assertEquals(Order.Status.Delivered, order.getStatus(), "Order status should be updated to 'Delivered'");
    }
}
