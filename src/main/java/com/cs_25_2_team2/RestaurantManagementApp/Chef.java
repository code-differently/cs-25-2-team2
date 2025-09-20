package com.cs_25_2_team2.RestaurantManagementApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs_25_2_team2.RestaurantManagementApp.exceptions.InvalidOrderStateException;
import com.cs_25_2_team2.RestaurantManagementApp.exceptions.OrderNotFoundException;

/**
 * Chef class that manages kitchen operations and order processing. Extends Staff and handles all
 * order management responsibilities.
 */
public class Chef extends Staff {
  private final Map<Integer, Order> orders;
  private final Map<Integer, Order> activeOrders; // Orders currently being prepared

  public Chef(String name, String address, String phoneNumber, String id) {
    super(name, address, phoneNumber, id, "Chef");
    this.orders = new HashMap<>();
    this.activeOrders = new HashMap<>();
  }

  // ========== ORDER MANAGEMENT METHODS ==========

  /**
   * Receives a new order from the customer checkout process.
   *
   * @param order The order to add to the kitchen queue
   * @throws IllegalArgumentException if order is null
   */
  public void receiveOrder(Order order) {
    if (order == null) {
      throw new IllegalArgumentException("Order cannot be null");
    }
    orders.put(order.getId(), order);
    System.out.println("Chef " + getName() + " received new order #" + order.getId());
  }

  /**
   * Retrieves an order by ID.
   *
   * @param orderId The ID of the order to retrieve
   * @return The order with the specified ID
   * @throws OrderNotFoundException if no order exists with the given ID
   */
  public Order getOrder(int orderId) {
    Order order = orders.get(orderId);
    if (order == null) {
      throw new OrderNotFoundException(orderId);
    }
    return order;
  }

  /**
   * Starts preparing an order.
   *
   * @param orderId The ID of the order to start preparing
   * @throws OrderNotFoundException if no order exists with the given ID
   * @throws IllegalStateException if the order is not in Placed status
   */
  public void startPreparingOrder(int orderId) {
    Order order = getOrder(orderId);
    if (order.getStatus() != Order.Status.Placed) {
      throw new IllegalStateException("Order must be in Placed status to start preparing");
    }

    order.updateStatus(Order.Status.Preparing);
    activeOrders.put(orderId, order);
    System.out.println("Chef " + getName() + " started preparing order #" + orderId);
  }

  /**
   * Completes preparation of an order and marks it ready for delivery.
   *
   * @param orderId The ID of the order to complete
   * @throws OrderNotFoundException if no order exists with the given ID
   * @throws IllegalStateException if the order is not being prepared
   */
  public void completeOrder(int orderId) {
    Order order = getOrder(orderId);
    if (order.getStatus() != Order.Status.Preparing) {
      throw new IllegalStateException("Order must be in Preparing status to complete");
    }

    order.updateStatus(Order.Status.ReadyForDelivery);
    activeOrders.remove(orderId);
    System.out.println(
        "Chef " + getName() + " completed order #" + orderId + " - Ready for delivery!");
  }

  /**
   * Updates the status of an order.
   *
   * @param orderId The ID of the order to update
   * @param newStatus The new status
   * @throws OrderNotFoundException if no order exists with the given ID
   * @throws IllegalStateException if the status transition is invalid
   */
  public void updateOrderStatus(int orderId, Order.Status newStatus) {
    Order order = getOrder(orderId);
    order.updateStatus(newStatus);
    System.out.println(
        "Chef " + getName() + " updated order #" + orderId + " status to " + newStatus);
  }

  /**
   * Cancels an order if it hasn't started being prepared yet.
   *
   * @param orderId The ID of the order to cancel
   * @throws OrderNotFoundException if no order exists with the given ID
   * @throws InvalidOrderStateException if the order cannot be cancelled in its current state
   */
  public void cancelOrder(int orderId) {
    Order order = getOrder(orderId);
    if (order.getStatus() != Order.Status.Placed) {
      throw new InvalidOrderStateException(orderId, order.getStatus().toString(), "cancel");
    }

    order.cancelOrder();
    orders.remove(orderId);
    System.out.println("Chef " + getName() + " cancelled order #" + orderId);
  }

  /**
   * Adds a special request to an order if it hasn't started being prepared.
   *
   * @param orderId The ID of the order
   * @param request The special request to add
   * @throws OrderNotFoundException if no order exists with the given ID
   * @throws InvalidOrderStateException if the order is already being prepared
   */
  public void addSpecialRequest(int orderId, String request) {
    Order order = getOrder(orderId);
    order.addSpecialRequest(request);
    System.out.println(
        "Chef " + getName() + " added special request to order #" + orderId + ": " + request);
  }

  // ========== KITCHEN OPERATION METHODS ==========

  /**
   * Gets all orders in the kitchen queue.
   *
   * @return A list of all orders
   */
  public List<Order> getAllOrders() {
    return new ArrayList<>(orders.values());
  }

  /**
   * Gets all orders currently being prepared.
   *
   * @return A list of orders being actively prepared
   */
  public List<Order> getActiveOrders() {
    return new ArrayList<>(activeOrders.values());
  }

  /**
   * Gets all orders waiting to be prepared (Placed status).
   *
   * @return A list of orders waiting in queue
   */
  public List<Order> getPendingOrders() {
    return orders.values().stream()
        .filter(order -> order.getStatus() == Order.Status.Placed)
        .toList();
  }

  /**
   * Gets all completed orders ready for delivery.
   *
   * @return A list of orders ready for delivery
   */
  public List<Order> getReadyOrders() {
    return orders.values().stream()
        .filter(order -> order.getStatus() == Order.Status.ReadyForDelivery)
        .toList();
  }

  /**
   * Gets the total number of orders managed by this chef.
   *
   * @return The total number of orders
   */
  public int getOrderCount() {
    return orders.size();
  }

  /**
   * Gets the number of orders currently being prepared.
   *
   * @return The number of active orders
   */
  public int getActiveOrderCount() {
    return activeOrders.size();
  }

  /**
   * Checks if the chef is currently busy (has active orders).
   *
   * @return True if the chef is preparing orders, false otherwise
   */
  public boolean isBusy() {
    return !activeOrders.isEmpty();
  }

  /**
   * Sends a completed order to delivery staff.
   *
   * @param orderId The ID of the order to send for delivery
   * @param deliveryStaff The delivery staff member to assign
   * @throws OrderNotFoundException if no order exists with the given ID
   * @throws IllegalStateException if the order is not ready for delivery
   */
  public void sendToDelivery(int orderId, Delivery deliveryStaff) {
    Order order = getOrder(orderId);
    if (order.getStatus() != Order.Status.ReadyForDelivery) {
      throw new IllegalStateException("Order must be ready for delivery to send to delivery staff");
    }

    if (deliveryStaff == null) {
      throw new IllegalArgumentException("Delivery staff cannot be null");
    }

    // Assign order to delivery staff and update status
    deliveryStaff.assignOrder(order);
    order.updateStatus(Order.Status.OutForDelivery);
    System.out.println(
        "Chef "
            + getName()
            + " sent order #"
            + orderId
            + " to delivery staff "
            + deliveryStaff.getName());
  }

  @Override
  public String toString() {
    return String.format(
        "Chef{name='%s', id='%s', orders=%d, activeOrders=%d}",
        getName(), getId(), orders.size(), activeOrders.size());
  }
}
