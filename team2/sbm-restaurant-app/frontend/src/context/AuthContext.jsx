'use client';

import React, { createContext, useState, useEffect, useContext } from 'react';
import { authService } from '../services/authService';

// Create auth context
export const AuthContext = createContext({
  user: null,
  loading: true,
  error: null,
  login: async () => {},
  logout: () => {},
  register: async () => {},
});

// Auth provider component
export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Load user from local storage on component mount
  useEffect(() => {
    const initAuth = () => {
      const currentUser = authService.getCurrentUser();
      setUser(currentUser);
      setLoading(false);
    };
    
    initAuth();
  }, []);

  // Login function
  const login = async (username, password) => {
    setLoading(true);
    setError(null);
    
    try {
      const loggedInUser = await authService.login(username, password);
      setUser(loggedInUser);
      return loggedInUser;
    } catch (err) {
      setError(err.message || 'Login failed');
      throw err;
    } finally {
      setLoading(false);
    }
  };

  // Logout function
  const logout = () => {
    authService.logout();
    setUser(null);
  };

  // Register function
  const register = async (userData) => {
    setLoading(true);
    setError(null);
    
    try {
      const result = await authService.register(userData);
      return result;
    } catch (err) {
      setError(err.message || 'Registration failed');
      throw err;
    } finally {
      setLoading(false);
    }
  };

  // Context value
  const value = {
    user,
    loading,
    error,
    login,
    logout,
    register,
    isLoggedIn: !!user,
    hasRole: (role) => user && user.role === role
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

// Custom hook to use auth context
export const useAuth = () => useContext(AuthContext);

export default AuthProvider;