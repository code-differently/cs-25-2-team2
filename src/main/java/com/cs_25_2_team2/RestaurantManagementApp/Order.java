package com.cs_25_2_team2.RestaurantManagementApp;

import java.sql.Date;
import java.util.List;

public class Order {
    private static int nextId = 1; // Static counter for generating unique IDs
    private final int id; // Order ID
    private final Customer customer; // Customer who placed the order
    private final List<CartItem> items; // Items in the order
    
    private final double totalPrice; // Total price of the order
    private final Date createdAt; // Timestamp when the order was created
    private Status status; // Order status
    
    public enum Status {
        Placed,
        Preparing,
        ReadyForDelivery,
        OutForDelivery,
        Delivered
    }

    public Order(int id, Customer customer, List<CartItem> items, Date createdAt) {
        this.id = nextId++;
        this.customer = customer;
        this.items = List.copyOf(items); // Create immutable copy of the items list
        this.status = Status.Placed;
        this.totalPrice = items.stream().mapToDouble(CartItem::getSubtotal).sum();
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() { 
        return customer; 
    }

    public List<CartItem> getItems() { 
        return items;
    }

    public double getTotalPrice() { 
        return totalPrice;
    }

    public Date getCreatedAt() { 
        return createdAt;
    }
    public Status getStatus() { 
        return status;
    }
     
    // Order status updates
    public void updateStatus(Status newStatus) {
        this.status = newStatus;
        notifyStatusChange();
        logStatusChange();
    }
    
    private void notifyStatusChange() {
        switch (status) {
            case Placed -> notifyChefAndCustomer();
            case Preparing -> notifyCustomer("Your order is being prepared");
            case ReadyForDelivery -> notifyCustomerAndDeliveryStaff();
            case OutForDelivery -> notifyCustomer("Your order is on the way");
            case Delivered -> notifyCustomerAndArchiveOrder();
            default -> {
                // No notification for unknown status
            }
        }
    }
    
    private void notifyChefAndCustomer() {
        System.out.println("Notifying chef about new order #" + id);
        System.out.println("Notifying customer: Your order #" + id + " has been placed");
    }
    
    private void notifyCustomer(String message) {
        System.out.println("Notifying customer: " + message + " (Order #" + id + ")");
    }
    
    private void notifyCustomerAndDeliveryStaff() {
        System.out.println("Notifying customer: Your order #" + id + " is ready for delivery");
        System.out.println("Notifying delivery staff about order #" + id);
    }
    
    private void notifyCustomerAndArchiveOrder() {
        System.out.println("Notifying customer: Your order #" + id + " has been delivered");
        System.out.println("Archiving order #" + id);
    }

    private void logStatusChange() {
        System.out.println("Order #" + id + " status changed to " + status);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Order #" + id + " for " + customer.getName() + "\n");
        for (CartItem ci : items) {
            sb.append(" - ").append(ci).append("\n");
        }
        sb.append("Total: $").append(totalPrice).append("\n");
        sb.append("Status: ").append(status).append("\n");
        sb.append("Created: ").append(createdAt);
        return sb.toString();
    }
}
