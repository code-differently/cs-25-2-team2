package com.cs_25_2_team2.RestaurantManagementApp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs_25_2_team2.RestaurantManagementApp.exceptions.OrderNotFoundException;

/**
 * Restaurant class - Central coordinator for all restaurant operations.
 *
 * <p>This class demonstrates all SOLID principles: - Single Responsibility: Coordinates restaurant
 * operations - Open/Closed: Extensible for new operations without modification - Liskov
 * Substitution: Staff polymorphism works correctly - Interface Segregation: Uses specific
 * interfaces for different operations - Dependency Inversion: Depends on abstractions (Staff), not
 * concretions
 */
public class Restaurant {
  private final String name;
  private final String address;
  private final Menu menu;
  private final OrderQueue orderQueue;
  private final List<Chef> chefs;
  private final List<Delivery> deliveryStaff;
  private final List<Customer> customers;
  private final Map<String, Order> completedOrders;
  private final RestaurantStats stats;
  private boolean isOpen;
  private final LocalDateTime openedAt;

  /** Restaurant statistics tracker. */
  public static class RestaurantStats {
    private int totalOrdersProcessed = 0;
    private double totalRevenue = 0.0;
    private int ordersDelivered = 0;
    private final Map<String, Integer> popularItems = new HashMap<>();
    private final LocalDateTime startTime = LocalDateTime.now();

    public void recordOrder(Order order) {
      totalOrdersProcessed++;
      totalRevenue += order.getTotalPrice();

      // Track popular items
      order
          .getItems()
          .forEach(
              item -> {
                String itemName = item.getMenuItem().getDishName();
                popularItems.merge(itemName, item.getQuantity(), Integer::sum);
              });
    }

    public void recordDelivery() {
      ordersDelivered++;
    }

    // Getters
    public int getTotalOrdersProcessed() {
      return totalOrdersProcessed;
    }

    public double getTotalRevenue() {
      return totalRevenue;
    }

    public int getOrdersDelivered() {
      return ordersDelivered;
    }

    public Map<String, Integer> getPopularItems() {
      return new HashMap<>(popularItems);
    }

    public LocalDateTime getStartTime() {
      return startTime;
    }
  }

  /** Creates a new Restaurant. */
  public Restaurant(String name, String address) {
    this.name = name;
    this.address = address;
    this.menu = new Menu();
    this.orderQueue = new OrderQueue();
    this.chefs = new ArrayList<>();
    this.deliveryStaff = new ArrayList<>();
    this.customers = new ArrayList<>();
    this.completedOrders = new HashMap<>();
    this.stats = new RestaurantStats();
    this.isOpen = false;
    this.openedAt = LocalDateTime.now();
  }

  // ========== RESTAURANT OPERATIONS (Single Responsibility Principle) ==========

  /**
   * Opens the restaurant for business.
   *
   * @throws IllegalStateException if restaurant is already open or has no staff
   */
  public void openRestaurant() {
    if (isOpen) {
      throw new IllegalStateException("Restaurant is already open");
    }
    if (chefs.isEmpty()) {
      throw new IllegalStateException("Cannot open restaurant without at least one chef");
    }
    if (deliveryStaff.isEmpty()) {
      throw new IllegalStateException("Cannot open restaurant without at least one delivery staff");
    }

    isOpen = true;
    System.out.println("🎉 " + name + " is now OPEN for business!");
    System.out.println("📍 Location: " + address);
    System.out.println("👨‍🍳 Chefs on duty: " + chefs.size());
    System.out.println("🚗 Delivery staff available: " + deliveryStaff.size());
  }

  /** Closes the restaurant. */
  public void closeRestaurant() {
    isOpen = false;
    System.out.println("🔒 " + name + " is now CLOSED");
    printDailyReport();
  }

  /**
   * Processes a complete customer order from cart to delivery assignment. This method demonstrates
   * the complete workflow coordination.
   */
  public Order processCustomerOrder(Customer customer) {
    if (!isOpen) {
      throw new IllegalStateException("Restaurant is closed");
    }
    if (customer.getCart().isEmpty()) {
      throw new IllegalStateException("Customer cart is empty");
    }

    System.out.println("📦 Processing order for customer: " + customer.getName());

    // Step 1: Customer checkout (creates order)
    Order order = customer.checkout();
    stats.recordOrder(order);

    // Step 2: Add to kitchen queue with priority based on order size
    int priority = calculateOrderPriority(order);
    orderQueue.add(order, priority);

    // Step 3: Assign to available chef
    Chef availableChef = findAvailableChef();
    if (availableChef != null) {
      availableChef.receiveOrder(order);
      System.out.println(
          "👨‍🍳 Order #" + order.getId() + " assigned to Chef " + availableChef.getName());
    } else {
      System.out.println("⏳ Order #" + order.getId() + " queued - all chefs busy");
    }

    return order;
  }

