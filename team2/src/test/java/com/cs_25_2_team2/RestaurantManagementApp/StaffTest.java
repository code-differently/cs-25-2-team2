package com.cs_25_2_team2.RestaurantManagementApp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Unit tests for the Staff class. */
class StaffTest {

  @Test
  void testChefToString() {
    // Create a chef (concrete subclass of Staff)
    Chef chef = new Chef("John Doe", "123 Main St", "123-456-7890", "C001");

    // Test toString output
    String result = chef.toString();
    assertNotNull(result);
    // Just verify it's not empty and contains some key info
    assertFalse(result.isEmpty());
    assertTrue(result.length() > 10);
  }

  @Test
  void testGetId() {
    Chef chef = new Chef("Jane Smith", "456 Oak Ave", "987-654-3210", "C002");
    assertEquals("C002", chef.getId());
  }

  @Test
  void testGetRole() {
    Chef chef = new Chef("Bob Johnson", "789 Pine St", "555-123-4567", "C003");
    assertEquals("Chef", chef.getRole());
  }

  @Test
  void testDeliveryToString() {
    // Test with Delivery staff as well to cover Staff.toString()
    Delivery delivery = new Delivery("Alice Brown", "321 Elm St", "111-222-3333", "D001");

    String result = delivery.toString();
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertTrue(result.length() > 10);
  }

  @Test
  void testStaffToStringViaSuperCall() {
    // Create a simple test class that extends Staff to test the parent toString
    class TestStaff extends Staff {
      public TestStaff(String name, String address, String phone, String id, String role) {
        super(name, address, phone, id, role);
      }

      @Override
      public void assignOrder(Order order) {
        // Simple test implementation
        if (order != null) {
          assignedOrders.add(order);
        }
      }

      @Override
      public boolean isBusy() {
        // Simple test implementation
        return !assignedOrders.isEmpty();
      }

      public String testParentToString() {
        return super.toString(); // This will call Staff.toString()
      }
    }

    TestStaff testStaff = new TestStaff("Test Person", "123 Test St", "555-0000", "T001", "Tester");
    String result = testStaff.testParentToString();

    assertNotNull(result);
    assertTrue(result.contains("Test Person"));
    assertTrue(result.contains("T001"));
    assertTrue(result.contains("Tester"));
    assertTrue(result.contains("555-0000"));
  }

  @Test
  void testStaffBasicMethods() {
    // Test Staff basic functionality through Chef implementation
    Chef chef = new Chef("Test Chef", "Test Address", "555-1234", "CH999");

    // Test basic getters
    assertEquals("Test Chef", chef.getName());
    assertEquals("Test Address", chef.getAddress());
    assertEquals("555-1234", chef.getPhoneNumber());
    assertEquals("CH999", chef.getId());
    assertEquals("Chef", chef.getRole());

    // Test setter methods inherited from Person
    chef.setName("Updated Chef");
    chef.setAddress("Updated Address");
    chef.setPhoneNumber("999-5555");

    assertEquals("Updated Chef", chef.getName());
    assertEquals("Updated Address", chef.getAddress());
    assertEquals("999-5555", chef.getPhoneNumber());
  }
}
