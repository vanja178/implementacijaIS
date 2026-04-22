"use client";

import { useEffect, useState } from "react";

interface StatItem {
  name: string;
  count: number;
}

export default function Home() {
  const [concertStats, setConcertStats] = useState<StatItem[]>([]);
  const [locationStats, setLocationStats] = useState<StatItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  const fetchData = () => {
    return Promise.all([
      fetch("http://localhost:8081/api/stats/concerts").then((res) => res.json()),
      fetch("http://localhost:8081/api/stats/locations").then((res) => res.json()),
    ]).then(([concerts, locations]) => {
      setConcertStats(concerts.map((item: any[]) => ({ name: item[0], count: Number(item[1]) })));
      setLocationStats(locations.map((item: any[]) => ({ name: item[0], count: Number(item[1]) })));
    });
  };

  useEffect(() => {
    fetchData().then(() => setLoading(false));
  }, []);

  const handleRefresh = async () => {
    setRefreshing(true);
    await fetchData();
    setRefreshing(false);
  };

  if (loading) return <main className="max-w-4xl mx-auto p-8"><p>Učitavanje...</p></main>;

 return (
    <main className="max-w-4xl mx-auto p-8">
      <h1 className="text-3xl font-bold mb-2">Portal za izveštavanje</h1>
      <p className="text-gray-500 mb-8">Pregled prodatih karata po koncertima i lokacijama.</p>

      <div className="mb-10">
        <h2 className="text-xl font-semibold mb-4">Karte po koncertima</h2>
        {concertStats.length === 0 ? (
          <p className="text-gray-500">Nema podataka.</p>
        ) : (
          <table className="w-full border-collapse">
            <thead>
              <tr className="border-b">
                <th className="text-left py-2">Koncert</th>
                <th className="text-left py-2">Broj karata</th>
              </tr>
            </thead>
            <tbody>
              {concertStats.map((item) => (
                <tr key={item.name} className="border-b">
                  <td className="py-2">{item.name}</td>
                  <td className="py-2">{item.count}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      <div className="mb-10">
        <h2 className="text-xl font-semibold mb-4">Karte po lokacijama</h2>
        {locationStats.length === 0 ? (
          <p className="text-gray-500">Nema podataka.</p>
        ) : (
          <table className="w-full border-collapse">
            <thead>
              <tr className="border-b">
                <th className="text-left py-2">Lokacija</th>
                <th className="text-left py-2">Broj karata</th>
              </tr>
            </thead>
            <tbody>
              {locationStats.map((item) => (
                <tr key={item.name} className="border-b">
                  <td className="py-2">{item.name}</td>
                  <td className="py-2">{item.count}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      <button
        onClick={handleRefresh}
        disabled={refreshing}
        className="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 disabled:opacity-50"
      >
        {refreshing ? "Osvežavanje..." : "Osveži podatke"}
      </button>
    </main>
  );
}