"use client";
import React from "react";

const FormLogin = () => {
  const handleSubmit = async (event) => {
    event.preventDefault();
    const formData = new FormData(event.target);
    const username = formData.get("username");
    const password = formData.get("password");
    event.target.reset();

    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_BASE_URL}/api/auth`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ username, password }),
        },
      );
      if (!response.ok) throw new Error("Login failed");

      const res = await response.json();
      const token = res.data.token;
      document.cookie = `token=${token}; path=/`;

      // Decodificar el JWT para obtener el groupSlug
      const payload = JSON.parse(atob(token.split(".")[1]));
      const slug = payload.groupSlug;

      window.location.href = "/mis-grupos";
    } catch (error) {
      alert("Usuario o contraseña incorrectos");
    }
  };

  return (
    <div className="w-full flex justify-center p-4">
      <form
        onSubmit={handleSubmit}
        className="bg-card/50 rounded-2xl p-2 flex flex-col justify-center gap-5"
      >
        <div className="flex flex-col">
          <label htmlFor="username">Usuario:</label>
          <input
            type="text"
            id="username"
            name="username"
            required
            className="bg-card rounded-lg p-1"
          />
        </div>
        <div className="flex flex-col">
          <label htmlFor="password">Contraseña:</label>
          <input
            type="password"
            id="password"
            name="password"
            required
            className="bg-card rounded-lg p-1"
          />
        </div>
        <button
          type="submit"
          className="mt-2 p-2 shadow-xs shadow-white/10 rounded-xl"
        >
          Iniciar
        </button>
      </form>
    </div>
  );
};

export default FormLogin;
