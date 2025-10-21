"use client";

import React, {useState} from "react";
// import { useNavigate } from "react-router-dom";
import "./menu.scss";


export default function MenuPage() {
  const [category, setCategory] = useState("All");
  const [search, setSearch] = useState("");
 

  const menuItems = [
    {id: 1, name: "Texas Style Baked Potato", category: "Main", price: "$12.99"},
     { id: 2, name: "Pollo Mexicano Baked Potato", category: "Main", price: "$13.99"},
    { id: 3, name: "CB Ranch Baked Potato", category: "Main", price: "$10.99"},
    { id: 4, name: "Loaded Baked Potato Soup", category: "Soup", price: "$5.99"},
     {id: 5, name: "Plain Jane Baked Potato", category: "Main", price: "$9.99"},
     {id: 6, name: "Loaded Fries", category: "Side", price: "$6.99"},
     {id: 7, name: "Hash Browns", category: "Side", price: "$5.99"},
     {id: 8, name: "Mashed Potatoes", category: "Side", price: "$6.99"},
     {id: 9, name: "HasselBack Potatoes", category: "Side", price: "$5.99"},
     { id: 10, name: "Gnocchi", category: "Soup", price: "$5.99"},
  ];
  
  

  const filterItems = menuItems.filter((item) => {
    let shouldShow = true;

    /**
     * If the selected category is NOT 'All' & the items category
     * does NOT match the selected one, then we dont want to show it.
    **/
    if(category !== "All" && item.category !== category){
      shouldShow = false;
    }

    /**
     * This makes sure the case does not matter,
     * if the user uses 'Potato' or 'potato' it wont 
     * matter.
     **/
    const itemName = item.name.toLowerCase();
    const searchText = search.toLowerCase();

    /**
     * If the items name doe not includes the search text, 
     * we dont want to show it.
     */
    if(!itemName.includes(searchText)){
      shouldShow = false;
    }
    return shouldShow;

  });
  return (
    <div className="menu-page">
      {/* Header Section */}
      <header className="menu-header">
        <h1>Our Menu</h1>
        <p>Discover every delicious potato creation we offer!</p>
      </header>

      {/*Controls Section - includes catgory button and search bar */}
      <section className="controls-section">

        {/*Category filter button*/}
        <div className="category-filter-buttons">
          
          {/*Map loops through the category list and creates one button for each */}
          {["All", "Main", "Side", "Soup"].map((cat) => (
          <button
            key={cat}
            onClick={() => setCategory(cat)}
            >{cat}
            </button>
          ))}
        </div>

        {/*Search input field*/}
        <input
          type="text"
          placeholder="Search menu..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}></input>
      </section>

        {/* Menu Grid Section - displays filtered menu items */}
      <section className="menu-grid-section">

        {/* If no items match, show this message */}
       {filterItems.length === 0 ? (
        <p>No items match your search or filter</p>
       ): (
         // Otherwise, show all filtered items in a grid layout
          filterItems.map((item) =>(
            <div 
              key={item.id} className="menu-card"
              >
                {/*Need to add image*/}
              <img></img>

              <div className="item-info">
                <h3>{item.name}</h3>
                <p className="item-category-text">{item.category}</p>
                <p className="item-price-text">{item.price}</p>
              </div>
        </div>
       ))
       )}
      </section>
    </div>
  );
}

// import React, { useState } from "react";
// import "./menu.scss";

// export default function MenuPage() {
//   const [category, setCategory] = useState("All");
//   const [search, setSearch] = useState("");

//   const menuItems = [
//     { id: 1, name: "Classic Baked Potato", category: "Main", price: "$6.99" },
//     { id: 2, name: "Loaded Potato Skins", category: "Appetizer", price: "$8.49" },
//     { id: 3, name: "Sweet Potato Fries", category: "Side", price: "$4.99" },
//     { id: 4, name: "Potato Soup", category: "Soup", price: "$5.99" },
//   ];

//   const filterItems = menuItems.filter((item) => {
//     let shouldShow = true;

//     if (category !== "All" && item.category !== category) {
//       shouldShow = false;
//     }

//     const itemName = item.name.toLowerCase();
//     const searchText = search.toLowerCase();

//     if (!itemName.includes(searchText)) {
//       shouldShow = false;
//     }
//     return shouldShow;
//   });

//   return (
//     <div className="menu-page-figma">
//       {/* Hero Section */}
//       <section className="hero-section">
//         {/* Beige Circle Background */}
//         <div className="beige-circle"></div>
        
//         {/* Potato Stack Image - Left Side */}
//         <img src='../images/potatoe.jpeg' alt="Loaded Potatoes" className="potato-stack" />

//         {/* Center "Our Menu" Text */}
//         <h1 className="our-menu-title">Our Menu</h1>

//         {/* Right Side Category Buttons */}
//         <div className="category-buttons-vertical">
//           <button
//             onClick={() => setCategory("Soup")}
//             className={category === "Soup" ? "active" : ""}
//           >
//             Soup
//           </button>
          
//           <button
//             onClick={() => setCategory("Side")}
//             className={category === "Side" ? "active" : ""}
//           >
//             Side
//           </button>
          
//           <button
//             onClick={() => setCategory("Main")}
//             className={category === "Main" ? "active" : ""}
//           >
//             Main
//           </button>
          
//           <button
//             onClick={() => setCategory("All")}
//             className={category === "All" ? "active" : ""}
//           >
//             All
//           </button>
//         </div>
//       </section>

//       {/* Search Bar Section */}
//       <section className="search-section">
//         <input
//           type="text"
//           placeholder="Search menu..."
//           value={search}
//           onChange={(e) => setSearch(e.target.value)}
//         />
//       </section>

//       {/* Menu Grid Section */}
//       <section className="menu-grid-section">
//         {filterItems.length === 0 ? (
//           <p className="no-items">No items match your search or filter</p>
//         ) : (
//           filterItems.map((item) => (
//             <div key={item.id} className="menu-card">
//               <div className="menu-card-image"></div>
//               <div className="item-info">
//                 <h3>{item.name}</h3>
//                 <p className="item-category-text">{item.category}</p>
//                 <p className="item-price-text">{item.price}</p>
//               </div>
//             </div>
//           ))
//         )}
//       </section>
//     </div>
//   );
// }