package com.cs_25_2_team2.RestaurantManagementApp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs_25_2_team2.RestaurantManagementApp.exceptions.MenuItemUnavailableException;

/** Represents a menu containing a collection of menu items. */
public class Menu {
  private final Map<Integer, MenuItem> items;
  private final Date lastUpdated;
  private boolean itemAvailability = true;

  /** Creates a new menu. */
  public Menu() {
    this.items = new HashMap<>();
    this.lastUpdated = new Date();
  }

  /**
   * Adds a menu item to the menu.
   *
   * @param item The menu item to add.
   * @throws IllegalArgumentException if the item is null
   */
  public void addMenuItem(MenuItem item) {
    if (item == null) {
      throw new IllegalArgumentException("MenuItem cannot be null");
    }
    items.put(item.getDishId(), item);
  }

  /**
   * Removes a menu item from the menu.
   *
   * @param dishId The ID of the dish to remove.
   * @throws IllegalArgumentException if dishId is negative
   */
  public void removeMenuItem(int dishId) {
    if (dishId < 0) {
      throw new IllegalArgumentException("Dish ID cannot be negative");
    }
    items.remove(dishId);
  }

  /**
   * Gets a menu item by its ID and validates it's available for ordering.
   *
   * @param dishId The ID of the dish to find.
   * @return The menu item with the given ID.
   * @throws IllegalArgumentException if dishId is negative
   * @throws MenuItemUnavailableException if the item exists but is not available
   */
  public MenuItem getAvailableItemById(int dishId) {
    if (dishId < 0) {
      throw new IllegalArgumentException("Dish ID cannot be negative");
    }

    MenuItem item = items.get(dishId);
    if (item != null && !item.isAvailable()) {
      throw new MenuItemUnavailableException(item.getDishId(), item.getDishName());
    }

    return item;
  }

  /**
   * Gets all menu items.
   *
   * @return The list of menu items.
   */
  public List<MenuItem> getItems() {
    return new ArrayList<>(items.values());
  }

  /**
   * Gets the last updated date.
   *
   * @return The last updated date.
   */
  public Date getLastUpdated() {
    return lastUpdated;
  }

  /**
   * Gets the menu availability.
   *
   * @return True if the menu is available, false otherwise.
   */
  public boolean isAvailable() {
    return itemAvailability; // Fixed variable name
  }

  /**
   * Sets the menu availability.
   *
   * @param availability The availability to set.
   */
  public void setAvailability(boolean availability) {
    this.itemAvailability = availability;
  }

  /**
   * Gets a menu item by its ID.
   *
   * @param dishId The ID of the dish to find.
   * @return The menu item with the given ID, or null if not found.
   */
  public MenuItem getItemById(int dishId) {
    return items.get(dishId);
  }

  /**
   * Gets all available menu items.
   *
   * @return A list of available menu items.
   */
  public List<MenuItem> getAvailableItems() {
    List<MenuItem> availableItems = new ArrayList<>();
    for (MenuItem item : items.values()) {
      if (item.isAvailable()) {
        availableItems.add(item);
      }
    }
    return availableItems;
  }

  /**
   * Gets menu items by cooking type.
   *
   * @param cookingType The cooking type to filter by.
   * @return A list of menu items with the specified cooking type.
   */
  public List<MenuItem> getItemsByCookingType(MenuItem.CookedType cookingType) {
    List<MenuItem> filteredItems = new ArrayList<>();
    for (MenuItem item : items.values()) {
      if (item.getCookedType() == cookingType) {
        filteredItems.add(item);
      }
    }
    return filteredItems;
  }

  /**
   * Gets menu items by potato type.
   *
   * @param potatoType The potato type to filter by.
   * @return A list of menu items with the specified potato type.
   */
  public List<MenuItem> getItemsByPotatoType(MenuItem.PotatoType potatoType) {
    List<MenuItem> filteredItems = new ArrayList<>();
    for (MenuItem item : items.values()) {
      if (item.getPotatoType() == potatoType) {
        filteredItems.add(item);
      }
    }
    return filteredItems;
  }

  /**
   * Gets the count of menu items.
   *
   * @return The number of items in the menu.
   */
  public int getItemCount() {
    return items.size();
  }
}
