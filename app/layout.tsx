import type { Metadata } from "next";
import "./globals.css";
import Providers from './providers';

export const metadata: Metadata = {
  title: "DomProduct - Onlayn Do'kon",
  description: "Eng sifatli mahsulotlarni onlayn xarid qiling",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="uz">
      <body className="antialiased">
        <Providers>
          {children}
        </Providers>
      </body>
    </html>
  );
}
