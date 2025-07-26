'use client';

import { useState } from 'react';
import { useTranslation } from 'react-i18next';

const LanguageSwitcher = () => {
  const { i18n } = useTranslation();
  const [currentLang, setCurrentLang] = useState(i18n.language);

  const toggleLanguage = () => {
    const newLang = currentLang === 'uz' ? 'ru' : 'uz';
    i18n.changeLanguage(newLang);
    setCurrentLang(newLang);
  };

  return (
    <button
      onClick={toggleLanguage}
      className="flex items-center space-x-2 px-3 py-2 rounded-lg border border-gray-300 hover:bg-gray-50 transition-colors"
    >
      <span className="text-sm font-medium">
        {currentLang === 'uz' ? '🇺🇿 UZ' : '🇷🇺 RU'}
      </span>
    </button>
  );
};

export default LanguageSwitcher;
