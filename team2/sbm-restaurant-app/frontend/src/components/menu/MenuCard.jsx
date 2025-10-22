import React from "react";
import ToppingItem from "./ToppingItem";


export default function MenuCard({ item, onClick }) {
  return (
    <div className="menu-card" onClick={onClick}>
      {/* Image Placeholder */}
      <div className="card-image">
        <div className="image-circle">
          <img src={item.image} alt={item.name} className="card-image" />
        </div>
      </div>
      {/* Item Info */}
      <div className="card-content">
        <h3>{item.name}</h3>
        <p className="item-meta">{item.price} — {item.calories}</p>
        {/* Toppings Preview */}
        <div className="toppings-preview">
          {item.toppings && item.toppings.length > 0 ? (
            item.toppings.map((topping, i) => (
              <ToppingItem key={i} name={topping.name} image={topping.image} />
            ))
          ) : (
            <p style={{ fontStyle: "italic", color: "#aaa" }}>No toppings</p>
          )}
        </div>
        <div className="card-actions">
          <button className="btn-modify">Modify</button>
          <button className="btn-add">Add to bag</button>
        </div>
      </div>
    </div>
  );
}
