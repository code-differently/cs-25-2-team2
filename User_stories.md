# User Stories & Acceptance Criteria

## 1. User Story: Browse Items
**As a user, I want to browse an item so that I can see what is available.**

### Acceptance Criteria
- The system displays a list of available menu items.  
- Each menu item shows its **name**, **description**, and **price**.  
- If no items are available, the system shows a *“No items available”* message.  

---

## 2. User Story: Add to Cart
**As a user, I want to select an item so that I can add it to my cart and see total.**

### Acceptance Criteria
- The user can select a menu item and add it to their cart.  
- The cart updates immediately with the added item and calculates total for Checkout.  
- The system prevents adding unavailable or out-of-stock items.
- The check out process creates an immutable order.

---

## 3. User Story: Deliver Order 
**As a delivery person, I want to mark an order as delivered after completing a delivery so that the customer knows their order has been delivered successfully.**

### Acceptance Criteria
- When I receive notification that an order is ready for pickup and delivery, I will collect the order and deliver it to the correct customer.    
- I can mark the order as **"Delivered"** in the system once I have completed the delivery.  
- The customer receives a confirmation (in-app or message) that their order has been delivered.  
