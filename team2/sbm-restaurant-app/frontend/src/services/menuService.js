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

// Mock menu data for development when backend is unavailable
const mockMenuItems = [
  {
    id: 1,
    name: "Texas Loaded Baked Potato",
    category: "Main",
    price: 7.99,
    calories: "650 cal",
    toppings: ["Cheese", "Bacon", "Sour Cream"]
  },
  {
    id: 2,
    name: "Aloo Tikki Chaat",
    category: "Main",
    price: 14.99,
    calories: "300 cal",
    toppings: ["Cheese", "Chicken", "Pico de Gallo"]
  },
  {
    id: 3,
    name: "Potato Salad",
    category: "Side",
    price: 5.99,
    calories: "357 cal",
    toppings: ["Cheese", "Green Onions", "Ranch"]
  },
  {
    id: 4,
    name: "Loaded Baked Potato Soup",
    category: "Soup",
    price: 5.99,
    calories: "450 cal",
    toppings: ["Cheese", "Green Onions", "Bacon"]
  }
];

// Menu API service functions
export const menuService = {
  // Get all menu items
  getAllMenuItems: async () => {
    try {
      const response = await api.get('/menu');
      return response.data;
    } catch (error) {
      console.warn('Backend API unreachable, using mock menu data:', error.message);
      return mockMenuItems;
    }
  },

  // Get menu item by ID
  getMenuItemById: async (id) => {
    try {
      const response = await api.get(`/menu/${id}`);
      return response.data;
    } catch (error) {
      console.warn(`Backend API unreachable for getMenuItemById(${id}), using mock data:`, error.message);
      return mockMenuItems.find(item => item.id === parseInt(id)) || null;
    }
  },

  // Get menu items by category
  getMenuItemsByCategory: async (category) => {
    try {
      const response = await api.get(`/menu/category/${category}`);
      return response.data;
    } catch (error) {
      console.warn(`Backend API unreachable for getMenuItemsByCategory(${category}), using mock data:`, error.message);
      return mockMenuItems.filter(item => 
        item.category.toLowerCase() === category.toLowerCase()
      );
    }
  },

  // Search menu items
  searchMenuItems: async (query) => {
    try {
      const response = await api.get(`/menu/search?q=${encodeURIComponent(query)}`);
      return response.data;
    } catch (error) {
      console.warn(`Backend API unreachable for searchMenuItems(${query}), using mock data:`, error.message);
      return mockMenuItems.filter(item =>
        item.name.toLowerCase().includes(query.toLowerCase()) ||
        item.category.toLowerCase().includes(query.toLowerCase()) ||
        item.toppings.some(topping => topping.toLowerCase().includes(query.toLowerCase()))
      );
    }
  },

  // Add new menu item (admin only)
  addMenuItem: async (menuItemData) => {
    try {
      const response = await api.post('/menu', menuItemData);
      return response.data;
    } catch (error) {
      console.warn('Backend API unreachable for addMenuItem(), using mock implementation:', error.message);
      const newItem = {
        ...menuItemData,
        id: Math.max(...mockMenuItems.map(item => item.id)) + 1
      };
      mockMenuItems.push(newItem);
      return newItem;
    }
  },

  // Update menu item (admin only)
  updateMenuItem: async (id, menuItemData) => {
    try {
      const response = await api.put(`/menu/${id}`, menuItemData);
      return response.data;
    } catch (error) {
      console.warn(`Backend API unreachable for updateMenuItem(${id}), using mock implementation:`, error.message);
      const itemIndex = mockMenuItems.findIndex(item => item.id === parseInt(id));
      if (itemIndex !== -1) {
        mockMenuItems[itemIndex] = { ...mockMenuItems[itemIndex], ...menuItemData };
        return mockMenuItems[itemIndex];
      }
      return null;
    }
  },

  // Delete menu item (admin only)
  deleteMenuItem: async (id) => {
    try {
      const response = await api.delete(`/menu/${id}`);
      return response.data;
    } catch (error) {
      console.warn(`Backend API unreachable for deleteMenuItem(${id}), using mock implementation:`, error.message);
      const itemIndex = mockMenuItems.findIndex(item => item.id === parseInt(id));
      if (itemIndex !== -1) {
        const deletedItem = mockMenuItems.splice(itemIndex, 1)[0];
        return `Menu item ${deletedItem.name} deleted successfully`;
      }
      return null;
    }
  },

  // Get all unique categories
  getCategories: async () => {
    try {
      const response = await api.get('/menu/categories');
      return response.data;
    } catch (error) {
      console.warn('Backend API unreachable for getCategories(), using mock data:', error.message);
      const categories = [...new Set(mockMenuItems.map(item => item.category))];
      return categories;
    }
  }
};

export default menuService;