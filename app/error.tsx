'use client'; // Next.js 15.4.4 Error boundaries client component bo'lishi kerak

import { useEffect } from 'react';

interface ErrorProps {
  error: Error & { digest?: string };
  reset: () => void;
}

// Next.js 15.4.4 Enhanced Error Handling
export default function Error({ error, reset }: ErrorProps) {
  useEffect(() => {
    // Error logging service ga yuborish
    console.error('Application Error:', error);
  }, [error]);

  return (
    <div className="error-container">
      <div className="error-content">
        <h1>🔧 Nimadir noto'g'ri ketdi!</h1>
        <p>Kechirasiz, kutilmagan xato yuz berdi.</p>
        
        {process.env.NODE_ENV === 'development' && (
          <details className="error-details">
            <summary>Texnik ma'lumotlar</summary>
            <pre>{error.message}</pre>
            {error.digest && <p>Error ID: {error.digest}</p>}
          </details>
        )}
        
        <div className="error-actions">
          <button 
            onClick={reset}
            className="btn btn-primary"
          >
            🔄 Qaytadan urinish
          </button>
          
          <button 
            onClick={() => window.location.href = '/'}
            className="btn btn-secondary"
          >
            🏠 Bosh sahifaga o'tish
          </button>
        </div>
      </div>
    </div>
  );
}
