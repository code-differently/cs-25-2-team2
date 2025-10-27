import axios from 'axios';

// Configure axios base URL to match your Spring Boot backend
const API_BASE_URL = 'http://localhost:8080/api';

// Create axios instance with base configuration
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
});

// Mock kitchen data for development when backend is unavailable
const mockChefs = [
  {
    id: "CHEF001",
    name: "Gordon Ramsay",
    phoneNumber: "987-654-3210",
    address: "456 Kitchen Ave",
    role: "CHEF",
    isBusy: false,
    assignedOrdersCount: 0
  },
  {
    id: "CHEF002",
    name: "Julia Child",
    phoneNumber: "555-987-6543",
    address: "789 Culinary St",
    role: "CHEF",
    isBusy: true,
    assignedOrdersCount: 2
  },
  {
    id: "CHEF003",
    name: "Anthony Bourdain",
    phoneNumber: "444-555-6666",
    address: "321 Food Blvd",
    role: "CHEF",
    isBusy: false,
    assignedOrdersCount: 1
  }
];

const mockKitchenOrders = [
  {
    id: 101,
    customerId: 1,
    customerName: "John Doe",
    totalPrice: 13.97,
    status: "Preparing",
    createdAt: "2025-10-22T10:30:00",
    items: ["Texas Loaded Baked Potato x2", "Potato Salad x1"]
  },
  {
    id: 102,
    customerId: 1,
    customerName: "John Doe",
    totalPrice: 4.99,
    status: "ReadyForDelivery",
    createdAt: "2025-10-22T12:45:00",
    items: ["Loaded Baked Potato Soup x1"]
  },
  {
    id: 103,
    customerId: 2,
    customerName: "Jane Smith",
    totalPrice: 14.99,
    status: "Delivered",
    createdAt: "2025-10-22T11:20:00",
    items: ["Aloo Tikki Chaat x1"]
  }
];

