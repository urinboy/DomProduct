
import React from 'react';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { categories } from '../data/categories';
import Meta from '../components/Meta';

function CategoriesPage() {
  const { t } = useTranslation();

  return (
    <div className="page-container">
      <Meta title={t('all_categories')} />
      <h1 className="page-title">{t('all_categories')}</h1>
      <div className="category-grid-full">
        {categories.map(category => (
          <Link to={`/products?category=${category.slug}`} key={category.id} className="category-card-full">
            <div className="category-card-icon">
              <i className={category.icon}></i>
            </div>
            <div className="category-card-name">
              {t(`category_${category.slug}`)} 
            </div>
          </Link>
        ))}
      </div>
    </div>
  );
}

export default CategoriesPage;
