package com.cs_25_2_team2.RestaurantManagementApp;

import java.util.List;

public class Customer {
  private int customerId;
  private String customerName;
  private String address;
  private String phoneNumber;

  public Customer(int customerId, String customerName, String address, String phoneNumber) {
    this.customerId = customerId;
    this.customerName = customerName;
    this.address = address;
    this.phoneNumber = phoneNumber;
  }

  public int getCustomerId() {
    return customerId;
  }

  public String getName() {
    return customerName;
  }

  public String getAddress() {
    return address;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setName(String name) {
    this.customerName = name;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  @Override
  public String toString() {
    return "Customer{id="
        + customerId
        + ", name='"
        + customerName
        + '\''
        + ", address='"
        + address
        + '\''
        + ", phoneNumber='"
        + phoneNumber
        + '\''
        + '}';
  }

  /**
   * Customer views the menu and gets a formatted display of available items.
   *
   * @param menu The menu to view
   * @return A formatted string of the menu items or a message if no items are available
   */
  public String viewMenu(Menu menu) {
    StringBuilder displayMenu = new StringBuilder();

    if (!menu.isAvailable()) {
      return "Menu is currently unavailable.";
    }

    List<MenuItem> availableItems = menu.getAvailableItems();

    if (availableItems.isEmpty()) {
      return "No items available";
    }

    displayMenu.append("Menu Items:\n");
    for (MenuItem item : availableItems) {
      displayMenu
          .append(item.getDishId())
          .append(". ")
          .append(item.getDishName())
          .append(" (")
          .append(item.getCookedType())
          .append(", ")
          .append(item.getPotatoType())
          .append(") - $")
          .append(String.format("%.2f", item.getPrice()))
          .append("\n");
    }

    return displayMenu.toString();
  }
}
