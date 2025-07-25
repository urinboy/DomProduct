import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import { resources } from './data/translations';

i18n
  .use(initReactI18next) // passes i18n down to react-i18next
  .init({
    resources,
    lng: 'uz', // default language
    fallbackLng: 'uz',

    interpolation: {
      escapeValue: false, // react already safes from xss
    },

    detection: {
      order: ['localStorage', 'navigator'],
      caches: ['localStorage'],
    },
  });

export default i18n;
