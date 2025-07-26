'use client';

import { useState, useMemo } from 'react';
import { useTranslation } from 'react-i18next';
import Link from 'next/link';
import ProductImage from '../components/ProductImage';
import { useToast } from '../components/Toast/ToastProvider';
import './categories.css';

interface Category {
  id: number;
  name: string;
  nameUz: string;
  icon: string;
  count: number;
  image: string;
  subcategories: Subcategory[];
}

interface Subcategory {
  id: number;
  name: string;
  nameUz: string;
  count: number;
}

/* Kategoriyalar sahifasi - avvalgi loyihadagi dizaynni takrorlash */
const CategoriesPage = () => {
  const { t, i18n } = useTranslation();
  const [searchQuery, setSearchQuery] = useState('');
  
  const isUzbek = i18n.language === 'uz';

  // Demo kategoriyalar - avvalgi loyihadagi kabi
  const categories: Category[] = [
    {
      id: 1,
      name: "Electronics",
      nameUz: "Elektronika",
      icon: "fas fa-laptop",
      count: 245,
      image: "/api/placeholder/300/200",
      subcategories: [
        { id: 11, name: "Smartphones", nameUz: "Smartfonlar", count: 89 },
        { id: 12, name: "Laptops", nameUz: "Noutbuklar", count: 56 },
        { id: 13, name: "Tablets", nameUz: "Planshetlar", count: 34 },
        { id: 14, name: "Accessories", nameUz: "Aksessuarlar", count: 66 }
      ]
    },
    {
      id: 2,
      name: "Fashion",
      nameUz: "Moda",
      icon: "fas fa-tshirt",
      count: 189,
      image: "/api/placeholder/300/200",
      subcategories: [
        { id: 21, name: "Men's Clothing", nameUz: "Erkaklar kiyimi", count: 78 },
        { id: 22, name: "Women's Clothing", nameUz: "Ayollar kiyimi", count: 89 },
        { id: 23, name: "Shoes", nameUz: "Poyabzal", count: 22 }
      ]
    },
    {
      id: 3,
      name: "Home & Garden",
      nameUz: "Uy va bog'",
      icon: "fas fa-home",
      count: 156,
      image: "/api/placeholder/300/200",
      subcategories: [
        { id: 31, name: "Furniture", nameUz: "Mebel", count: 67 },
        { id: 32, name: "Decor", nameUz: "Dekor", count: 45 },
        { id: 33, name: "Garden", nameUz: "Bog'", count: 44 }
      ]
    },
    {
      id: 4,
      name: "Sports & Outdoors",
      nameUz: "Sport va dam olish",
      icon: "fas fa-football-ball",
      count: 134,
      image: "/api/placeholder/300/200",
      subcategories: [
        { id: 41, name: "Fitness", nameUz: "Fitnes", count: 56 },
        { id: 42, name: "Outdoor Gear", nameUz: "Tashqi jihozlar", count: 38 },
        { id: 43, name: "Team Sports", nameUz: "Jamoaviy sport", count: 40 }
      ]
    },
    {
      id: 5,
      name: "Beauty & Health",
      nameUz: "Go'zallik va salomatlik",
      icon: "fas fa-spa",
      count: 98,
      image: "/api/placeholder/300/200",
      subcategories: [
        { id: 51, name: "Skincare", nameUz: "Teriga parvarish", count: 45 },
        { id: 52, name: "Makeup", nameUz: "Makiyaj", count: 32 },
        { id: 53, name: "Health", nameUz: "Salomatlik", count: 21 }
      ]
    },
    {
      id: 6,
      name: "Books & Media",
      nameUz: "Kitoblar va media",
      icon: "fas fa-book",
      count: 76,
      image: "/api/placeholder/300/200",
      subcategories: [
        { id: 61, name: "Books", nameUz: "Kitoblar", count: 54 },
        { id: 62, name: "Music", nameUz: "Musiqa", count: 12 },
        { id: 63, name: "Movies", nameUz: "Filmlar", count: 10 }
      ]
    }
  ];

  // Kategoriyalarni qidirish va filterlash
  const filteredCategories = useMemo(() => {
    return categories.filter(category => {
      const searchText = isUzbek ? category.nameUz : category.name;
      return searchText.toLowerCase().includes(searchQuery.toLowerCase());
    });
  }, [categories, searchQuery, isUzbek]);

  // Umumiy statistikalar
  const totalProducts = categories.reduce((sum, cat) => sum + cat.count, 0);
  const totalCategories = categories.length;

  return (
    <div id="categoriesPage">
      {/* Zamonaviy header */}
      <div className="categories-header">
        <h1>
          <i className="fas fa-th-large" style={{ marginRight: '1rem' }}></i>
          {isUzbek ? 'Kategoriyalar' : 'Категории'}
        </h1>
        <p>
          {isUzbek ? 
            'Barcha mahsulot kategoriyalarini kashf eting va kerakli mahsulotni toping' : 
            'Исследуйте все категории товаров и найдите нужные продукты'
          }
        </p>
      </div>

      {/* Qidiruv bo'limi */}
      <div className="categories-search">
        <div className="search-container">
          <i className="fas fa-search search-icon"></i>
          <input
            type="text"
            className="search-input"
            placeholder={isUzbek ? 'Kategoriyalar ichida qidirish...' : 'Поиск по категориям...'}
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
        </div>
      </div>

      {/* Statistikalar */}
      <div className="categories-stats">
        <div className="stat-item">
          <span className="stat-number">{totalCategories}</span>
          <span className="stat-label">
            {isUzbek ? 'Kategoriya' : 'Категорий'}
          </span>
        </div>
        <div className="stat-item">
          <span className="stat-number">{totalProducts.toLocaleString()}</span>
          <span className="stat-label">
            {isUzbek ? 'Mahsulot' : 'Товаров'}
          </span>
        </div>
        <div className="stat-item">
          <span className="stat-number">{filteredCategories.length}</span>
          <span className="stat-label">
            {isUzbek ? 'Topildi' : 'Найдено'}
          </span>
        </div>
      </div>

      {/* Asosiy kontent */}
      <div className="categories-content">
        {filteredCategories.length === 0 ? (
          <div className="categories-empty">
            <i className="fas fa-search"></i>
            <h3>
              {isUzbek ? "Kategoriya topilmadi" : "Категория не найдена"}
            </h3>
            <p>
              {isUzbek ? 
                "Qidiruv so'rovingizni o'zgartirib, qayta urinib ko'ring yoki barcha kategoriyalarni ko'rib chiqing" : 
                "Попробуйте изменить поисковый запрос или просмотрите все категории"
              }
            </p>
          </div>
        ) : (
          <div className="categories-grid">
            {filteredCategories.map((category) => (
              <div key={category.id} className="category-card">
                {/* Kategoriya rasmi va icon */}
                <div className="category-image">
                  <div className="category-overlay">
                    <i className={`${category.icon} category-icon`}></i>
                  </div>
                  <div className="category-count">
                    {category.count} {isUzbek ? 'ta' : 'шт'}
                  </div>
                </div>

                {/* Kategoriya ma'lumotlari */}
                <div className="category-info">
                  <h3 className="category-title">
                    {isUzbek ? category.nameUz : category.name}
                  </h3>
                  <p className="category-description">
                    {isUzbek 
                      ? `${category.count} ta mahsulot mavjud` 
                      : `Доступно ${category.count} товаров`}
                  </p>

                  {/* Subkategoriyalar */}
                  {category.subcategories && category.subcategories.length > 0 && (
                    <div className="subcategories">
                      <div className="subcategories-title">
                        <i className="fas fa-list"></i>
                        {isUzbek ? 'Subkategoriyalar' : 'Подкатегории'}
                      </div>
                      <div className="subcategories-grid">
                        {category.subcategories.map((sub) => (
                          <Link
                            key={sub.id}
                            href={`/products?category=${category.id}&subcategory=${sub.id}`}
                            className="subcategory-item"
                          >
                            <div className="subcategory-name">
                              {isUzbek ? sub.nameUz : sub.name}
                            </div>
                            <div className="subcategory-count">
                              {sub.count}
                            </div>
                          </Link>
                        ))}
                      </div>
                    </div>
                  )}
                </div>

                {/* Kategoriya actionlari */}
                <div className="category-actions">
                  <Link
                    href={`/products?category=${category.id}`}
                    className="btn-view-category"
                  >
                    <i className="fas fa-eye"></i>
                    {isUzbek ? 'Ko\'rish' : 'Просмотреть'}
                  </Link>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default CategoriesPage;
