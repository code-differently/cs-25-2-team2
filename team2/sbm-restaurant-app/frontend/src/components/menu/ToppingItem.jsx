import React from "react";

export default function ToppingItem({ name, image }) {
  return (
    <div className="topping-item">
      <div className="topping-image">
        <img
          src={image}
          alt={name}
          style={{ width: "2rem", height: "2rem", borderRadius: "50%" }}
        />
      </div>
      <p>{name}</p>
    </div>
  );
}