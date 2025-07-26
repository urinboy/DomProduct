'use client';

import React from 'react';
import Link from 'next/link';
import { useTranslation } from 'react-i18next';
import { products, categories } from './data/products';
import { useCart } from './contexts/CartContext';
import { useWishlist } from './contexts/WishlistContext';
import { useToast } from './components/Toast/ToastProvider';
import ImageSlider from './components/ImageSlider';
import ProductImage from './components/ProductImage';

/* Avvalgi loyihadagi bosh sahifani to'liq takrorlash */
const HomePage = () => {
  const { t } = useTranslation();
  const { addToCart } = useCart();
  const { addToWishlist, removeFromWishlist, isInWishlist } = useWishlist();
  const { showSuccess, showInfo } = useToast();

  // Bosh sahifa uchun cheklangan kategoriyalar va mahsulotlar
  const featuredCategories = categories.slice(0, 5);
  const featuredProducts = products.slice(0, 4);

  // Savatga qo'shish funksiyasi
  const handleAddToCart = (product: any) => {
    addToCart(product);
    showSuccess("Mahsulot savatga qo'shildi!");
  };

  // Sevimlilar ro'yxatini boshqarish
  const handleToggleWishlist = (product: any) => {
    if (isInWishlist(product.id)) {
      removeFromWishlist(product.id);
      showInfo("Mahsulot sevimlilardan o'chirildi");
    } else {
      addToWishlist(product);
      showSuccess("Mahsulot sevimlilarga qo'shildi!");
    }
  };

  return (
    <div id="homePage">
      {/* Rasm slayderi */}
      <ImageSlider />
      
      {/* Kategoriyalar bo'limi */}
      <div className="section-header">
        <h2>{t('categories')}</h2>
        <Link href="/categories" className="see-all-link">
          {t('see_all') || 'Barchasini ko\'rish'}
        </Link>
      </div>
      
      <div className="category-grid" id="categoriesGrid">
        {featuredCategories.map(category => (
          <Link 
            href={`/products?category=${category.id}`}
            key={category.id}
            className="category-card"
          >
            <div className="category-icon">
              {category.icon}
            </div>
            <span className="category-name">
              {category.name}
            </span>
          </Link>
        ))}
      </div>

      {/* Yangi mahsulotlar bo'limi */}
      <div className="section-header">
        <h2>{t('new_products') || 'Yangi mahsulotlar'}</h2>
        <Link href="/products" className="see-all-link">
          {t('see_all') || 'Barchasini ko\'rish'}
        </Link>
      </div>

      <div className="product-grid" id="featuredProducts">
        {featuredProducts.map(product => (
          <div className="product-card" key={product.id}>
            {/* Mahsulot rasmi */}
            <Link href={`/products/${product.id}`} className="product-image-link">
              <div className="product-image">
                <ProductImage 
                  src={product.images[0] || ''} 
                  alt={product.nameUz} 
                  loading="lazy"
                  width={250}
                  height={250}
                />
                {/* Chegirma badge */}
                {product.discount > 0 && (
                  <div className="discount-badge">
                    -{product.discount}%
                  </div>
                )}
              </div>
            </Link>

            {/* Mahsulot ma'lumotlari */}
            <div className="product-info">
              <div className="product-title">
                {product.nameUz}
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

              {/* Rating */}
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

              {/* Mahsulot tugmalari */}
              <div className="product-card-actions">
                <Link 
                  href={`/products/${product.id}`} 
                  className="btn btn-primary btn-sm"
                >
                  <i className="fas fa-eye"></i>
                  {t('details') || 'Batafsil'}
                </Link>
                
                <button 
                  className="btn-icon"
                  onClick={() => handleAddToCart(product)}
                  disabled={!product.inStock}
                  title={t('add_to_cart')}
                >
                  <i className="fas fa-shopping-cart"></i>
                </button>
                
                <button 
                  className={`btn-icon ${isInWishlist(product.id) ? 'active' : ''}`}
                  onClick={() => handleToggleWishlist(product)}
                  title={t('add_to_wishlist')}
                >
                  <i className={`${isInWishlist(product.id) ? 'fas' : 'far'} fa-heart`}></i>
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default HomePage;
