package com.cs_25_2_team2.RestaurantManagementApp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cs_25_2_team2.RestaurantManagementApp.exceptions.InvalidOrderStateException;
import com.cs_25_2_team2.RestaurantManagementApp.exceptions.OrderNotFoundException;

/**
 * Delivery staff class that handles order pickup and delivery operations. Extends Staff and manages
 * delivery workflow.
 */
public class Delivery extends Staff {
  @Override
  public String getFormattedId() {
    // Always D + zero-padded 3 digits (e.g., D001)
    return String.format("D%03d", id);
  }
  private static final Set<Integer> globallyAssignedOrders =
      new HashSet<>(); // Track orders assigned globally

  public Delivery(String name, String address, String phoneNumber, Long id) {
    super(name, address, phoneNumber, id, "Delivery");
  }

  /**
   * Assigns an order to this delivery staff member.
   *
   * @param order The order to assign
   * @throws IllegalArgumentException if order is null
   * @throws InvalidOrderStateException if order is not ready for delivery or already assigned
   */
  public void assignOrder(Order order) {
    if (order == null) {
      throw new IllegalArgumentException("Order cannot be null");
    }
    if (order.getStatus() != Order.Status.ReadyForDelivery) {
      throw new InvalidOrderStateException(
          order.getId(), order.getStatus().toString(), "assign for delivery");
    }
    if (globallyAssignedOrders.contains(order.getId())) {
      throw new InvalidOrderStateException(
          order.getId(), "already assigned", "assign to another delivery staff");
    }

    assignedOrders.add(order);
    globallyAssignedOrders.add(order.getId());
    System.out.println("Delivery staff " + getName() + " assigned order #" + order.getId());
  }

  /**
   * Picks up an order and marks it as out for delivery.
   *
   * @param orderId The ID of the order to pick up
   * @throws OrderNotFoundException if the order is not assigned to this delivery staff
   * @throws InvalidOrderStateException if the order is not ready for pickup
   */
  public void pickupOrder(int orderId) {
    Order order = findAssignedOrder(orderId);
    if (order.getStatus() != Order.Status.ReadyForDelivery) {
      throw new InvalidOrderStateException(orderId, order.getStatus().toString(), "pickup");
    }

    order.updateStatus(Order.Status.OutForDelivery);
    System.out.println("Delivery staff " + getName() + " picked up order #" + orderId);
  }

  /**
   * Delivers an order to the customer.
   *
   * @param orderId The ID of the order to deliver
   * @throws OrderNotFoundException if the order is not assigned to this delivery staff
   * @throws InvalidOrderStateException if the order is not out for delivery
   */
  public void deliverOrder(int orderId) {
    Order order = findAssignedOrder(orderId);
    if (order.getStatus() != Order.Status.OutForDelivery) {
      throw new InvalidOrderStateException(orderId, order.getStatus().toString(), "deliver");
    }

    order.updateStatus(Order.Status.Delivered);
    assignedOrders.remove(order);
    completedOrders.add(order);
    globallyAssignedOrders.remove(orderId); // Remove from global tracking
    System.out.println(
        "Delivery staff "
            + getName()
            + " delivered order #"
            + orderId
            + " to "
            + order.getCustomer().getName());
  }

  /**
   * Gets all orders assigned to this delivery staff.
   *
   * @return List of assigned orders
   */
  @Override
  public List<Order> getAssignedOrders() {
    return new ArrayList<>(assignedOrders);
  }

  /**
   * Gets all orders delivered by this delivery staff.
   *
   * @return List of delivered orders
   */
  public List<Order> getDeliveredOrders() {
    return new ArrayList<>(completedOrders);
  }

  /**
   * Gets orders currently out for delivery.
   *
   * @return List of orders out for delivery
   */
  public List<Order> getOrdersOutForDelivery() {
    return getOrdersByStatus(Order.Status.OutForDelivery);
  }

  /**
   * Gets orders ready for pickup.
   *
   * @return List of orders ready for pickup
   */
  public List<Order> getOrdersReadyForPickup() {
    return getOrdersByStatus(Order.Status.ReadyForDelivery);
  }

  /**
   * Gets the total number of orders delivered by this delivery staff.
   *
   * @return The number of delivered orders
   */
  public int getDeliveredOrderCount() {
    return completedOrders.size();
  }

  /**
   * Checks if the delivery staff is currently busy (has orders out for delivery).
   *
   * @return True if busy, false otherwise
   */
  public boolean isBusy() {
    return assignedOrders.stream()
        .anyMatch(order -> order.getStatus() == Order.Status.OutForDelivery);
  }

  @Override
  public String toString() {
  return String.format(
    "Delivery{name='%s', id='%s', assignedOrders=%d, deliveredOrders=%d}",
    getName(), getFormattedId(), assignedOrders.size(), completedOrders.size());
  }
}
