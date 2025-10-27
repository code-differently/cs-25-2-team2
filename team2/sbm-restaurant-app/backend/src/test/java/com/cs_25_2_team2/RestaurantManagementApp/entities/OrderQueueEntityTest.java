package com.cs_25_2_team2.RestaurantManagementApp.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderQueueEntityTest {
    @Test
    void testGettersAndSetters() {
        OrderQueueEntity queue = new OrderQueueEntity();
        queue.setQueueId(1L);
        OrderEntity order = new OrderEntity();
        queue.setOrder(order);
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        queue.setAddedToQueueAt(now);
        queue.setStartedPreparingAt(now);
        queue.setReadyForDeliveryAt(now);
        queue.setOutForDeliveryAt(now);
        queue.setDeliveredAt(now);
        assertEquals(1L, queue.getQueueId());
        assertEquals(order, queue.getOrder());
        assertEquals(now, queue.getAddedToQueueAt());
        assertEquals(now, queue.getStartedPreparingAt());
        assertEquals(now, queue.getReadyForDeliveryAt());
        assertEquals(now, queue.getOutForDeliveryAt());
        assertEquals(now, queue.getDeliveredAt());
    }
}
