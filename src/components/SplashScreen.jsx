'use client';

import { useTranslation } from 'react-i18next';

/* Avvalgi loyihadagi splash screen komponentini takrorlash */
const SplashScreen = () => {
  const { t } = useTranslation();

  return (
    <div style={{
      position: 'fixed',
      top: 0,
      left: 0,
      width: '100%',
      height: '100%',
      background: 'var(--splash-bg-color)',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      zIndex: 10000
    }}>
      {/* Logo qismi */}
      <div style={{
        marginBottom: '2rem',
        textAlign: 'center'
      }}>
        <img 
          src="/loadings/light-splash.gif" 
          alt="DomProduct Logo" 
          style={{
            height: '240px',
            marginBottom: '1rem'
          }}
        />
        {/* <h1 style={{
          fontFamily: 'var(--font-logo)',
          fontSize: '2.5rem',
          color: 'var(--primary-color)',
          margin: 0
        }}>
          DomProduct
        </h1> */}
      </div>

      {/* Yuklanish indikatori */}
      <div className="loading-spinner" style={{
        marginTop: '7rem',
        border: '4px solid rgba(8, 124, 54, 0.1)',
        borderTop: '4px solid var(--primary-color)',
        borderRadius: '50%',
        width: '30px',
        height: '30px',
        animation: 'spin 1s linear infinite'
      }}></div>

      {/* Yuklanish xabari */}
      <p style={{
        marginTop: '1rem',
        color: 'var(--gray-600)',
        fontSize: '1.1rem'
      }}>
        {t('loading') || 'Yuklanmoqda...'}
      </p>
    </div>
  );
};

export default SplashScreen;
