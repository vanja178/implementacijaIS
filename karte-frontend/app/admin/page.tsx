"use client";

import { useEffect, useState } from "react";

interface Location {
  id: number;
  name: string;
  address: string;
}

interface Category {
  id: number;
  name: string;
}

interface Currency {
  id: number;
  code: string;
  name: string;
}

interface Concert {
  id: number;
  name: string;
  dateTime: string;
  location: { id: number; name: string };
  category: { id: number; name: string };
}

export default function Admin() {
  const [activeTab, setActiveTab] = useState("locations");
  const [locations, setLocations] = useState<Location[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [currencies, setCurrencies] = useState<Currency[]>([]);
  const [concerts, setConcerts] = useState<Concert[]>([]);

  const [newLocation, setNewLocation] = useState({ name: "", address: "" });
  const [newCategory, setNewCategory] = useState({ name: "" });
  const [newCurrency, setNewCurrency] = useState({ code: "", name: "" });
  const [newConcert, setNewConcert] = useState({
    name: "",
    dateTime: "",
    locationId: "",
    categoryId: "",
  });

  const [success, setSuccess] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    fetchAll();
  }, []);

  const fetchAll = () => {
    fetch("http://localhost:8080/api/locations")
      .then((res) => res.json())
      .then(setLocations);
    fetch("http://localhost:8080/api/concert-categories")
      .then((res) => res.json())
      .then(setCategories);
    fetch("http://localhost:8080/api/currencies")
      .then((res) => res.json())
      .then(setCurrencies);
    fetch("http://localhost:8080/api/concerts")
      .then((res) => res.json())
      .then(setConcerts);
  };

  const showSuccess = (msg: string) => {
    setSuccess(msg);
    setError("");
    setTimeout(() => setSuccess(""), 3000);
  };

  const addLocation = async () => {
    const res = await fetch("http://localhost:8080/api/locations", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(newLocation),
    });
    if (res.ok) {
      showSuccess("Lokacija dodana!");
      setNewLocation({ name: "", address: "" });
      fetchAll();
    } else {
      setError("Greška pri dodavanju lokacije.");
    }
  };

  const addCategory = async () => {
    const res = await fetch("http://localhost:8080/api/concert-categories", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(newCategory),
    });
    if (res.ok) {
      showSuccess("Kategorija dodana!");
      setNewCategory({ name: "" });
      fetchAll();
    } else {
      setError("Greška pri dodavanju kategorije.");
    }
  };

  const addCurrency = async () => {
    const res = await fetch("http://localhost:8080/api/currencies", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(newCurrency),
    });
    if (res.ok) {
      showSuccess("Valuta dodana!");
      setNewCurrency({ code: "", name: "" });
      fetchAll();
    } else {
      setError("Greška pri dodavanju valute.");
    }
  };

  const addConcert = async () => {
    const res = await fetch("http://localhost:8080/api/concerts", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        name: newConcert.name,
        dateTime: newConcert.dateTime,
        location: { id: Number(newConcert.locationId) },
        category: { id: Number(newConcert.categoryId) },
      }),
    });
    if (res.ok) {
      showSuccess("Koncert dodan!");
      setNewConcert({ name: "", dateTime: "", locationId: "", categoryId: "" });
      fetchAll();
    } else {
      setError("Greška pri dodavanju koncerta.");
    }
  };

  const tabs = [
    { id: "locations", label: "Lokacije" },
    { id: "categories", label: "Kategorije" },
    { id: "concerts", label: "Koncerti" },
    { id: "currencies", label: "Valute" },
  ];

  return (
    <main className="max-w-4xl mx-auto p-8">
      <h1 className="text-3xl font-bold mb-8">Admin panel</h1>

      {success && <p className="text-green-500 mb-4">{success}</p>}
      {error && <p className="text-red-500 mb-4">{error}</p>}

      <div className="flex gap-2 mb-8">
        {tabs.map((tab) => (
          <button
            key={tab.id}
            onClick={() => setActiveTab(tab.id)}
            className={`px-4 py-2 rounded-lg font-medium ${
              activeTab === tab.id
                ? "bg-blue-600 text-white"
                : "border hover:bg-gray-100 hover:text-black"
            }`}
          >
            {tab.label}
          </button>
        ))}
      </div>

      {activeTab === "locations" && (
        <div>
          <h2 className="text-xl font-semibold mb-4">Lokacije</h2>
          <div className="flex flex-col gap-2 mb-6">
            <input
              placeholder="Naziv lokacije"
              value={newLocation.name}
              onChange={(e) => setNewLocation({ ...newLocation, name: e.target.value })}
              className="border rounded p-2 bg-transparent"
            />
            <input
              placeholder="Adresa"
              value={newLocation.address}
              onChange={(e) => setNewLocation({ ...newLocation, address: e.target.value })}
              className="border rounded p-2 bg-transparent"
            />
            <button onClick={addLocation} className="bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700">
              Dodaj lokaciju
            </button>
          </div>
          <div className="flex flex-col gap-2">
            {locations.map((loc) => (
              <div key={loc.id} className="border rounded p-3">
                <p className="font-medium">{loc.name}</p>
                <p className="text-sm text-gray-500">{loc.address}</p>
              </div>
            ))}
          </div>
        </div>
      )}

      {activeTab === "categories" && (
        <div>
          <h2 className="text-xl font-semibold mb-4">Kategorije koncerata</h2>
          <div className="flex gap-2 mb-6">
            <input
              placeholder="Naziv kategorije"
              value={newCategory.name}
              onChange={(e) => setNewCategory({ name: e.target.value })}
              className="border rounded p-2 bg-transparent flex-1"
            />
            <button onClick={addCategory} className="bg-blue-600 text-white px-4 rounded-lg hover:bg-blue-700">
              Dodaj
            </button>
          </div>
          <div className="flex flex-col gap-2">
            {categories.map((cat) => (
              <div key={cat.id} className="border rounded p-3">
                <p className="font-medium">{cat.name}</p>
              </div>
            ))}
          </div>
        </div>
      )}

      {activeTab === "concerts" && (
        <div>
          <h2 className="text-xl font-semibold mb-4">Koncerti</h2>
          <div className="flex flex-col gap-2 mb-6">
            <input
              placeholder="Naziv koncerta"
              value={newConcert.name}
              onChange={(e) => setNewConcert({ ...newConcert, name: e.target.value })}
              className="border rounded p-2 bg-transparent"
            />
            
            <input
                    type="date"
                    value={newConcert.dateTime.split("T")[0] || ""}
                     onChange={(e) => setNewConcert({ 
                           ...newConcert, 
                       dateTime: e.target.value + "T" + (newConcert.dateTime.split("T")[1] || "00:00") 
                       })}
                     className="border rounded p-2 bg-white text-black"
          />
        
          <input
                type="time"
                value={newConcert.dateTime.split("T")[1] || ""}
                onChange={(e) => setNewConcert({ 
                ...newConcert, 
                 dateTime: (newConcert.dateTime.split("T")[0] || "") + "T" + e.target.value 
                })}
               className="border rounded p-2 bg-white text-black"
            />
            <select
              value={newConcert.locationId}
              onChange={(e) => setNewConcert({ ...newConcert, locationId: e.target.value })}
              className="border rounded p-2 bg-white text-black"
            >
              <option value="">Izaberi lokaciju</option>
              {locations.map((loc) => (
                <option key={loc.id} value={loc.id}>{loc.name}</option>
              ))}
            </select>
            <select
              value={newConcert.categoryId}
              onChange={(e) => setNewConcert({ ...newConcert, categoryId: e.target.value })}
              className="border rounded p-2 bg-white text-black"
            >
              <option value="">Izaberi kategoriju</option>
              {categories.map((cat) => (
                <option key={cat.id} value={cat.id}>{cat.name}</option>
              ))}
            </select>
            <button onClick={addConcert} className="bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700">
              Dodaj koncert
            </button>
          </div>
          <div className="flex flex-col gap-2">
            {concerts.map((concert) => (
              <div key={concert.id} className="border rounded p-3">
                <p className="font-medium">{concert.name}</p>
                <p className="text-sm text-gray-500">{concert.location?.name} — {new Date(concert.dateTime).toLocaleString("sr-RS")}</p>
              </div>
            ))}
          </div>
        </div>
      )}

      {activeTab === "currencies" && (
        <div>
          <h2 className="text-xl font-semibold mb-4">Valute</h2>
          <div className="flex flex-col gap-2 mb-6">
            <input
              placeholder="Kod valute (npr. EUR)"
              value={newCurrency.code}
              onChange={(e) => setNewCurrency({ ...newCurrency, code: e.target.value })}
              className="border rounded p-2 bg-transparent"
            />
            <input
              placeholder="Naziv valute"
              value={newCurrency.name}
              onChange={(e) => setNewCurrency({ ...newCurrency, name: e.target.value })}
              className="border rounded p-2 bg-transparent"
            />
            <button onClick={addCurrency} className="bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700">
              Dodaj valutu
            </button>
          </div>
          <div className="flex flex-col gap-2">
            {currencies.map((cur) => (
              <div key={cur.id} className="border rounded p-3">
                <p className="font-medium">{cur.name} ({cur.code})</p>
              </div>
            ))}
          </div>
        </div>
      )}
    </main>
  );
}