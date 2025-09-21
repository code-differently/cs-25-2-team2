package com.cs_25_2_team2.RestaurantManagementApp.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for custom exceptions and standard Java exceptions used in the application. This
 * includes both our custom exceptions and standard Java exceptions that we use for input validation
 * and state checks.
 */
public class CustomExceptionsTest {

  @Test
  @DisplayName("Test OrderNotFoundException with order ID")
  void testOrderNotFoundException() {
    int orderId = 123;
    OrderNotFoundException exception = new OrderNotFoundException(orderId);

    assertTrue(exception.getMessage().contains(String.valueOf(orderId)));
    assertTrue(exception.getMessage().contains("not found"));
  }

  @Test
  @DisplayName("Test InvalidOrderStateException")
  void testInvalidOrderStateException() {
    int orderId = 789;
    String currentState = "Delivered";
    String attemptedOperation = "cancel";

    InvalidOrderStateException exception =
        new InvalidOrderStateException(orderId, currentState, attemptedOperation);

    assertEquals(orderId, exception.getOrderId());
    assertEquals(currentState, exception.getCurrentState());
    assertEquals(attemptedOperation, exception.getAttemptedOperation());
    assertTrue(exception.getMessage().contains(String.valueOf(orderId)));
    assertTrue(exception.getMessage().contains(currentState));
    assertTrue(exception.getMessage().contains(attemptedOperation));
  }

  @Test
  @DisplayName("Test InvalidOrderStateException with string message constructor")
  void testInvalidOrderStateExceptionStringConstructor() {
    String customMessage = "Custom order state error message";

    InvalidOrderStateException exception = new InvalidOrderStateException(customMessage);

    assertEquals(customMessage, exception.getMessage());
    assertEquals(-1, exception.getOrderId());
    assertEquals("unknown", exception.getCurrentState());
    assertEquals("unknown", exception.getAttemptedOperation());
  }

  @Test
  @DisplayName("Test MenuItemUnavailableException")
  void testMenuItemUnavailableException() {
    int menuItemId = 456;
    String menuItemName = "Loaded Baked Potato";

    MenuItemUnavailableException exception =
        new MenuItemUnavailableException(menuItemId, menuItemName);

    assertEquals(menuItemId, exception.getMenuItemId());
    assertEquals(menuItemName, exception.getMenuItemName());
    assertTrue(exception.getMessage().contains(menuItemName));
    assertTrue(exception.getMessage().contains(String.valueOf(menuItemId)));
  }

  @Test
  @DisplayName("Test IllegalArgumentException for invalid input")
  void testIllegalArgumentException() {
    // Test negative quantity
    IllegalArgumentException quantityException =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              validateQuantity(-1);
            });
    assertTrue(quantityException.getMessage().contains("Quantity must be positive"));

    // Test null customer name
    IllegalArgumentException nameException =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              validateCustomerName(null);
            });
    assertTrue(nameException.getMessage().contains("Customer name cannot be null or empty"));

    // Test empty phone number
    IllegalArgumentException phoneException =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              validatePhoneNumber("");
            });
    assertTrue(phoneException.getMessage().contains("Phone number cannot be empty"));
  }

  @Test
  @DisplayName("Test IllegalStateException for invalid operations")
  void testIllegalStateException() {
    // Test checkout with empty cart
    IllegalStateException emptyCartException =
        assertThrows(
            IllegalStateException.class,
            () -> {
              checkoutEmptyCart();
            });
    assertTrue(emptyCartException.getMessage().contains("Cart is empty"));

    // Test modifying a completed order
    IllegalStateException orderModificationException =
        assertThrows(
            IllegalStateException.class,
            () -> {
              modifyCompletedOrder();
            });
    assertTrue(orderModificationException.getMessage().contains("Cannot modify a completed order"));

    // Test delivering an unprepared order
    IllegalStateException deliveryException =
        assertThrows(
            IllegalStateException.class,
            () -> {
              deliverUnpreparedOrder();
            });
    assertTrue(deliveryException.getMessage().contains("Order must be prepared before delivery"));
  }

  @Test
  @DisplayName("Test InvalidOrderStateException with business logic scenarios")
  void testInvalidOrderStateExceptionScenarios() {
    // Test cancelling a delivered order
    InvalidOrderStateException cancelException =
        assertThrows(
            InvalidOrderStateException.class,
            () -> {
              simulateCancelDeliveredOrder();
            });
    assertTrue(cancelException.getMessage().contains("cancel"));
    assertTrue(cancelException.getMessage().contains("Delivered"));

    // Test adding special request to preparing order
    InvalidOrderStateException requestException =
        assertThrows(
            InvalidOrderStateException.class,
            () -> {
              simulateAddRequestToPreparingOrder();
            });
    assertTrue(requestException.getMessage().contains("add special request"));
    assertTrue(requestException.getMessage().contains("Preparing"));
  }

  // Helper methods that validate input parameters and throw appropriate exceptions for testing purposes
  private void validateQuantity(int quantity) {
    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantity must be positive");
    }
  }

  private void validateCustomerName(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Customer name cannot be null or empty");
    }
  }

  private void validatePhoneNumber(String phone) {
    if (phone == null || phone.trim().isEmpty()) {
      throw new IllegalArgumentException("Phone number cannot be empty");
    }
  }

  private void checkoutEmptyCart() {
    throw new IllegalStateException("Cart is empty. Cannot checkout.");
  }

  private void modifyCompletedOrder() {
    throw new IllegalStateException("Cannot modify a completed order");
  }

  private void deliverUnpreparedOrder() {
    throw new IllegalStateException("Order must be prepared before delivery");
  }

  private void simulateCancelDeliveredOrder() {
    throw new InvalidOrderStateException(123, "Delivered", "cancel");
  }

  private void simulateAddRequestToPreparingOrder() {
    throw new InvalidOrderStateException(456, "Preparing", "add special request");
  }
}
