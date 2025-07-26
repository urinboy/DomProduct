'use client';

import { useState } from 'react';
import Loading from '../loading';

// Loading komponentini test qilish uchun sahifa
export default function LoadingTestPage() {
  const [showLoading, setShowLoading] = useState(false);

  const resetSplashScreen = () => {
    sessionStorage.removeItem('domproduct_visited');
    alert('Splash screen reset! Sahifani yangilang va splash ekran qayta ko\'rinadi.');
  };

  const simulateLoading = () => {
    setShowLoading(true);
    // 3 soniya keyin loading ni yashirish
    setTimeout(() => {
      setShowLoading(false);
    }, 3000);
  };

  return (
    <div className="test-page">
      <div className="test-content">
        <h1>Loading Test Sahifasi</h1>
        <p>Bu sahifada aylanadigan loader ni test qilishingiz mumkin.</p>
        
        <button 
          onClick={simulateLoading} 
          className="btn btn-primary"
          disabled={showLoading}
        >
          {showLoading ? 'Yuklanmoqda...' : 'Loading ni ko\'rsatish'}
        </button>
        
        <button 
          onClick={resetSplashScreen} 
          className="btn btn-secondary"
          style={{marginLeft: '10px'}}
        >
          🔄 Splash Screen Reset
        </button>
        
        <div className="loading-examples">
          <h2>Loading Variantlari:</h2>
          
          {/* Ring Spinner */}
          <div className="example-box">
            <h3>Ring Spinner (Joriy)</h3>
            <div className="spinner-ring">
              <div></div>
              <div></div>
              <div></div>
              <div></div>
            </div>
          </div>
          
          {/* Simple Spinner */}
          <div className="example-box">
            <h3>Simple Spinner (Eski)</h3>
            <div className="spinner"></div>
          </div>
          
          {/* Dots Loader - Yangi dizayn */}
          <div className="example-box">
            <h3>Dots Loader (Sahifalar orasida)</h3>
            <div className="dots-loader">
              <div></div>
              <div></div>
              <div></div>
            </div>
          </div>
        </div>
      </div>
      
      {/* Full screen loading overlay */}
      {showLoading && <Loading />}
      
      <style jsx>{`
        .test-page {
          padding: 40px 20px;
          max-width: 800px;
          margin: 0 auto;
        }
        
        .test-content {
          text-align: center;
        }
        
        .btn {
          padding: 12px 24px;
          border: none;
          border-radius: 8px;
          font-size: 16px;
          font-weight: 600;
          cursor: pointer;
          transition: all 0.3s ease;
          margin: 20px 0;
        }
        
        .btn-primary {
          background: #007bff;
          color: white;
        }
        
        .btn-primary:hover:not(:disabled) {
          background: #0056b3;
          transform: translateY(-2px);
        }
        
        .btn-secondary {
          background: #6c757d;
          color: white;
        }
        
        .btn-secondary:hover:not(:disabled) {
          background: #5a6268;
          transform: translateY(-2px);
        }
        
        .btn:disabled {
          opacity: 0.6;
          cursor: not-allowed;
        }
        
        .loading-examples {
          margin-top: 40px;
          display: grid;
          grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
          gap: 20px;
        }
        
        .example-box {
          padding: 20px;
          border: 2px solid #ddd;
          border-radius: 12px;
          background: #f8f9fa;
        }
        
        .example-box h3 {
          margin-bottom: 20px;
          color: #333;
        }
        
        /* Dots Loader Animation - bouncing */
        .dots-loader {
          display: inline-block;
          position: relative;
          width: 80px;
          height: 40px;
        }
        
        .dots-loader div {
          position: absolute;
          width: 12px;
          height: 12px;
          border-radius: 50%;
          background: #007bff;
          animation: bouncing-dots 1.5s ease-in-out infinite;
        }
        
        .dots-loader div:nth-child(1) {
          left: 10px;
          animation-delay: 0s;
        }
        
        .dots-loader div:nth-child(2) {
          left: 35px;
          animation-delay: 0.3s;
        }
        
        .dots-loader div:nth-child(3) {
          left: 60px;
          animation-delay: 0.6s;
        }
        
        @keyframes bouncing-dots {
          0%, 60%, 100% {
            transform: translateY(0);
            background: #007bff;
          }
          30% {
            transform: translateY(-20px);
            background: #0056b3;
          }
        }
      `}</style>
    </div>
  );
}
