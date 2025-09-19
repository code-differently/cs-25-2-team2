package com.cs_25_2_team2.RestaurantManagementApp.exceptions;

/**
 * Exception thrown when an operation violates order state business rules.
 * This is different from IllegalStateException as it represents domain-specific 
 * business logic violations related to order processing workflow.
 */
public class InvalidOrderStateException extends RuntimeException {
    private final int orderId;
    private final String currentState;
    private final String attemptedOperation;

    public InvalidOrderStateException(int orderId, String currentState, String attemptedOperation) {
        super(String.format("Cannot perform '%s' operation on Order #%d in state '%s'", 
            attemptedOperation, orderId, currentState));
        this.orderId = orderId;
        this.currentState = currentState;
        this.attemptedOperation = attemptedOperation;
    }

    public InvalidOrderStateException(String message) {
        super(message);
        this.orderId = -1;
        this.currentState = "unknown";
        this.attemptedOperation = "unknown";
    }

    public int getOrderId() {
        return orderId;
    }

    public String getCurrentState() {
        return currentState;
    }

    public String getAttemptedOperation() {
        return attemptedOperation;
    }
}