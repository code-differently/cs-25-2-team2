package com.cs_25_2_team2.RestaurantManagementApp.exceptions;

/**
 * Exception thrown when attempting to access or modify an order that does not exist.
 */
public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(int orderId) {
        super("Order #" + orderId + " not found in the system.");
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}