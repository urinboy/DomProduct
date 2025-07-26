import type { Metadata, Viewport } from "next";
import "./globals.css";
import Providers from "./providers/providers";

// Next.js 15.4.4 uchun optimallashtirilgan metadata
export const metadata: Metadata = {
  title: {
    default: "DomProduct - Onlayn Do'kon",
    template: "%s | DomProduct"
  },
  description: "Eng sifatli mahsulotlarni onlayn xarid qiling. Tez yetkazib berish va kafolat bilan.",
  keywords: ["onlayn do'kon", "mahsulotlar", "xarid", "yetkazib berish", "O'zbekiston"],
  authors: [{ name: "DomProduct Team" }],
  creator: "DomProduct",
  publisher: "DomProduct",
  metadataBase: new URL("https://domproduct.uz"),
  alternates: {
    canonical: "/",
    languages: {
      "uz-UZ": "/uz",
      "ru-RU": "/ru",
    },
  },
  openGraph: {
    type: "website",
    locale: "uz_UZ",
    url: "https://domproduct.uz",
    siteName: "DomProduct",
    title: "DomProduct - Onlayn Do'kon",
    description: "Eng sifatli mahsulotlarni onlayn xarid qiling",
    images: [
      {
        url: "/og-image.jpg",
        width: 1200,
        height: 630,
        alt: "DomProduct Logo",
      },
    ],
  },
  twitter: {
    card: "summary_large_image",
    title: "DomProduct - Onlayn Do'kon",
    description: "Eng sifatli mahsulotlarni onlayn xarid qiling",
    images: ["/twitter-image.jpg"],
  },
  robots: {
    index: true,
    follow: true,
    googleBot: {
      index: true,
      follow: true,
      "max-video-preview": -1,
      "max-image-preview": "large",
      "max-snippet": -1,
    },
  },
};

// Next.js 15.4.4 Viewport API
export const viewport: Viewport = {
  width: "device-width",
  initialScale: 1,
  maximumScale: 1,
  userScalable: false,
  themeColor: [
    { media: "(prefers-color-scheme: light)", color: "#ffffff" },
    { media: "(prefers-color-scheme: dark)", color: "#000000" },
  ],
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
