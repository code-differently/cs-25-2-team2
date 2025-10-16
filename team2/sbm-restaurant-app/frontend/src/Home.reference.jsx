// REFERENCE FILE ONLY - NOT USED BY APP
// This file contains the original Home component code for reference
// The app now uses pages/home/HomePage.jsx instead

// import React, { useState } from 'react';
// import MenuList from '../components/MenuList';
// import CartItem from '../components/CartItem';
// import "./homestyle.scss";

// Mock data for testing - Potato Recipe Menu
// const mockItems = [
//   {
//     id: 1,
//     name: "Classic Loaded Baked Potato",
//     description: "Fluffy baked russet potato topped with melted cheddar, crispy bacon bits, sour cream, and fresh chives",
//     price: 12.99
//   },
//   {
//     id: 2,
//     name: "Garlic Herb Roasted Baby Potatoes", 
//     description: "Golden baby potatoes roasted with rosemary, thyme, garlic, and olive oil until crispy outside and tender inside",
//     price: 9.99
//   },
//   {
//     id: 3,
//     name: "Truffle Parmesan Potato Gratin",
//     description: "Layers of thinly sliced potatoes baked in cream sauce with truffle oil and topped with parmesan cheese",
//     price: 16.99
//   },
//   {
//     id: 4,
//     name: "Spiced Sweet Potato Fries",
//     description: "Hand-cut sweet potato fries seasoned with paprika, cumin, and a hint of cinnamon, served with chipotle aioli",
//     price: 8.99
//   },
//   {
//     id: 5,
//     name: "Potato Gnocchi in Brown Butter Sage", 
//     description: "House-made potato gnocchi tossed in brown butter with crispy sage leaves and grated pecorino romano",
//     price: 14.99
//   },
//   {
//     id: 6,
//     name: "Hasselback Potatoes",
//     description: "Swedish-style sliced potatoes roasted with herbs, butter, and breadcrumbs until golden and accordion-like",
//     price: 11.99
//   }
// ];

// export default function Home() {
//   const [cart, setCart] = useState([]);
//   const [search, setSearch] = useState('');
//   const [loading, setLoading] = useState(false);
//   const [error, setError] = useState(null);

//   // Filter items based on search term
//   const filteredItems = mockItems.filter(item =>
//     item.name.toLowerCase().includes(search.toLowerCase())
//   );

//   // Handler for adding items to cart
//   const handleAddToCart = (item) => {
//     try {
//       const existingItem = cart.find(cartItem => cartItem.id === item.id);
//       
//       if (existingItem) {
//         // If item already exists, increase quantity
//         setCart(cart.map(cartItem => 
//           cartItem.id === item.id 
//             ? { ...cartItem, quantity: cartItem.quantity + 1 }
//             : cartItem
//         ));
//       } else {
//         // If new item, add with quantity 1
//         setCart([...cart, { ...item, quantity: 1 }]);
//       }
//       setError(null);
//     } catch (error) {
//       console.error('Error adding to cart:', error);
//       setError('Failed to add item to cart');
//     }
//   };

//   // Handler for updating item quantity
//   const handleUpdateQuantity = (id, newQuantity) => {
//     try {
//       const quantity = parseInt(newQuantity);
//       if (quantity <= 0) {
//         handleRemoveFromCart(id);
//       } else {
//         setCart(cart.map(item => 
//           item.id === id ? { ...item, quantity: quantity } : item
//         ));
//       }
//       setError(null);
//     } catch (error) {
//       console.error('Error updating quantity:', error);
//       setError('Failed to update quantity');
//     }
//   };

//   // Handler for removing items from cart
//   const handleRemoveFromCart = (id) => {
//     try {
//       setCart(cart.filter(item => item.id !== id));
//       setError(null);
//     } catch (error) {
//       console.error('Error removing from cart:', error);
//       setError('Failed to remove item');
//     }
//   };

//   // Handler for checkout
//   const handleCheckout = async () => {
//     try {
//       setLoading(true);
//       // TODO: Implement checkout flow
//       console.log('Checkout with cart:', cart);
//       alert(`Order placed successfully! Total: $${cart.reduce((sum, item) => sum + (item.price * item.quantity), 0).toFixed(2)}`);
//       setCart([]);
//       setError(null);
//     } catch (error) {
//       console.error('Error during checkout:', error);
//       setError('Checkout failed. Please try again.');
//     } finally {
//       setLoading(false);
//     }
//   };

//   if (error) {
//     return (
//       <div className="flex justify-center items-center min-h-[200px]">
//         <div className="error-text">Error: {error}</div>
//       </div>
//     );
//   }

