"use client";

import React, { useEffect, useState } from 'react';
import { orderService } from '../../services/orderService';
import OrderCard from '../../components/order/OrderCards';
import "../../components/order/orderstyle.scss";

export default function OrdersPage() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  const fetchAllOrders = async () => {
    try {
      setLoading(true);
      const ordersData = await orderService.getAllOrders();
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
      await fetchAllOrders();
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
      await fetchAllOrders();
    } catch (error) {
      console.error('Error updating order status:', error);
      setError('Failed to update order status');
    }
  };
  
  useEffect(() => {
    fetchAllOrders();
  }, []);

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
    <div className="orders-page">
      <div className="mb-8">
        <h1 className="page-title text-4xl md:text-5xl font-bold mb-2">
          Your Orders
        </h1>
        <p className="page-subtitle text-lg">
          Track your potato paradise deliveries
        </p>
      </div>
      
      <div className="space-y-4">
        {orders.length === 0 ? (
          <div className="text-center py-8">
            <p className="empty-text">No orders found</p>
          </div>
        ) : (
          orders.map(order => (
            <OrderCard 
              key={order.id} 
              order={order} 
              onCancel={handleCancelOrder}
              onStatusUpdate={handleStatusUpdate}
            />
          ))
        )}
      </div>
    </div>
  );
}

