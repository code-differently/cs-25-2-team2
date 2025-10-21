"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { useState, useEffect } from "react";
import { useAuth } from "../context/AuthContext";
import { User, LogOut, ShoppingCart, Home, Menu as MenuIcon, Clock } from "lucide-react";
import { cartService } from "../services/cartService";
import ThemeToggle from "./ui/ThemeToggle";

export default function Navigation() {
  const pathname = usePathname();
  const { user, logout, isLoggedIn } = useAuth();
  const [isUserMenuOpen, setIsUserMenuOpen] = useState(false);
  const [cartItemCount, setCartItemCount] = useState(0);
  
  // Update cart count when component mounts and when cart changes
  useEffect(() => {
    const updateCartCount = () => {
      const cart = cartService.getCart();
      const count = cart.reduce((total, item) => total + item.quantity, 0);
      setCartItemCount(count);
    };
    
    // Listen for storage events to update cart count when cart changes
    const handleStorageChange = (e) => {
      if (!e.key || e.key === cartService.CART_STORAGE_KEY) {
        updateCartCount();
      }
    };
    
    // Listen for custom cart updated event
    const handleCartUpdate = () => {
      updateCartCount();
    };
    
    window.addEventListener('storage', handleStorageChange);
    window.addEventListener('cartUpdated', handleCartUpdate);
    updateCartCount();
    
    return () => {
      window.removeEventListener('storage', handleStorageChange);
      window.removeEventListener('cartUpdated', handleCartUpdate);
    };
  }, []);

  const navItems = [
    { name: "Home", path: "/", icon: <Home size={18} /> },
    { name: "Menu", path: "/menu", icon: <MenuIcon size={18} /> },
    { name: "Cart", path: "/cart", icon: <ShoppingCart size={18} />, badge: true },
    { name: "Orders", path: "/orders", icon: <Clock size={18} /> },
  ];

  const toggleUserMenu = () => {
    setIsUserMenuOpen(!isUserMenuOpen);
  };

  const handleLogout = () => {
    logout();
    setIsUserMenuOpen(false);
    // You might want to redirect to the home page here
  };

  return (
    <nav className="bg-white shadow-md border-b-2 sticky top-0 z-50" style={{borderColor: 'var(--gold-primary)'}}>
      <div className="container mx-auto px-6 py-4">
        <div className="flex justify-between items-center">
          <h1 className="text-2xl font-bold" style={{color: 'var(--gold-primary)'}}>Spud Munch Bunch</h1>
          <div className="flex gap-4 items-center">
            {navItems.map((item) => (
              <Link
                key={item.path}
                href={item.path}
                className={`px-4 py-2 rounded-lg font-semibold transition-colors duration-200 flex items-center ${
                  pathname === item.path
                    ? "text-white"
                    : "bg-gray-100 text-gray-700"
                }`}
                style={pathname === item.path ? {backgroundColor: 'var(--gold-primary)'} : {}}
                onMouseEnter={(e) => {
                  if (pathname !== item.path) {
                    e.target.style.backgroundColor = 'rgba(212, 175, 55, 0.1)';
                  }
                }}
                onMouseLeave={(e) => {
                  if (pathname !== item.path) {
                    e.target.style.backgroundColor = '';
                  }
                }}
              >
                <div className="relative">
                  <span className="mr-2">{item.icon}</span>
                  {item.badge && cartItemCount > 0 && (
                    <span 
                      className="absolute -top-2 -right-2 text-white text-xs font-bold rounded-full h-5 w-5 flex items-center justify-center"
                      style={{backgroundColor: 'var(--error-color)'}}
                    >
                      {cartItemCount}
                    </span>
                  )}
                </div>
                {item.name}
              </Link>
            ))}
            
            {/* Theme Toggle */}
            <ThemeToggle />
            
            {/* User Menu or Sign In */}
            {isLoggedIn ? (
              <div className="relative">
                <button 
                  onClick={toggleUserMenu}
                  className="flex items-center px-4 py-2 rounded-lg font-semibold transition-colors duration-200 text-white"
                  style={{backgroundColor: 'var(--gold-primary)'}}
                >
                  <User size={18} className="mr-2" />
                  {user?.name || "Account"}
                </button>
                
                {/* User dropdown menu */}
                {isUserMenuOpen && (
                  <div className="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-50 border border-gray-200">
                    <div className="px-4 py-2 text-sm text-gray-700 border-b border-gray-200">
                      <p className="font-semibold">Signed in as</p>
                      <p className="truncate">{user?.username}</p>
                      {user?.phoneNumber && (
                        <p className="text-xs text-gray-500">{user.phoneNumber}</p>
                      )}
                    </div>
                    <button
                      onClick={handleLogout}
                      className="flex items-center w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                    >
                      <LogOut size={16} className="mr-2" />
                      Sign Out
                    </button>
                  </div>
                )}
              </div>
            ) : (
              <Link
                href="/auth/login"
                className="px-4 py-2 rounded-lg font-semibold transition-colors duration-200 text-white flex items-center"
                style={{backgroundColor: 'var(--gold-primary)'}}
              >
                <User size={18} className="mr-2" />
                Sign In
              </Link>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
}