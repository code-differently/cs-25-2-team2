"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { useState } from "react";
import { useAuth } from "../context/AuthContext";
import { User, LogOut, ShoppingCart, Home, Menu as MenuIcon, Clock } from "lucide-react";

export default function Navigation() {
  const pathname = usePathname();
  const { user, logout, isLoggedIn } = useAuth();
  const [isUserMenuOpen, setIsUserMenuOpen] = useState(false);

  const navItems = [
    { name: "Home", path: "/", icon: <Home size={18} /> },
    { name: "Menu", path: "/menus", icon: <MenuIcon size={18} /> },
    { name: "Cart", path: "/cart", icon: <ShoppingCart size={18} /> },
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
    <nav className="bg-white shadow-md border-b-2 border-amber-200 sticky top-0 z-50">
      <div className="container mx-auto px-6 py-4">
        <div className="flex justify-between items-center">
          <h1 className="text-2xl font-bold text-amber-600">Spud Munch Bunch</h1>
          <div className="flex gap-4 items-center">
            {navItems.map((item) => (
              <Link
                key={item.path}
                href={item.path}
                className={`px-4 py-2 rounded-lg font-semibold transition-colors duration-200 flex items-center ${
                  pathname === item.path
                    ? "bg-amber-500 text-white"
                    : "bg-gray-100 text-gray-700 hover:bg-amber-100"
                }`}
              >
                <span className="mr-2">{item.icon}</span>
                {item.name}
              </Link>
            ))}
            
            {/* Auth section */}
            {isLoggedIn ? (
              <div className="relative">
                <button 
                  onClick={toggleUserMenu}
                  className="flex items-center px-4 py-2 rounded-lg font-semibold transition-colors duration-200 bg-amber-500 text-white"
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
                className="px-4 py-2 rounded-lg font-semibold transition-colors duration-200 bg-amber-500 text-white flex items-center"
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