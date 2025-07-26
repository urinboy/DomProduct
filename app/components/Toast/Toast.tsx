'use client';

import { useEffect } from 'react';
import './toast.css';

export interface ToastProps {
  id: string;
  type: 'success' | 'error' | 'info' | 'warning';
  title: string;
  message?: string;
  duration?: number;
  onClose: (id: string) => void;
}

export const Toast: React.FC<ToastProps> = ({
  id,
  type,
  title,
  message,
  duration = 3000,
  onClose
}) => {
  useEffect(() => {
    const timer = setTimeout(() => {
      onClose(id);
    }, duration);

    return () => clearTimeout(timer);
  }, [id, duration, onClose]);

  const getIcon = () => {
    switch (type) {
      case 'success':
        return 'fas fa-check-circle';
      case 'error':
        return 'fas fa-times-circle';
      case 'warning':
        return 'fas fa-exclamation-triangle';
      case 'info':
      default:
        return 'fas fa-info-circle';
    }
  };

  const getColor = () => {
    switch (type) {
      case 'success':
        return '#087c36';
      case 'error':
        return '#dc3545';
      case 'warning':
        return '#fd7e14';
      case 'info':
      default:
        return '#0dcaf0';
    }
  };

  return (
    <div className={`toast toast-${type}`} style={{
      background: 'white',
      borderRadius: '12px',
      padding: '16px',
      boxShadow: '0 8px 32px rgba(0,0,0,0.12)',
      border: `2px solid ${getColor()}`,
      minWidth: '320px',
      maxWidth: '400px',
      position: 'relative',
      overflow: 'hidden',
      animation: 'slideInRight 0.3s ease-out forwards'
    }}>
      {/* Progress bar */}
      <div 
        className="toast-progress"
        style={{
          position: 'absolute',
          top: 0,
          left: 0,
          height: '3px',
          background: getColor(),
          animation: `progress ${duration}ms linear forwards`
        }}
      />

      <div style={{ display: 'flex', alignItems: 'flex-start', gap: '12px' }}>
        {/* Icon */}
        <div style={{
          width: '40px',
          height: '40px',
          borderRadius: '8px',
          background: `linear-gradient(135deg, ${getColor()}, ${getColor()}dd)`,
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          color: 'white',
          fontSize: '18px',
          flexShrink: 0
        }}>
          <i className={getIcon()}></i>
        </div>

        {/* Content */}
        <div style={{ flex: 1, minWidth: 0 }}>
          <h4 style={{
            margin: '0 0 4px 0',
            fontSize: '14px',
            fontWeight: '600',
            color: '#212529',
            lineHeight: '1.2'
          }}>
            {title}
          </h4>
          {message && (
            <p style={{
              margin: 0,
              fontSize: '13px',
              color: '#6c757d',
              lineHeight: '1.3'
            }}>
              {message}
            </p>
          )}
        </div>

        {/* Close button */}
        <button
          onClick={() => onClose(id)}
          style={{
            background: 'none',
            border: 'none',
            color: '#6c757d',
            cursor: 'pointer',
            padding: '4px',
            borderRadius: '4px',
            fontSize: '14px',
            transition: 'all 0.2s ease',
            flexShrink: 0
          }}
          onMouseOver={(e) => {
            e.currentTarget.style.background = '#f8f9fa';
            e.currentTarget.style.color = '#495057';
          }}
          onMouseOut={(e) => {
            e.currentTarget.style.background = 'none';
            e.currentTarget.style.color = '#6c757d';
          }}
        >
          <i className="fas fa-times"></i>
        </button>
      </div>
    </div>
  );
};
