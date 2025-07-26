'use client';

import { useState, useEffect, useRef } from 'react';
import { useTranslation } from 'react-i18next';

/* Avvalgi loyihadagi qidiruv overlay komponentini takrorlash */
const SearchOverlay = ({ onClose, onSearch }) => {
  const { t } = useTranslation();
  const [searchQuery, setSearchQuery] = useState('');
  const inputRef = useRef(null);

  // Overlay ochilganda input ga focus qilish
  useEffect(() => {
    if (inputRef.current) {
      inputRef.current.focus();
    }
  }, []);

  // Escape tugmasi bosilganda yopish
  useEffect(() => {
    const handleKeyDown = (e) => {
      if (e.key === 'Escape') {
        onClose();
      }
    };

    document.addEventListener('keydown', handleKeyDown);
    return () => {
      document.removeEventListener('keydown', handleKeyDown);
    };
  }, [onClose]);

  // Qidiruv funksiyasi
  const handleSearch = () => {
    if (searchQuery.trim()) {
      onSearch(searchQuery.trim());
    }
  };

  // Enter tugmasi bosilganda qidiruv
  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

  return (
    <div style={{
      position: 'fixed',
      top: 0,
      left: 0,
      width: '100%',
      height: '100%',
      background: 'rgba(0, 0, 0, 0.8)',
      zIndex: 9999,
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      padding: '1rem',
      backdropFilter: 'blur(5px)'
    }}>
      <div style={{
        background: 'white',
        borderRadius: 'var(--radius-lg)',
        padding: '2rem',
        width: '100%',
        maxWidth: '500px',
        boxShadow: 'var(--shadow-lg)'
      }}>
        {/* Sarlavha */}
        <div style={{
          textAlign: 'center',
          marginBottom: '1.5rem'
        }}>
          <h2 style={{
            fontSize: '1.5rem',
            fontWeight: 'bold',
            color: 'var(--gray-800)',
            marginBottom: '0.5rem'
          }}>
            {t('search') || 'Qidirish'}
          </h2>
          <p style={{
            color: 'var(--gray-600)',
            fontSize: '0.95rem'
          }}>
            {t('search_description') || 'Kerakli mahsulotni toping'}
          </p>
        </div>

        {/* Qidiruv input */}
        <div style={{
          position: 'relative',
          marginBottom: '1.5rem'
        }}>
          <i className="fas fa-search" style={{
            position: 'absolute',
            left: '1rem',
            top: '50%',
            transform: 'translateY(-50%)',
            color: 'var(--gray-500)',
            fontSize: '1.1rem'
          }}></i>
          <input 
            ref={inputRef}
            type="text"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            onKeyPress={handleKeyPress}
            placeholder={t('search_placeholder') || 'Mahsulot qidirish...'}
            style={{
              width: '100%',
              padding: '1rem 1rem 1rem 3rem',
              border: '2px solid var(--gray-300)',
              borderRadius: 'var(--radius-full)',
              fontSize: '1rem',
              outline: 'none',
              transition: 'border-color 0.2s'
            }}
            onFocus={(e) => {
              e.target.style.borderColor = 'var(--primary-color)';
            }}
            onBlur={(e) => {
              e.target.style.borderColor = 'var(--gray-300)';
            }}
          />
        </div>

        {/* Tugmalar */}
        <div style={{
          display: 'flex',
          gap: '1rem',
          justifyContent: 'flex-end'
        }}>
          <button 
            onClick={onClose}
            style={{
              padding: '0.75rem 1.5rem',
              border: '1px solid var(--gray-300)',
              borderRadius: 'var(--radius-md)',
              background: 'white',
              color: 'var(--gray-700)',
              cursor: 'pointer',
              fontSize: '1rem',
              transition: 'all 0.2s'
            }}
            onMouseEnter={(e) => {
              e.target.style.background = 'var(--gray-50)';
            }}
            onMouseLeave={(e) => {
              e.target.style.background = 'white';
            }}
          >
            {t('cancel') || 'Bekor qilish'}
          </button>
          
          <button 
            onClick={handleSearch}
            disabled={!searchQuery.trim()}
            style={{
              padding: '0.75rem 1.5rem',
              border: 'none',
              borderRadius: 'var(--radius-md)',
              background: searchQuery.trim() ? 'var(--primary-color)' : 'var(--gray-300)',
              color: 'white',
              cursor: searchQuery.trim() ? 'pointer' : 'not-allowed',
              fontSize: '1rem',
              transition: 'all 0.2s'
            }}
            onMouseEnter={(e) => {
              if (searchQuery.trim()) {
                e.target.style.background = 'var(--secondary-color)';
              }
            }}
            onMouseLeave={(e) => {
              if (searchQuery.trim()) {
                e.target.style.background = 'var(--primary-color)';
              }
            }}
          >
            <i className="fas fa-search" style={{ marginRight: '0.5rem' }}></i>
            {t('search') || 'Qidirish'}
          </button>
        </div>
      </div>
    </div>
  );
};

export default SearchOverlay;
