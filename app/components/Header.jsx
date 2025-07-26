'use client';

import { useState, useEffect, useRef } from 'react';
import { useTranslation } from 'react-i18next';
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { useCart } from '../contexts/CartContext';

/* Avvalgi loyihadagi header komponentini to'liq takrorlash */
const Header = ({ onSearchOpen }) => {
  const { t, i18n } = useTranslation();
  const { getCartItemsCount } = useCart();
  const pathname = usePathname();
  const [isLanguageDropdownOpen, setIsLanguageDropdownOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const dropdownRef = useRef(null);

  // Dropdown tashqarisiga bosilganda yopish
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setIsLanguageDropdownOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  // Tilni almashtirish funksiyasi
  const handleLanguageChange = (lang) => {
    i18n.changeLanguage(lang);
    setIsLanguageDropdownOpen(false);
  };

  // Qidiruv funksiyasi
  const handleSearch = (e) => {
    if (e.key === 'Enter' && searchQuery.trim()) {
      window.location.href = `/search?q=${encodeURIComponent(searchQuery.trim())}`;
    }
  };

  // Mahsulot detail sahifasida header ko'rsatmaslik
  const isDetailPage = pathname?.startsWith('/products/') && pathname.split('/').length === 3;
  
  if (isDetailPage) {
    return null;
  }

  return (
    <header className="header">
      <div className="header-content">
        {/* Logo qismi */}
        <Link href="/" className="logo">
          <img src="/logos/white.png" alt="DomProduct Logo" className="logo-image" />
        </Link>

        {/* Desktop qidiruv konteyneri */}
        <div className="search-container desktop-search">
          <i className="fas fa-search search-icon"></i>
          <input 
            type="text" 
            className="search-input" 
            placeholder={t('search_placeholder')}
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            onKeyDown={handleSearch}
          />
        </div>

        {/* Header harakatlari */}
        <div className="header-actions">
          {/* Mobil qidiruv tugmasi */}
          <button 
            className="icon-btn mobile-search-btn" 
            onClick={() => onSearchOpen && onSearchOpen()}
          >
            <i className="fas fa-search"></i>
          </button>

          {/* Til almashtiruvchi */}
          <div className="language-switcher" ref={dropdownRef}>
            <button 
              className="icon-btn language-btn"
              onClick={() => setIsLanguageDropdownOpen(!isLanguageDropdownOpen)}
            >
              <span className="language-flag">
                {i18n.language === 'uz' ? '🇺🇿' : '🇷🇺'}
              </span>
            </button>
            
            {isLanguageDropdownOpen && (
              <div className="language-dropdown">
                <div className="dropdown-header">
                  {t('choose_language') || 'Tilni tanlang'}
                </div>
                <button 
                  className={`dropdown-item ${i18n.language === 'uz' ? 'active' : ''}`}
                  onClick={() => handleLanguageChange('uz')}
                >
                  🇺🇿 <span className="language-name">O'zbek</span>
                </button>
                <button 
                  className={`dropdown-item ${i18n.language === 'ru' ? 'active' : ''}`}
                  onClick={() => handleLanguageChange('ru')}
                >
                  🇷🇺 <span className="language-name">Русский</span>
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;
