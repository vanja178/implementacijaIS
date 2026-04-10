import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import Link from "next/link";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "Prodaja karata za koncerte",
  description: "Aplikacija za rezervaciju karata za koncerte",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html
      lang="en"
      className={`${geistSans.variable} ${geistMono.variable} h-full antialiased`}
    >
      <body className="min-h-full flex flex-col">
        <nav className="border-b px-8 py-4 flex justify-around items-center">
  <Link href="/" className="font-bold text-lg hover:text-blue-500">
    🎵 Koncerti
  </Link>
  <Link href="/rezervacija" className="hover:text-blue-500">
    Rezervacija
  </Link>
  <Link href="/izmena" className="hover:text-blue-500">
    Izmena i otkazivanje
  </Link>
  <Link href="/admin" className="hover:text-blue-500">
    Admin
  </Link>
</nav>
        {children}
      </body>
    </html>
  );
}