'use client';

import { useState, useEffect } from 'react';
import Header from './Header';
import BottomNavigation from './BottomNavigation';
import SearchOverlay from './SearchOverlay';
import SplashScreen from './SplashScreen';

interface AppLayoutProps {
  children: React.ReactNode;
}

/* Avvalgi loyihadagi App componentining layout qismini takrorlash */
const AppLayout = ({ children }: AppLayoutProps) => {
  const [isSearchOpen, setIsSearchOpen] = useState(false);
  const [isAppLoading, setIsAppLoading] = useState(true);

  // Dastur yuklash holatini boshqarish
  useEffect(() => {
    const timer = setTimeout(() => setIsAppLoading(false), 2000);
    return () => clearTimeout(timer);
  }, []);

  // Qidiruv funksiyasi
  const handleSearch = (query: string) => {
    setIsSearchOpen(false);
    window.location.href = `/search?q=${encodeURIComponent(query)}`;
  };

  // Agar dastur yuklanayotgan bo'lsa, splash screen ko'rsatish
  if (isAppLoading) {
    return <SplashScreen />;
  }

  return (
    <div className="app-container" id="app">
      {/* Qidiruv overlay */}
      {isSearchOpen && (
        <SearchOverlay 
          onClose={() => setIsSearchOpen(false)} 
          onSearch={handleSearch} 
        />
      )}

      {/* Header qismi */}
      <Header onSearchOpen={() => setIsSearchOpen(true)} />

      {/* Asosiy kontent */}
      <main className="main-content">
        {children}
      </main>

      {/* Pastki navigatsiya */}
      <BottomNavigation />
    </div>
  );
};

export default AppLayout;
