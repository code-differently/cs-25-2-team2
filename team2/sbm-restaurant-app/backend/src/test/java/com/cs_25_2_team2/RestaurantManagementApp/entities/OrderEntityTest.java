package com.cs_25_2_team2.RestaurantManagementApp.entities;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderEntityTest {
    @Test
    void testGettersAndSetters() {
        OrderEntity order = new OrderEntity();

        Long id = 123L;
        order.setOrderId(id);
        assertEquals(id, order.getOrderId());

        CustomerEntity customer = new CustomerEntity();
        order.setCustomer(customer);
        assertEquals(customer, order.getCustomer());

        Long restaurantId = 456L;
        order.setRestaurantId(restaurantId);
        assertEquals(restaurantId, order.getRestaurantId());

        BigDecimal price = new BigDecimal("19.99");
        order.setTotalPrice(price);
        assertEquals(price, order.getTotalPrice());

        OrderEntity.OrderStatus status = OrderEntity.OrderStatus.Delivered;
        order.setStatus(status);
        assertEquals(status, order.getStatus());

        StaffEntity chef = new StaffEntity();
        order.setAssignedChef(chef);
        assertEquals(chef, order.getAssignedChef());

        StaffEntity delivery = new StaffEntity();
        order.setAssignedDelivery(delivery);
        assertEquals(delivery, order.getAssignedDelivery());

        String lastFour = "1234";
        order.setCreditCardLastFour(lastFour);
        assertEquals(lastFour, order.getCreditCardLastFour());

        String token = "tok_abc";
        order.setCreditCardToken(token);
        assertEquals(token, order.getCreditCardToken());

        Integer expMonth = 12;
        order.setCardExpiryMonth(expMonth);
        assertEquals(expMonth, order.getCardExpiryMonth());

        Integer expYear = 2025;
        order.setCardExpiryYear(expYear);
        assertEquals(expYear, order.getCardExpiryYear());

        String cardholder = "John Doe";
        order.setCardholderName(cardholder);
        assertEquals(cardholder, order.getCardholderName());

        LocalDateTime created = LocalDateTime.now();
        order.setCreatedAt(created);
        assertEquals(created, order.getCreatedAt());

        LocalDateTime updated = LocalDateTime.now();
        order.setUpdatedAt(updated);
        assertEquals(updated, order.getUpdatedAt());

        List<OrderItemEntity> items = new ArrayList<>();
        order.setOrderItems(items);
        assertEquals(items, order.getOrderItems());

        OrderQueueEntity queue = new OrderQueueEntity();
        order.setOrderQueue(queue);
        assertEquals(queue, order.getOrderQueue());
    }

    @Test
    void testConstructorWithFields() {
        CustomerEntity customer = new CustomerEntity();
        BigDecimal price = new BigDecimal("29.99");
        OrderEntity order = new OrderEntity(customer, price);
        assertEquals(customer, order.getCustomer());
        assertEquals(price, order.getTotalPrice());
    }
}
