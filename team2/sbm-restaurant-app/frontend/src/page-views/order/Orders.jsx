"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { orderService } from "../../services/orderService";
import { authService } from "../../services/authService";
import OrderCard from '../../components/order/OrderCards.jsx';
import "../../components/order/orderstyle.scss";

export default function OrdersPage() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('all');
  const [sortBy, setSortBy] = useState('newest');
  
  // Add styles for option elements
  useEffect(() => {
    const style = document.createElement('style');
    style.textContent = `
      select option {
        background-color: var(--charcoal-light);
        color: var(--cream);
      }
      input::placeholder {
        color: var(--cream-dark);
      }
    `;
    document.head.appendChild(style);
    return () => document.head.removeChild(style);
  }, []);
  
  // Fetch orders based on current filters
  const fetchOrders = async () => {
    try {
      setLoading(true);
      
      // Use the appropriate API based on filter selection
      let ordersData;
      
      if (statusFilter === 'all') {
        ordersData = await orderService.getAllOrders();
      } else {
        // Call the backend API to filter by status
        ordersData = await orderService.getOrdersByStatus(statusFilter);
      }
      
      setOrders(ordersData);
      setError(null);
    } catch (error) {
      console.error('Error fetching orders:', error);
      setError('Failed to load orders');
    } finally {
      setLoading(false);
    }
  };
  
  // Handler for canceling an order
  const handleCancelOrder = async (orderId) => {
    try {
      await orderService.cancelOrder(orderId);
      // Refresh orders list after cancellation
      await fetchOrders();
    } catch (error) {
      console.error('Error canceling order:', error);
      setError('Failed to cancel order');
    }
  };

  // Handler for updating order status (if needed for admin view)
  const handleStatusUpdate = async (orderId, newStatus) => {
    try {
      await orderService.updateOrderStatus(orderId, newStatus);
      // Refresh orders list after status update
      await fetchOrders();
    } catch (error) {
      console.error('Error updating order status:', error);
      setError('Failed to update order status');
    }
  };
  
  // Filter and sort orders
  const filteredOrders = orders
    .filter(order => {
      // Only filter by search term as status filtering is now done on the backend
      return searchTerm === '' || 
        order.id.toString().includes(searchTerm) || 
        order.items?.some(item => item.name?.toLowerCase().includes(searchTerm.toLowerCase()));
    })
    .sort((a, b) => {
      switch (sortBy) {
        case 'newest':
          return new Date(b.createdAt) - new Date(a.createdAt);
        case 'oldest':
          return new Date(a.createdAt) - new Date(b.createdAt);
        case 'price-high':
          return b.totalPrice - a.totalPrice;
        case 'price-low':
          return a.totalPrice - b.totalPrice;
        default:
          return 0;
      }
    });

  // Fetch orders whenever the status filter changes
  useEffect(() => {
    fetchOrders();
  }, [statusFilter]);

  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-[200px]">
        <div className="loading-text">Loading your orders...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex justify-center items-center min-h-[200px]">
        <div className="error-text">Error: {error}</div>
      </div>
    );
  }

  return (
    <div className="orders-page p-6">
      <div className="mb-8">
        <h1 className="page-title text-4xl md:text-5xl font-bold mb-2">
          Your Orders
        </h1>
        <p className="page-subtitle text-lg mb-6">
          Track your potato paradise deliveries
        </p>
        
        {/* Search and Filter Controls */}
        <div className="filter-container p-6 rounded-lg shadow-md mb-6">
          <div className="grid md:grid-cols-3 gap-4">
            {/* Search */}
            <div className="relative">
              <input
                type="text"
                placeholder="Search orders or items..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:border-transparent"
              />
            </div>
            
            {/* Status Filter */}
            <select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:border-transparent"
            >
              <option value="all">All Status</option>
              <option value="Placed">Placed</option>
              <option value="Preparing">Preparing</option>
              <option value="ReadyForDelivery">Ready for Delivery</option>
              <option value="OutForDelivery">Out for Delivery</option>
              <option value="Delivered">Delivered</option>
            </select>
            
            {/* Sort By */}
            <select
              value={sortBy}
              onChange={(e) => setSortBy(e.target.value)}
              className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:border-transparent"
            >
              <option value="newest">Newest First</option>
              <option value="oldest">Oldest First</option>
              <option value="price-high">Price: High to Low</option>
              <option value="price-low">Price: Low to High</option>
            </select>
          </div>
          
          {/* Quick Stats */}
          <div className="mt-4 grid grid-cols-2 md:grid-cols-4 gap-4 text-center">
            <div className="stats-card p-3 rounded-lg border">
              <div className="stats-number text-2xl font-bold">{orders.length}</div>
              <div className="stats-label text-sm">Total Orders</div>
            </div>
            <div className="stats-card p-3 rounded-lg border">
              <div className="stats-number success text-2xl font-bold">
                {orders.filter(o => o.status === 'Delivered').length}
              </div>
              <div className="stats-label text-sm">Delivered</div>
            </div>
            <div className="stats-card p-3 rounded-lg border">
              <div className="stats-number info text-2xl font-bold">
                {orders.filter(o => ['Placed', 'Preparing', 'ReadyForDelivery', 'OutForDelivery'].includes(o.status)).length}
              </div>
              <div className="stats-label text-sm">In Progress</div>
            </div>
            <div className="stats-card p-3 rounded-lg border">
              <div className="stats-number text-2xl font-bold">
                ${orders.reduce((sum, order) => sum + order.totalPrice, 0).toFixed(2)}
              </div>
              <div className="stats-label text-sm">Total Spent</div>
            </div>
          </div>
        </div>
      </div>
      
      <div className="space-y-4">
        {filteredOrders.length === 0 ? (
          <div className="empty-state text-center">
            <div className="text-6xl mb-4">🍟</div>
            <h3 className="text-xl font-semibold mb-2 empty-title">
              {orders.length === 0 ? 'No orders yet' : 'No orders match your search'}
            </h3>
            <p className="empty-text mb-4">
              {orders.length === 0 
                ? 'Start your potato journey by placing your first order!' 
                : 'Try adjusting your search or filter criteria'
              }
            </p>
            {orders.length === 0 && (
              <button className="bg-amber-500 hover:bg-amber-600 text-white font-semibold py-2 px-6 rounded-lg transition-colors duration-200">
                Browse Menu
              </button>
            )}
          </div>
        ) : (
          <>
            <div className="flex justify-between items-center mb-4">
              <p className="order-count-text">
                Showing {filteredOrders.length} of {orders.length} orders
              </p>
              <button 
                onClick={fetchOrders}
                className="font-medium transition-colors duration-200 refresh-button"
                onMouseEnter={(e) => e.target.style.color = 'var(--theme-accent-dark)'}
                onMouseLeave={(e) => e.target.style.color = 'var(--theme-accent)'}
              >
                🔄 Refresh
              </button>
            </div>
            
            {filteredOrders.map(order => (
              <OrderCard 
                key={order.id} 
                order={order} 
                onCancel={handleCancelOrder}
                onStatusUpdate={handleStatusUpdate}
              />
            ))}
          </>
        )}
      </div>
    </div>
  );
}

