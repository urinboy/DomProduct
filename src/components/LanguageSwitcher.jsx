
import React, { useState, useEffect, useRef } from 'react';
import { useTranslation } from 'react-i18next';
import { languages } from '../data/languages'; // Import languages data

function LanguageSwitcher() {
  const { i18n, t } = useTranslation();
  const [isOpen, setIsOpen] = useState(false);
  const dropdownRef = useRef(null);

  const changeLanguage = (lng) => {
    i18n.changeLanguage(lng);
    setIsOpen(false);
  };

  useEffect(() => {
    function handleClickOutside(event) {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setIsOpen(false);
      }
    }
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [dropdownRef]);

  const currentLanguage = languages.find(lang => lang.code === i18n.resolvedLanguage) || languages[0];

  return (
    <div className="language-switcher" ref={dropdownRef}>
      <button className="icon-btn language-btn" onClick={() => setIsOpen(!isOpen)}>
        <span className="language-flag">{currentLanguage.flag}</span>
      </button>
      {isOpen && (
        <div className="language-dropdown">
          <p className="dropdown-header">{t('language')}</p>
          {languages.map((lang) => (
            <button
              key={lang.code}
              className={`dropdown-item ${i18n.resolvedLanguage === lang.code ? 'active' : ''}`}
              type="button" // Changed to button type
              onClick={() => changeLanguage(lang.code)}
            >
              <span className="language-flag">{lang.flag}</span>
              <span className="language-name">{lang.name}</span>
            </button>
          ))}
        </div>
      )}
    </div>
  );
}

export default LanguageSwitcher;
