'use client';

import { useTranslation } from 'react-i18next';

const Loading = ({ size = 'medium' }) => {
  const { t } = useTranslation();
  
  const sizeClasses = {
    small: 'w-6 h-6',
    medium: 'w-8 h-8', 
    large: 'w-12 h-12'
  };

  return (
    <div className="flex flex-col items-center justify-center p-8">
      <div className={`animate-spin rounded-full border-4 border-gray-300 border-t-blue-600 ${sizeClasses[size]}`}></div>
      <p className="mt-2 text-gray-600">{t('loading')}</p>
    </div>
  );
};

export default Loading;
