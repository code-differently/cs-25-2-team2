package com.cs_25_2_team2.RestaurantManagementApp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.cs_25_2_team2.RestaurantManagementApp.exceptions.OrderNotFoundException;

/**
 * Custom data structure for managing orders in a restaurant kitchen.
 * Implements FIFO (First In, First Out) principle with priority handling.
 * This is a custom collection that provides adding, removing, updating items,
 * and custom iteration functionality.
 */
public class OrderQueue implements Iterable<Order> {
    private final List<OrderNode> queue;
    private int size;

    /**
     * Internal node class to store orders with priority information.
     */
    private static class OrderNode {
        Order order;
        int priority; // Lower number = higher priority (1 = highest, 5 = lowest)
        long addedTime; // For FIFO within same priority

        OrderNode(Order order, int priority) {
            this.order = order;
            this.priority = priority;
            this.addedTime = System.currentTimeMillis();
        }
    }

    /**
     * Creates a new empty OrderQueue.
     */
    public OrderQueue() {
        this.queue = new ArrayList<>();
        this.size = 0;
    }

    /**
     * Adds an order to the queue with default priority (3 - normal).
     * @param order The order to add
     * @throws IllegalArgumentException if order is null
     */
    public void add(Order order) {
        add(order, 3); // Default priority
    }

    /**
     * Adds an order to the queue with specified priority.
     * Orders are sorted by priority first, then by arrival time (FIFO within priority).
     * 
     * @param order The order to add
     * @param priority Priority level (1=highest, 5=lowest)
     * @throws IllegalArgumentException if order is null or priority is invalid
     */
    public void add(Order order, int priority) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (priority < 1 || priority > 5) {
            throw new IllegalArgumentException("Priority must be between 1 (highest) and 5 (lowest)");
        }

        OrderNode newNode = new OrderNode(order, priority);
        
        // Find the correct position to insert (priority first, then FIFO)
        int insertPosition = findInsertPosition(priority, newNode.addedTime);
        queue.add(insertPosition, newNode);
        size++;
    }

    /**
     * Finds the correct position to insert a new order based on priority and time.
     */
    private int findInsertPosition(int priority, long addedTime) {
        for (int i = 0; i < queue.size(); i++) {
            OrderNode current = queue.get(i);
            // If current has lower priority (higher number), insert here
            if (current.priority > priority) {
                return i;
            }
            // If same priority, check time for FIFO
            if (current.priority == priority && current.addedTime > addedTime) {
                return i;
            }
        }
        return queue.size(); // Insert at end
    }

    /**
     * Removes and returns the highest priority order (FIFO within priority).
     * @return The next order to process
     * @throws NoSuchElementException if the queue is empty
     */
    public Order remove() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        
        OrderNode node = queue.remove(0); // Always remove from front (highest priority)
        size--;
        return node.order;
    }

    /**
     * Removes a specific order by ID.
     * @param orderId The ID of the order to remove
     * @return The removed order
     * @throws OrderNotFoundException if the order is not in the queue
     */
    public Order remove(int orderId) {
        for (int i = 0; i < queue.size(); i++) {
            if (queue.get(i).order.getId() == orderId) {
                OrderNode node = queue.remove(i);
                size--;
                return node.order;
            }
        }
        throw new OrderNotFoundException(orderId);
    }

    /**
     * Peeks at the next order without removing it.
     * @return The next order to be processed, or null if queue is empty
     */
    public Order peek() {
        if (isEmpty()) {
            return null;
        }
        return queue.get(0).order;
    }

    /**
     * Updates the priority of an order in the queue.
     * @param orderId The ID of the order to update
     * @param newPriority The new priority level
     * @throws OrderNotFoundException if the order is not in the queue
     * @throws IllegalArgumentException if priority is invalid
     */
    public void updatePriority(int orderId, int newPriority) {
        if (newPriority < 1 || newPriority > 5) {
            throw new IllegalArgumentException("Priority must be between 1 (highest) and 5 (lowest)");
        }

        // Find and remove the order
        Order order = remove(orderId);
        
        // Re-add with new priority
        add(order, newPriority);
    }

    /**
     * Updates an order's information (re-sorts if needed).
     * @param orderId The ID of the order to update
     * @param updatedOrder The updated order object
     * @throws OrderNotFoundException if the order is not in the queue
     */
    public void update(int orderId, Order updatedOrder) {
        if (updatedOrder == null) {
            throw new IllegalArgumentException("Updated order cannot be null");
        }

        // Find the order and preserve its priority
        int priority = 3; // default
        for (OrderNode node : queue) {
            if (node.order.getId() == orderId) {
                priority = node.priority;
                break;
            }
        }

        // Remove old order and add updated one
        remove(orderId);
        add(updatedOrder, priority);
    }

    /**
     * Checks if the queue contains an order with the given ID.
     * @param orderId The ID to search for
     * @return True if the order exists in the queue
     */
    public boolean contains(int orderId) {
        return queue.stream().anyMatch(node -> node.order.getId() == orderId);
    }

    /**
     * Gets an order by ID without removing it.
     * @param orderId The ID of the order to find
     * @return The order if found
     * @throws OrderNotFoundException if the order is not in the queue
     */
    public Order get(int orderId) {
        return queue.stream()
            .filter(node -> node.order.getId() == orderId)
            .findFirst()
            .map(node -> node.order)
            .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    /**
     * Gets all orders in the queue as a list (in priority order).
     * @return A copy of all orders in the queue
     */
    public List<Order> getAll() {
        return queue.stream()
            .map(node -> node.order)
            .toList();
    }

    /**
     * Gets orders by priority level.
     * @param priority The priority level to filter by
     * @return List of orders with the specified priority
     */
    public List<Order> getByPriority(int priority) {
        return queue.stream()
            .filter(node -> node.priority == priority)
            .map(node -> node.order)
            .toList();
    }

    /**
     * Clears all orders from the queue.
     */
    public void clear() {
        queue.clear();
        size = 0;
    }

    /**
     * Checks if the queue is empty.
     * @return True if the queue has no orders
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Gets the number of orders in the queue.
     * @return The size of the queue
     */
    public int size() {
        return size;
    }

    /**
     * Returns an iterator over the orders in the queue.
     * Iteration is in priority order (highest priority first, FIFO within priority).
     */
    @Override
    public Iterator<Order> iterator() {
        return new OrderQueueIterator();
    }

    /**
     * Custom iterator for the OrderQueue.
     */
    private class OrderQueueIterator implements Iterator<Order> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < queue.size();
        }

        @Override
        public Order next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return queue.get(currentIndex++).order;
        }

        @Override
        public void remove() {
            if (currentIndex <= 0) {
                throw new IllegalStateException("Cannot remove before calling next()");
            }
            queue.remove(--currentIndex);
            size--;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("OrderQueue [");
        for (int i = 0; i < queue.size(); i++) {
            OrderNode node = queue.get(i);
            if (i > 0) sb.append(", ");
            sb.append(String.format("Order#%d(P%d)", node.order.getId(), node.priority));
        }
        sb.append("]");
        return sb.toString();
    }
}