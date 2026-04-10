"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

interface Concert {
  id: number;
  name: string;
  dateTime: string;
  location: { name: string };
  category: { name: string };
}

export default function RezervacijaIzbor() {
  const [concerts, setConcerts] = useState<Concert[]>([]);
  const [loading, setLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    fetch("http://localhost:8080/api/concerts")
      .then((res) => res.json())
      .then((data) => {
        setConcerts(data);
        setLoading(false);
      });
  }, []);

  return (
    <main className="max-w-4xl mx-auto p-8">
      <h1 className="text-3xl font-bold mb-2">Rezervacija karte</h1>
      <p className="text-gray-500 mb-8">Izaberite koncert za koji želite da rezervišete kartu.</p>

      {loading ? (
        <p>Učitavanje...</p>
      ) : concerts.length === 0 ? (
        <p>Nema dostupnih koncerata.</p>
      ) : (
        <div className="flex flex-col gap-4">
          {concerts.map((concert) => (
            <div
              key={concert.id}
              onClick={() => router.push(`/rezervacija/${concert.id}`)}
              className="border rounded-lg p-4 cursor-pointer hover:border-blue-500"
            >
              <h2 className="text-xl font-bold">{concert.name}</h2>
              <p className="text-gray-500">{concert.location?.name}</p>
              <p className="text-gray-400 text-sm">{new Date(concert.dateTime).toLocaleString("sr-RS")}</p>
              <p className="text-blue-500 text-sm mt-1">{concert.category?.name}</p>
            </div>
          ))}
        </div>
      )}
    </main>
  );
}