  /**
   * Calculates order priority based on order characteristics. Smaller orders get higher priority
   * (lower number = higher priority).
   */
  private int calculateOrderPriority(Order order) {
    int itemCount = order.getItems().stream().mapToInt(CartItem::getQuantity).sum();

    // Priority 1-5: 1=highest, 5=lowest
    if (itemCount <= 2) return 1; // Quick orders
    else if (itemCount <= 4) return 2; // Normal orders
    else if (itemCount <= 6) return 3; // Medium orders
    else if (itemCount <= 8) return 4; // Large orders
    else return 5; // Very large orders
  }

  /** Processes the kitchen queue - moves orders through the cooking process. */
  public void processKitchenQueue() {
    if (!isOpen) return;

    // Process orders that are ready to be started
    chefs.stream()
        .filter(chef -> !chef.isBusy())
        .forEach(
            chef -> {
              if (!orderQueue.isEmpty()) {
                Order nextOrder = orderQueue.remove();
                chef.startPreparingOrder(nextOrder.getId());
              }
            });
  }

  /** Completes an order and assigns it for delivery. */
  public void completeOrder(int orderId, Long chefId) {
    Chef chef = findChefById(chefId);
    if (chef == null) {
      throw new OrderNotFoundException("Chef not found: " + chefId);
    }

    // Chef completes the order
    chef.completeOrder(orderId);

    // Assign to delivery staff
    Delivery availableDelivery = findAvailableDeliveryStaff();
    if (availableDelivery != null) {
      chef.sendToDelivery(orderId, availableDelivery);
      System.out.println("🚗 Order #" + orderId + " assigned to " + availableDelivery.getName());
    } else {
      System.out.println("⏳ Order #" + orderId + " waiting for available delivery staff");
    }
  }

  /** Completes delivery of an order. */
  public void deliverOrder(int orderId, Long deliveryStaffId) {
    Delivery deliveryPerson = findDeliveryStaffById(deliveryStaffId);
    if (deliveryPerson == null) {
      throw new OrderNotFoundException("Delivery staff not found: " + deliveryStaffId);
    }

    deliveryPerson.deliverOrder(orderId);

    // Move to completed orders
    Order order =
        deliveryPerson.getAssignedOrders().stream()
            .filter(o -> o.getId() == orderId)
            .findFirst()
            .orElse(null);

    if (order != null) {
      completedOrders.put("ORDER_" + orderId, order);
      stats.recordDelivery();
      System.out.println("✅ Order #" + orderId + " successfully delivered!");
    }
  }

  // ========== STAFF MANAGEMENT (Liskov Substitution Principle) ==========

  /**
   * Adds a chef to the restaurant.
   *
   * @param chef The chef to add
   * @throws IllegalArgumentException if chef is null or ID already exists
   */
  public void addChef(Chef chef) {
    validateStaffMember(chef);
    if (findChefById(chef.getId()) != null) {
      throw new IllegalArgumentException("Chef ID already exists: " + chef.getId());
    }
    chefs.add(chef);
    System.out.println("✅ Chef " + chef.getName() + " joined the team!");
  }

  /** Adds delivery staff to the restaurant. */
  public void addDeliveryStaff(Delivery delivery) {
    validateStaffMember(delivery);
    if (findDeliveryStaffById(delivery.getId()) != null) {
      throw new IllegalArgumentException("Delivery staff ID already exists: " + delivery.getId());
    }
    deliveryStaff.add(delivery);
    System.out.println("✅ Delivery staff " + delivery.getName() + " joined the team!");
  }

  /** Validates staff member (Dependency Inversion - depends on Staff abstraction). */
  private void validateStaffMember(Staff staff) {
    if (staff == null) {
      throw new IllegalArgumentException("Staff member cannot be null");
    }
    if (staff.getName() == null || staff.getName().trim().isEmpty()) {
      throw new IllegalArgumentException("Staff member must have a name");
    }
    if (staff.getId() == null) {
      throw new IllegalArgumentException("Staff member must have an ID");
    }
  }

  /** Finds available chef (demonstrates polymorphism). */
  private Chef findAvailableChef() {
    return chefs.stream().filter(chef -> !chef.isBusy()).findFirst().orElse(null);
  }

  /** Finds available delivery staff. */
  private Delivery findAvailableDeliveryStaff() {
    return deliveryStaff.stream().filter(delivery -> !delivery.isBusy()).findFirst().orElse(null);
  }

