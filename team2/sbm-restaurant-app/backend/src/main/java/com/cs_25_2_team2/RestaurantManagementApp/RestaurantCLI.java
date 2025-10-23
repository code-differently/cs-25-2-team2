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
    // Add sample menu items - All potato-based dishes!
    MenuItem fries =
        new MenuItem(
            1, "French Fries", 3.99, MenuItem.CookedType.Fried, MenuItem.PotatoType.Russet, true);
    MenuItem mashedPotatoes =
        new MenuItem(
            2,
            "Mashed Potatoes",
            4.99,
            MenuItem.CookedType.Mashed,
            MenuItem.PotatoType.YukonGold,
            true);
    MenuItem bakedPotato =
        new MenuItem(
            3,
            "Loaded Baked Potato",
            6.99,
            MenuItem.CookedType.Baked,
            MenuItem.PotatoType.Russet,
            true);
    MenuItem hashBrowns =
        new MenuItem(
            4, "Hash Browns", 3.49, MenuItem.CookedType.Fried, MenuItem.PotatoType.Russet, true);
    MenuItem sweetPotatoFries =
        new MenuItem(
            5,
            "Sweet Potato Fries",
            4.49,
            MenuItem.CookedType.Fried,
            MenuItem.PotatoType.JapaneseSweet,
            true);

    menu.addMenuItem(fries);
    menu.addMenuItem(mashedPotatoes);
    menu.addMenuItem(bakedPotato);
    menu.addMenuItem(hashBrowns);
    menu.addMenuItem(sweetPotatoFries);

    // Add sample staff - NOW WITH PROPER INHERITANCE!
    chefs.add(new Chef("Gordon Ramsay", "123 Kitchen St", "555-CHEF", 7777777L));
    chefs.add(new Chef("Julia Child", "456 Culinary Ave", "555-COOK", 44444448L));

    deliveryStaff.add(new Delivery("Fast Eddie", "789 Speed Lane", "555-FAST", 7777766L));
    deliveryStaff.add(new Delivery("Quick Quinn", "321 Rush Road", "555-RUSH",  8888888L));

    // Add sample customer
    customers.add(new Customer(77111117L, "John Doe", "123 Main St", "555-1234"));
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

        // Cart Management
      case "cart-add" -> addToCart(parts);
      case "cart-remove" -> removeFromCart(parts);
      case "cart-view" -> viewCart(parts);
      case "cart-clear" -> clearCart(parts);

        // Order Workflow Commands
      case "order-place" -> placeOrder(parts);
      case "order-prepare" -> startPreparingOrder(parts);
      case "order-complete" -> completeOrder(parts);
      case "order-pickup" -> pickupOrder(parts);
      case "order-deliver" -> deliverOrder(parts);

        // Workflow demonstration
      case "demo-workflow" -> demonstrateFullWorkflow();

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

            � Customer Management:
              customers                     - List all customers
              customer-add <id> <name> <address> <phone> - Add customer

            🛒 Cart Management:
              cart-add <customer-id> <menu-item-id> <quantity> - Add item to cart
              cart-remove <customer-id> <menu-item-id> - Remove item from cart
              cart-view <customer-id>       - View customer's cart
              cart-clear <customer-id>      - Clear customer's cart

            �📦 Order Management:
              orders                        - Show all orders
              order-status <id>             - Show specific order status
              order-place <customer-id>     - Place order from customer's cart
              order-prepare <order-id> <chef-id> - Chef starts preparing order
              order-complete <order-id> <chef-id> - Chef completes order
              order-pickup <order-id> <delivery-id> - Delivery picks up order
              order-deliver <order-id> <delivery-id> - Delivery delivers order

            🎭 Demonstrations:
              demo-workflow                 - Demonstrate complete order workflow

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

    Long staffId = Long.valueOf(parts[1]);
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
    Long id = Long.valueOf(parts[4]);

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
    Long id = Long.valueOf(parts[4]);

    if (findStaffById(id) != null) {
      System.out.println("❌ Staff ID already exists: " + id);
      return;
    }

    Delivery newDelivery = new Delivery(name, address, phone, id);
    deliveryStaff.add(newDelivery);

    System.out.println("✅ Added new delivery staff: " + newDelivery.toString());
  }

  /** Find staff by ID across all staff types. */
  private Staff findStaffById(Long id) {
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
      Long id = Long.valueOf(parts[1]);
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

  // ========== CART MANAGEMENT ==========

  private void addToCart(String[] parts) {
    if (parts.length < 4) {
      System.out.println("Usage: cart-add <customer-id> <menu-item-id> <quantity>");
      return;
    }

    try {
      int customerId = Integer.parseInt(parts[1]);
      int menuItemId = Integer.parseInt(parts[2]);
      int quantity = Integer.parseInt(parts[3]);

      Customer customer = findCustomerById(customerId);
      if (customer == null) {
        System.out.println("❌ Customer not found: " + customerId);
        return;
      }

      MenuItem menuItem = menu.getItemById(menuItemId);
      if (menuItem == null) {
        System.out.println("❌ Menu item not found: " + menuItemId);
        return;
      }

      customer.getCart().addItem(menuItem, quantity);
      System.out.println(
          "✅ Added "
              + quantity
              + "x "
              + menuItem.getDishName()
              + " to "
              + customer.getName()
              + "'s cart");

    } catch (NumberFormatException e) {
      System.out.println("❌ Invalid number format in command");
    }
  }

  private void removeFromCart(String[] parts) {
    if (parts.length < 3) {
      System.out.println("Usage: cart-remove <customer-id> <menu-item-id>");
      return;
    }

    try {
      int customerId = Integer.parseInt(parts[1]);
      int menuItemId = Integer.parseInt(parts[2]);

      Customer customer = findCustomerById(customerId);
      if (customer == null) {
        System.out.println("❌ Customer not found: " + customerId);
        return;
      }

      // Check if the item exists in the cart before removing
      boolean itemFound = customer.getCart().getItems().stream()
          .anyMatch(cartItem -> cartItem.getMenuItem().getDishId() == menuItemId);

      if (!itemFound) {
        System.out.println("❌ Menu item " + menuItemId + " not found in cart");
        return;
      }

      // Get the menu item name for the confirmation message
      String itemName = customer.getCart().getItems().stream()
          .filter(cartItem -> cartItem.getMenuItem().getDishId() == menuItemId)
          .map(cartItem -> cartItem.getMenuItem().getDishName())
          .findFirst()
          .orElse("Unknown Item");

      customer.getCart().removeItem(menuItemId);
      System.out.println("✅ Removed " + itemName + " from " + customer.getName() + "'s cart");

    } catch (NumberFormatException e) {
      System.out.println("❌ Invalid number format in command");
    }
  }

  private void viewCart(String[] parts) {
    if (parts.length < 2) {
      System.out.println("Usage: cart-view <customer-id>");
      return;
    }

    try {
      int customerId = Integer.parseInt(parts[1]);
      Customer customer = findCustomerById(customerId);
      if (customer == null) {
        System.out.println("❌ Customer not found: " + customerId);
        return;
      }

      System.out.println("🛒 Cart for " + customer.getName() + ":");
      if (customer.getCart().isEmpty()) {
        System.out.println("  Cart is empty");
      } else {
        customer
            .getCart()
            .getItems()
            .forEach(
                item ->
                    System.out.printf(
                        "  %dx %s - $%.2f each%n",
                        item.getQuantity(),
                        item.getMenuItem().getDishName(),
                        item.getMenuItem().getPrice()));
        System.out.printf("  Total: $%.2f%n", calculateCartTotal(customer.getCart()));
      }

    } catch (NumberFormatException e) {
      System.out.println("❌ Invalid customer ID format");
    }
  }

  private void clearCart(String[] parts) {
    if (parts.length < 2) {
      System.out.println("Usage: cart-clear <customer-id>");
      return;
    }

    try {
      int customerId = Integer.parseInt(parts[1]);
      Customer customer = findCustomerById(customerId);
      if (customer == null) {
        System.out.println("❌ Customer not found: " + customerId);
        return;
      }

      customer.getCart().clear();
      System.out.println("✅ Cleared cart for " + customer.getName());

    } catch (NumberFormatException e) {
      System.out.println("❌ Invalid customer ID format");
    }
  }

  // ========== ORDER WORKFLOW COMMANDS ==========

  private void placeOrder(String[] parts) {
    if (parts.length < 2) {
      System.out.println("Usage: order-place <customer-id>");
      return;
    }

    try {
      int customerId = Integer.parseInt(parts[1]);
      Customer customer = findCustomerById(customerId);
      if (customer == null) {
        System.out.println("❌ Customer not found: " + customerId);
        return;
      }

      if (customer.getCart().isEmpty()) {
        System.out.println("❌ Customer's cart is empty");
        return;
      }

      // Create order from cart
      Order order = customer.checkout();
      orderQueue.add(order, 1); // Add with normal priority

      // Assign to available chef
      Chef availableChef = findAvailableChef();
      if (availableChef != null) {
        availableChef.receiveOrder(order);
        System.out.println(
            "✅ Order #"
                + order.getId()
                + " placed and assigned to Chef "
                + availableChef.getName());
      } else {
        System.out.println("✅ Order #" + order.getId() + " placed and queued (all chefs busy)");
      }

    } catch (NumberFormatException e) {
      System.out.println("❌ Invalid customer ID format");
    }
  }

  private void startPreparingOrder(String[] parts) {
    if (parts.length < 3) {
      System.out.println("Usage: order-prepare <order-id> <chef-id>");
      return;
    }

    try {
      int orderId = Integer.parseInt(parts[1]);
      String chefId = parts[2];

      Chef chef = findChefById(chefId);
      if (chef == null) {
        System.out.println("❌ Chef not found: " + chefId);
        return;
      }

      chef.startPreparingOrder(orderId);
      System.out.println("✅ Chef " + chef.getName() + " started preparing order #" + orderId);

    } catch (NumberFormatException e) {
      System.out.println("❌ Invalid order ID format");
    } catch (Exception e) {
      System.out.println("❌ Error: " + e.getMessage());
    }
  }

  private void completeOrder(String[] parts) {
    if (parts.length < 3) {
      System.out.println("Usage: order-complete <order-id> <chef-id>");
      return;
    }

    try {
      int orderId = Integer.parseInt(parts[1]);
      String chefId = parts[2];

      Chef chef = findChefById(chefId);
      if (chef == null) {
        System.out.println("❌ Chef not found: " + chefId);
        return;
      }

      chef.completeOrder(orderId);
      System.out.println("✅ Chef " + chef.getName() + " completed order #" + orderId);

    } catch (NumberFormatException e) {
      System.out.println("❌ Invalid order ID format");
    } catch (Exception e) {
      System.out.println("❌ Error: " + e.getMessage());
    }
  }

  private void pickupOrder(String[] parts) {
    if (parts.length < 3) {
      System.out.println("Usage: order-pickup <order-id> <delivery-id>");
      return;
    }

    try {
      int orderId = Integer.parseInt(parts[1]);
      String deliveryId = parts[2];

      Delivery delivery = findDeliveryById(deliveryId);
      if (delivery == null) {
        System.out.println("❌ Delivery staff not found: " + deliveryId);
        return;
      }

      // First assign the order, then pick it up
      Order order = orderQueue.get(orderId);
      if (order != null && order.getStatus() == Order.Status.ReadyForDelivery) {
        delivery.assignOrder(order);
        delivery.pickupOrder(orderId);
        System.out.println("✅ " + delivery.getName() + " picked up order #" + orderId);
      } else {
        System.out.println("❌ Order not ready for pickup or not found");
      }

    } catch (NumberFormatException e) {
      System.out.println("❌ Invalid order ID format");
    } catch (Exception e) {
      System.out.println("❌ Error: " + e.getMessage());
    }
  }

  private void deliverOrder(String[] parts) {
    if (parts.length < 3) {
      System.out.println("Usage: order-deliver <order-id> <delivery-id>");
      return;
    }

    try {
      int orderId = Integer.parseInt(parts[1]);
      String deliveryId = parts[2];

      Delivery delivery = findDeliveryById(deliveryId);
      if (delivery == null) {
        System.out.println("❌ Delivery staff not found: " + deliveryId);
        return;
      }

      delivery.deliverOrder(orderId);
      System.out.println("✅ " + delivery.getName() + " delivered order #" + orderId);

    } catch (NumberFormatException e) {
      System.out.println("❌ Invalid order ID format");
    } catch (Exception e) {
      System.out.println("❌ Error: " + e.getMessage());
    }
  }

  private void demonstrateFullWorkflow() {
    System.out.println("🎭 Demonstrating Full Order Workflow");
    System.out.println("=====================================");

    // Ensure we have the required entities
    if (customers.isEmpty()) {
      System.out.println("❌ No customers found. Adding demo customer...");
      customers.add(new Customer(999L, "Demo Customer", "123 Demo St", "555-DEMO"));
    }

    if (chefs.isEmpty()) {
      System.out.println("❌ No chefs found. Please add a chef first.");
      return;
    }

    if (deliveryStaff.isEmpty()) {
      System.out.println("❌ No delivery staff found. Please add delivery staff first.");
      return;
    }

    Customer customer = customers.get(0);
    Chef chef = chefs.get(0);
    Delivery delivery = deliveryStaff.get(0);

    // Step 1: Add items to cart
    System.out.println("\n1️⃣ Adding items to customer cart...");
    List<MenuItem> availableItems = menu.getAvailableItems();
    if (!availableItems.isEmpty()) {
      customer.getCart().addItem(availableItems.get(0), 2);
      System.out.println("   Added 2x " + availableItems.get(0).getDishName());
    }

    // Step 2: Place order
    System.out.println("\n2️⃣ Placing order...");
    if (!customer.getCart().isEmpty()) {
      Order order = customer.checkout();
      orderQueue.add(order, 1);
      chef.receiveOrder(order);
      System.out.println(
          "   Order #" + order.getId() + " placed and assigned to " + chef.getName());

      // Step 3: Prepare order
      System.out.println("\n3️⃣ Chef preparing order...");
      chef.startPreparingOrder(order.getId());
      System.out.println("   " + chef.getName() + " started preparing order #" + order.getId());

      // Step 4: Complete order
      System.out.println("\n4️⃣ Chef completing order...");
      chef.completeOrder(order.getId());
      System.out.println("   " + chef.getName() + " completed order #" + order.getId());

      // Step 5: Assign to delivery
      System.out.println("\n5️⃣ Assigning to delivery...");
      delivery.assignOrder(order);
      delivery.pickupOrder(order.getId());
      System.out.println("   " + delivery.getName() + " picked up order #" + order.getId());

      // Step 6: Deliver order
      System.out.println("\n6️⃣ Delivering order...");
      delivery.deliverOrder(order.getId());
      System.out.println("   " + delivery.getName() + " delivered order #" + order.getId());

      System.out.println("\n✅ Complete workflow demonstrated!");
    } else {
      System.out.println("❌ No menu items available for demo");
    }
  }

  // ========== HELPER METHODS ==========

  private Customer findCustomerById(int id) {
    return customers.stream().filter(c -> c.getCustomerId() == id).findFirst().orElse(null);
  }

  private Chef findChefById(String id) {
    return chefs.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
  }

  private Delivery findDeliveryById(String id) {
    return deliveryStaff.stream().filter(d -> d.getId().equals(id)).findFirst().orElse(null);
  }

  private Chef findAvailableChef() {
    return chefs.stream().filter(chef -> !chef.isBusy()).findFirst().orElse(null);
  }

  private double calculateCartTotal(Cart cart) {
    return cart.getItems().stream()
        .mapToDouble(item -> item.getMenuItem().getPrice() * item.getQuantity())
        .sum();
  }

  /** Main method to run the CLI. */
  public static void main(String[] args) {
    new RestaurantCLI().start();
  }
}
