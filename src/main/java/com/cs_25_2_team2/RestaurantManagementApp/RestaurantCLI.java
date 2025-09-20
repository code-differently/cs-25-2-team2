package com.cs_25_2_team2.RestaurantManagementApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import com.cs_25_2_team2.RestaurantManagementApp.exceptions.OrderNotFoundException;

/**
 * Command Line Interface for the Restaurant Management System. Demonstrates how proper inheritance
 * hierarchy enables unified staff management.
 */
public class RestaurantCLI {
  private final Scanner scanner;
  private final Menu menu;
  private final List<Customer> customers;
  private final List<Chef> chefs;
  private final List<Delivery> deliveryStaff;
  private final OrderQueue orderQueue;
  private boolean running;

  public RestaurantCLI() {
    this.scanner = new Scanner(System.in);
    this.menu = new Menu();
    this.customers = new ArrayList<>();
    this.chefs = new ArrayList<>();
    this.deliveryStaff = new ArrayList<>();
    this.orderQueue = new OrderQueue();
    this.running = true;
    initializeTestData();
  }

  /** Initialize some test data for demonstration. */
  private void initializeTestData() {
    // Add sample menu items
    MenuItem fries =
        new MenuItem(
            1, "French Fries", 3.99, MenuItem.CookedType.Fried, MenuItem.PotatoType.Russet, true);
    MenuItem burger =
        new MenuItem(
            2, "Burger", 8.99, MenuItem.CookedType.Fried, MenuItem.PotatoType.Russet, true);
    menu.addMenuItem(fries);
    menu.addMenuItem(burger);

    // Add sample staff - NOW WITH PROPER INHERITANCE!
    chefs.add(new Chef("Gordon Ramsay", "123 Kitchen St", "555-CHEF", "CH001"));
    chefs.add(new Chef("Julia Child", "456 Culinary Ave", "555-COOK", "CH002"));

    deliveryStaff.add(new Delivery("Fast Eddie", "789 Speed Lane", "555-FAST", "DL001"));
    deliveryStaff.add(new Delivery("Quick Quinn", "321 Rush Road", "555-RUSH", "DL002"));

    // Add sample customer
    customers.add(new Customer(1, "John Doe", "123 Main St", "555-1234"));
  }

  /** Main CLI loop. */
  public void start() {
    System.out.println("🍽️  Welcome to Restaurant Management System CLI!");
    System.out.println("Type 'help' for available commands or 'quit' to exit.\n");

    while (running) {
      System.out.print("restaurant> ");
      String input = scanner.nextLine().trim();

      if (input.isEmpty()) continue;

      try {
        handleCommand(input);
      } catch (Exception e) {
        System.err.println("❌ Error: " + e.getMessage());
      }
    }

    scanner.close();
    System.out.println("👋 Goodbye!");
  }

  /** Handle user commands. */
  private void handleCommand(String input) {
    String[] parts = input.split("\\s+");
    String command = parts[0].toLowerCase();

    switch (command) {
      case "help" -> showHelp();
      case "quit", "exit" -> running = false;

        // Staff Management Commands (enabled by proper inheritance!)
      case "staff-list" -> listAllStaff();
      case "staff-contact" -> showStaffContact(parts);
      case "add-chef" -> addChef(parts);
      case "add-delivery" -> addDeliveryStaff(parts);

        // Menu Management
      case "menu" -> showMenu();
      case "menu-add" -> addMenuItem(parts);

        // Order Management
      case "orders" -> showOrders();
      case "order-status" -> showOrderStatus(parts);

        // Customer Management
      case "customers" -> listCustomers();
      case "customer-add" -> addCustomer(parts);

      default ->
          System.out.println(
              "❓ Unknown command: " + command + ". Type 'help' for available commands.");
    }
  }