// Kitchen API service functions
export const kitchenService = {
  // Get all pending orders for kitchen preparation
  getPendingOrders: async () => {
    try {
      const response = await api.get('/kitchen/orders/pending');
      return response.data;
    } catch (error) {
      console.warn('Backend API unreachable for getPendingOrders(), using mock data:', error.message);
      return mockKitchenOrders.filter(order => [
        'Pending',
        'Delivered',
        'Preparing',
        'Out for Delivery',
        'ReadyForDelivery',
        'Cancelled'
      ].includes(order.status));
    }
  },

  // Start preparing an order - assigns to available chef
  startPreparingOrder: async (orderId) => {
    try {
      const response = await api.put(`/kitchen/orders/${orderId}/start`);
      return response.data;
    } catch (error) {
      console.warn(`Backend API unreachable for startPreparingOrder(${orderId}), using mock implementation:`, error.message);
      
      const order = mockKitchenOrders.find(o => o.id === orderId);
      if (order && order.status === 'Pending') {
        order.status = 'Preparing';
        return {
          success: true,
          message: "Order preparation started successfully",
          orderId: orderId
        };
      }
      
      return {
        success: false,
        message: "Failed to start order preparation - no available chef or order not found"
      };
    }
  },

  // Mark order as ready/complete
  completeOrder: async (orderId) => {
    try {
      const response = await api.put(`/kitchen/orders/${orderId}/complete`);
      return response.data;
    } catch (error) {
      console.warn(`Backend API unreachable for completeOrder(${orderId}), using mock implementation:`, error.message);
      
      const order = mockKitchenOrders.find(o => o.id === orderId);
      if (order && order.status === 'Preparing') {
        order.status = 'Out for Delivery';
        return {
          success: true,
          message: "Order marked as out for delivery",
          orderId: orderId
        };
      }
      if (order && order.status === 'Out for Delivery') {
        order.status = 'ReadyForDelivery';
        return {
          success: true,
          message: "Order marked as ready for delivery",
          orderId: orderId
        };
      }
      
      return {
        success: false,
        message: "Failed to complete order - order not found or already completed"
      };
    }
  },

  // Get the complete kitchen order queue
  getOrderQueue: async () => {
    try {
      const response = await api.get('/kitchen/order-queue');
      return response.data;
    } catch (error) {
      console.warn('Backend API unreachable for getOrderQueue(), using mock data:', error.message);
      
      return {
        pending: mockKitchenOrders.filter(order => order.status === 'Pending'),
        delivered: mockKitchenOrders.filter(order => order.status === 'Delivered'),
        preparing: mockKitchenOrders.filter(order => order.status === 'Preparing'),
        outForDelivery: mockKitchenOrders.filter(order => order.status === 'Out for Delivery'),
        readyForDelivery: mockKitchenOrders.filter(order => order.status === 'ReadyForDelivery'),
        cancelled: mockKitchenOrders.filter(order => order.status === 'Cancelled')
      };
    }
  },

  // Get estimated preparation time
  getEstimatedPreparationTime: async () => {
    try {
      const response = await api.post('/kitchen/estimate-time');
      return response.data;
    } catch (error) {
      console.warn('Backend API unreachable for getEstimatedPreparationTime(), using mock data:', error.message);
      
  const pendingCount = mockKitchenOrders.filter(o => o.status === 'Pending').length;
  const deliveredCount = mockKitchenOrders.filter(o => o.status === 'Delivered').length;
  const preparingCount = mockKitchenOrders.filter(o => o.status === 'Preparing').length;
  const outForDeliveryCount = mockKitchenOrders.filter(o => o.status === 'Out for Delivery').length;
  const readyForDeliveryCount = mockKitchenOrders.filter(o => o.status === 'ReadyForDelivery').length;
  const cancelledCount = mockKitchenOrders.filter(o => o.status === 'Cancelled').length;
      const availableChefs = mockChefs.filter(chef => !chef.isBusy).length;
      
  const totalOrders = pendingCount + deliveredCount + preparingCount + outForDeliveryCount + readyForDeliveryCount + cancelledCount;
      const estimatedMinutes = availableChefs > 0 ? Math.ceil((totalOrders * 10) / availableChefs) : -1;
      
      return {
        estimatedMinutes,
        estimatedTime: estimatedMinutes > 0 ? `${estimatedMinutes} minutes` : "No chefs available",
        availableChefs,
        totalChefs: mockChefs.length
      };
    }
  },

  // Get all chefs in the kitchen
  getAllChefs: async () => {
    try {
      const response = await api.get('/kitchen/chefs');
      return response.data;
    } catch (error) {
      console.warn('Backend API unreachable for getAllChefs(), using mock data:', error.message);
      return mockChefs;
    }
  },

  // Get available chefs (not busy)
  getAvailableChefs: async () => {
    try {
      const response = await api.get('/kitchen/chefs/available');
      return response.data;
    } catch (error) {
      console.warn('Backend API unreachable for getAvailableChefs(), using mock data:', error.message);
      return mockChefs.filter(chef => !chef.isBusy);
    }
  },

  // Get orders assigned to a specific chef
  getOrdersForChef: async (chefId) => {
    try {
      const response = await api.get(`/kitchen/chefs/${chefId}/orders`);
      return response.data;
    } catch (error) {
      console.warn(`Backend API unreachable for getOrdersForChef(${chefId}), using mock data:`, error.message);
      
      // For mock data, return preparing orders for busy chefs
      const chef = mockChefs.find(c => c.id === chefId);
      if (chef && chef.isBusy) {
        return mockKitchenOrders.filter(order => order.status === 'Preparing').slice(0, chef.assignedOrdersCount);
      }
      return [];
    }
  },

  // Get kitchen statistics for admin dashboard
  getKitchenStatistics: async () => {
    try {
      const response = await api.get('/kitchen/statistics');
      return response.data;
    } catch (error) {
      console.warn('Backend API unreachable for getKitchenStatistics(), using mock data:', error.message);
      
      const availableChefs = mockChefs.filter(chef => !chef.isBusy).length;
      const estimatedTime = await kitchenService.getEstimatedPreparationTime();
      
      return {
        totalChefs: mockChefs.length,
        availableChefs,
        pendingOrders: mockKitchenOrders.filter(o => o.status === 'Pending').length,
        deliveredOrders: mockKitchenOrders.filter(o => o.status === 'Delivered').length,
        preparingOrders: mockKitchenOrders.filter(o => o.status === 'Preparing').length,
        outForDeliveryOrders: mockKitchenOrders.filter(o => o.status === 'Out for Delivery').length,
        readyForDeliveryOrders: mockKitchenOrders.filter(o => o.status === 'ReadyForDelivery').length,
        cancelledOrders: mockKitchenOrders.filter(o => o.status === 'Cancelled').length,
        estimatedWaitTime: estimatedTime.estimatedMinutes
      };
    }
  },

  // Add order to kitchen queue (called from order creation)
  addOrderToQueue: async (orderData) => {
    try {
      const response = await api.post('/kitchen/orders', orderData);
      return response.data;
    } catch (error) {
      console.warn('Backend API unreachable for addOrderToQueue(), using mock implementation:', error.message);
      
      // Add to mock orders
      const newOrder = {
        id: Math.floor(1000 + Math.random() * 9000),
        ...orderData,
        status: 'Pending',
        createdAt: new Date().toISOString()
      };
      
      mockKitchenOrders.push(newOrder);
      
      return {
        success: true,
        message: "Order added to kitchen queue"
      };
    }
  }
};

export default kitchenService;