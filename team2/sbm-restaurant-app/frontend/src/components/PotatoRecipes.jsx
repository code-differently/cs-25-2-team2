import React, { useEffect, useState } from "react";
import axios from "axios";

export default function PotatoRecipes({ number = 5 }) {
  const [recipes, setRecipes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    setLoading(true);
    axios
      .get("/api/recipes/potato?number=" + number)
      .then((res) => {
        // Spoonacular returns JSON as a string, so parse it
        const data = typeof res.data === "string" ? JSON.parse(res.data) : res.data;
        setRecipes(data.results || []);
        setLoading(false);
      })
      .catch((err) => {
        setError("Failed to load recipes");
        setLoading(false);
      });
  }, [number]);

  if (loading) return <div className="text-center py-8">Loading potato recipes...</div>;
  if (error) return <div className="text-center text-red-500 py-8">{error}</div>;
  if (!recipes.length) return <div className="text-center py-8">No recipes found.</div>;

  return (
    <section className="potato-recipes-section py-12 px-6">
      <div className="container mx-auto max-w-5xl">
        <h2 className="text-3xl font-bold mb-8 text-center text-amber-700">Potato Recipes</h2>
        <div className="grid md:grid-cols-3 gap-8">
          {recipes.map((recipe) => (
            <div key={recipe.id} className="recipe-card bg-white rounded-xl shadow-lg p-6 flex flex-col items-center hover:shadow-2xl transition-shadow duration-300">
              <img
                src={recipe.image}
                alt={recipe.title}
                className="w-full h-48 object-cover rounded-lg mb-4 border"
                style={{ maxWidth: "320px" }}
              />
              <h3 className="text-xl font-semibold mb-2 text-center">{recipe.title}</h3>
              <a
                href={`https://spoonacular.com/recipes/${recipe.title.replace(/\s+/g, "-").toLowerCase()}-${recipe.id}`}
                target="_blank"
                rel="noopener noreferrer"
                className="mt-2 inline-block bg-amber-600 hover:bg-amber-700 text-white font-bold py-2 px-4 rounded-lg shadow-md transition-all duration-200"
              >
                View Recipe
              </a>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
