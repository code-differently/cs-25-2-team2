package com.cs_25_2_team2.RestaurantManagementApp;

import java.sql.Date;
import java.util.List;

import com.cs_25_2_team2.RestaurantManagementApp.exceptions.InvalidOrderStateException;

public class Order {
  private static int nextId = 1; // Static counter for generating unique IDs
  private final int id; // Order ID
  private final Customer customer; // Customer who placed the order
  private final List<CartItem> items; // Items in the order

  private final double totalPrice; // Total price of the order
  private final Date createdAt; // Timestamp when the order was created
  private Status status; // Order status

  public enum Status {
    Placed,
    Preparing,
    ReadyForDelivery,
    OutForDelivery,
    Delivered
  }

  /** Constructor for creating a new order with auto-generated ID. */
  public Order(Customer customer, List<CartItem> items, Date createdAt) {
    if (customer == null) {
      throw new IllegalArgumentException("Customer cannot be null");
    }
    if (items == null || items.isEmpty()) {
      throw new IllegalArgumentException("Order must contain at least one item");
    }
    if (createdAt == null) {
      throw new IllegalArgumentException("Created date cannot be null");
    }

    this.id = nextId++;
    this.customer = customer;
    this.items = List.copyOf(items); // Create immutable copy of the items list
    this.status = Status.Placed;
    this.totalPrice = items.stream().mapToDouble(CartItem::getSubtotal).sum();
    this.createdAt = createdAt;
  }

  /** Constructor for creating an order with a specific ID (used for updates). */
  public Order(int id, Customer customer, List<CartItem> items, Date createdAt) {
    if (customer == null) {
      throw new IllegalArgumentException("Customer cannot be null");
    }
    if (items == null || items.isEmpty()) {
      throw new IllegalArgumentException("Order must contain at least one item");
    }
    if (createdAt == null) {
      throw new IllegalArgumentException("Created date cannot be null");
    }

    this.id = id; // Use provided ID instead of generating new one
    this.customer = customer;
    this.items = List.copyOf(items); // Create immutable copy of the items list
    this.status = Status.Placed;
    this.totalPrice = items.stream().mapToDouble(CartItem::getSubtotal).sum();
    this.createdAt = createdAt;
  }

  public int getId() {
    return id;
  }

  public Customer getCustomer() {
    return customer;
  }

  public List<CartItem> getItems() {
    return items;
  }

  public double getTotalPrice() {
    return totalPrice;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public Status getStatus() {
    return status;
  }

  // Order status updates
  public void updateStatus(Status newStatus) {
    if (newStatus == null) {
      throw new IllegalArgumentException("Status cannot be null");
    }

    // Validate state transitions
    validateStatusTransition(newStatus);

    this.status = newStatus;
    notifyStatusChange();
    logStatusChange();
  }

  /**
   * Validates that the status transition is allowed.
   *
   * @param newStatus The new status to transition to
   * @throws IllegalStateException if the transition is not allowed
   */
  private void validateStatusTransition(Status newStatus) {
    switch (status) {
      case Delivered -> {
        throw new IllegalStateException("Cannot change status of a delivered order");
      }
      case Placed -> {
        if (newStatus != Status.Preparing) {
          throw new IllegalStateException("Order can only move from Placed to Preparing");
        }
      }
      case Preparing -> {
        if (newStatus != Status.ReadyForDelivery) {
          throw new IllegalStateException("Order can only move from Preparing to ReadyForDelivery");
        }
      }
      case ReadyForDelivery -> {
        if (newStatus != Status.OutForDelivery) {
          throw new IllegalStateException(
              "Order can only move from ReadyForDelivery to OutForDelivery");
        }
      }
      case OutForDelivery -> {
        if (newStatus != Status.Delivered) {
          throw new IllegalStateException("Order can only move from OutForDelivery to Delivered");
        }
      }
    }
  }

  private void notifyStatusChange() {
    switch (status) {
      case Placed -> notifyChefAndCustomer();
      case Preparing -> notifyCustomer("Your order is being prepared");
      case ReadyForDelivery -> notifyCustomerAndDeliveryStaff();
      case OutForDelivery -> notifyCustomer("Your order is on the way");
      case Delivered -> notifyCustomerAndArchiveOrder();
      default -> {
        // No notification for unknown status
      }
    }
  }

  private void notifyChefAndCustomer() {
    System.out.println("Notifying chef about new order #" + id);
    System.out.println("Notifying customer: Your order #" + id + " has been placed");
  }

  private void notifyCustomer(String message) {
    System.out.println("Notifying customer: " + message + " (Order #" + id + ")");
  }

  private void notifyCustomerAndDeliveryStaff() {
    System.out.println("Notifying customer: Your order #" + id + " is ready for delivery");
    System.out.println("Notifying delivery staff about order #" + id);
  }

  private void notifyCustomerAndArchiveOrder() {
    System.out.println("Notifying customer: Your order #" + id + " has been delivered");
    System.out.println("Archiving order #" + id);
  }

  private void logStatusChange() {
    System.out.println("Order #" + id + " status changed to " + status);
  }

  /**
   * Cancels the order if it's in a cancellable state.
   *
   * @throws InvalidOrderStateException if the order cannot be cancelled in its current state
   */
  public void cancelOrder() {
    if (status == Status.OutForDelivery || status == Status.Delivered) {
      throw new InvalidOrderStateException(id, status.toString(), "cancel");
    }
    this.status = Status.Delivered; // Using Delivered as "cancelled" for simplicity
    System.out.println("Order #" + id + " has been cancelled");
  }

  /**
   * Adds a special request to the order.
   *
   * @param request The special request to add
   * @throws InvalidOrderStateException if the order is already being prepared
   */
  public void addSpecialRequest(String request) {
    if (status != Status.Placed) {
      throw new InvalidOrderStateException(id, status.toString(), "add special request");
    }
    System.out.println("Special request added to Order #" + id + ": " + request);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Order #" + id + " for " + customer.getName() + "\n");
    for (CartItem ci : items) {
      sb.append(" - ").append(ci).append("\n");
    }
    sb.append("Total: $").append(totalPrice).append("\n");
    sb.append("Status: ").append(status).append("\n");
    sb.append("Created: ").append(createdAt);
    return sb.toString();
  }
}
