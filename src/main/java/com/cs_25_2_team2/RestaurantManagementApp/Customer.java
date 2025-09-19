package com.cs_25_2_team2.RestaurantManagementApp;

import java.util.List;

import org.springframework.core.annotation.Order;

public class Customer {
  private int customerId;
  private String customerName;
  private String address;
  private String phoneNumber;
  private Cart cart; 

  public Customer(int customerId, String customerName, String address, String phoneNumber) {
    this.customerId = customerId;
    this.customerName = customerName;
    this.address = address;
    this.phoneNumber = phoneNumber;
    this.cart = new Cart(customerId); // Each customer starts with an empty cart linked to their ID
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

  public Cart getCart() { 
    return cart;
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
  
  // Convert Cart → Order
  public Order checkout() {
      if (cart.isEmpty()) {
          throw new IllegalStateException("Cart is empty. Cannot checkout.");
      }
      // Create new Order with current cart contents
      Order order = new Order(0, this, cart.getItems(), new java.sql.Date(System.currentTimeMillis()));
      
      // Clear the cart for future use
      cart.clear();

      return order;
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
