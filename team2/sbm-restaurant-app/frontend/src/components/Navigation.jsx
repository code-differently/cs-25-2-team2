"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";

export default function Navigation() {
  const pathname = usePathname();

  const navItems = [
    { name: "Home", path: "/" },
    { name: "Menu", path: "/menu" },
    { name: "Cart", path: "/cart" },
    { name: "Orders", path: "/orders" },
  ];

  return (
    <nav className="bg-white shadow-md border-b-2 border-amber-200 sticky top-0 z-50">
      <div className="container mx-auto px-6 py-4">
        <div className="flex justify-between items-center">
          <h1 className="text-2xl font-bold text-amber-600">Spud Munch Bunch</h1>
          <div className="flex gap-4">
            {navItems.map((item) => (
              <Link
                key={item.path}
                href={item.path}
                className={`px-4 py-2 rounded-lg font-semibold transition-colors duration-200 ${
                  pathname === item.path
                    ? "bg-amber-500 text-white"
                    : "bg-gray-100 text-gray-700 hover:bg-amber-100"
                }`}
              >
                {item.name}
              </Link>
            ))}
          </div>
        </div>
      </div>
    </nav>
  );
}