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
  const [isFirstLoad, setIsFirstLoad] = useState(true);

  // Faqat birinchi safar splash ekran ko'rsatish
  useEffect(() => {
    const hasVisited = sessionStorage.getItem('domproduct_visited');
    
    if (!hasVisited) {
      // Birinchi safar tashrif
      const timer = setTimeout(() => {
        setIsFirstLoad(false);
        sessionStorage.setItem('domproduct_visited', 'true');
      }, 2500); // 2.5 soniya splash
      
      return () => clearTimeout(timer);
    } else {
      // Oldin tashrif bo'lgan
      setIsFirstLoad(false);
    }
  }, []);

  // Qidiruv funksiyasi
  const handleSearch = (query: string) => {
    setIsSearchOpen(false);
    window.location.href = `/search?q=${encodeURIComponent(query)}`;
  };

  // Faqat birinchi safar splash screen ko'rsatish
  if (isFirstLoad) {
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
