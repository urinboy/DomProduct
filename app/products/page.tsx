'use client';

import { useState, useMemo } from 'react';
import { useTranslation } from 'react-i18next';
import Link from 'next/link';
import { useSearchParams } from 'next/navigation';
import { useCart } from '../../src/contexts/CartContext';
import { useWishlist } from '../../src/contexts/WishlistContext';
import { useToast } from '../../src/components/Toast/ToastProvider';

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

/* Mahsulotlar sahifasi - avvalgi loyihadagi dizaynni takrorlash */
export default function ProductsPage() {
  const { t, i18n } = useTranslation();
  const { addToCart } = useCart();
  const { items: wishlistItems, addToWishlist, removeFromWishlist } = useWishlist();
  const { showSuccess, showInfo } = useToast();
  const searchParams = useSearchParams();
  
  const [searchTerm, setSearchTerm] = useState('');
  const [sortOrder, setSortOrder] = useState('');
  
  const isUzbek = i18n.language === 'uz';
  const selectedCategorySlug = searchParams.get('category');

  // Demo mahsulotlar - avvalgi loyihadagi kabi
  const allProducts: Product[] = [
    {
      id: 1,
      name: "iPhone 15 Pro",
      nameUz: "iPhone 15 Pro",
      price: 15000000,
      originalPrice: 16000000,
      discount: 6,
      images: ["/api/placeholder/400/400"],
      rating: 4.8,
      reviewsCount: 234,
      inStock: true,
      category: "electronics",
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
      images: ["/api/placeholder/400/400"],
      rating: 4.6,
      reviewsCount: 189,
      inStock: true,
      category: "electronics",
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
      images: ["/api/placeholder/400/400"],
      rating: 4.9,
      reviewsCount: 156,
      inStock: true,
      category: "computers",
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
      images: ["/api/placeholder/400/400"],
      rating: 4.7,
      reviewsCount: 445,
      inStock: true,
      category: "audio",
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
      images: ["/api/placeholder/400/400"],
      rating: 4.5,
      reviewsCount: 123,
      inStock: false,
      category: "computers",
      categoryUz: "Kompyuterlar",
      description: "Ultrabook with premium design",
      descriptionUz: "Premium dizaynli ultrabook"
    },
    {
      id: 6,
      name: "Sony WH-1000XM5",
      nameUz: "Sony WH-1000XM5",
      price: 3500000,
      originalPrice: 4000000,
      discount: 12,
      images: ["/api/placeholder/400/400"],
      rating: 4.8,
      reviewsCount: 278,
      inStock: true,
      category: "audio",
      categoryUz: "Audio",
      description: "Premium noise-canceling headphones",
      descriptionUz: "Premium shovqinni bekor qiluvchi quloqchinlar"
    }
  ];

  const categories = [
    { slug: 'electronics', name: 'Electronics', nameUz: 'Elektronika' },
    { slug: 'computers', name: 'Computers', nameUz: 'Kompyuterlar' },
    { slug: 'audio', name: 'Audio', nameUz: 'Audio' }
  ];

  const displayedProducts = useMemo(() => {
    let products = selectedCategorySlug
      ? allProducts.filter(p => p.category === selectedCategorySlug)
      : allProducts;

    if (searchTerm) {
      products = products.filter(p => {
        const searchText = isUzbek ? 
          `${p.nameUz} ${p.categoryUz} ${p.descriptionUz}`.toLowerCase() :
          `${p.name} ${p.category} ${p.description}`.toLowerCase();
        return searchText.includes(searchTerm.toLowerCase());
      });
    }

    if (sortOrder) {
      products = [...products].sort((a, b) => {
        switch (sortOrder) {
          case 'price-asc': return a.price - b.price;
          case 'price-desc': return b.price - a.price;
          case 'name-asc': return isUzbek ? a.nameUz.localeCompare(b.nameUz) : a.name.localeCompare(b.name);
          default: return 0;
        }
      });
    }

    return products;
  }, [selectedCategorySlug, searchTerm, sortOrder, isUzbek]);

  const getCategoryName = (slug: string | null) => {
    if (!slug) return isUzbek ? 'Barcha mahsulotlar' : 'Все товары';
    const category = categories.find(cat => cat.slug === slug);
    return category ? (isUzbek ? category.nameUz : category.name) : (isUzbek ? 'Barcha mahsulotlar' : 'Все товары');
  };

  const handleAddToCart = (product: Product) => {
    addToCart(product);
    showSuccess(
      isUzbek ? "Savatga qo'shildi!" : "Добавлено в корзину!",
      isUzbek ? `${product.nameUz} mahsuloti savatga qo'shildi` : `${product.name} добавлен в корзину`
    );
  };

  const handleToggleWishlist = (product: Product) => {
    const isInWishlist = wishlistItems.some((item: any) => item.id === product.id);
    if (isInWishlist) {
      removeFromWishlist(product.id);
      showInfo(
        isUzbek ? "Sevimlilardan olib tashlandi" : "Удалено из избранного",
        isUzbek ? `${product.nameUz} sevimlilardan olib tashlandi` : `${product.name} удален из избранного`
      );
    } else {
      addToWishlist(product);
      showSuccess(
        isUzbek ? "Sevimlilarga qo'shildi!" : "Добавлено в избранное!",
        isUzbek ? `${product.nameUz} sevimlilar ro'yxatiga qo'shildi` : `${product.name} добавлен в избранное`
      );
    }
  };

  const isInWishlist = (productId: number) => {
    return wishlistItems.some((item: any) => item.id === productId);
  };

  return (
    <div id="productsPage">
      <h2 style={{ marginBottom: '1rem' }}>
        {getCategoryName(selectedCategorySlug)}
      </h2>

      {/* Yaxshilangan filtrlar */}
      <div className="filters-container" style={{ 
        background: 'linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%)',
        borderRadius: '15px',
        padding: '20px',
        marginBottom: '30px',
        boxShadow: '0 4px 15px rgba(0,0,0,0.1)',
        border: '1px solid #dee2e6'
      }}>
        <div style={{ 
          display: 'flex', 
          flexWrap: 'wrap', 
          gap: '15px', 
          alignItems: 'center',
          justifyContent: 'space-between'
        }}>
          {/* Qidiruv maydon */}
          <div className="search-form-group" style={{ 
            position: 'relative',
            minWidth: '300px',
            flex: '1 1 auto'
          }}>
            <div style={{ 
              position: 'relative',
              background: 'white',
              borderRadius: '10px',
              boxShadow: '0 2px 8px rgba(0,0,0,0.08)',
              overflow: 'hidden'
            }}>
              <i className="fas fa-search" style={{ 
                position: 'absolute',
                left: '15px',
                top: '50%',
                transform: 'translateY(-50%)',
                color: '#087c36',
                fontSize: '16px',
                zIndex: 1
              }}></i>
              <input
                type="text"
                placeholder={isUzbek ? "Mahsulotlarni qidirish..." : "Поиск товаров..."}
                className="filter-input"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                style={{
                  width: '100%',
                  padding: '12px 20px 12px 45px',
                  border: 'none',
                  outline: 'none',
                  fontSize: '14px',
                  borderRadius: '10px',
                  transition: 'all 0.3s ease'
                }}
              />
              {searchTerm && (
                <button
                  onClick={() => setSearchTerm('')}
                  style={{
                    position: 'absolute',
                    right: '15px',
                    top: '50%',
                    transform: 'translateY(-50%)',
                    background: 'none',
                    border: 'none',
                    color: '#6c757d',
                    cursor: 'pointer',
                    fontSize: '14px'
                  }}
                >
                  <i className="fas fa-times"></i>
                </button>
              )}
            </div>
          </div>

          {/* Saralash */}
          <div className="search-form-group" style={{ 
            position: 'relative',
            minWidth: '200px'
          }}>
            <div style={{ 
              position: 'relative',
              background: 'white',
              borderRadius: '10px',
              boxShadow: '0 2px 8px rgba(0,0,0,0.08)',
              overflow: 'hidden'
            }}>
              <i className="fas fa-sort-amount-down" style={{ 
                position: 'absolute',
                left: '15px',
                top: '50%',
                transform: 'translateY(-50%)',
                color: '#087c36',
                fontSize: '16px',
                zIndex: 1,
                pointerEvents: 'none'
              }}></i>
              <select
                className="filter-select"
                value={sortOrder}
                onChange={(e) => setSortOrder(e.target.value)}
                style={{
                  width: '100%',
                  padding: '12px 20px 12px 45px',
                  border: 'none',
                  outline: 'none',
                  fontSize: '14px',
                  borderRadius: '10px',
                  background: 'white',
                  cursor: 'pointer',
                  appearance: 'none'
                }}
              >
                <option value="">{isUzbek ? "Saralash" : "Сортировка"}</option>
                <option value="price-asc">{isUzbek ? "Narx: arzondan qimmatge" : "Цена: по возрастанию"}</option>
                <option value="price-desc">{isUzbek ? "Narx: qimmatdan arzonga" : "Цена: по убыванию"}</option>
                <option value="name-asc">{isUzbek ? "Nomi bo'yicha" : "По названию"}</option>
              </select>
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

          {/* Natijalar soni */}
          <div style={{
            background: '#087c36',
            color: 'white',
            padding: '8px 16px',
            borderRadius: '20px',
            fontSize: '14px',
            fontWeight: '500',
            display: 'flex',
            alignItems: 'center',
            gap: '8px'
          }}>
            <i className="fas fa-list"></i>
            {displayedProducts.length} {isUzbek ? 'ta mahsulot' : 'товаров'}
          </div>
        </div>

        {/* Kategoriya ko'rsatkichi */}
        {selectedCategorySlug && (
          <div style={{
            marginTop: '15px',
            padding: '10px 15px',
            background: 'rgba(8, 124, 54, 0.1)',
            borderRadius: '8px',
            borderLeft: '4px solid #087c36',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between'
          }}>
            <span style={{ fontSize: '14px', color: '#087c36', fontWeight: '500' }}>
              <i className="fas fa-filter" style={{ marginRight: '8px' }}></i>
              {isUzbek ? 'Kategoriya:' : 'Категория:'} {getCategoryName(selectedCategorySlug)}
            </span>
            <Link 
              href="/products"
              style={{
                color: '#087c36',
                textDecoration: 'none',
                fontSize: '12px',
                padding: '4px 8px',
                borderRadius: '4px',
                background: 'white',
                transition: 'all 0.3s ease'
              }}
            >
              <i className="fas fa-times" style={{ marginRight: '4px' }}></i>
              {isUzbek ? 'Tozalash' : 'Очистить'}
            </Link>
          </div>
        )}
      </div>

      {/* Mahsulotlar */}
      {displayedProducts.length > 0 ? (
        <div className="product-grid" id="productsGrid">
          {displayedProducts.map(product => (
            <div className="product-card" key={product.id}>
              <Link href={`/products/${product.id}`} className="product-image-link">
                <div className="product-image">
                  <img 
                    src={product.images[0]} 
                    alt={isUzbek ? product.nameUz : product.name}
                    loading="lazy"
                  />
                  {product.discount > 0 && (
                    <div className="discount-badge">
                      -{product.discount}%
                    </div>
                  )}
                </div>
              </Link>
              
              <div className="product-info">
                <div className="product-title">
                  {isUzbek ? product.nameUz : product.name}
                </div>
                
                <div className="product-price">
                  {product.originalPrice && (
                    <span className="original-price">
                      {product.originalPrice.toLocaleString()} so'm
                    </span>
                  )}
                  <span className="current-price">
                    {product.price.toLocaleString()} so'm
                  </span>
                </div>

                <div className="product-rating">
                  <div className="stars">
                    {[...Array(5)].map((_, i) => (
                      <i 
                        key={i} 
                        className={`fas fa-star ${i < Math.floor(product.rating) ? 'filled' : ''}`}
                      ></i>
                    ))}
                  </div>
                  <span className="rating-text">
                    {product.rating} ({product.reviewsCount})
                  </span>
                </div>
                
                <div className="product-card-actions">
                  <button 
                    className="btn btn-primary btn-sm"
                    onClick={() => handleAddToCart(product)}
                    disabled={!product.inStock}
                  >
                    {product.inStock ? t('add_to_cart') : 
                      (isUzbek ? "Tugagan" : "Нет в наличии")
                    }
                  </button>
                  
                  <Link 
                    href={`/products/${product.id}`} 
                    className="btn-icon"
                    title={t('details')}
                  >
                    <i className="fas fa-eye"></i>
                  </Link>
                  
                  <button 
                    className={`btn-icon ${isInWishlist(product.id) ? 'active' : ''}`}
                    onClick={() => handleToggleWishlist(product)}
                    title={isUzbek ? "Sevimlilar" : "В избранное"}
                  >
                    <i className={`${isInWishlist(product.id) ? 'fas' : 'far'} fa-heart`}></i>
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div className="cart-empty" style={{ padding: '2rem', textAlign: 'center' }}>
          <i className="fas fa-box-open cart-empty-icon"></i>
          <div className="cart-empty-title">
            {isUzbek ? "Mahsulotlar topilmadi" : "Товары не найдены"}
          </div>
          <p className="cart-empty-message">
            {isUzbek ? 
              "Qidiruv shartlarini o'zgartirib, qayta urinib ko'ring" : 
              "Попробуйте изменить условия поиска"
            }
          </p>
          <Link href="/" className="btn btn-primary">
            {isUzbek ? "Bosh sahifaga qaytish" : "Вернуться на главную"}
          </Link>
        </div>
      )}
    </div>
  );
}
