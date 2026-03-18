import React from "react";

const BoxMenu = ({ slug }) => {
  return (
    <div className="flex-1 container mx-auto px-4 py-8 md:py-10">
      <div className="flex flex-col items-center">
        <div className="flex w-full mb-2 gap-2 text-center text-sm">
          <a href={`/${slug}/players`} className="w-1/2">
            <div className="bg-secondary px-5 py-6 border-b-2 border-white/50 rounded-2xl active:scale-95">
              <p>JUGADORES</p>
            </div>
          </a>
          <a href={`/${slug}/history`} className="w-1/2">
            <div className="bg-secondary px-5 py-6 border-b-2 border-white/50 rounded-2xl active:scale-95">
              <p>PARTIDOS</p>
            </div>
          </a>
        </div>
        {/* <a href="/news" className="w-full">
          <div className="mt-1 bg-secondary p-4 border-b-2 border-white/50 text-center text-sm rounded-2xl active:scale-95">
            NOTICIAS
          </div>
        </a> */}
      </div>
    </div>
  );
};

export default BoxMenu;
