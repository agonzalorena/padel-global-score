"use client";
import { useEffect, useState } from "react";

const FormCreateMatch = ({ slug, group, hasPendingMatch }) => {
  if (hasPendingMatch) {
    return (
      <div className="w-full flex justify-center mt-5">
        <p className="text-sm text-muted-foreground">
          Ya hay un partido pendiente. Cargá el resultado antes de crear uno
          nuevo.
        </p>
      </div>
    );
  }
  const [locations, setLocations] = useState([]);
  const [location, setLocation] = useState(1);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const data = {
      date: formData.get("date"),
      time: formData.get("time"),
      locationId: parseInt(location), // ← agregar parseInt
      teamAId: group.teamA.id, // ← directo del prop
      teamBId: group.teamB.id, // ← directo del prop
    };
    console.log("Enviando:", data);
    const token = document.cookie
      .split("; ")
      .find((row) => row.startsWith("token="))
      ?.split("=")[1];

    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_BASE_URL}/api/groups/${slug}/matches`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify(data),
        },
      );
      if (!response.ok) {
        const errorData = await response.json();
        setError(errorData.message);
        throw new Error("Network response was not ok");
      }
      alert("Partido creado exitosamente");
      window.location.reload();
    } catch (error) {
      alert("Error creating match");
    }
  };

  const fetchLocations = async () => {
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_BASE_URL}/api/locations`,
      );
      const res = await response.json();
      setLocations(res.data);
    } catch (error) {
      console.error("Error fetching locations:", error);
    }
  };

  useEffect(() => {
    fetchLocations();
  }, []);

  return (
    <div className="w-full justify-center items-center flex flex-col mt-5">
      <p>Crear Partido</p>
      <p className="text-xs text-red-500">
        {error != null && error.replace("Bad Request: ", "")}
      </p>
      <form
        onSubmit={handleSubmit}
        className="flex flex-col items-center gap-4 mt-4 bg-card/50 p-3 rounded-lg"
      >
        <input
          type="date"
          name="date"
          required
          className="bg-card p-2 rounded-lg w-full"
        />
        <input
          type="time"
          name="time"
          required
          className="bg-card p-2 rounded-lg w-full"
        />
        <select
          value={location}
          onChange={(e) => setLocation(e.target.value)}
          className="w-full bg-card p-2 rounded-lg"
        >
          {locations.map((loc) => (
            <option key={loc.id} value={loc.id}>
              {loc.name}
            </option>
          ))}
        </select>
        <button type="submit" className="underline">
          Crear
        </button>
      </form>
    </div>
  );
};

export default FormCreateMatch;
