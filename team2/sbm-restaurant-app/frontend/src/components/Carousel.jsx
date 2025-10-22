"use client";

import { useState, useEffect } from "react";
import carouselData from "./carouselData";

const dishes = carouselData.length > 0 ? carouselData : [
  {
    img: "https://picsum.photos/800/400?random=1",
    title: "Crispy Golden Fries",
  },
  {
    img: "https://picsum.photos/800/400?random=2",
    title: "Loaded Baked Potato",
  },
  {
    img: "https://picsum.photos/800/400?random=3",
    title: "Creamy Mashed Potatoes",
  },
];

const Carousel = () => {
  const [index, setIndex] = useState(0);

  const nextSlide = () => setIndex((index + 1) % dishes.length);
  const prevSlide = () => setIndex((index - 1 + dishes.length) % dishes.length);

  // Auto-rotation effect
  useEffect(() => {
    const interval = setInterval(() => {
      setIndex((prevIndex) => (prevIndex + 1) % dishes.length);
    }, 4000); // Change slide every 4 seconds

    return () => clearInterval(interval); // Cleanup on unmount
  }, []);

  return (
    <div className="carousel">
      <button onClick={prevSlide}>‹</button>
      <div className="carousel-slide">
        <img src={dishes[index].img} alt={dishes[index].title} />
        <h3>{dishes[index].title}</h3>
      </div>
      <button onClick={nextSlide}>›</button>
    </div>
  );
};

export default Carousel;
