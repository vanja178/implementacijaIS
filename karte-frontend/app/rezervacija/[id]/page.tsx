"use client";

import { useEffect, useState, use } from "react";

interface Region {
  id: number;
  name: string;
  price: number;
}

interface Currency {
  id: number;
  code: string;
  name: string;
}

interface FormData {
  firstName: string;
  lastName: string;
  address: string;
  postalCode: string;
  city: string;
  country: string;
  email: string;
  emailConfirm: string;
  promoCode: string;
}

export default function Rezervacija({ params }: { params: Promise<{ id: string }> }) {
  const { id } = use(params);
  const [regions, setRegions] = useState<Region[]>([]);
  const [selectedRegions, setSelectedRegions] = useState<number[]>([]);
  const [currencies, setCurrencies] = useState<Currency[]>([]);
  const [selectedCurrency, setSelectedCurrency] = useState<Currency | null>(null);
  const [convertedPrice, setConvertedPrice] = useState<number | null>(null);
  const [formData, setFormData] = useState<FormData>({
    firstName: "",
    lastName: "",
    address: "",
    postalCode: "",
    city: "",
    country: "",
    email: "",
    emailConfirm: "",
    promoCode: "",
  });
  const [result, setResult] = useState<any>(null);
  const [error, setError] = useState("");

  useEffect(() => {
    fetch(`http://localhost:8080/api/concert-region-prices/concert/${id}`)
      .then((res) => res.json())
      .then((data) => {
        if (Array.isArray(data)) {
          setRegions(data);
        } else {
          setRegions([]);
        }
      });

    fetch("http://localhost:8080/api/currencies")
      .then((res) => res.json())
      .then((data) => {
        setCurrencies(data);
        const rsd = data.find((c: Currency) => c.code === "RSD");
        if (rsd) setSelectedCurrency(rsd);
      });
  }, [id]);

  useEffect(() => {
    if (selectedRegions.length === 0 || !selectedCurrency) {
      setConvertedPrice(null);
      return;
    }

    const totalRSD = selectedRegions.reduce((sum, regionId) => {
      const region = regions.find((r) => r.id === regionId);
      return sum + ((region as any)?.price || 0);
    }, 0);

    if (selectedCurrency.code === "RSD") {
      setConvertedPrice(totalRSD);
      return;
    }

    fetch(`http://localhost:8080/api/currencies/convert?amount=${totalRSD}&from=RSD&to=${selectedCurrency.code}`)
      .then((res) => res.json())
      .then((data) => setConvertedPrice(data));
  }, [selectedRegions, selectedCurrency, regions]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const toggleRegion = (regionPriceId: number) => {
    setSelectedRegions((prev) =>
      prev.includes(regionPriceId)
        ? prev.filter((r) => r !== regionPriceId)
        : [...prev, regionPriceId]
    );
  };

  const handleSubmit = async () => {
    if (formData.email !== formData.emailConfirm) {
      setError("Email adrese se ne poklapaju!");
      return;
    }
    if (selectedRegions.length === 0) {
      setError("Morate izabrati bar jedno mesto!");
      return;
    }

    const ticket = {
      concert: { id: id },
      currency: { id: selectedCurrency?.id || 1 },
      firstName: formData.firstName,
      lastName: formData.lastName,
      address: formData.address,
      postalCode: formData.postalCode,
      city: formData.city,
      country: formData.country,
      email: formData.email,
    };

    let url = `http://localhost:8080/api/tickets?regionPriceIds=${selectedRegions.join("&regionPriceIds=")}`;
    if (formData.promoCode) {
      url += `&promoCode=${formData.promoCode}`;
    }

    const res = await fetch(url, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(ticket),
    });

    if (res.ok) {
      const data = await res.json();
      setResult(data);
      setError("");
    } else {
      setError("Došlo je do greške pri rezervaciji.");
    }
  };

  if (result) {
    return (
      <main className="max-w-2xl mx-auto p-8">
        <h1 className="text-3xl font-bold mb-4">Rezervacija uspešna!</h1>
        <p className="mb-2">Vaša šifra: <strong>{result.ticket?.code}</strong></p>
        <p className="mb-2">Ukupna cena: <strong>{result.ticket?.totalPrice} RSD</strong></p>
        <p className="mb-2">Vaš promo kod za sledeću kupovinu: <strong>{result.promoCode}</strong></p>
        <p className="text-gray-500 text-sm">Sačuvajte šifru i promo kod za kasniji pristup.</p>
      </main>
    );
  }

  return (
    <main className="max-w-2xl mx-auto p-8">
      <h1 className="text-3xl font-bold mb-8">Rezervacija karte</h1>

      {error && <p className="text-red-500 mb-4">{error}</p>}

      <div className="flex flex-col gap-4 mb-8">
        <h2 className="text-xl font-semibold">Lični podaci</h2>
        {[
          { name: "firstName", label: "Ime" },
          { name: "lastName", label: "Prezime" },
          { name: "address", label: "Adresa" },
          { name: "postalCode", label: "Poštanski broj" },
          { name: "city", label: "Mesto" },
          { name: "country", label: "Država" },
          { name: "email", label: "Email" },
          { name: "emailConfirm", label: "Potvrda emaila" },
          { name: "promoCode", label: "Promo kod (opciono)" },
        ].map((field) => (
          <div key={field.name}>
            <label className="block text-sm mb-1">{field.label}</label>
            <input
              name={field.name}
              value={formData[field.name as keyof FormData]}
              onChange={handleChange}
              className="w-full border rounded p-2 bg-transparent"
            />
          </div>
        ))}
      </div>

      <div className="mb-8">
        <h2 className="text-xl font-semibold mb-4">Izaberi mesta</h2>
        {regions.length === 0 ? (
          <p>Nema dostupnih regiona.</p>
        ) : (
          regions.map((region) => (
            <div
              key={region.id}
              onClick={() => toggleRegion(region.id)}
              className={`border rounded p-4 mb-2 cursor-pointer ${
                selectedRegions.includes(region.id)
                  ? "border-blue-500 bg-blue-50 text-black"
                  : ""
              }`}
            >
              <p className="font-medium">{(region as any).region?.name}</p>
              <p className="text-sm text-gray-500">{(region as any).price} RSD</p>
            </div>
          ))
        )}
      </div>

      <div className="mb-8">
        <h2 className="text-xl font-semibold mb-4">Izaberi valutu</h2>
        <select
          className="w-full border rounded p-2 bg-white text-black"
          value={selectedCurrency?.id || ""}
          onChange={(e) => {
            const currency = currencies.find((c) => c.id === Number(e.target.value));
            setSelectedCurrency(currency || null);
          }}
        >
          {currencies.map((currency) => (
            <option key={currency.id} value={currency.id}>
              {currency.name} ({currency.code})
            </option>
          ))}
        </select>

        {convertedPrice !== null && selectedCurrency && (
          <p className="mt-4 text-lg font-semibold">
            Ukupna cena: {convertedPrice} {selectedCurrency.code}
          </p>
        )}
      </div>

      <button
        onClick={handleSubmit}
        className="w-full bg-blue-600 text-white py-3 rounded-lg font-medium hover:bg-blue-700"
      >
        Rezerviši
      </button>
    </main>
  );
}