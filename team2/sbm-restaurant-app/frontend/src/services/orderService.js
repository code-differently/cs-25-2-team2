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
    id: 200,
    customer: { id: "1", name: "John Doe", address: "123 Main St, City, State", phoneNumber: "123-456-7890" },
    chef: { id: "CHEF001", name: "Gordon Ramsay" },
    items: [
      {
        id: 0,
        menuItem: {
          id: 2,
          name: "Aloo Tikki Chaat",
          category: "Main",
          price: 14.99
        },
        name: "Aloo Tikki Chaat",
        quantity: 1,
        subtotal: 14.99
      }
    ],
    totalPrice: 14.99,
    createdAt: "2025-10-15T09:00:00",
  status: "Pending"
  },
  {
    id: 201,
    customer: { id: "1", name: "John Doe", address: "123 Main St, City, State", phoneNumber: "123-456-7890" },
    chef: { id: "CHEF001", name: "Gordon Ramsay" },
    items: [
      {
        id: 1,
        menuItem: {
          id: 1,
          name: "Texas Loaded Baked Potato",
          category: "Main",
          price: 7.99
        },
        name: "Texas Loaded Baked Potato",
        quantity: 2,
        subtotal: 15.98
      },
      {
        id: 2,
        menuItem: {
          id: 3,
          name: "Potato Salad",
          category: "Side",
          price: 5.99
        },
        name: "Potato Salad",
        quantity: 1,
        subtotal: 5.99
      }
    ],
    totalPrice: 21.97,
    createdAt: "2025-10-16T10:30:00",
  status: "Delivered"
  },
  {
    id: 202,
    customer: { id: "1", name: "John Doe", address: "123 Main St, City, State", phoneNumber: "123-456-7890" },
    chef: { id: "CHEF001", name: "Gordon Ramsay" },
    items: [
      {
        id: 3,
        menuItem: {
          id: 2,
          name: "Aloo Tikki Chaat",
          category: "Main",
          price: 14.99
        },
        name: "Aloo Tikki Chaat",
        quantity: 1,
        subtotal: 14.99
      },
      {
        id: 4,
        menuItem: {
          id: 4,
          name: "Loaded Baked Potato Soup",
          category: "Soup",
          price: 5.99
        },
        name: "Loaded Baked Potato Soup",
        quantity: 2,
        subtotal: 11.98
      }
    ],
    totalPrice: 26.97,
    createdAt: "2025-10-17T12:45:00",
  status: "Preparing"
  },
  {
    id: 203,
    customer: { id: "1", name: "John Doe", address: "123 Main St, City, State", phoneNumber: "123-456-7890" },
    chef: { id: "CHEF001", name: "Gordon Ramsay" },
    items: [
      {
        id: 5,
        menuItem: {
          id: 4,
          name: "Loaded Baked Potato Soup",
          category: "Soup",
          price: 5.99
        },
        name: "Loaded Baked Potato Soup",
        quantity: 1,
        subtotal: 5.99
      },
      {
        id: 6,
        menuItem: {
          id: 3,
          name: "Potato Salad",
          category: "Side",
          price: 5.99
        },
        name: "Potato Salad",
        quantity: 2,
        subtotal: 11.98
      }
    ],
    totalPrice: 17.97,
    createdAt: "2025-10-18T18:20:00",
  status: "OutForDelivery"
  },
  {
    id: 204,
    customer: { id: "1", name: "John Doe", address: "123 Main St, City, State", phoneNumber: "123-456-7890" },
    chef: { id: "CHEF001", name: "Gordon Ramsay" },
    items: [
      {
        id: 7,
        menuItem: {
          id: 2,
          name: "Aloo Tikki Chaat",
          category: "Main",
          price: 14.99
        },
        name: "Aloo Tikki Chaat",
        quantity: 2,
        subtotal: 29.98
      }
    ],
    totalPrice: 29.98,
    createdAt: "2025-10-19T09:15:00",
  status: "ReadyForDelivery"
  },
  {
    id: 205,
    customer: { id: "1", name: "John Doe", address: "123 Main St, City, State", phoneNumber: "123-456-7890" },
    chef: { id: "CHEF001", name: "Gordon Ramsay" },
    items: [
      {
        id: 8,
        menuItem: {
          id: 1,
          name: "Texas Loaded Baked Potato",
          category: "Main",
          price: 7.99
        },
        name: "Texas Loaded Baked Potato",
        quantity: 1,
        subtotal: 7.99
      },
      {
        id: 9,
        menuItem: {
          id: 2,
          name: "Aloo Tikki Chaat",
          category: "Main",
          price: 14.99
        },
        name: "Aloo Tikki Chaat",
        quantity: 1,
        subtotal: 14.99
      },
      {
        id: 10,
        menuItem: {
          id: 3,
          name: "Potato Salad",
          category: "Side",
          price: 5.99
        },
        name: "Potato Salad",
        quantity: 1,
        subtotal: 5.99
      }
    ],
    totalPrice: 28.97,
    createdAt: "2025-10-20T14:00:00",
  status: "Cancelled"
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