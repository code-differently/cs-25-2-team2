import axios from 'axios';

// Configure axios base URL to match your Spring Boot backend
const API_BASE_URL = 'http://localhost:8080/api';

// Create axios instance with base configuration
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Order API service functions
export const orderService = {
  // Get all orders
  getAllOrders: async () => {
    const response = await api.get('/orders');
    return response.data;
  },

  // Get specific order by ID
  getOrderById: async (id) => {
    const response = await api.get(`/orders/${id}`);
    return response.data;
  },

  // Get all order items across orders
  getAllOrderItems: async () => {
    const response = await api.get('/orders/items');
    return response.data;
  },

  // Create new order
  createOrder: async (orderData) => {
    const response = await api.post('/orders', orderData);
    return response.data;
  },

  // Update order status
  updateOrderStatus: async (id, status) => {
    const response = await api.put(`/orders/${id}/status`, status, {
      headers: {
        'Content-Type': 'text/plain',
      },
    });
    return response.data;
  },

  // Cancel order
  cancelOrder: async (id) => {
    const response = await api.delete(`/orders/${id}`);
    return response.data;
  },

  // Get orders by customer ID
  getOrdersByCustomer: async (customerId) => {
    const response = await api.get(`/orders/customer/${customerId}`);
    return response.data;
  },

  // Get orders by status
  getOrdersByStatus: async (status) => {
    const response = await api.get(`/orders/status/${status}`);
    return response.data;
  },
};

export default orderService;