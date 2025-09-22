package com.cs_25_2_team2.RestaurantManagementApp;

import java.util.ArrayList;
import java.util.List;

import com.cs_25_2_team2.RestaurantManagementApp.exceptions.OrderNotFoundException;

import java.util.ArrayList;
import java.util.List;

import com.cs_25_2_team2.RestaurantManagementApp.exceptions.OrderNotFoundException;

public abstract class Staff extends Person {
  protected String id;
  protected String role;
  protected final List<Order> assignedOrders;
  protected final List<Order> completedOrders;
  protected String role;
  protected final List<Order> assignedOrders;
  protected final List<Order> completedOrders;

  public Staff(String name, String address, String phoneNumber, String id, String role) {
    super(name, address, phoneNumber);
  public Staff(String name, String address, String phoneNumber, String id, String role) {
    super(name, address, phoneNumber);
    this.id = id;
    this.role = role;
    this.assignedOrders = new ArrayList<>();
    this.completedOrders = new ArrayList<>();
    this.role = role;
    this.assignedOrders = new ArrayList<>();
    this.completedOrders = new ArrayList<>();
  }

  public String getId() {
    return id;
  }

  public String getRole() {
    return role;
  }

  /**
   * Abstract method for assigning orders - each staff type implements differently.
   *
   * @param order The order to assign
   */
  public abstract void assignOrder(Order order);

  /**
   * Abstract method to check if staff is busy - each staff type defines differently.
   *
   * @return True if busy, false otherwise
   */
  public abstract boolean isBusy();

  /**
   * Gets all orders assigned to this staff member.
   *
   * @return List of assigned orders
   */
  public List<Order> getAssignedOrders() {
    return new ArrayList<>(assignedOrders);
  }

  /**
   * Gets the total number of orders assigned to this staff member.
   *
   * @return The number of assigned orders
   */
  public int getAssignedOrderCount() {
    return assignedOrders.size();
  }

  /**
   * Gets the total number of orders completed by this staff member.
   *
   * @return The number of completed orders
   */
  public int getCompletedOrderCount() {
    return completedOrders.size();
  }

  /**
   * Finds an assigned order by ID.
   *
   * @param orderId The ID of the order to find
   * @return The order if found
   * @throws OrderNotFoundException if the order is not assigned to this staff member
   */
  protected Order findAssignedOrder(int orderId) {
    return assignedOrders.stream()
        .filter(order -> order.getId() == orderId)
        .findFirst()
        .orElseThrow(
            () ->
                new OrderNotFoundException(
                    "Order #" + orderId + " not assigned to " + getRole() + " " + getName()));
  }

  /**
   * Gets orders with a specific status from assigned orders.
   *
   * @param status The status to filter by
   * @return List of orders with the specified status
   */
  protected List<Order> getOrdersByStatus(Order.Status status) {
    return assignedOrders.stream().filter(order -> order.getStatus() == status).toList();
  }

  @Override
  public String toString() {
    return String.format(
        "Staff{name='%s', id='%s', role='%s', phone='%s'}", getName(), id, role, getPhoneNumber());
  }
}
