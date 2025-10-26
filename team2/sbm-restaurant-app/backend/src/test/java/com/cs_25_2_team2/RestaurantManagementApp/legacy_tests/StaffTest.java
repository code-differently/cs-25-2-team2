
package com.cs_25_2_team2.RestaurantManagementApp.legacy_tests;

import com.cs_25_2_team2.RestaurantManagementApp.Staff;
import com.cs_25_2_team2.RestaurantManagementApp.Order;

import com.cs_25_2_team2.RestaurantManagementApp.Chef;
import com.cs_25_2_team2.RestaurantManagementApp.Delivery;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/** Unit tests for the Staff class. */
class StaffTest {

  @Test
  void testChefToString() {
    Chef chef = new Chef("John Doe", "123 Main St", "123-456-7890", 1001L);
    String result = chef.toString();
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertTrue(result.length() > 10);
  }

  @Test
  void testGetId() {
  Chef chef = new Chef("Jane Smith", "456 Oak Ave", "987-654-3210", 1002L);
  assertEquals("CH1002", chef.getId());
  }

  @Test
  void testGetRole() {
    Chef chef = new Chef("Bob Johnson", "789 Pine St", "555-123-4567", 1003L);
    assertEquals("Chef", chef.getRole());
  }

  @Test
  void testDeliveryToString() {
    Delivery delivery = new Delivery("Alice Brown", "321 Elm St", "111-222-3333", 2001L);
    String result = delivery.toString();
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertTrue(result.length() > 10);
  }

  @Test
  void testStaffToStringViaSuperCall() {
    class TestStaff extends Staff {
      public TestStaff(String name, String address, String phone, Long id, String role) {
        super(name, address, phone, id, role);
      }
      @Override
      public void assignOrder(Order order) {}
  public boolean isBusy() { return false; }
      public String testParentToString() { return super.toString(); }
    }
    TestStaff testStaff = new TestStaff("Test Person", "123 Test St", "555-0000", 1001L, "Tester");
    String result = testStaff.testParentToString();
    assertNotNull(result);
    assertTrue(result.contains("Test Person"));
    assertTrue(result.contains("Tester"));
    assertTrue(result.contains("555-0000"));
  }

  @Test
  void testStaffBasicMethods() {
    Chef chef = new Chef("Test Chef", "Test Address", "555-1234", 999L);
    assertEquals("Test Chef", chef.getName());
    assertEquals("Test Address", chef.getAddress());
    assertEquals("555-1234", chef.getPhoneNumber());
  assertTrue(chef.getId().startsWith("CH"));
    assertEquals("Chef", chef.getRole());
    chef.setName("Updated Chef");
    chef.setAddress("Updated Address");
    chef.setPhoneNumber("999-5555");
    assertEquals("Updated Chef", chef.getName());
    assertEquals("Updated Address", chef.getAddress());
    assertEquals("999-5555", chef.getPhoneNumber());
  }
}
