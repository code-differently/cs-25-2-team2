import axios from 'axios';

// Configure axios base URL to match your Spring Boot backend
const API_BASE_URL = 'http://localhost:8080/api';

// Create axios instance with base configuration
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  // Add a longer timeout to handle slow connections
  timeout: 10000,
});

// Mock data for development when backend is unavailable
const mockOrders = [
  {
    id: 101,
    customer: { id: 1, name: "John Doe", address: "123 Main St", phone: "555-1234" },
    items: [
      { 
        id: 1, 
        menuItem: { id: 1, name: "French Fries", price: 3.99 },
        name: "French Fries",
        quantity: 2, 
        subtotal: 7.98 
      },
      { 
        id: 2,
        menuItem: { id: 2, name: "Loaded Potato Skins", price: 5.99 },
        name: "Loaded Potato Skins",
        quantity: 1, 
        subtotal: 5.99
      }
    ],
    totalPrice: 13.97,
    createdAt: "2025-10-16T10:30:00",
    status: "Delivered"
  },
  {
    id: 102,
    customer: { id: 1, name: "John Doe", address: "123 Main St", phone: "555-1234" },
    items: [
      { 
        id: 3,
        menuItem: { id: 3, name: "Potato Soup", price: 4.99 },
        name: "Potato Soup",
        quantity: 1, 
        subtotal: 4.99
      }
    ],
    totalPrice: 4.99,
    createdAt: "2025-10-16T12:45:00",
    status: "Preparing"
  },
  {
    id: 103,
    customer: { id: 1, name: "John Doe", address: "123 Main St", phone: "555-1234" },
    items: [
      { 
        id: 4,
        menuItem: { id: 4, name: "Baked Potato", price: 4.49 },
        name: "Baked Potato",
        quantity: 2, 
        subtotal: 8.98
      },
      { 
        id: 5,
        menuItem: { id: 5, name: "Sweet Potato Fries", price: 4.29 },
        name: "Sweet Potato Fries", 
        quantity: 1, 
        subtotal: 4.29
      }
    ],
    totalPrice: 13.27,
    createdAt: "2025-10-15T18:20:00",
    status: "Placed"
  }
];

// Order API service functions
export const orderService = {
  // Get all orders
  getAllOrders: async () => {
    try {
      const response = await api.get('/orders');
      return response.data;
    } catch (error) {
      console.warn('Backend API unreachable, using mock data instead:', error.message);
      return mockOrders;
    }
  },

  // Get specific order by ID
  getOrderById: async (id) => {
    try {
      const response = await api.get(`/orders/${id}`);
      return response.data;
    } catch (error) {
      console.warn(`Backend API unreachable for getOrderById(${id}), using mock data:`, error.message);
      return mockOrders.find(order => order.id === parseInt(id)) || null;
    }
  },

  // Get all order items across orders
  getAllOrderItems: async () => {
    try {
      const response = await api.get('/orders/items');
      return response.data;
    } catch (error) {
      console.warn('Backend API unreachable for getAllOrderItems(), using mock data:', error.message);
      return mockOrders.flatMap(order => order.items);
    }
  },

  // Create new order
  createOrder: async (orderData) => {
    try {
      const response = await api.post('/orders', orderData);
      return response.data;
    } catch (error) {
      console.warn('Backend API unreachable for createOrder(), using mock implementation:', error.message);
      // Create a mock implementation that simulates creating an order
      const newOrder = {
        ...orderData,
        id: Math.floor(1000 + Math.random() * 9000), // Generate a random 4-digit ID
        createdAt: new Date().toISOString(),
        status: "Placed"
      };
      mockOrders.push(newOrder);
      return newOrder;
    }
  },

  // Update order status
  updateOrderStatus: async (id, status) => {
    try {
      const response = await api.put(`/orders/${id}/status`, status, {
        headers: {
          'Content-Type': 'text/plain',
        },
      });
      return response.data;
    } catch (error) {
      console.warn(`Backend API unreachable for updateOrderStatus(${id}, ${status}), using mock implementation:`, error.message);
      const orderToUpdate = mockOrders.find(order => order.id === parseInt(id));
      if (orderToUpdate) {
        orderToUpdate.status = status;
        return orderToUpdate;
      }
      return null;
    }
  },

  // Cancel order
  cancelOrder: async (id) => {
    try {
      const response = await api.delete(`/orders/${id}`);
      return response.data;
    } catch (error) {
      console.warn(`Backend API unreachable for cancelOrder(${id}), using mock implementation:`, error.message);
      const orderIndex = mockOrders.findIndex(order => order.id === parseInt(id));
      if (orderIndex !== -1) {
        const deletedOrder = mockOrders.splice(orderIndex, 1)[0];
        return `Order #${id} cancelled successfully`;
      }
      return null;
    }
  },

  // Get orders by customer ID
  getOrdersByCustomer: async (customerId) => {
    try {
      const response = await api.get(`/orders/customer/${customerId}`);
      return response.data;
    } catch (error) {
      console.warn(`Backend API unreachable for getOrdersByCustomer(${customerId}), using mock data:`, error.message);
      return mockOrders.filter(order => order.customer.id === parseInt(customerId));
    }
  },

  // Get orders by status
  getOrdersByStatus: async (status) => {
    try {
      const response = await api.get(`/orders/status/${status}`);
      return response.data;
    } catch (error) {
      console.warn(`Backend API unreachable for getOrdersByStatus(${status}), using mock data:`, error.message);
      return mockOrders.filter(order => order.status === status);
    }
  },
};

export default orderService;