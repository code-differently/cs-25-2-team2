package com.cs_25_2_team2.RestaurantManagementApp;

import java.util.ArrayList;
import java.util.List;

import com.cs_25_2_team2.RestaurantManagementApp.exceptions.MenuItemUnavailableException;

public class Cart {
  private int userId;
  private List<CartItem> items;

  public Cart(int userId) {
    this.userId = userId;
    this.items = new ArrayList<>();
  }

  public int getUserId() {
    return userId;
  }

  public List<CartItem> getItems() {
    return items;
  }

  public void addItem(MenuItem item, int quantity) {
    if (item == null) {
      throw new IllegalArgumentException("MenuItem cannot be null");
    }
    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantity must be positive");
    }
    if (!item.isAvailable()) {
      throw new MenuItemUnavailableException(item.getDishId(), item.getDishName());
    }

    for (CartItem ci : items) {
      if (ci.getMenuItem().getDishId() == item.getDishId()) {
        ci.setQuantity(ci.getQuantity() + quantity);
        return;
      }
    }
    items.add(new CartItem(item, quantity));
  }

  public void removeItem(int dishId) {
    for (int i = 0; i < items.size(); i++) {
      CartItem ci = items.get(i);
      if (ci.getMenuItem().getDishId() == dishId) {
        items.remove(i);
        break;
      }
    }
  }

  public boolean isEmpty() {
    return items.isEmpty();
  }

  public void clear() {
    items.clear();
  }

  // Add getCustomerId method to match controller expectations
  public int getCustomerId() {
    return userId;
  }

  // Add calculateTotal method to match controller expectations
  public double calculateTotal() {
    double total = 0.0;
    for (CartItem item : items) {
      total += item.getMenuItem().getPrice() * item.getQuantity();
    }
    return total;
  }
}
