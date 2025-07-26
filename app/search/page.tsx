'use client';

import { useState, useEffect, useMemo } from 'react';
import { useTranslation } from 'react-i18next';
import { useSearchParams } from 'next/navigation';
import Link from 'next/link';
import { useCart } from '../contexts/CartContext';
import { useWishlist } from '../contexts/WishlistContext';
import { useToast } from '../components/Toast/ToastProvider';
import ProductImage from '../components/ProductImage';

interface Product {
  id: number;
  name: string;
  nameUz: string;
  price: number;
  originalPrice?: number;
  discount: number;
  images: string[];
  rating: number;
  reviewsCount: number;
  inStock: boolean;
  category: string;
  categoryUz: string;
  description: string;
  descriptionUz: string;
}

/* Qidiruv sahifasi - avvalgi loyihadagi dizaynni takrorlash */
const SearchPage = () => {
  const { t, i18n } = useTranslation();
  const searchParams = useSearchParams();
  const { addToCart } = useCart();
  const { items: wishlistItems, addToWishlist, removeFromWishlist } = useWishlist();
  const { showSuccess, showInfo } = useToast();
  
  const [query, setQuery] = useState(searchParams.get('q') || '');
  const [sortBy, setSortBy] = useState('relevance');
  const [priceRange, setPriceRange] = useState<[number, number]>([0, 1000000]);
  const [selectedCategory, setSelectedCategory] = useState('all');
  const [isLoading, setIsLoading] = useState(false);

  const isUzbek = i18n.language === 'uz';

  // Demo mahsulotlar - avvalgi loyihadagi kabi
  const allProducts: Product[] = [
    {
      id: 1,
      name: "iPhone 15 Pro",
      nameUz: "iPhone 15 Pro",
      price: 15000000,
      originalPrice: 16000000,
      discount: 6,
      images: ["/api/placeholder/300/300"],
      rating: 4.8,
      reviewsCount: 234,
      inStock: true,
      category: "Electronics",
      categoryUz: "Elektronika",
      description: "Latest iPhone with advanced camera system",
      descriptionUz: "Ilg'or kamera tizimi bilan eng yangi iPhone"
    },
    {
      id: 2,
      name: "Samsung Galaxy S24",
      nameUz: "Samsung Galaxy S24",
      price: 12000000,
      originalPrice: 13000000,
      discount: 8,
      images: ["/api/placeholder/300/300"],
      rating: 4.6,
      reviewsCount: 189,
      inStock: true,
      category: "Electronics",
      categoryUz: "Elektronika",
      description: "Powerful Android smartphone",
      descriptionUz: "Kuchli Android smartfon"
    },
    {
      id: 3,
      name: "MacBook Pro 16",
      nameUz: "MacBook Pro 16",
      price: 25000000,
      discount: 0,
      images: ["/api/placeholder/300/300"],
      rating: 4.9,
      reviewsCount: 156,
      inStock: true,
      category: "Computers",
      categoryUz: "Kompyuterlar",
      description: "Professional laptop for creators",
      descriptionUz: "Kreatorlar uchun professional noutbuk"
    },
    {
      id: 4,
      name: "AirPods Pro",
      nameUz: "AirPods Pro",
      price: 2500000,
      originalPrice: 3000000,
      discount: 17,
      images: ["/api/placeholder/300/300"],
      rating: 4.7,
      reviewsCount: 445,
      inStock: true,
      category: "Audio",
      categoryUz: "Audio",
      description: "Wireless earbuds with noise cancellation",
      descriptionUz: "Shovqinni bekor qiluvchi simsiz quloqchinlar"
    },
    {
      id: 5,
      name: "Dell XPS 13",
      nameUz: "Dell XPS 13",
      price: 18000000,
      discount: 0,
      images: ["/api/placeholder/300/300"],
      rating: 4.5,
      reviewsCount: 123,
      inStock: false,
      category: "Computers",
      categoryUz: "Kompyuterlar",
      description: "Ultrabook with premium design",
      descriptionUz: "Premium dizaynli ultrabook"
    }
  ];

  // Qidiruv natijalarini filtrlash
  const filteredProducts = useMemo(() => {
    let results = allProducts;

    // Matn bo'yicha qidirish
    if (query.trim()) {
      const searchTerms = query.toLowerCase().split(' ');
      results = results.filter(product => {
        const searchText = isUzbek ? 
          `${product.nameUz} ${product.categoryUz} ${product.descriptionUz}`.toLowerCase() :
          `${product.name} ${product.category} ${product.description}`.toLowerCase();
        
        return searchTerms.every(term => searchText.includes(term));
      });
    }

    // Kategoriya bo'yicha filtrlash
    if (selectedCategory !== 'all') {
      results = results.filter(product => 
        product.category.toLowerCase() === selectedCategory.toLowerCase()
      );
    }

    // Narx oralig'i bo'yicha filtrlash
    results = results.filter(product => 
      product.price >= priceRange[0] && product.price <= priceRange[1]
    );

    // Saralash
    switch (sortBy) {
      case 'price_low':
        results.sort((a, b) => a.price - b.price);
        break;
      case 'price_high':
        results.sort((a, b) => b.price - a.price);
        break;
      case 'rating':
        results.sort((a, b) => b.rating - a.rating);
        break;
      case 'name':
        results.sort((a, b) => 
          isUzbek ? a.nameUz.localeCompare(b.nameUz) : a.name.localeCompare(b.name)
        );
        break;
      default:
        // relevance - default order
        break;
    }

    return results;
  }, [query, sortBy, priceRange, selectedCategory, isUzbek]);

  const handleAddToCart = (product: Product) => {
    addToCart(product);
    showSuccess("Mahsulot savatga qo'shildi!");
  };

  const handleWishlistToggle = (product: Product) => {
    const isInWishlist = wishlistItems.some((item: any) => item.id === product.id);
    if (isInWishlist) {
      removeFromWishlist(product.id);
      showInfo("Mahsulot sevimlilardan o'chirildi");
    } else {
      addToWishlist(product);
      showSuccess("Mahsulot sevimlilarga qo'shildi!");
    }
  };

  const isInWishlist = (productId: number) => {
    return wishlistItems.some((item: any) => item.id === productId);
  };

  const categories = [
    { value: 'all', label: isUzbek ? 'Barcha kategoriyalar' : 'Все категории' },
    { value: 'electronics', label: isUzbek ? 'Elektronika' : 'Электроника' },
    { value: 'computers', label: isUzbek ? 'Kompyuterlar' : 'Компьютеры' },
    { value: 'audio', label: isUzbek ? 'Audio' : 'Аудио' }
  ];

  return (
    <div id="searchPage">
      {/* Yaxshilangan qidiruv header */}
      <div style={{ 
        background: 'linear-gradient(135deg, #087c36 0%, #0a9d44 100%)',
        borderRadius: '20px',
        padding: '2rem',
        marginBottom: '2rem',
        color: 'white',
        position: 'relative',
        overflow: 'hidden'
      }}>
        <div style={{
          position: 'absolute',
          top: '-50px',
          right: '-50px',
          width: '150px',
          height: '150px',
          background: 'rgba(255,255,255,0.1)',
          borderRadius: '50%'
        }}></div>
        <div style={{
          position: 'absolute',
          bottom: '-30px',
          left: '-30px',
          width: '100px',
          height: '100px',
          background: 'rgba(255,255,255,0.1)',
          borderRadius: '50%'
        }}></div>
        
        <div style={{ position: 'relative', zIndex: 1 }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '15px', marginBottom: '15px' }}>
            <div style={{
              width: '50px',
              height: '50px',
              background: 'rgba(255,255,255,0.2)',
              borderRadius: '12px',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              fontSize: '20px'
            }}>
              <i className="fas fa-search"></i>
            </div>
            <div>
              <h2 style={{ margin: 0, fontSize: '28px', fontWeight: '700' }}>
                {query ? 
                  (isUzbek ? `"${query}" uchun natijalar` : `Результаты для "${query}"`) :
                  (isUzbek ? 'Qidiruv natijalar' : 'Результаты поиска')
                }
              </h2>
              <p style={{ margin: '5px 0 0 0', opacity: 0.9, fontSize: '16px' }}>
                {isUzbek ? 
                  `${filteredProducts.length} ta mahsulot topildi` : 
                  `Найдено товаров: ${filteredProducts.length}`
                }
              </p>
            </div>
          </div>

          {/* Qidiruv taklifi */}
          {!query && (
            <div style={{
              background: 'rgba(255,255,255,0.15)',
              borderRadius: '10px',
              padding: '15px',
              marginTop: '15px'
            }}>
              <p style={{ margin: 0, fontSize: '14px', opacity: 0.9 }}>
                <i className="fas fa-lightbulb" style={{ marginRight: '8px' }}></i>
                {isUzbek ? 
                  'Maslahat: Mahsulot nomini, brendni yoki kategoriyani kiriting' : 
                  'Совет: Введите название товара, бренд или категорию'
                }
              </p>
            </div>
          )}
        </div>
      </div>

      {/* Yaxshilangan filtrlar va saralash */}
      <div style={{ 
        background: 'linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%)',
        borderRadius: '20px',
        padding: '25px',
        marginBottom: '30px',
        boxShadow: '0 8px 25px rgba(0,0,0,0.1)',
        border: '1px solid #dee2e6'
      }}>
        <div style={{ 
          display: 'grid', 
          gap: '20px',
          gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))',
          alignItems: 'end'
        }}>
          {/* Yaxshilangan saralash */}
          <div>
            <label style={{ 
              display: 'block', 
              marginBottom: '10px', 
              fontWeight: '600',
              color: '#495057',
              fontSize: '14px',
              textTransform: 'uppercase',
              letterSpacing: '0.5px'
            }}>
              <i className="fas fa-sort-amount-down" style={{ marginRight: '8px', color: '#087c36' }}></i>
              {isUzbek ? 'Saralash' : 'Сортировка'}
            </label>
            <div style={{ position: 'relative' }}>
              <select 
                value={sortBy} 
                onChange={(e) => setSortBy(e.target.value)}
                style={{
                  width: '100%',
                  padding: '12px 20px 12px 45px',
                  border: '2px solid #e9ecef',
                  borderRadius: '10px',
                  backgroundColor: 'white',
                  fontSize: '14px',
                  fontWeight: '500',
                  cursor: 'pointer',
                  outline: 'none',
                  transition: 'all 0.3s ease',
                  appearance: 'none'
                }}
                onFocus={(e) => e.target.style.borderColor = '#087c36'}
                onBlur={(e) => e.target.style.borderColor = '#e9ecef'}
              >
                <option value="relevance">{isUzbek ? 'Tegishlilik' : 'По релевантности'}</option>
                <option value="price_low">{isUzbek ? 'Narx: arzondan qimmatge' : 'Цена: по возрастанию'}</option>
                <option value="price_high">{isUzbek ? 'Narx: qimmatdan arzonga' : 'Цена: по убыванию'}</option>
                <option value="rating">{isUzbek ? 'Reyting' : 'По рейтингу'}</option>
                <option value="name">{isUzbek ? 'Nomi' : 'По названию'}</option>
              </select>
              <i className="fas fa-sort-amount-down" style={{
                position: 'absolute',
                left: '15px',
                top: '50%',
                transform: 'translateY(-50%)',
                color: '#087c36',
                fontSize: '16px',
                pointerEvents: 'none'
              }}></i>
              <i className="fas fa-chevron-down" style={{
                position: 'absolute',
                right: '15px',
                top: '50%',
                transform: 'translateY(-50%)',
                color: '#6c757d',
                fontSize: '12px',
                pointerEvents: 'none'
              }}></i>
            </div>
          </div>

          {/* Yaxshilangan kategoriya */}
          <div>
            <label style={{ 
              display: 'block', 
              marginBottom: '10px', 
              fontWeight: '600',
              color: '#495057',
              fontSize: '14px',
              textTransform: 'uppercase',
              letterSpacing: '0.5px'
            }}>
              <i className="fas fa-layer-group" style={{ marginRight: '8px', color: '#6f42c1' }}></i>
              {isUzbek ? 'Kategoriya' : 'Категория'}
            </label>
            <div style={{ position: 'relative' }}>
              <select 
                value={selectedCategory} 
                onChange={(e) => setSelectedCategory(e.target.value)}
                style={{
                  width: '100%',
                  padding: '12px 20px 12px 45px',
                  border: '2px solid #e9ecef',
                  borderRadius: '10px',
                  backgroundColor: 'white',
                  fontSize: '14px',
                  fontWeight: '500',
                  cursor: 'pointer',
                  outline: 'none',
                  transition: 'all 0.3s ease',
                  appearance: 'none'
                }}
                onFocus={(e) => e.target.style.borderColor = '#6f42c1'}
                onBlur={(e) => e.target.style.borderColor = '#e9ecef'}
              >
                {categories.map(category => (
                  <option key={category.value} value={category.value}>
                    {category.label}
                  </option>
                ))}
              </select>
              <i className="fas fa-layer-group" style={{
                position: 'absolute',
                left: '15px',
                top: '50%',
                transform: 'translateY(-50%)',
                color: '#6f42c1',
                fontSize: '16px',
                pointerEvents: 'none'
              }}></i>
              <i className="fas fa-chevron-down" style={{
                position: 'absolute',
                right: '15px',
                top: '50%',
                transform: 'translateY(-50%)',
                color: '#6c757d',
                fontSize: '12px',
                pointerEvents: 'none'
              }}></i>
            </div>
          </div>

          {/* Yaxshilangan narx oralig'i */}
          <div>
            <label style={{ 
              display: 'block', 
              marginBottom: '10px', 
              fontWeight: '600',
              color: '#495057',
              fontSize: '14px',
              textTransform: 'uppercase',
              letterSpacing: '0.5px'
            }}>
              <i className="fas fa-dollar-sign" style={{ marginRight: '8px', color: '#fd7e14' }}></i>
              {isUzbek ? 'Narx oralig\'i' : 'Диапазон цен'}
            </label>
            <div style={{ 
              display: 'flex', 
              gap: '10px', 
              alignItems: 'center',
              background: 'white',
              borderRadius: '10px',
              padding: '4px',
              border: '2px solid #e9ecef'
            }}>
              <div style={{ position: 'relative', flex: 1 }}>
                <input
                  type="number"
                  placeholder={isUzbek ? 'Dan' : 'От'}
                  value={priceRange[0]}
                  onChange={(e) => setPriceRange([parseInt(e.target.value) || 0, priceRange[1]])}
                  style={{
                    width: '100%',
                    padding: '8px 16px',
                    border: '1px solid #dee2e6',
                    borderRadius: '6px',
                    fontSize: '13px',
                    outline: 'none',
                    transition: 'all 0.3s ease'
                  }}
                  onFocus={(e) => e.target.style.borderColor = '#fd7e14'}
                  onBlur={(e) => e.target.style.borderColor = '#dee2e6'}
                />
              </div>
              <div style={{
                width: '20px',
                height: '2px',
                background: '#fd7e14',
                borderRadius: '1px'
              }}></div>
              <div style={{ position: 'relative', flex: 1 }}>
                <input
                  type="number"
                  placeholder={isUzbek ? 'Gacha' : 'До'}
                  value={priceRange[1]}
                  onChange={(e) => setPriceRange([priceRange[0], parseInt(e.target.value) || 1000000])}
                  style={{
                    width: '100%',
                    padding: '8px 16px',
                    border: '1px solid #dee2e6',
                    borderRadius: '6px',
                    fontSize: '13px',
                    outline: 'none',
                    transition: 'all 0.3s ease'
                  }}
                  onFocus={(e) => e.target.style.borderColor = '#fd7e14'}
                  onBlur={(e) => e.target.style.borderColor = '#dee2e6'}
                />
              </div>
            </div>
            
          </div>

          {/* Filtrlarni tozalash tugmasi */}
          <div style={{ display: 'flex', justifyContent: 'center' }}>
            <button
              onClick={() => {
                setSortBy('relevance');
                setSelectedCategory('all');
                setPriceRange([0, 1000000]);
              }}
              style={{
                padding: '12px 20px',
                background: 'linear-gradient(135deg, #dc3545, #e85d75)',
                color: 'white',
                border: 'none',
                borderRadius: '10px',
                fontSize: '14px',
                fontWeight: '600',
                cursor: 'pointer',
                display: 'flex',
                alignItems: 'center',
                gap: '8px',
                transition: 'all 0.3s ease',
                boxShadow: '0 3px 10px rgba(220, 53, 69, 0.3)'
              }}
              onMouseOver={(e) => {
                e.currentTarget.style.transform = 'translateY(-2px)';
                e.currentTarget.style.boxShadow = '0 5px 15px rgba(220, 53, 69, 0.4)';
              }}
              onMouseOut={(e) => {
                e.currentTarget.style.transform = 'translateY(0)';
                e.currentTarget.style.boxShadow = '0 3px 10px rgba(220, 53, 69, 0.3)';
              }}
            >
              <i className="fas fa-eraser"></i>
              {isUzbek ? 'Tozalash' : 'Очистить'}
            </button>
          </div>
        </div>

        {/* Faol filtrlar ko'rsatkichi */}
        {(sortBy !== 'relevance' || selectedCategory !== 'all' || priceRange[0] !== 0 || priceRange[1] !== 1000000) && (
          <div style={{
            marginTop: '20px',
            padding: '15px',
            background: 'rgba(8, 124, 54, 0.1)',
            borderRadius: '10px',
            border: '1px solid rgba(8, 124, 54, 0.2)'
          }}>
            <div style={{ 
              display: 'flex', 
              alignItems: 'center', 
              justifyContent: 'space-between',
              marginBottom: '10px'
            }}>
              <span style={{ 
                fontSize: '14px', 
                color: '#087c36', 
                fontWeight: '600',
                display: 'flex',
                alignItems: 'center',
                gap: '8px'
              }}>
                <i className="fas fa-filter"></i>
                {isUzbek ? 'Faol filtrlar:' : 'Активные фильтры:'}
              </span>
            </div>
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px' }}>
              {sortBy !== 'relevance' && (
                <span style={{
                  padding: '4px 8px',
                  background: '#087c36',
                  color: 'white',
                  borderRadius: '15px',
                  fontSize: '12px',
                  fontWeight: '500'
                }}>
                  {isUzbek ? 'Saralash:' : 'Сортировка:'} {
                    sortBy === 'price_low' ? (isUzbek ? 'Arzon' : 'Дешевые') :
                    sortBy === 'price_high' ? (isUzbek ? 'Qimmat' : 'Дорогие') :
                    sortBy === 'rating' ? (isUzbek ? 'Reyting' : 'Рейтинг') :
                    sortBy === 'name' ? (isUzbek ? 'Nomi' : 'Название') : ''
                  }
                </span>
              )}
              {selectedCategory !== 'all' && (
                <span style={{
                  padding: '4px 8px',
                  background: '#6f42c1',
                  color: 'white',
                  borderRadius: '15px',
                  fontSize: '12px',
                  fontWeight: '500'
                }}>
                  {categories.find(c => c.value === selectedCategory)?.label}
                </span>
              )}
              {(priceRange[0] !== 0 || priceRange[1] !== 1000000) && (
                <span style={{
                  padding: '4px 8px',
                  background: '#fd7e14',
                  color: 'white',
                  borderRadius: '15px',
                  fontSize: '12px',
                  fontWeight: '500'
                }}>
                  {priceRange[0].toLocaleString()} - {priceRange[1].toLocaleString()} so'm
                </span>
              )}
            </div>
          </div>
        )}
      </div>

      {/* Yaxshilangan qidiruv natijalari */}
      {filteredProducts.length === 0 ? (
        <div style={{ 
          textAlign: 'center', 
          padding: '4rem 2rem',
          background: 'linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%)',
          borderRadius: '20px',
          border: '1px solid #dee2e6'
        }}>
          <div style={{
            width: '100px',
            height: '100px',
            background: 'linear-gradient(135deg, #6c757d, #868e96)',
            borderRadius: '50%',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            margin: '0 auto 2rem',
            fontSize: '40px',
            color: 'white',
            boxShadow: '0 8px 25px rgba(108, 117, 125, 0.3)'
          }}>
            <i className="fas fa-search"></i>
          </div>
          <h3 style={{ 
            margin: '0 0 1rem 0', 
            fontSize: '24px', 
            fontWeight: '600',
            color: '#495057'
          }}>
            {isUzbek ? "Hech narsa topilmadi" : "Ничего не найдено"}
          </h3>
          <p style={{ 
            margin: '0 0 2rem 0', 
            color: '#6c757d', 
            fontSize: '16px',
            maxWidth: '400px',
            marginLeft: 'auto',
            marginRight: 'auto',
            lineHeight: '1.5'
          }}>
            {isUzbek ? 
              "Qidiruv shartlarini o'zgartirib, qayta urinib ko'ring yoki filtrlarni tozalang" : 
              "Попробуйте изменить условия поиска или очистить фильтры"
            }
          </p>
          <div style={{ display: 'flex', gap: '15px', justifyContent: 'center', flexWrap: 'wrap' }}>
            <button
              onClick={() => {
                setSortBy('relevance');
                setSelectedCategory('all');
                setPriceRange([0, 1000000]);
                setQuery('');
              }}
              style={{
                padding: '12px 24px',
                background: 'linear-gradient(135deg, #087c36, #0a9d44)',
                color: 'white',
                border: 'none',
                borderRadius: '10px',
                fontSize: '14px',
                fontWeight: '600',
                cursor: 'pointer',
                display: 'flex',
                alignItems: 'center',
                gap: '8px',
                transition: 'all 0.3s ease'
              }}
            >
              <i className="fas fa-eraser"></i>
              {isUzbek ? 'Filtrlarni tozalash' : 'Очистить фильтры'}
            </button>
            <Link 
              href="/products" 
              style={{
                padding: '12px 24px',
                background: 'linear-gradient(135deg, #6f42c1, #8e5dd9)',
                color: 'white',
                border: 'none',
                borderRadius: '10px',
                fontSize: '14px',
                fontWeight: '600',
                textDecoration: 'none',
                display: 'flex',
                alignItems: 'center',
                gap: '8px',
                transition: 'all 0.3s ease'
              }}
            >
              <i className="fas fa-th-large"></i>
              {isUzbek ? "Barcha mahsulotlar" : "Все товары"}
            </Link>
          </div>
        </div>
      ) : (
        <>
          {/* Natijalar statistikasi */}
          <div style={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            marginBottom: '20px',
            padding: '15px 20px',
            background: 'linear-gradient(135deg, #087c36 0%, #0a9d44 100%)',
            borderRadius: '15px',
            color: 'white'
          }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
              <i className="fas fa-chart-bar" style={{ fontSize: '18px' }}></i>
              <span style={{ fontWeight: '600', fontSize: '16px' }}>
                {isUzbek ? 
                  `${filteredProducts.length} ta mahsulot ko'rsatilmoqda` : 
                  `Показано ${filteredProducts.length} товаров`
                }
              </span>
            </div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '15px' }}>
              {query && (
                <div style={{ 
                  background: 'rgba(255,255,255,0.2)', 
                  padding: '5px 12px', 
                  borderRadius: '15px',
                  fontSize: '13px',
                  fontWeight: '500'
                }}>
                  <i className="fas fa-search" style={{ marginRight: '5px' }}></i>
                  "{query}"
                </div>
              )}
              <div style={{ 
                background: 'rgba(255,255,255,0.2)', 
                padding: '5px 12px', 
                borderRadius: '15px',
                fontSize: '13px',
                fontWeight: '500'
              }}>
                <i className="fas fa-clock" style={{ marginRight: '5px' }}></i>
                {new Date().toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})}
              </div>
            </div>
          </div>

          {/* Mahsulotlar grid */}
          <div className="product-grid">
            {filteredProducts.map((product) => (
              <div key={product.id} className="product-card" style={{
                position: 'relative',
                background: 'white',
                borderRadius: '15px',
                overflow: 'hidden',
                boxShadow: '0 4px 15px rgba(0,0,0,0.1)',
                border: '1px solid #dee2e6',
                transition: 'all 0.3s ease'
              }}
              onMouseOver={(e) => {
                e.currentTarget.style.transform = 'translateY(-5px)';
                e.currentTarget.style.boxShadow = '0 8px 25px rgba(0,0,0,0.15)';
              }}
              onMouseOut={(e) => {
                e.currentTarget.style.transform = 'translateY(0)';
                e.currentTarget.style.boxShadow = '0 4px 15px rgba(0,0,0,0.1)';
              }}>
                {/* Yaxshilangan mahsulot rasmi */}
                <Link href={`/products/${product.id}`} className="product-image-link">
                  <div className="product-image" style={{
                    position: 'relative',
                    overflow: 'hidden',
                    borderRadius: '15px 15px 0 0'
                  }}>
                    <ProductImage 
                      src={product.images[0] || ''} 
                      alt={isUzbek ? product.nameUz : product.name}
                      loading="lazy"
                      width={250}
                      height={200}
                      style={{
                        width: '100%',
                        height: '200px',
                        objectFit: 'cover',
                        transition: 'transform 0.3s ease'
                      }}
                      onMouseOver={(e) => (e.currentTarget as HTMLImageElement).style.transform = 'scale(1.05)'}
                      onMouseOut={(e) => (e.currentTarget as HTMLImageElement).style.transform = 'scale(1)'}
                    />
                    {product.discount > 0 && (
                      <div className="discount-badge" style={{
                        position: 'absolute',
                        top: '10px',
                        left: '10px',
                        background: 'linear-gradient(135deg, #dc3545, #e85d75)',
                        color: 'white',
                        padding: '5px 10px',
                        borderRadius: '15px',
                        fontSize: '12px',
                        fontWeight: '600',
                        boxShadow: '0 2px 8px rgba(220, 53, 69, 0.3)'
                      }}>
                        -{product.discount}%
                      </div>
                    )}
                    {!product.inStock && (
                      <div style={{
                        position: 'absolute',
                        top: '10px',
                        right: '10px',
                        background: 'rgba(108, 117, 125, 0.9)',
                        color: 'white',
                        padding: '5px 10px',
                        borderRadius: '15px',
                        fontSize: '12px',
                        fontWeight: '600'
                      }}>
                        {isUzbek ? 'Tugagan' : 'Нет в наличии'}
                      </div>
                    )}
                  </div>
                </Link>

                {/* Yaxshilangan mahsulot ma'lumotlari */}
                <div className="product-info" style={{
                  padding: '20px'
                }}>
                  <div className="product-title" style={{
                    fontSize: '16px',
                    fontWeight: '600',
                    color: '#212529',
                    marginBottom: '10px',
                    lineHeight: '1.4',
                    minHeight: '44px',
                    display: '-webkit-box',
                    WebkitLineClamp: 2,
                    WebkitBoxOrient: 'vertical',
                    overflow: 'hidden'
                  }}>
                    {isUzbek ? product.nameUz : product.name}
                  </div>
                  
                  <div className="product-price" style={{
                    marginBottom: '12px'
                  }}>
                    {product.originalPrice && (
                      <span className="original-price" style={{
                        fontSize: '14px',
                        color: '#6c757d',
                        textDecoration: 'line-through',
                        marginRight: '8px'
                      }}>
                        {product.originalPrice.toLocaleString()} so'm
                      </span>
                    )}
                    <span className="current-price" style={{
                      fontSize: '18px',
                      fontWeight: '700',
                      color: '#087c36'
                    }}>
                      {product.price.toLocaleString()} so'm
                    </span>
                  </div>

                  {/* Yaxshilangan rating */}
                  <div className="product-rating" style={{
                    display: 'flex',
                    alignItems: 'center',
                    gap: '8px',
                    marginBottom: '15px'
                  }}>
                    <div className="stars" style={{ display: 'flex', gap: '2px' }}>
                      {[...Array(5)].map((_, i) => (
                        <i 
                          key={i} 
                          className={`fas fa-star`}
                          style={{
                            color: i < Math.floor(product.rating) ? '#ffc107' : '#e9ecef',
                            fontSize: '14px'
                          }}
                        ></i>
                      ))}
                    </div>
                    <span className="rating-text" style={{
                      fontSize: '13px',
                      color: '#6c757d',
                      fontWeight: '500'
                    }}>
                      {product.rating} ({product.reviewsCount})
                    </span>
                  </div>
                  
                  {/* Yaxshilangan tugmalar */}
                  <div className="product-card-actions" style={{
                    display: 'flex',
                    gap: '8px',
                    alignItems: 'center'
                  }}>
                    <button 
                      className="btn btn-primary btn-sm"
                      onClick={() => handleAddToCart(product)}
                      disabled={!product.inStock}
                      style={{
                        flex: 1,
                        padding: '10px',
                        background: product.inStock ? 
                          'linear-gradient(135deg, #087c36, #0a9d44)' : 
                          '#6c757d',
                        color: 'white',
                        border: 'none',
                        borderRadius: '8px',
                        fontSize: '13px',
                        fontWeight: '600',
                        cursor: product.inStock ? 'pointer' : 'not-allowed',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        gap: '6px',
                        transition: 'all 0.3s ease'
                      }}
                    >
                      <i className="fas fa-shopping-cart"></i>
                      {product.inStock ? 
                        (isUzbek ? "Savatga" : "В корзину") : 
                        (isUzbek ? "Tugagan" : "Нет в наличии")
                      }
                    </button>
                    
                    <Link 
                      href={`/products/${product.id}`} 
                      className="btn-icon"
                      title={t('details')}
                      style={{
                        width: '40px',
                        height: '40px',
                        background: 'linear-gradient(135deg, #6f42c1, #8e5dd9)',
                        color: 'white',
                        borderRadius: '8px',
                        textDecoration: 'none',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        transition: 'all 0.3s ease'
                      }}
                    >
                      <i className="fas fa-eye"></i>
                    </Link>
                    
                    <button 
                      className={`btn-icon ${isInWishlist(product.id) ? 'active' : ''}`}
                      onClick={() => handleWishlistToggle(product)}
                      title={isUzbek ? "Sevimlilar" : "В избранное"}
                      style={{
                        width: '40px',
                        height: '40px',
                        background: isInWishlist(product.id) ? 
                          'linear-gradient(135deg, #dc3545, #e85d75)' : 
                          'linear-gradient(135deg, #e9ecef, #f8f9fa)',
                        color: isInWishlist(product.id) ? 'white' : '#6c757d',
                        border: 'none',
                        borderRadius: '8px',
                        cursor: 'pointer',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        transition: 'all 0.3s ease'
                      }}
                    >
                      <i className={`${isInWishlist(product.id) ? 'fas' : 'far'} fa-heart`}></i>
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </>
      )}
    </div>
  );
};

export default SearchPage;
