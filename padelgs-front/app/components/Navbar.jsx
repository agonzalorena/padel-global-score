import React from "react";

const Navbar = () => {
  return (
    <header className="w-full border-b border-white/10 backdrop-blur-sm sticky top-0 z-50">
      <div className="container mx-auto px-4 py-6">
        <div className="flex items-center justify-center gap-4 drop-shadow-xl drop-shadow-white/40">
          <a href="/" className="flex">
            <div className="w-9 h-9 ">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="24"
                height="24"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                strokeWidth="2"
                strokeLinecap="round"
                strokeLinejoin="round"
                className="lucide lucide-trophy w-8 h-8 text-primary"
              >
                <path d="M6 9H4.5a2.5 2.5 0 0 1 0-5H6"></path>
                <path d="M18 9h1.5a2.5 2.5 0 0 0 0-5H18"></path>
                <path d="M4 22h16"></path>
                <path d="M10 14.66V17c0 .55-.47.98-.97 1.21C7.85 18.75 7 20.24 7 22"></path>
                <path d="M14 14.66V17c0 .55.47.98.97 1.21C16.15 18.75 17 20.24 17 22"></path>
                <path d="M18 2H6v7a6 6 0 0 0 12 0V2Z"></path>
              </svg>
            </div>
            <h1 className="text-3xl md:text-4xl bg-gold-real bg-clip-text text-transparent font-black">
              Padel Global Score
            </h1>
          </a>
        </div>
      </div>
    </header>
  );
};

export default Navbar;
