package com.cs_25_2_team2.RestaurantManagementApp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CartTest {
  private Cart cart;
  private MenuItem frenchFries;
  private MenuItem bakedPotato;

  @BeforeEach
  void setUp() {
    cart = new Cart(101);
    frenchFries =
        new MenuItem(
            1, "French Fries", 3.99, MenuItem.CookedType.Fried, MenuItem.PotatoType.Russet, true);
    bakedPotato =
        new MenuItem(
            2,
            "Baked Potato",
            4.99,
            MenuItem.CookedType.Baked,
            MenuItem.PotatoType.YukonGold,
            true);
  }

  @Test
  void testGetUserId() {
    assertEquals(101, cart.getUserId());
  }

  @Test
  void testAddNewItem() {
    cart.addItem(frenchFries, 2);
    List<CartItem> items = cart.getItems();

    assertEquals(1, items.size());
    assertEquals(frenchFries, items.get(0).getMenuItem());
    assertEquals(2, items.get(0).getQuantity());
  }

  @Test
  void testRemoveItem() {
    cart.addItem(frenchFries, 2);
    cart.addItem(bakedPotato, 1);

    cart.removeItem(1);

    List<CartItem> items = cart.getItems();

    assertEquals(1, items.size());
    assertEquals(bakedPotato, items.get(0).getMenuItem());
  }
}
