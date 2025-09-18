package com.cs_25_2_team2.RestaurantManagementApp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Test class for the Customer functionality. */
public class CustomerTest {

  private Customer customer;

  @BeforeEach
  void setUp() {
    // Create a new customer for testing
    customer =
        new Customer(
            1, // customerId
            "John Doe", // customerName
            "123 Main St", // address
            "555-123-4567" // phoneNumber
            );
  }

  @Test
  void testGetCustomerId() {
    assertEquals(1, customer.getCustomerId(), "Customer ID should be 1");
  }

  @Test
  void testGetName() {
    assertEquals("John Doe", customer.getName(), "Customer name should be 'John Doe'");
  }

  @Test
  void testGetAddress() {
    assertEquals("123 Main St", customer.getAddress(), "Customer address should be '123 Main St'");
  }

  @Test
  void testGetPhoneNumber() {
    assertEquals(
        "555-123-4567", customer.getPhoneNumber(), "Phone number should be '555-123-4567'");
  }

  @Test
  void testSetName() {
    customer.setName("Jane Smith");
    assertEquals(
        "Jane Smith", customer.getName(), "Customer name should be updated to 'Jane Smith'");
  }

  @Test
  void testSetAddress() {
    customer.setAddress("456 Oak Ave");
    assertEquals(
        "456 Oak Ave",
        customer.getAddress(),
        "Customer address should be updated to '456 Oak Ave'");
  }

  @Test
  void testSetPhoneNumber() {
    customer.setPhoneNumber("555-987-6543");
    assertEquals(
        "555-987-6543",
        customer.getPhoneNumber(),
        "Phone number should be updated to '555-987-6543'");
  }

  @Test
  void testSetCustomerId() {
    customer.setCustomerId(99);
    assertEquals(99, customer.getCustomerId(), "Customer ID should be updated to 99");
  }

  @Test
  void testToString() {
    String customerString = customer.toString();
    assertTrue(customerString.contains("id=1"), "toString should contain the customer ID");
    assertTrue(
        customerString.contains("name='John Doe'"), "toString should contain the customer name");
    assertTrue(
        customerString.contains("address='123 Main St'"), "toString should contain the address");
    assertTrue(
        customerString.contains("phoneNumber='555-123-4567'"),
        "toString should contain the phone number");
  }
}
