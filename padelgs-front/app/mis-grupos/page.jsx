"use client";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import Loading from "../components/Loading";

export default function MisGrupos() {
  const [grupos, setGrupos] = useState([]);
  const [loading, setLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    const token = document.cookie
      .split("; ")
      .find((row) => row.startsWith("token="))
      ?.split("=")[1];

    if (!token) {
      router.push("/login");
      return;
    }

    fetch(`${process.env.NEXT_PUBLIC_BASE_URL}/api/auth/me/groups`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => res.json())
      .then((data) => {
        const groups = data.data;
        if (groups.length === 1) {
          // Si solo tiene un grupo, redirigir directo
          router.push(`/${groups[0].slug}/admin`);
        } else {
          setGrupos(groups);
          setLoading(false);
        }
      });
  }, []);

  if (loading) return <Loading />;

  return (
    <div className="max-w-md mx-auto mt-10 px-4">
      <h2 className="text-xl font-bold text-center mb-6">Tus grupos</h2>
      <div className="flex flex-col gap-3">
        {grupos.map((grupo) => (
          <a key={grupo.id} href={`/${grupo.slug}/admin`}>
            <div className="bg-black shadow-amber-50/50 shadow-2xs p-5 clip-skew w-full hover:opacity-80 transition-opacity">
              <p className="text-center gold-text">{grupo.name}</p>
            </div>
          </a>
        ))}
      </div>
    </div>
  );
}