  /** Show help information. */
  private void showHelp() {
    System.out.println(
        """
            📋 Available Commands:

            🏢 Staff Management:
              staff-list                    - List all staff members
              staff-contact <id>            - Show contact info for staff member
              add-chef <name> <address> <phone> <id>     - Add new chef
              add-delivery <name> <address> <phone> <id> - Add delivery staff

            🍽️  Menu Management:
              menu                          - Show current menu
              menu-add <id> <name> <price>  - Add menu item

            📦 Order Management:
              orders                        - Show all orders
              order-status <id>             - Show specific order status

            👥 Customer Management:
              customers                     - List all customers
              customer-add <id> <name> <address> <phone> - Add customer

            ⚙️  System:
              help                          - Show this help
              quit/exit                     - Exit the application
            """);
  }

  // ========== STAFF MANAGEMENT (Enabled by proper inheritance!) ==========

  /** List all staff members - demonstrates unified Person management. */
  private void listAllStaff() {
    System.out.println("👨‍🍳👩‍🍳 Restaurant Staff:");

    List<Staff> allStaff = new ArrayList<>();
    allStaff.addAll(chefs);
    allStaff.addAll(deliveryStaff);

    if (allStaff.isEmpty()) {
      System.out.println("No staff members found.");
      return;
    }

    // This works because of proper inheritance hierarchy!
    allStaff.forEach(
        staff -> {
          System.out.printf(
              "  %s (%s) - %s - 📞 %s%n",
              staff.getName(), staff.getId(), staff.getRole(), staff.getPhoneNumber());
        });
  }

  /** Show contact information for a staff member. */
  private void showStaffContact(String[] parts) {
    if (parts.length < 2) {
      System.out.println("Usage: staff-contact <staff-id>");
      return;
    }

    String staffId = parts[1];
    Staff staff = findStaffById(staffId);

    if (staff == null) {
      System.out.println("❌ Staff member not found: " + staffId);
      return;
    }

    // This demonstrates the power of proper inheritance!
    System.out.println("📋 Staff Contact Information:");
    System.out.println("  Name: " + staff.getName());
    System.out.println("  ID: " + staff.getId());
    System.out.println("  Role: " + staff.getRole());
    System.out.println("  Phone: " + staff.getPhoneNumber());
    System.out.println("  Address: " + staff.getAddress());

    // Role-specific information
    if (staff instanceof Chef chef) {
      System.out.println("  📊 Kitchen Stats:");
      System.out.println("    Orders Managed: " + chef.getOrderCount());
      System.out.println("    Currently Active: " + chef.getActiveOrderCount());
      System.out.println("    Status: " + (chef.isBusy() ? "🔥 Busy" : "✅ Available"));
    } else if (staff instanceof Delivery delivery) {
      System.out.println("  📊 Delivery Stats:");
      System.out.println("    Orders Assigned: " + delivery.getAssignedOrderCount());
      System.out.println("    Orders Delivered: " + delivery.getDeliveredOrderCount());
      System.out.println(
          "    Status: " + (delivery.isBusy() ? "🚗 Out for Delivery" : "✅ Available"));
    }
  }

  /** Add a new chef - demonstrates proper constructor usage. */
  private void addChef(String[] parts) {
    if (parts.length < 5) {
      System.out.println("Usage: add-chef <name> <address> <phone> <id>");
      return;
    }

    String name = parts[1];
    String address = parts[2];
    String phone = parts[3];
    String id = parts[4];

    // Check if ID already exists
    if (findStaffById(id) != null) {
      System.out.println("❌ Staff ID already exists: " + id);
      return;
    }

    // NOW THIS WORKS PROPERLY with fixed inheritance!
    Chef newChef = new Chef(name, address, phone, id);
    chefs.add(newChef);

    System.out.println("✅ Added new chef: " + newChef.toString());
  }

  /** Add delivery staff. */
  private void addDeliveryStaff(String[] parts) {
    if (parts.length < 5) {
      System.out.println("Usage: add-delivery <name> <address> <phone> <id>");
      return;
    }

    String name = parts[1];
    String address = parts[2];
    String phone = parts[3];
    String id = parts[4];

    if (findStaffById(id) != null) {
      System.out.println("❌ Staff ID already exists: " + id);
      return;
    }

    Delivery newDelivery = new Delivery(name, address, phone, id);
    deliveryStaff.add(newDelivery);

    System.out.println("✅ Added new delivery staff: " + newDelivery.toString());
  }

