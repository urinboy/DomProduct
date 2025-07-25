import React from 'react';
import { useTranslation } from 'react-i18next';

const Meta = ({ title, description, keywords }) => {
  const { t } = useTranslation();

  const defaultTitle = t('default_meta_title');
  const defaultDescription = t('default_meta_description');
  const defaultKeywords = t('default_meta_keywords');

  return (
    <>
      <title>{title || defaultTitle}</title>
      <meta name="description" content={description || defaultDescription} />
      <meta name="keywords" content={keywords || defaultKeywords} />
    </>
  );
};

export default Meta;