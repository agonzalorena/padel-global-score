import React from "react";
const brands = [
  {
    name: "Orella",
    imgSrc: "/brands/orella1.webp",
    link: "https://www.fortnite.com/?lang=es-ES",
  },
  {
    name: "Red Bull TV",
    imgSrc: "/brands/redbull1.webp",
    link: "https://www.redbull.com/ar-es/live-events",
  },
  {
    name: "Word Padel Tour",
    imgSrc: "/brands/wpt1.webp",
    link: "https://www.worldpadeltour.com/",
  },
  {
    name: "EA Sports",
    imgSrc: "/brands/ea1.webp",
    link: "https://www.ea.com/es-es/",
  },
  {
    name: "Joma",
    imgSrc: "/brands/joma2.webp",
    link: "https://www.joma-sport.com/es/",
  },
  {
    name: "FD",
    imgSrc: "/brands/fd3.webp",
    link: "https://www.instagram.com/f.dgrafico/",
  },
  {
    name: "Messi",
    imgSrc: "/brands/messi1.webp",
    link: "https://messi.com/",
  },
  {
    name: "Bullpadel",
    imgSrc: "/brands/bullpadel1.webp",
    link: "https://www.bullpadel.com/es/es/",
  },
  {
    name: "Head",
    imgSrc: "/brands/head1.webp",
    link: "https://www.head.com/es-es",
  },
  {
    name: "Wilson",
    imgSrc: "/brands/wilson1.webp",
    link: "https://www.wilsonstore.com.ar/pages/padel",
  },
];

const Footer = () => {
  return (
    <footer className="bg-[#302e17] w-full md:px-20 px-2 py-10 mt-8">
      <div className="w-full">
        <h2 className="text-2xl font-bold font-mono mb-2">SPONSORS</h2>
      </div>
      <div className="flex flex-col py-px-32 lg:py-px-48 gap-px-16 lg:gap-px-24">
        <div className="flex flex-wrap px-3 gap-px-24 md:gap-px-32 lg:gap-px-48">
          {brands.map((brand) => (
            <a
              key={brand.name}
              href={brand.link}
              target="_blank"
              rel="noopener noreferrer"
              className="flex items-center rounded-m -m-px-8 p-px-8 py-3 w-[72px] md:w-[88px] lg:w-24 hover:bg-static-static-7 "
            >
              <img
                className="w-14 md:w-[72px] lg:w-20 aspect-video object-contain"
                src={brand.imgSrc}
                alt={brand.name}
              />
            </a>
          ))}
        </div>
        <div className="w-full flex flex-col items-center">
          <h2 className="text-2xl font-bold font-mono mt-10 mb-2">Contacto</h2>
          <p>En la cancha</p>
          <a
            href="https://t.me/+S5o5kvc7sx0yYzVh"
            target="_blank"
            rel="noopener noreferrer"
            className="mt-2 text-muted-foreground text-sm underline"
          >
            Recibir novedades
          </a>
          <a href="/admin" className="mt-8 text-muted-foreground underline">
            Administrar
          </a>
        </div>
        <div className="w-full flex justify-center mt-10">
          <p className="text-sm text-center text-muted-foreground">
            © 2025 Padel Global Score. All rights reserved.
          </p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