//   return (
//     <div className="home-page min-h-screen">
//       {/* Hero Header Section */}
//       <header className="hero-header bg-gradient-to-r text-white shadow-lg" 
//               style={{background: 'linear-gradient(to right, #d4af37, #f4d03f)'}}>
//         <div className="container mx-auto px-6 py-4">
//           <div className="hero-content flex justify-between items-center">
//             <div className="hero-text">
//               <h1 className="page-title text-4xl font-bold mb-2">Welcome to Spud Munch Bunch!</h1>
//               <p className="page-subtitle text-lg" style={{color: '#fff8e1'}}>Your potato paradise journey starts here</p>
//             </div>
//             <div className="hero-actions flex gap-4">
//               <button className="auth-button bg-white hover:bg-amber-50 font-semibold py-3 px-6 rounded-lg shadow-md transition-colors duration-200 border-2 border-transparent hover:border-amber-200" 
//                       style={{color: '#d4af37'}}>
//                 Sign In
//               </button>
//               <button className="auth-button font-semibold py-3 px-6 rounded-lg shadow-md transition-colors duration-200 border-2" 
//                       style={{backgroundColor: '#d4af37', borderColor: '#d4af37', color: 'white'}}>
//                 Log In
//               </button>
//             </div>
//           </div>
//           <button className="cta-button mt-6 text-white font-bold py-3 px-8 rounded-full shadow-lg transition-all duration-200 transform hover:scale-105" 
//                   style={{backgroundColor: '#d4af37'}}>
//             Get Started
//           </button>
//         </div>
//       </header>

//       {/* Menu Section */}
//       <section className="py-12 px-6">
//         <div className="container mx-auto max-w-6xl">
//           <h2 className="menu-title text-3xl font-bold mb-8 text-center">
//             Our Featured Menu
//           </h2>
//           <div className="mb-8 flex justify-center">
//             <input
//               type="text"
//               placeholder="Search items..."
//               value={search}
//               onChange={(e) => setSearch(e.target.value)}
//               className="w-full max-w-md px-4 py-3 border-2 border-amber-200 rounded-lg focus:outline-none focus:border-amber-500 focus:ring-2 focus:ring-amber-200 text-gray-700 placeholder-gray-400 shadow-sm"
//             />
//           </div>
//           {filteredItems.length === 0 && search ? (
//             <div className="text-center py-8">
//               <p className="empty-text">No matching items found</p>
//             </div>
//           ) : (
//             <MenuList items={filteredItems} addToCart={handleAddToCart} />
//           )}
//         </div>
//       </section>

//       {/* Cart Section */}
//       <section className="bg-white py-12 px-6 border-t-4 border-amber-200">
//         <div className="container mx-auto max-w-4xl">
//           <h3 className="cart-title text-3xl font-bold text-center flex items-center justify-center gap-2 mb-8">
//             🛒 Shopping Cart
//           </h3>
//           {cart.length === 0 ? (
//             <div className="text-center py-12 bg-gray-50 rounded-xl border-2 border-dashed border-gray-200">
//               <p className="empty-text text-lg">Your cart is empty</p>
//               <p className="empty-subtext text-sm mt-2">Add some delicious potato dishes to get started!</p>
//             </div>
//           ) : (
//             <div className="space-y-4">
//               {cart.map(item => (
//                 <CartItem 
//                   key={item.id} 
//                   item={item} 
//                   updateQuantity={handleUpdateQuantity}
//                   removeFromCart={handleRemoveFromCart}
//                 />
//               ))}
//               <div className="bg-gradient-to-r from-amber-50 to-yellow-50 p-6 rounded-xl border-2 border-amber-200 mt-6">
//                 <div className="flex justify-between items-center">
//                   <div>
//                     <h4 className="text-2xl font-bold text-gray-800">
//                       Total: ${cart.reduce((sum, item) => sum + (item.price * item.quantity), 0).toFixed(2)}
//                     </h4>
//                     <p className="text-gray-600 mt-1">
//                       Items in cart: {cart.reduce((total, item) => total + item.quantity, 0)}
//                     </p>
//                   </div>
//                   <button 
//                     onClick={handleCheckout}
//                     disabled={loading || cart.length === 0}
//                     className={`text-white font-bold py-3 px-8 rounded-lg shadow-lg transition-all duration-200 ${
//                       loading ? 'opacity-50 cursor-not-allowed' : 'transform hover:scale-105'
//                     }`}
//                     style={{backgroundColor: '#d4af37'}}
//                   >
//                     {loading ? 'Processing...' : 'Checkout'}
//                   </button>
//                 </div>
//               </div>
//             </div>
//           )}
//         </div>
//       </section>

//       {/* Features Section */}
//       <section className="features-section py-16 px-6">
//         <div className="container mx-auto max-w-6xl">
//           <h2 className="features-title text-3xl font-bold mb-12 text-center">
//             Why Choose Spud Munch Bunch?
//           </h2>
//           <div className="features-grid grid md:grid-cols-3 gap-8">
//             <div className="feature-card p-8 rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-300 border">
//               <div className="feature-icon text-4xl mb-4">🥔</div>
//               <h3 className="feature-title text-xl font-bold mb-3">Fresh Ingredients</h3>
//               <p className="feature-description">We use only the finest, locally-sourced potatoes and fresh ingredients in all our dishes.</p>
//             </div>
//             <div className="feature-card p-8 rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-300 border">
//               <div className="feature-icon text-4xl mb-4">👨‍🍳</div>
//               <h3 className="feature-title text-xl font-bold mb-3">Expert Chefs</h3>
//               <p className="feature-description">Our skilled chefs craft each potato dish with passion and decades of culinary expertise.</p>
//             </div>
//             <div className="feature-card p-8 rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-300 border">
//               <div className="feature-icon text-4xl mb-4">🚚</div>
//               <h3 className="feature-title text-xl font-bold mb-3">Fast Delivery</h3>
//               <p className="feature-description">Enjoy our delicious potato creations delivered hot and fresh to your door in 30 minutes or less.</p>
//             </div>
//           </div>
//         </div>
//       </section>
//     </div>
//   );
// }
