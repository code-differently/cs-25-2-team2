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
    id: 1,
    username: 'customer',
    password: 'password',
    name: 'John Doe',
    phoneNumber: '123-456-7890',
    role: 'CUSTOMER'
  },
  {
    id: 2,
    username: 'chef',
    password: 'password',
    name: 'Gordon Ramsay',
    phoneNumber: '987-654-3210',
    role: 'CHEF'
  },
  {
    id: 3,
    username: 'admin',
    password: 'password',
    name: 'Admin User',
    phoneNumber: '555-123-4567',
    role: 'ADMIN'
  }
];

// Authentication service
export const authService = {
  // Login user
  login: async (username, password) => {
    try {
      const response = await api.post('/auth/login', { username, password });
      // Store token/user data in localStorage
      localStorage.setItem('user', JSON.stringify(response.data));
      return response.data;
    } catch (error) {
      console.warn('Backend API unreachable for login, using mock implementation:', error.message);
      
      // Mock authentication logic
      const user = mockUsers.find(u => 
        u.username === username && u.password === password
      );
      
      if (user) {
        // Remove password from user object before storing
        const { password, ...userWithoutPassword } = user;
        const mockResponse = {
          ...userWithoutPassword,
          token: `mock-jwt-token-${user.id}-${Date.now()}`
        };
        
        // Store user data in localStorage
        localStorage.setItem('user', JSON.stringify(mockResponse));
        return mockResponse;
      }
      
      throw new Error('Invalid username or password');
    }
  },

  // Register new user
  register: async (userData) => {
    try {
      const response = await api.post('/auth/register', userData);
      return response.data;
    } catch (error) {
      console.warn('Backend API unreachable for register, using mock implementation:', error.message);
      
      // Check if username already exists in mock data
      if (mockUsers.some(u => u.username === userData.username)) {
        throw new Error('Username already exists');
      }
      
      // Create new mock user
      const newUser = {
        id: mockUsers.length + 1,
        ...userData,
        role: 'CUSTOMER'
      };
      
      mockUsers.push(newUser);
      
      // Return user without password
      const { password, ...userWithoutPassword } = newUser;
      return userWithoutPassword;
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
    const user = authService.getCurrentUser();
    return user && user.role === role;
  }
};

export default authService;