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

      {/* Yuklanish indikatori - ring spinner splash uchun */}
      <div className="splash-loader" style={{
        marginTop: '3rem',
        position: 'relative',
        width: '60px',
        height: '60px'
      }}>
        <div style={{
          position: 'absolute',
          width: '60px',
          height: '60px',
          border: '6px solid rgba(8, 124, 54, 0.1)',
          borderTop: '6px solid var(--primary-color)',
          borderRadius: '50%',
          animation: 'spin 1.2s linear infinite'
        }}></div>
        <div style={{
          position: 'absolute',
          width: '40px',
          height: '40px',
          top: '10px',
          left: '10px',
          border: '4px solid rgba(8, 124, 54, 0.1)',
          borderTop: '4px solid #00b894',
          borderRadius: '50%',
          animation: 'spin 0.8s linear infinite reverse'
        }}></div>
      </div>

      {/* Yuklanish xabari */}
      <p style={{
        marginTop: '2rem',
        color: 'var(--gray-600)',
        fontSize: '1.2rem',
        fontWeight: '500',
        letterSpacing: '0.5px'
      }}>
        {t('loading') || 'Yuklanmoqda...'}
      </p>
      
      {/* Brand tagline */}
      <p style={{
        marginTop: '0.5rem',
        color: 'var(--gray-500)',
        fontSize: '0.9rem',
        opacity: 0.8
      }}>
        Sifatli mahsulotlar onlayn do'koni
      </p>
    </div>
  );
};

export default SplashScreen;