  // ========== SEARCH AND RETRIEVAL ==========

  private Chef findChefById(Long id) {
    return chefs.stream().filter(chef -> chef.getId().equals(id)).findFirst().orElse(null);
  }

  private Delivery findDeliveryStaffById(Long id) {
    return deliveryStaff.stream()
        .filter(delivery -> delivery.getId().equals(id))
        .findFirst()
        .orElse(null);
  }

  /** Finds staff member by ID (demonstrates polymorphism). */
  public Staff findStaffById(Long id) {
    // Check chefs first
    Staff staff = findChefById(id);
    if (staff != null) return staff;

    // Then check delivery staff
    return findDeliveryStaffById(id);
  }

  // ========== CUSTOMER MANAGEMENT ==========

  public void registerCustomer(Customer customer) {
    if (customer == null) {
      throw new IllegalArgumentException("Customer cannot be null");
    }
    if (findCustomerById(customer.getCustomerId()) != null) {
      throw new IllegalArgumentException("Customer ID already exists: " + customer.getCustomerId());
    }
    customers.add(customer);
    System.out.println("✅ Customer " + customer.getName() + " registered!");
  }

  public Customer findCustomerById(Long customerId) {
    return customers.stream()
        .filter(customer -> customer.getCustomerId().equals(customerId))
        .findFirst()
        .orElse(null);
  }

  // ========== MENU MANAGEMENT (Open/Closed Principle) ==========

  public void addMenuItem(MenuItem item) {
    menu.addMenuItem(item);
    System.out.println("✅ Added menu item: " + item.getDishName());
  }

  public void removeMenuItem(int dishId) {
    menu.removeMenuItem(dishId);
    System.out.println("✅ Removed menu item ID: " + dishId);
  }

  public void updateMenuItemAvailability(int dishId, boolean available) {
    MenuItem item = menu.getItemById(dishId);
    if (item != null) {
      item.setAvailability(available);
      System.out.println("✅ Updated availability for " + item.getDishName() + ": " + available);
    }
  }

  // ========== STATUS AND REPORTING ==========

  /** Gets current restaurant status. */
  public RestaurantStatus getStatus() {
    return new RestaurantStatus(
        isOpen,
        orderQueue.size(),
        chefs.size(),
        (int) chefs.stream().filter(chef -> !chef.isBusy()).count(),
        deliveryStaff.size(),
        (int) deliveryStaff.stream().filter(delivery -> !delivery.isBusy()).count(),
        stats.getTotalRevenue(),
        stats.getTotalOrdersProcessed());
  }

  /** Restaurant status data class. */
  public record RestaurantStatus(
      boolean isOpen,
      int ordersInQueue,
      int totalChefs,
      int availableChefs,
      int totalDeliveryStaff,
      int availableDeliveryStaff,
      double totalRevenue,
      int totalOrdersProcessed) {}

  /** Prints daily report. */
  private void printDailyReport() {
    System.out.println(
        "\n📊 DAILY REPORT - "
            + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    System.out.println("=".repeat(50));
    System.out.println("📦 Orders Processed: " + stats.getTotalOrdersProcessed());
    System.out.println("🚚 Orders Delivered: " + stats.getOrdersDelivered());
    System.out.println("💰 Total Revenue: $" + String.format("%.2f", stats.getTotalRevenue()));
    System.out.println("⭐ Popular Items:");

    stats.getPopularItems().entrySet().stream()
        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
        .limit(3)
        .forEach(
            entry ->
                System.out.println("   " + entry.getKey() + ": " + entry.getValue() + " sold"));
  }

  // ========== GETTERS ==========

  public String getName() {
    return name;
  }

  public String getAddress() {
    return address;
  }

  public Menu getMenu() {
    return menu;
  }

  public OrderQueue getOrderQueue() {
    return orderQueue;
  }

  public List<Chef> getChefs() {
    return new ArrayList<>(chefs);
  }

  public List<Delivery> getDeliveryStaff() {
    return new ArrayList<>(deliveryStaff);
  }

  public List<Customer> getCustomers() {
    return new ArrayList<>(customers);
  }

  public RestaurantStats getStats() {
    return stats;
  }

  public boolean isOpen() {
    return isOpen;
  }

  public LocalDateTime getOpenedAt() {
    return openedAt;
  }

  @Override
  public String toString() {
    return String.format(
        "Restaurant{name='%s', address='%s', isOpen=%s, chefs=%d, delivery=%d}",
        name, address, isOpen, chefs.size(), deliveryStaff.size());
  }
}
