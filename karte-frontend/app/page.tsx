"use client";

import { useEffect, useState } from "react";

interface Concert {
  id: number;
  name: string;
  dateTime: string;
  location: { name: string };
  category: { name: string };
}

export default function Home() {
  const [concerts, setConcerts] = useState<Concert[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch("http://localhost:8080/api/concerts")
      .then((res) => res.json())
      .then((data) => {
        setConcerts(data);
        setLoading(false);
      });
  }, []);

  const grouped = concerts.reduce((acc, concert) => {
    const category = concert.category?.name || "Ostalo";
    if (!acc[category]) acc[category] = [];
    acc[category].push(concert);
    return acc;
  }, {} as Record<string, Concert[]>);

  return (
    <main className="max-w-4xl mx-auto p-8">
      <div className="mb-10">
        <h1 className="text-4xl font-bold mb-3">Prodaja karata za koncerte</h1>
        <p className="text-gray-500 text-lg">
          Dobrodošli! Ovde možete pronaći i rezervisati karte za najpoznatije koncerte. 
          Izaberite koncert, odaberite mesta i rezervišite kartu u nekoliko koraka.
        </p>
      </div>

      <h2 className="text-2xl font-semibold mb-6">Koncerti po kategorijama</h2>

      {loading ? (
        <p>Učitavanje...</p>
      ) : concerts.length === 0 ? (
        <p>Nema koncerata.</p>
      ) : (
        Object.entries(grouped).map(([category, items]) => (
          <div key={category} className="mb-8">
            <h3 className="text-xl font-semibold mb-4">{category}</h3>
            <div className="grid grid-cols-1 gap-4">
              {items.map((concert) => (
                <a href={`/rezervacija/${concert.id}`} key={concert.id} className="block border rounded-lg p-4 hover:border-blue-500 cursor-pointer">
                  <h4 className="font-medium">{concert.name}</h4>
                  <p className="text-gray-600">{concert.location?.name}</p>
                  <p className="text-gray-500 text-sm">{new Date(concert.dateTime).toLocaleString("sr-RS")}</p>
                </a>
              ))}
            </div>
          </div>
        ))
      )}
    </main>
  );
}