import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata = {
  title: "Padel Global Score",
  description: "Estadísticas y resultados",
};

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body
        className={`${geistSans.variable} ${geistMono.variable}  antialiased flex flex-col w-full items-center`}
      >
        <Navbar />
        <main className="w-full md:max-w-2/6 ">{children}</main>
        <Footer />
      </body>
    </html>
  );
}
