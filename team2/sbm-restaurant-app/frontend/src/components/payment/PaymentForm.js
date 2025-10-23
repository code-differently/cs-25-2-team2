"use client";

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { motion } from 'framer-motion';
import { 
  CreditCard, 
  Lock, 
  AlertCircle, 
  CheckCircle, 
  User, 
  Calendar,
  Shield 
} from 'lucide-react';
import { authService } from '../../services/authService';
import { cartService } from '../../services/cartService';
import { orderService } from '../../services/orderService';
import './PaymentForm.scss';

export default function PaymentForm() {
  const router = useRouter();
  
  // Authentication state
  const [user, setUser] = useState(null);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  
  // Cart and order state
  const [cart, setCart] = useState([]);
  const [cartTotals, setCartTotals] = useState({});
  
  // Payment form state
  const [paymentData, setPaymentData] = useState({
    cardNumber: '',
    expiryMonth: '',
    expiryYear: '',
    cvv: '',
    cardholderName: '',
    billingAddress: {
      street: '',
      city: '',
      state: '',
      zipCode: ''
    }
  });
  
  // UI state
  const [isProcessing, setIsProcessing] = useState(false);
  const [errors, setErrors] = useState({});
  const [paymentSuccess, setPaymentSuccess] = useState(false);
  const [orderDetails, setOrderDetails] = useState(null);

  // Check authentication and load cart on component mount
  useEffect(() => {
    const currentUser = authService.getCurrentUser();
    const loggedIn = authService.isLoggedIn();
    
    if (!loggedIn) {
      // Redirect to login if not authenticated
      router.push('/auth/login?redirect=/payment');
      return;
    }
    
    setUser(currentUser);
    setIsLoggedIn(loggedIn);
    
    // Load cart data
    const cartItems = cartService.getCart();
    const totals = cartService.getCartTotals(cartItems);
    

        // Test Payment Handler
        const handleTestPayment = () => {
          setIsProcessing(true);
          setTimeout(() => {
  const [testMode, setTestMode] = useState(false); // Detect test mode from query param
            // Simulate order creation
            const fakeOrder = {
              id: Math.floor(Math.random() * 1000000),
              payment: {
                transactionId: 'TEST_' + Date.now(),
              },
            };
            setOrderDetails(fakeOrder);
            setPaymentSuccess(true);
            setIsProcessing(false);
          }, 1200);
        };
    if (cartItems.length === 0) {
      // Redirect to menu if cart is empty
      router.push('/menus');
      return;
    }
    
    setCart(cartItems);
    setCartTotals(totals);
  }, [router]);

  // Validation functions
  const validateCardNumber = (number) => {
    // Remove all non-numeric characters
    const cleaned = number.replace(/\D/g, '');
    
    // Check if it's exactly 16 digits
    if (cleaned.length !== 16) {
      return 'Card number must be exactly 16 digits';
    }
    
    // Basic Luhn algorithm check
    let sum = 0;
    let isEven = false;
    
  // Pre-fill and auto-submit in test mode
  useEffect(() => {
    if (testMode) {
      setPaymentData({
        cardNumber: '4242424242424242',
        expiryMonth: (new Date().getMonth() + 1).toString().padStart(2, '0'),
        expiryYear: (new Date().getFullYear() + 1).toString(),
        cvv: '123',
        cardholderName: 'Test User',
        billingAddress: {
          street: '123 Test St',
          city: 'Testville',
          state: 'TS',
          zipCode: '12345'
        }
      });
    }
  }, [testMode]);

    for (let i = cleaned.length - 1; i >= 0; i--) {
      let digit = parseInt(cleaned[i]);
      
      if (isEven) {
        digit *= 2;
        if (digit > 9) {
          digit -= 9;
        }
      }
      
      sum += digit;
      isEven = !isEven;
    }
    
    if (sum % 10 !== 0) {
      return 'Invalid card number';
    }
    
    return null;
  };

  const validateExpiry = (month, year) => {
    if (!month || !year) {
      return 'Expiry date is required';
    }
    
    const currentDate = new Date();
    const currentYear = currentDate.getFullYear();
    const currentMonth = currentDate.getMonth() + 1;
    
    const expYear = parseInt(year);
    const expMonth = parseInt(month);
    
    if (expYear < currentYear || (expYear === currentYear && expMonth < currentMonth)) {
      return 'Card has expired';
    }
    
    return null;
  };

  const validateCVV = (cvv) => {
    if (!cvv || cvv.length < 3 || cvv.length > 4) {
      return 'CVV must be 3 or 4 digits';
    }
    return null;
  };

  // Handle form input changes
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    
    // Special handling for card number (format with spaces)
    if (name === 'cardNumber') {
      // Remove all non-numeric characters
      const cleaned = value.replace(/\D/g, '');
      // Limit to 16 digits
      const limited = cleaned.substring(0, 16);
      // Add spaces every 4 digits for display
      const formatted = limited.replace(/(\d{4})(?=\d)/g, '$1 ');
      
      setPaymentData(prev => ({
        ...prev,
        [name]: limited // Store without spaces
      }));
      
      // Update the input display value
      e.target.value = formatted;
      
      // Clear card number error when user starts typing
      if (errors.cardNumber) {
        setErrors(prev => ({ ...prev, cardNumber: null }));
      }
    } else if (name.startsWith('billing.')) {
      // Handle nested billing address
      const field = name.split('.')[1];
      setPaymentData(prev => ({
        ...prev,
        billingAddress: {
          ...prev.billingAddress,
          [field]: value
        }
      }));
    } else {
      setPaymentData(prev => ({
        ...prev,
        [name]: value
      }));
      
      // Clear specific field errors when user starts typing
      if (errors[name]) {
        setErrors(prev => ({ ...prev, [name]: null }));
      }
    }
  };

  // Validate entire form
  const validateForm = () => {
    const newErrors = {};
    
    // Card number validation
    const cardError = validateCardNumber(paymentData.cardNumber);
    if (cardError) newErrors.cardNumber = cardError;
    
    // Expiry validation
    const expiryError = validateExpiry(paymentData.expiryMonth, paymentData.expiryYear);
    if (expiryError) newErrors.expiry = expiryError;
    
    // CVV validation
    const cvvError = validateCVV(paymentData.cvv);
    if (cvvError) newErrors.cvv = cvvError;
    
    // Cardholder name validation
    if (!paymentData.cardholderName.trim()) {
      newErrors.cardholderName = 'Cardholder name is required';
    }
    
    // Basic billing address validation
    if (!paymentData.billingAddress.street.trim()) {
      newErrors.billingStreet = 'Street address is required';
    }
    if (!paymentData.billingAddress.city.trim()) {
      newErrors.billingCity = 'City is required';
    }
    if (!paymentData.billingAddress.state.trim()) {
      newErrors.billingState = 'State is required';
    }
    if (!paymentData.billingAddress.zipCode.trim()) {
      newErrors.billingZip = 'ZIP code is required';
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // Process payment and create order (combined functionality)
  const processPayment = async () => {
    if (!validateForm()) {
      return false;
    }
    
    setIsProcessing(true);
    
    try {
      // Simulate payment processing delay
      await new Promise(resolve => setTimeout(resolve, 2000));
      
      // Create order data from cart (using cartService functionality)
      const orderData = await cartService.cartToOrder();
      
      // Generate payment transaction details
      const transactionId = generateTransactionId();
      const processedAt = new Date().toISOString();
      
      // Create unified order with payment information
      const completeOrder = {
        ...orderData,
        // Payment details
        payment: {
          method: 'credit_card',
          cardLastFour: paymentData.cardNumber.slice(-4),
          cardType: getCardType(paymentData.cardNumber),
          amount: cartTotals.total,
          transactionId: transactionId,
          processedAt: processedAt,
          status: 'completed'
        },
        billingAddress: paymentData.billingAddress,
        // Order status progression: Placed -> Paid
        status: 'Paid', // Final status after successful payment
        paymentMethod: 'Credit Card',
        orderNotes: `Order placed and paid via credit card ending in ${paymentData.cardNumber.slice(-4)}`
      };
      
      console.log('Creating complete order with payment:', completeOrder);
      
      // Create the order through orderService (combines placement + payment)
      const createdOrder = await orderService.createOrder(completeOrder);
      
      console.log('Order placed and paid successfully:', createdOrder);
      
      // Clear cart after successful order completion
      cartService.clearCart();
      
      // Set order details for success page
      setOrderDetails({
        ...createdOrder,
        payment: completeOrder.payment
      });
      setPaymentSuccess(true);
      
      return true;
    } catch (error) {
      console.error('Payment processing error:', error);
      setErrors({ general: 'Payment processing failed. Please try again.' });
      return false;
    } finally {
      setIsProcessing(false);
    }
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    await processPayment();
  };

  // Utility functions
  const getCardType = (number) => {
    const firstDigit = number.charAt(0);
    if (firstDigit === '4') return 'Visa';
    if (firstDigit === '5') return 'MasterCard';
    if (firstDigit === '3') return 'American Express';
    return 'Credit Card';
  };

  const generateTransactionId = () => {
    return 'TXN_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9);
  };

  // Format card number for display
  const formatCardNumber = (number) => {
    return number.replace(/(\d{4})(?=\d)/g, '$1 ');
  };

  // If payment is successful, show success page
  if (paymentSuccess && orderDetails) {
    return (
      <motion.div 
        className="payment-success"
        initial={{ opacity: 0, scale: 0.9 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.5 }}
      >
        <div className="success-container">
          <CheckCircle className="success-icon" />
          <h1>Payment Successful!</h1>
          <p>Your order has been placed successfully.</p>
          
          <div className="order-summary">
            <h3>Order Details</h3>
            <p><strong>Order ID:</strong> #{orderDetails.id}</p>
            <p><strong>Amount Paid:</strong> ${cartTotals.total.toFixed(2)}</p>
            <p><strong>Payment Method:</strong> **** **** **** {paymentData.cardNumber.slice(-4)}</p>
            <p><strong>Transaction ID:</strong> {orderDetails.payment?.transactionId}</p>
          </div>
          
          <div className="success-actions">
            <button 
              onClick={() => router.push('/orders')} 
              className="btn-primary"
            >
              View My Orders
            </button>
            <button 
              onClick={() => router.push('/menus')} 
              className="btn-secondary"
            >
              Continue Shopping
            </button>
          </div>
        </div>
      </motion.div>
    );
  }

  // Loading state while checking authentication
  if (!isLoggedIn || !user) {
    return (
      <div className="payment-loading">
        <p>Checking authentication...</p>
      </div>
    );
  }

  // Main payment form
  return (
    <div className="payment-page">
      <div className="payment-container">
        <header className="payment-header">
          <h1>
            <Lock className="header-icon" />
            Secure Payment
          </h1>
          <p>Complete your order with secure payment processing</p>
        </header>

        <div className="payment-content">
          {/* Order Summary */}
          <div className="order-summary-section">
            <h2>Order Summary</h2>
            <div className="cart-items">
              {cart.map(item => (
                <div key={item.id} className="summary-item">
                  <span className="item-name">{item.name}</span>
                  <span className="item-quantity">x{item.quantity}</span>
                  <span className="item-price">${(item.price * item.quantity).toFixed(2)}</span>
                </div>
              ))}
            </div>
            <div className="totals">
              <div className="total-line">
                <span>Subtotal:</span>
                <span>${cartTotals.subtotal?.toFixed(2)}</span>
              </div>
              <div className="total-line total-final">
                <span>Total:</span>
                <span>${cartTotals.total?.toFixed(2)}</span>
              </div>
            </div>
          </div>

          {/* Payment Form */}
          <form onSubmit={handleSubmit} className="payment-form">
            <h2>
              <CreditCard className="section-icon" />
              Payment Information
            </h2>

            {/* General Error */}
            {errors.general && (
              <div className="error-message general-error">
                <AlertCircle className="error-icon" />
                {errors.general}
              </div>
            )}

            {/* Card Number */}
            <div className="form-group">
              <label htmlFor="cardNumber">
                <CreditCard className="input-icon" />
                Card Number
              </label>
              <input
                type="text"
                id="cardNumber"
                name="cardNumber"
                placeholder="1234 5678 9012 3456"
                maxLength="19" // Including spaces
                onChange={handleInputChange}
                className={errors.cardNumber ? 'error' : ''}
                required
              />
              {errors.cardNumber && (
                <span className="field-error">{errors.cardNumber}</span>
              )}
            </div>

            {/* Expiry and CVV */}
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="expiryMonth">
                  <Calendar className="input-icon" />
                  Expiry Date
                </label>
                <div className="expiry-inputs">
                  <select
                    id="expiryMonth"
                    name="expiryMonth"
                    value={paymentData.expiryMonth}
                    onChange={handleInputChange}
                    className={errors.expiry ? 'error' : ''}
                    required
                  >
                    <option value="">MM</option>
                    {Array.from({ length: 12 }, (_, i) => i + 1).map(month => (
                      <option key={month} value={month.toString().padStart(2, '0')}>
                        {month.toString().padStart(2, '0')}
                      </option>
                    ))}
                  </select>
                  <select
                    id="expiryYear"
                    name="expiryYear"
                    value={paymentData.expiryYear}
                    onChange={handleInputChange}
                    className={errors.expiry ? 'error' : ''}
                    required
                  >
                    <option value="">YYYY</option>
                    {Array.from({ length: 10 }, (_, i) => new Date().getFullYear() + i).map(year => (
                      <option key={year} value={year}>
                        {year}
                      </option>
                    ))}
                  </select>
                </div>
                {errors.expiry && (
                  <span className="field-error">{errors.expiry}</span>
                )}
              </div>

              <div className="form-group">
                <label htmlFor="cvv">
                  <Shield className="input-icon" />
                  CVV
                </label>
                <input
                  type="text"
                  id="cvv"
                  name="cvv"
                  placeholder="123"
                  maxLength="4"
                  value={paymentData.cvv}
                  onChange={handleInputChange}
                  className={errors.cvv ? 'error' : ''}
                  required
                />
                {errors.cvv && (
                  <span className="field-error">{errors.cvv}</span>
                )}
              </div>
            </div>

            {/* Cardholder Name */}
            <div className="form-group">
              <label htmlFor="cardholderName">
                <User className="input-icon" />
                Cardholder Name
              </label>
              <input
                type="text"
                id="cardholderName"
                name="cardholderName"
                placeholder="John Doe"
                value={paymentData.cardholderName}
                onChange={handleInputChange}
                className={errors.cardholderName ? 'error' : ''}
                required
              />
              {errors.cardholderName && (
                <span className="field-error">{errors.cardholderName}</span>
              )}
            </div>

            {/* Billing Address */}
            <h3>Billing Address</h3>
            
            <div className="form-group">
              <label htmlFor="billingStreet">Street Address</label>
              <input
                type="text"
                id="billingStreet"
                name="billing.street"
                placeholder="123 Main Street"
                value={paymentData.billingAddress.street}
                onChange={handleInputChange}
                className={errors.billingStreet ? 'error' : ''}
                required
              />
              {errors.billingStreet && (
                <span className="field-error">{errors.billingStreet}</span>
              )}
            </div>

            <div className="form-row">
              <div className="form-group">
                <label htmlFor="billingCity">City</label>
                <input
                  type="text"
                  id="billingCity"
                  name="billing.city"
                  placeholder="New York"
                  value={paymentData.billingAddress.city}
                  onChange={handleInputChange}
                  className={errors.billingCity ? 'error' : ''}
                  required
                />
                {errors.billingCity && (
                  <span className="field-error">{errors.billingCity}</span>
                )}
              </div>

              <div className="form-group">
                <label htmlFor="billingState">State</label>
                <input
                  type="text"
                  id="billingState"
                  name="billing.state"
                  placeholder="NY"
                  value={paymentData.billingAddress.state}
                  onChange={handleInputChange}
                  className={errors.billingState ? 'error' : ''}
                  required
                />
                {errors.billingState && (
                  <span className="field-error">{errors.billingState}</span>
                )}
              </div>

              <div className="form-group">
                <label htmlFor="billingZip">ZIP Code</label>
                <input
                  type="text"
                  id="billingZip"
                  name="billing.zipCode"
                  placeholder="10001"
                  value={paymentData.billingAddress.zipCode}
                  onChange={handleInputChange}
                  className={errors.billingZip ? 'error' : ''}
                  required
                />
                {errors.billingZip && (
                  <span className="field-error">{errors.billingZip}</span>
                )}
              </div>
            </div>

            {/* Submit Button */}
            <button
              type="submit"
              className="payment-submit-btn"
              disabled={isProcessing}
            >
              {isProcessing ? (
                <>
                  <div className="spinner"></div>
                  Processing Payment...
                </>
              ) : (
                <>
                  <Lock className="btn-icon" />
                  Pay ${cartTotals.total?.toFixed(2)}
                </>
              )}
            </button>

            <div className="security-notice">
              <Shield className="security-icon" />
              <p>Your payment information is encrypted and secure</p>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}