// Menu Pages
// This folder contains all menu-related pages
//
// Suggested structure:
// - MenuPage.jsx (main menu page)
// - MenuCategoryPage.jsx (category-specific pages)
// - MenuItemDetailPage.jsx (detailed menu item view)
// - menupagestyle.scss (menu page styles)
import React, {useState} from "react";


export default function MenuPage() {
  const [category, setCategory] = useState("All");
  const [search, setSearch] = useState("");

  const menuItems = [
    {id: 1, name: "Classic Baked Potato", category: "Main", price: "$6.99"},
     { id: 2, name: "Loaded Potato Skins", category: "Appetizer", price: "$8.49"},
    { id: 3, name: "Sweet Potato Fries", category: "Side", price: "$4.99"},
    { id: 4, name: "Potato Soup", category: "Soup", price: "$5.99"},
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
          {["All", "Main", "Appetizer", "Side", "Soup"].map((cat) => (
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
        <div>
          {/* Loop through all filtered items and show a card for each */}
          {filterItems.map((item) =>(
            <div 
              key={item.id}
              >
                {/*Need to add image*/}
              <img></img>

              <div className="item-info">
                <h3>{item.name}</h3>
                <p className="item-category-text">{item.category}</p>
                <p className="item-price-text">{item.price}</p>
              </div>
        </div>
       ))}
      </div>
       )}
      </section>
    </div>
  );
}