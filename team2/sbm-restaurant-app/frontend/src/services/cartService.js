import { authService } from './authService';
import { orderService } from './orderService';

// Cart Service for managing shopping cart functionality
export const cartService = {
  // Constants
  CART_STORAGE_KEY: 'spud_cart',
  
  // Custom event for cart updates
  dispatchCartUpdateEvent: () => {
    // Create and dispatch a custom event when cart changes
    const event = new Event('cartUpdated');
    window.dispatchEvent(event);
    
    // Also dispatch a storage event for cross-component communication
    // This helps with updating UI elements in different components
    window.dispatchEvent(new StorageEvent('storage', {
      key: cartService.CART_STORAGE_KEY,
    }));
  },
  
  // Get cart items from localStorage
  getCart: () => {
    try {
      const savedCart = localStorage.getItem(cartService.CART_STORAGE_KEY);
      return savedCart ? JSON.parse(savedCart) : [];
    } catch (error) {
      console.error('Error getting cart from localStorage:', error);
      return [];
    }
  },
  
  // Save cart to localStorage
  saveCart: (cart) => {
    try {
      localStorage.setItem(cartService.CART_STORAGE_KEY, JSON.stringify(cart));
      // Dispatch custom event to notify components about cart changes
      cartService.dispatchCartUpdateEvent();
    } catch (error) {
      console.error('Error saving cart to localStorage:', error);
    }
  },
  
  // Add item to cart
  addToCart: (item, quantity = 1) => {
    const cart = cartService.getCart();
    
    // Check if item already exists
    const existingItemIndex = cart.findIndex(cartItem => cartItem.id === item.id);
    
    if (existingItemIndex !== -1) {
      // Update quantity if item exists
      cart[existingItemIndex].quantity += quantity;
    } else {
      // Add new item
      cart.push({
        ...item,
        quantity
      });
    }
    
    cartService.saveCart(cart);
    return cart;
  },
  
  // Update item quantity
  updateQuantity: (itemId, quantity) => {
    const cart = cartService.getCart();
    
    const updatedCart = cart.map(item => 
      item.id === itemId ? { ...item, quantity } : item
    );
    
    cartService.saveCart(updatedCart);
    return updatedCart;
  },
  
  // Remove item from cart
  removeItem: (itemId) => {
    const cart = cartService.getCart();
    
    const updatedCart = cart.filter(item => item.id !== itemId);
    
    cartService.saveCart(updatedCart);
    return updatedCart;
  },
  
  // Clear cart
  clearCart: () => {
    localStorage.removeItem(cartService.CART_STORAGE_KEY);
    // Dispatch custom event to notify components about cart changes
    cartService.dispatchCartUpdateEvent();
    return [];
  },
  
  // Calculate cart totals
  getCartTotals: (cart = null) => {
    const items = cart || cartService.getCart();
    
    const subtotal = items.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    const tax = subtotal * 0.08; // 8% tax rate
    const total = subtotal + tax;
    
    return {
      subtotal: parseFloat(subtotal.toFixed(2)),
      tax: parseFloat(tax.toFixed(2)),
      total: parseFloat(total.toFixed(2)),
      itemCount: items.reduce((count, item) => count + item.quantity, 0)
    };
  },
  
  // Convert cart to order format
  cartToOrder: async () => {
    const cart = cartService.getCart();
    const totals = cartService.getCartTotals(cart);
    
    // Check if user is logged in
    const user = authService.getCurrentUser();
    if (!user) {
      throw new Error('User must be logged in to create an order');
    }
    
    // Convert cart items to order items format
    const orderItems = cart.map(item => ({
      menu_item_id: item.id,
      name: item.name,
      price: item.price,
      quantity: item.quantity,
      subtotal: item.price * item.quantity
    }));
    
    return {
      customer_id: user.id,
      customer_name: user.name || user.username,
      items: orderItems,
      total_amount: totals.total,
      status: "Placed"
    };
  },
  
  // Create order from cart
  checkout: async (specialInstructions = '') => {
    try {
      // Convert cart to order format
      const orderData = await cartService.cartToOrder();
      
      // Add special instructions if provided
      if (specialInstructions) {
        orderData.special_instructions = specialInstructions;
      }
      
      // Create order
      const createdOrder = await orderService.createOrder(orderData);
      
      // Clear cart after successful order creation
      cartService.clearCart();
      
      return createdOrder;
    } catch (error) {
      console.error('Error during checkout:', error);
      throw error;
    }
  }
};

export default cartService;