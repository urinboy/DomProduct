'use client';

import { useState } from 'react';
import { useTranslation } from 'react-i18next';
import Link from 'next/link';

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

  // Kategoriyalarni qidirish
  const filteredCategories = categories.filter(category => {
    const searchText = isUzbek ? category.nameUz : category.name;
    return searchText.toLowerCase().includes(searchQuery.toLowerCase());
  });

  return (
    <div id="categoriesPage">
      {/* Sahifa header */}
      <div style={{ marginBottom: '2rem' }}>
        <h2 style={{ marginBottom: '1rem' }}>
          {isUzbek ? 'Kategoriyalar' : 'Категории'}
        </h2>
        <p style={{ color: 'var(--gray-600)', margin: 0 }}>
          {isUzbek ? 
            'Barcha mahsulot kategoriyalarini ko\'ring va kerakli mahsulotni toping' : 
            'Просматривайте все категории товаров и находите нужные продукты'
          }
        </p>
      </div>

      {/* Qidiruv */}
      <div style={{ marginBottom: '2rem' }}>
        <div style={{ position: 'relative', maxWidth: '400px' }}>
          <input
            type="text"
            placeholder={isUzbek ? 'Kategoriyalar ichida qidirish...' : 'Поиск по категориям...'}
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            style={{
              width: '100%',
              padding: '0.75rem 1rem 0.75rem 3rem',
              border: '1px solid var(--gray-300)',
              borderRadius: 'var(--radius-md)',
              fontSize: '1rem',
              backgroundColor: 'white'
            }}
          />
          <i 
            className="fas fa-search" 
            style={{
              position: 'absolute',
              left: '1rem',
              top: '50%',
              transform: 'translateY(-50%)',
              color: 'var(--gray-500)'
            }}
          ></i>
        </div>
      </div>

      {/* Kategoriyalar grid */}
      {filteredCategories.length === 0 ? (
        <div className="cart-empty" style={{ textAlign: 'center', padding: '3rem' }}>
          <i className="fas fa-search cart-empty-icon"></i>
          <div className="cart-empty-title">
            {isUzbek ? "Kategoriya topilmadi" : "Категория не найдена"}
          </div>
          <p className="cart-empty-message">
            {isUzbek ? 
              "Qidiruv so'zini o'zgartirib, qayta urinib ko'ring" : 
              "Попробуйте изменить поисковый запрос"
            }
          </p>
        </div>
      ) : (
        <div className="category-grid">
          {filteredCategories.map((category) => (
            <div key={category.id} className="category-card">
              {/* Kategoriya rasmi */}
              <div className="category-image">
                <img 
                  src={category.image} 
                  alt={isUzbek ? category.nameUz : category.name}
                  loading="lazy"
                />
                <div className="category-overlay">
                  <i className={category.icon}></i>
                </div>
              </div>

              {/* Kategoriya ma'lumotlari */}
              <div className="category-info">
                <h3 className="category-title">
                  {isUzbek ? category.nameUz : category.name}
                </h3>
                <p className="category-count">
                  {category.count} {isUzbek ? 'mahsulot' : 'товаров'}
                </p>

                {/* Subkategoriyalar */}
                <div className="subcategories">
                  {category.subcategories.slice(0, 3).map((sub) => (
                    <Link 
                      key={sub.id}
                      href={`/products?category=${category.id}&subcategory=${sub.id}`}
                      className="subcategory-link"
                    >
                      {isUzbek ? sub.nameUz : sub.name} ({sub.count})
                    </Link>
                  ))}
                  {category.subcategories.length > 3 && (
                    <span className="subcategory-more">
                      +{category.subcategories.length - 3} {isUzbek ? 'ko\'proq' : 'еще'}
                    </span>
                  )}
                </div>

                {/* Kategoriya tugmasi */}
                <Link 
                  href={`/products?category=${category.id}`}
                  className="btn btn-primary btn-sm category-btn"
                >
                  {isUzbek ? 'Barcha mahsulotlar' : 'Все товары'}
                  <i className="fas fa-arrow-right" style={{ marginLeft: '0.5rem' }}></i>
                </Link>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Qo'shimcha ma'lumot */}
      <div style={{ 
        marginTop: '3rem',
        padding: '2rem',
        backgroundColor: 'var(--gray-100)',
        borderRadius: 'var(--radius-lg)',
        textAlign: 'center'
      }}>
        <h3 style={{ marginBottom: '1rem', color: 'var(--gray-800)' }}>
          {isUzbek ? "Kerakli mahsulot topa olmadingizmi?" : "Не нашли нужный товар?"}
        </h3>
        <p style={{ marginBottom: '1.5rem', color: 'var(--gray-600)' }}>
          {isUzbek ? 
            "Qidiruv orqali barcha mahsulotlar ichidan kerakli mahsulotni toping" :
            "Используйте поиск, чтобы найти нужный товар среди всех продуктов"
          }
        </p>
        <Link href="/search" className="btn btn-primary">
          {isUzbek ? "Umumiy qidiruv" : "Общий поиск"}
        </Link>
      </div>
    </div>
  );
};

export default CategoriesPage;