  /** Find staff by ID across all staff types. */
  private Staff findStaffById(String id) {
    // This unified search is possible because of proper inheritance!
    return Stream.concat(chefs.stream(), deliveryStaff.stream())
        .filter(staff -> staff.getId().equals(id))
        .findFirst()
        .orElse(null);
  }

  // ========== MENU MANAGEMENT ==========

  private void showMenu() {
    System.out.println("🍽️  Current Menu:");
    List<MenuItem> items = menu.getAvailableItems();

    if (items.isEmpty()) {
      System.out.println("No menu items available.");
      return;
    }

    items.forEach(
        item -> {
          System.out.printf(
              "  %d. %s - $%.2f (%s, %s)%n",
              item.getDishId(),
              item.getDishName(),
              item.getPrice(),
              item.getCookedType(),
              item.getPotatoType());
        });
  }

  private void addMenuItem(String[] parts) {
    if (parts.length < 4) {
      System.out.println("Usage: menu-add <id> <name> <price>");
      return;
    }

    try {
      int id = Integer.parseInt(parts[1]);
      String name = parts[2];
      double price = Double.parseDouble(parts[3]);

      MenuItem item =
          new MenuItem(
              id, name, price, MenuItem.CookedType.Fried, MenuItem.PotatoType.Russet, true);
      menu.addMenuItem(item);

      System.out.println("✅ Added menu item: " + item.getDishName());
    } catch (NumberFormatException e) {
      System.out.println("❌ Invalid number format in command");
    }
  }

  // ========== ORDER MANAGEMENT ==========

  private void showOrders() {
    System.out.println("📦 Order Queue:");

    if (orderQueue.isEmpty()) {
      System.out.println("No orders in queue.");
      return;
    }

    List<Order> orders = orderQueue.getAll();
    orders.forEach(
        order -> {
          System.out.printf(
              "  Order #%d - %s - Status: %s - Total: $%.2f%n",
              order.getId(),
              order.getCustomer().getName(),
              order.getStatus(),
              order.getTotalPrice());
        });
  }

  private void showOrderStatus(String[] parts) {
    if (parts.length < 2) {
      System.out.println("Usage: order-status <order-id>");
      return;
    }

    try {
      int orderId = Integer.parseInt(parts[1]);
      Order order = orderQueue.get(orderId);

      System.out.println("📦 Order Details:");
      System.out.println(order.toString());
    } catch (NumberFormatException e) {
      System.out.println("❌ Invalid order ID format");
    } catch (OrderNotFoundException e) {
      System.out.println("❌ " + e.getMessage());
    }
  }

  // ========== CUSTOMER MANAGEMENT ==========

  private void listCustomers() {
    System.out.println("👥 Customers:");

    if (customers.isEmpty()) {
      System.out.println("No customers found.");
      return;
    }

    customers.forEach(
        customer -> {
          System.out.printf(
              "  %d. %s - 📞 %s - 📍 %s%n",
              customer.getCustomerId(),
              customer.getName(),
              customer.getPhoneNumber(),
              customer.getAddress());
        });
  }

  private void addCustomer(String[] parts) {
    if (parts.length < 5) {
      System.out.println("Usage: customer-add <id> <name> <address> <phone>");
      return;
    }

    try {
      int id = Integer.parseInt(parts[1]);
      String name = parts[2];
      String address = parts[3];
      String phone = parts[4];

      Customer customer = new Customer(id, name, address, phone);
      customers.add(customer);

      System.out.println("✅ Added customer: " + customer.getName());
    } catch (NumberFormatException e) {
      System.out.println("❌ Invalid customer ID format");
    }
  }

  /** Main method to run the CLI. */
  public static void main(String[] args) {
    new RestaurantCLI().start();
  }
}
