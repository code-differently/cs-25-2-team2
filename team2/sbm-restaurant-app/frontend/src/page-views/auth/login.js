"use client";

import React, { useState, useEffect } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import Link from "next/link";
import "../../../styles/auth.scss";
import { authService } from '../../services/authService';
import { User, Lock, LogIn, UserPlus, AlertCircle } from 'lucide-react';
import '../../../styles/auth.scss';

export default function LoginPage() {
  const [isLogin, setIsLogin] = useState(true); // Toggle between login and register
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    confirmPassword: '',
    name: '',
    phoneNumber: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const router = useRouter();
  const searchParams = useSearchParams();

  // Check if user is already logged in on component mount and handle mode parameter
  useEffect(() => {
    if (authService.isLoggedIn()) {
      router.push('/'); // Redirect to home if already logged in
      return;
    }
    
    // Check for register mode from URL parameters
    const mode = searchParams.get('mode');
    if (mode === 'register') {
      setIsLogin(false);
    }
  }, [router, searchParams]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      if (isLogin) {
        // Handle login
        const { username, password } = formData;
        await authService.login(username, password);
        router.push('/'); // Redirect to home after successful login
      } else {
        // Handle registration
        const { username, password, confirmPassword, name, phoneNumber } = formData;
        
        // Validation
        if (password !== confirmPassword) {
          setError('Passwords do not match');
          setLoading(false);
          return;
        }
        
        await authService.register({ username, password, name, phoneNumber });
        // Switch to login mode after successful registration
        setIsLogin(true);
        setFormData({
          ...formData,
          password: '',
          confirmPassword: '',
        });
        setError('Registration successful! Please log in.');
      }
    } catch (err) {
      setError(err.message || 'An error occurred');
    } finally {
      setLoading(false);
    }
  };

  const toggleMode = () => {
    setIsLogin(!isLogin);
    setError(null);
  };

  return (
    <div className="auth-page flex min-h-screen items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
      <div className="auth-container w-full max-w-md space-y-8 p-8 rounded-2xl shadow-lg border transition-all duration-300 hover:shadow-xl">
        <div className="text-center">
          <h1 className="auth-title text-3xl font-bold mb-2">
            {isLogin ? 'Welcome Back' : 'Create an Account'}
          </h1>
          <p className="auth-subtitle mb-8">
            {isLogin 
              ? 'Sign in to access your account' 
              : 'Join Spud Munch Bunch for a potato-filled adventure'}
          </p>
        </div>

        {error && (
          <div className={`p-4 rounded-md flex items-center space-x-2 ${error.includes('successful') ? 'bg-green-50 text-green-700' : 'bg-red-50 text-red-700'}`} style={{
            backgroundColor: error.includes('successful') ? 'rgba(34, 197, 94, 0.1)' : 'rgba(239, 68, 68, 0.1)',
            color: error.includes('successful') ? 'var(--success-color)' : 'var(--error-color)'
          }}>
            <AlertCircle size={18} />
            <span>{error}</span>
          </div>
        )}

        <form className="space-y-6" onSubmit={handleSubmit}>
          <div>
            <label htmlFor="username" className="block text-sm font-medium mb-1">
              Username
            </label>
            <div className="relative">
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <User className="h-5 w-5" />
              </div>
              <input
                id="username"
                name="username"
                type="text"
                required
                value={formData.username}
                onChange={handleChange}
                className="w-full py-3 px-4 pl-10 border rounded-lg focus:outline-none focus:ring-2 transition-all duration-200"
                placeholder="Enter your username"
              />
            </div>
          </div>

          {!isLogin && (
            <>
              <div>
                <label htmlFor="name" className="block text-sm font-medium mb-1">
                  Full Name
                </label>
                <input
                  id="name"
                  name="name"
                  type="text"
                  required={!isLogin}
                  value={formData.name}
                  onChange={handleChange}
                  className="w-full py-3 px-4 border rounded-lg focus:outline-none focus:ring-2 transition-all duration-200"
                  placeholder="Enter your full name"
                />
              </div>

              <div>
                <label htmlFor="phoneNumber" className="block text-sm font-medium mb-1">
                  Phone Number
                </label>
                <input
                  id="phoneNumber"
                  name="phoneNumber"
                  type="tel"
                  required={!isLogin}
                  value={formData.phoneNumber}
                  onChange={handleChange}
                  className="w-full py-3 px-4 border rounded-lg focus:outline-none focus:ring-2 transition-all duration-200"
                  placeholder="(555) 123-4567"
                />
              </div>
            </>
          )}

          <div>
            <label htmlFor="password" className="block text-sm font-medium mb-1">
              Password
            </label>
            <div className="relative">
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <Lock className="h-5 w-5" />
              </div>
              <input
                id="password"
                name="password"
                type="password"
                required
                value={formData.password}
                onChange={handleChange}
                className="w-full py-3 px-4 pl-10 border rounded-lg focus:outline-none focus:ring-2 transition-all duration-200"
                placeholder="Enter your password"
              />
            </div>
          </div>

          {!isLogin && (
            <div>
              <label htmlFor="confirmPassword" className="block text-sm font-medium mb-1">
                Confirm Password
              </label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <Lock className="h-5 w-5" />
                </div>
                <input
                  id="confirmPassword"
                  name="confirmPassword"
                  type="password"
                  required={!isLogin}
                  value={formData.confirmPassword}
                  onChange={handleChange}
                  className="w-full py-3 px-4 pl-10 border rounded-lg focus:outline-none focus:ring-2 transition-all duration-200"
                  placeholder="Confirm your password"
                />
              </div>
            </div>
          )}

          <div>
            <button
              type="submit"
              disabled={loading}
              className="w-full flex justify-center items-center py-3 px-4 font-semibold rounded-lg shadow-md transition-colors duration-200"
            >
              {loading ? (
                "Processing..."
              ) : isLogin ? (
                <>
                  <LogIn className="h-5 w-5 mr-2" />
                  Sign In
                </>
              ) : (
                <>
                  <UserPlus className="h-5 w-5 mr-2" />
                  Create Account
                </>
              )}
            </button>
          </div>
        </form>

        <div className="text-center mt-4">
          <button
            onClick={toggleMode}
            className="toggle-button font-medium transition-colors duration-300 px-4 py-2 rounded-lg"
          >
            {isLogin
              ? "Don't have an account? Sign up"
              : "Already have an account? Sign in"}
          </button>
        </div>
      </div>
    </div>
  );
}