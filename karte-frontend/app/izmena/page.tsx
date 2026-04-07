"use client";

import { useState, useEffect } from "react";

interface Ticket {
  id: number;
  code: string;
  firstName: string;
  lastName: string;
  totalPrice: number;
  status: string;
  email: string;
  concert: { id: number };
}

interface TicketSeat {
  id: number;
  pricePaid: number;
  concertRegionPrice: {
    region: { name: string };
    price: number;
  };
}

interface RegionPrice {
  id: number;
  price: number;
  region: { name: string };
}

export default function Izmena() {
  const [code, setCode] = useState("");
  const [email, setEmail] = useState("");
  const [ticket, setTicket] = useState<Ticket | null>(null);
  const [seats, setSeats] = useState<TicketSeat[]>([]);
  const [availableRegions, setAvailableRegions] = useState<RegionPrice[]>([]);
  const [regionQuantities, setRegionQuantities] = useState<Record<number, number>>({});
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  useEffect(() => {
    if (ticket) {
      fetch(`http://localhost:8080/api/tickets/${ticket.id}/seats`)
        .then((res) => res.json())
        .then((data) => {
          if (Array.isArray(data)) setSeats(data);
        });

      fetch(`http://localhost:8080/api/concert-region-prices/concert/${ticket.concert.id}`)
        .then((res) => res.json())
        .then((data) => {
          if (Array.isArray(data)) setAvailableRegions(data);
        });
    }
  }, [ticket]);

  const findTicket = async () => {
    setError("");
    setSuccess("");
    const res = await fetch(
      `http://localhost:8080/api/tickets?code=${code}&email=${email}`
    );
    if (res.ok) {
      const data = await res.json();
      setTicket(data);
    } else {
      setError("Karta nije pronađena. Proverite šifru i email.");
      setTicket(null);
    }
  };

  const cancelTicket = async () => {
    const res = await fetch(
      `http://localhost:8080/api/tickets/cancel?code=${code}&email=${email}`,
      { method: "POST" }
    );
    if (res.ok) {
      setSuccess("Karta je uspešno otkazana.");
      setTicket(null);
    } else {
      setError("Greška pri otkazivanju karte.");
    }
  };

  const updateQuantity = (regionId: number, delta: number) => {
    setRegionQuantities((prev) => ({
      ...prev,
      [regionId]: Math.max(0, (prev[regionId] || 0) + delta),
    }));
  };

  const addSeats = async () => {
    const regionPriceIds: number[] = [];
    availableRegions.forEach((region) => {
      const qty = regionQuantities[region.id] || 0;
      for (let i = 0; i < qty; i++) {
        regionPriceIds.push(region.id);
      }
    });

    if (regionPriceIds.length === 0) {
      setError("Izaberite bar jedno mesto za dodavanje.");
      return;
    }

    const url = `http://localhost:8080/api/tickets/add-seats?code=${code}&email=${email}&regionPriceIds=${regionPriceIds.join("&regionPriceIds=")}`;
    const res = await fetch(url, { method: "POST" });
    if (res.ok) {
      const data = await res.json();
      setTicket(data);
      setRegionQuantities({});
      setSuccess("Mesta su uspešno dodana.");
      setError("");
    } else {
      setError("Greška pri dodavanju mesta.");
    }
  };

  const removeSeat = async (seatId: number) => {
    const url = `http://localhost:8080/api/tickets/remove-seats?code=${code}&email=${email}&seatIds=${seatId}`;
    const res = await fetch(url, { method: "POST" });
    if (res.ok) {
      const data = await res.json();
      setTicket(data);
      setSeats((prev) => prev.filter((s) => s.id !== seatId));
      setSuccess("Mesto je uspešno uklonjeno.");
      setError("");
    } else {
      setError("Greška pri uklanjanju mesta.");
    }
  };

  return (
    <main className="max-w-2xl mx-auto p-8">
      <h1 className="text-3xl font-bold mb-8">Izmena / Otkazivanje karte</h1>

      <div className="flex flex-col gap-4 mb-8">
        <div>
          <label className="block text-sm mb-1">Šifra karte</label>
          <input
            value={code}
            onChange={(e) => setCode(e.target.value)}
            className="w-full border rounded p-2 bg-transparent"
          />
        </div>
        <div>
          <label className="block text-sm mb-1">Email</label>
          <input
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="w-full border rounded p-2 bg-transparent"
          />
        </div>
        <button
          onClick={findTicket}
          className="w-full bg-blue-600 text-white py-3 rounded-lg font-medium hover:bg-blue-700"
        >
          Pronađi kartu
        </button>
      </div>

      {error && <p className="text-red-500 mb-4">{error}</p>}
      {success && <p className="text-green-500 mb-4">{success}</p>}

      {ticket && (
        <div className="border rounded-lg p-6">
          <h2 className="text-xl font-semibold mb-4">Podaci o karti</h2>
          <p>Ime: <strong>{ticket.firstName} {ticket.lastName}</strong></p>
          <p>Šifra: <strong>{ticket.code}</strong></p>
          <p>Ukupna cena: <strong>{ticket.totalPrice} RSD</strong></p>
          <p>Status: <strong>{ticket.status}</strong></p>

          {ticket.status === "ACTIVE" && (
            <>
              <div className="mt-6">
                <h3 className="text-lg font-semibold mb-2">Trenutna mesta</h3>
                {seats.length === 0 ? (
                  <p className="text-gray-500">Nema mesta.</p>
                ) : (
                  seats.map((seat) => (
                    <div key={seat.id} className="flex justify-between items-center border rounded p-3 mb-2">
                      <div>
                        <p className="font-medium">{seat.concertRegionPrice?.region?.name}</p>
                        <p className="text-sm text-gray-500">{seat.pricePaid} RSD</p>
                      </div>
                      <button
                        onClick={() => removeSeat(seat.id)}
                        className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 text-sm"
                      >
                        Ukloni
                      </button>
                    </div>
                  ))
                )}
              </div>

              <div className="mt-6">
                <h3 className="text-lg font-semibold mb-2">Dodaj nova mesta</h3>
                {availableRegions.map((region) => (
                  <div key={region.id} className="border rounded p-3 mb-2">
                    <p className="font-medium">{(region as any).region?.name}</p>
                    <p className="text-sm text-gray-500">{region.price} RSD po mestu</p>
                    <div className="flex items-center gap-3 mt-2">
                      <button
                        onClick={() => updateQuantity(region.id, -1)}
                        className="border rounded px-3 py-1 hover:bg-gray-100 hover:text-black"
                      >-</button>
                      <span>{regionQuantities[region.id] || 0}</span>
                      <button
                        onClick={() => updateQuantity(region.id, 1)}
                        className="border rounded px-3 py-1 hover:bg-gray-100 hover:text-black"
                      >+</button>
                    </div>
                  </div>
                ))}
                <button
                  onClick={addSeats}
                  className="w-full bg-green-600 text-white py-3 rounded-lg font-medium hover:bg-green-700 mt-2"
                >
                  Dodaj mesta
                </button>
              </div>

              <div className="mt-6">
                <button
                  onClick={cancelTicket}
                  className="w-full bg-red-600 text-white py-3 rounded-lg font-medium hover:bg-red-700"
                >
                  Otkaži kartu
                </button>
              </div>
            </>
          )}
        </div>
      )}
    </main>
  );
}