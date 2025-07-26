'use client';

import { useState, useEffect } from 'react';

const Toast = ({ message, type = 'info', duration = 3000, onClose }) => {
  const [isVisible, setIsVisible] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      setIsVisible(false);
      onClose?.();
    }, duration);

    return () => clearTimeout(timer);
  }, [duration, onClose]);

  if (!isVisible) return null;

  const typeClasses = {
    success: 'bg-green-100 border-green-500 text-green-700',
    error: 'bg-red-100 border-red-500 text-red-700',
    warning: 'bg-yellow-100 border-yellow-500 text-yellow-700',
    info: 'bg-blue-100 border-blue-500 text-blue-700'
  };

  const icons = {
    success: '✓',
    error: '✕',
    warning: '⚠',
    info: 'ℹ'
  };

  return (
    <div className={`fixed top-4 right-4 z-50 max-w-sm p-4 border-l-4 rounded shadow-lg ${typeClasses[type]} animate-slide-in-right`}>
      <div className="flex items-center justify-center">
        <span className="mr-2 text-lg">{icons[type]}</span>
        <p className="text-sm font-medium">{message}</p>
        <button
          onClick={() => {
            setIsVisible(false);
            onClose?.();
          }}
          className="ml-auto text-lg hover:opacity-70"
        >
          ×
        </button>
      </div>
    </div>
  );
};

export default Toast;
