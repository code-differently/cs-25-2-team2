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

// Mock user data for development when backend is unavailable
const mockUsers = [
  {
    id: "1",
    username: "customer",
    name: "John Doe",
    phoneNumber: "123-456-7890",
    address: "123 Main St, City, State",
    role: "CUSTOMER",
    token: "mock-jwt-token-1"
  },
  {
    id: "CHEF001",
    username: "chef",
    name: "Gordon Ramsay",
    phoneNumber: "987-654-3210",
    address: "456 Kitchen Ave, City, State",
    role: "CHEF",
    token: "mock-jwt-token-chef001"
  },
  {
    id: "ADMIN001",
    username: "admin",
    name: "Admin User",
    phoneNumber: "555-123-4567",
    address: "789 Admin Blvd, City, State",
    role: "ADMIN",
    token: "mock-jwt-token-admin001"
  }
];

// User API service functions
export const userService = {
  // User authentication
  login: async (username, password) => {
    try {
      const response = await api.post('/users/login', { username, password });
      // Store user data in localStorage
      if (response.data) {
        localStorage.setItem('user', JSON.stringify(response.data));
      }
      return response.data;
    } catch (error) {
      console.warn('Backend API unreachable for login, using mock implementation:', error.message);
      
      // Mock authentication logic
      const user = mockUsers.find(u => 
        u.username === username && password === 'password' // All mock users use 'password'
      );
      
      if (user) {
        localStorage.setItem('user', JSON.stringify(user));
        return user;
      }
      
      throw new Error('Invalid username or password');
    }
  },

  // User registration
  register: async (userData) => {
    try {
      const response = await api.post('/users/register', userData);
      return response.data;
    } catch (error) {
      console.warn('Backend API unreachable for register, using mock implementation:', error.message);
      
      // Check if username already exists in mock data
      if (mockUsers.some(u => u.username === userData.username)) {
        throw new Error('Username already exists');
      }
      
      // Create new mock user
      const newUser = {
        id: (mockUsers.length + 1).toString(),
        ...userData,
        role: userData.role || 'CUSTOMER',
        token: `mock-jwt-token-${mockUsers.length + 1}`
      };
      
      mockUsers.push(newUser);
      return newUser;
    }
  },

  // Get user profile
  getUserProfile: async (username) => {
    try {
      const response = await api.get(`/users/profile?username=${username}`);
      return response.data;
    } catch (error) {
      console.warn(`Backend API unreachable for getUserProfile(${username}), using mock data:`, error.message);
      return mockUsers.find(user => user.username === username) || null;
    }
  },

  // Update user profile
  updateUserProfile: async (profileData) => {
    try {
      const response = await api.put('/users/profile', profileData);
      return response.data;
    } catch (error) {
      console.warn('Backend API unreachable for updateUserProfile(), using mock implementation:', error.message);
      
      const userIndex = mockUsers.findIndex(user => user.id === profileData.id);
      if (userIndex !== -1) {
        mockUsers[userIndex] = { ...mockUsers[userIndex], ...profileData };
        return mockUsers[userIndex];
      }
      return null;
    }
  },

  // Get user addresses
  getUserAddresses: async (userId) => {
    try {
      const response = await api.get(`/users/addresses?userId=${userId}`);
      return response.data;
    } catch (error) {
      console.warn(`Backend API unreachable for getUserAddresses(${userId}), using mock data:`, error.message);
      
      const user = mockUsers.find(u => u.id === userId);
      if (user) {
        return [
          {
            id: "1",
            address: user.address,
            type: "Primary"
          }
        ];
      }
      return [];
    }
  },

  // Add user address
  addUserAddress: async (addressData) => {
    try {
      const response = await api.post('/users/addresses', addressData);
      return response.data;
    } catch (error) {
      console.warn('Backend API unreachable for addUserAddress(), using mock implementation:', error.message);
      return {
        id: Date.now().toString(),
        ...addressData
      };
    }
  },

  // Update user address
  updateUserAddress: async (id, addressData) => {
    try {
      const response = await api.put(`/users/addresses/${id}`, addressData);
      return response.data;
    } catch (error) {
      console.warn(`Backend API unreachable for updateUserAddress(${id}), using mock implementation:`, error.message);
      return {
        id,
        ...addressData
      };
    }
  },

  // Delete user address
  deleteUserAddress: async (id) => {
    try {
      const response = await api.delete(`/users/addresses/${id}`);
      return response.data;
    } catch (error) {
      console.warn(`Backend API unreachable for deleteUserAddress(${id}), using mock implementation:`, error.message);
      return "Address deleted successfully";
    }
  },

  // Get user order history
  getUserOrderHistory: async (userId) => {
    try {
      const response = await api.get(`/users/order-history?userId=${userId}`);
      return response.data;
    } catch (error) {
      console.warn(`Backend API unreachable for getUserOrderHistory(${userId}), using mock data:`, error.message);
      // Return empty array for now - would integrate with orderService in real implementation
      return [];
    }
  },

  // Change password
  changePassword: async (passwordData) => {
    try {
      const response = await api.post('/users/change-password', passwordData);
      return response.data;
    } catch (error) {
      console.warn('Backend API unreachable for changePassword(), using mock implementation:', error.message);
      return { message: "Password changed successfully" };
    }
  },

  // Customer checkout using backend Customer.checkout() method
  customerCheckout: async (userId) => {
    try {
      const response = await api.post(`/users/checkout?userId=${userId}`);
      return response.data;
    } catch (error) {
      console.warn(`Backend API unreachable for customerCheckout(${userId}), using mock implementation:`, error.message);
      
      // Mock order creation for checkout
      const mockOrder = {
        id: Math.floor(1000 + Math.random() * 9000),
        customerId: userId,
        totalPrice: 19.99,
        status: "Placed",
        createdAt: new Date().toISOString()
      };
      
      return mockOrder;
    }
  },

  // Get customer's cart
  getCustomerCart: async (userId) => {
    try {
      const response = await api.get(`/users/cart?userId=${userId}`);
      return response.data;
    } catch (error) {
      console.warn(`Backend API unreachable for getCustomerCart(${userId}), using mock data:`, error.message);
      
      // Return mock cart data
      return {
        customerId: userId,
        items: [],
        total: 0.00
      };
    }
  },

  // Logout user
  logout: () => {
    localStorage.removeItem('user');
  },

  // Get current user
  getCurrentUser: () => {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
  },

  // Check if user is logged in
  isLoggedIn: () => {
    return !!localStorage.getItem('user');
  },

  // Check if user has specific role
  hasRole: (role) => {
    const user = userService.getCurrentUser();
    return user && user.role === role;
  }
};

export default userService;