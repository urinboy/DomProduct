'use client';

import { useState, useEffect } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { useTranslation } from 'react-i18next';
import Link from 'next/link';
import { useCart } from '../../../src/contexts/CartContext';
import { useWishlist } from '../../../src/contexts/WishlistContext';
import './product-detail.css';

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
  specifications?: Record<string, string>;
  reviews?: Review[];
}

interface Review {
  id: number;
  userName: string;
  rating: number;
  comment: string;
  date: string;
}

/* Mahsulot batafsil sahifasi - avvalgi loyihadagi dizaynni takrorlash */
export default function ProductDetailPage() {
  const { t, i18n } = useTranslation();
  const params = useParams();
  const router = useRouter();
  const { addToCart } = useCart();
  const { items: wishlistItems, addToWishlist, removeFromWishlist } = useWishlist();
  
  const [selectedImageIndex, setSelectedImageIndex] = useState(0);
  const [quantity, setQuantity] = useState(1);
  const [activeTab, setActiveTab] = useState('description');
  const [isLoading, setIsLoading] = useState(true);

  const isUzbek = i18n.language === 'uz';
  const productId = parseInt(params.id as string);

  // Demo mahsulot - avvalgi loyihadagi kabi
  const product: Product = {
    id: productId,
    name: "iPhone 15 Pro",
    nameUz: "iPhone 15 Pro",
    price: 15000000,
    originalPrice: 16000000,
    discount: 6,
    images: [
      "/api/placeholder/500/500",
      "/api/placeholder/500/500",
      "/api/placeholder/500/500"
    ],
    rating: 4.8,
    reviewsCount: 234,
    inStock: true,
    category: "Electronics",
    categoryUz: "Elektronika",
    description: "Latest iPhone with advanced camera system and powerful A17 Pro chip. Perfect for photography, gaming, and professional work.",
    descriptionUz: "Ilg'or kamera tizimi va kuchli A17 Pro chip bilan eng yangi iPhone. Fotografiya, o'yin va professional ish uchun mukammal.",
    specifications: {
      "Display": "6.1-inch Super Retina XDR",
      "Chip": "A17 Pro",
      "Camera": "48MP Main, 12MP Ultra Wide",
      "Storage": "128GB, 256GB, 512GB, 1TB",
      "Battery": "Up to 23 hours video playback"
    },
    reviews: [
      {
        id: 1,
        userName: "Alisher",
        rating: 5,
        comment: "Juda zo'r telefon, kamerasi a'lo!",
        date: "2025-01-20"
      },
      {
        id: 2,
        userName: "Madina",
        rating: 4,
        comment: "Yaxshi, lekin narxi qimmat",
        date: "2025-01-18"
      }
    ]
  };

  useEffect(() => {
    // Simulate loading
    setTimeout(() => setIsLoading(false), 500);
  }, []);

  const isInWishlist = wishlistItems.some((item: any) => item.id === product.id);

  const handleGoBack = () => {
    router.back();
  };

  const handleShare = async () => {
    if (navigator.share) {
      try {
        await navigator.share({
          title: isUzbek ? product.nameUz : product.name,
          text: isUzbek ? product.descriptionUz : product.description,
          url: window.location.href,
        });
      } catch (error) {
        console.error('Share failed:', error);
      }
    } else {
      // Fallback to clipboard
      navigator.clipboard.writeText(window.location.href);
    }
  };

  const handleWishlistToggle = () => {
    if (isInWishlist) {
      removeFromWishlist(product.id);
    } else {
      addToWishlist(product);
    }
  };

  const handleAddToCart = () => {
    const cartProduct = { ...product, quantity };
    addToCart(cartProduct);
  };

  if (isLoading) {
    return (
      <div className="product-detail-loading">
        <div className="spinner"></div>
      </div>
    );
  }

  if (!product) {
    return (
      <div className="product-not-found">
        <i className="fas fa-exclamation-triangle"></i>
        <h3>{isUzbek ? "Mahsulot topilmadi" : "Товар не найден"}</h3>
        <Link href="/products" className="btn btn-primary">
          {isUzbek ? "Barcha mahsulotlar" : "Все товары"}
        </Link>
      </div>
    );
  }

  return (
    <div className="product-detail-page">
      {/* Header */}
      <header className="detail-header">
        <button onClick={handleGoBack} className="header-btn">
          <i className="fas fa-arrow-left"></i>
        </button>
        <div className="header-actions">
          <button onClick={handleShare} className="header-btn">
            <i className="fas fa-share-alt"></i>
          </button>
          <button 
            onClick={handleWishlistToggle} 
            className={`header-btn ${isInWishlist ? 'liked' : ''}`}
          >
            <i className={isInWishlist ? "fas fa-heart" : "far fa-heart"}></i>
          </button>
        </div>
      </header>

      {/* Main Content */}
      <main className="detail-content">
        {/* Image Gallery */}
        <div className="product-image-gallery">
          <div className="main-image-container">
            <img 
              src={product.images[selectedImageIndex]} 
              alt={isUzbek ? product.nameUz : product.name}
              className="main-product-image"
            />
            {product.discount > 0 && (
              <div className="discount-badge">
                -{product.discount}%
              </div>
            )}
          </div>
          
          {product.images.length > 1 && (
            <div className="image-thumbnails">
              {product.images.map((image, index) => (
                <button
                  key={index}
                  className={`thumbnail ${index === selectedImageIndex ? 'active' : ''}`}
                  onClick={() => setSelectedImageIndex(index)}
                >
                  <img src={image} alt={`${product.name} ${index + 1}`} />
                </button>
              ))}
            </div>
          )}
        </div>

        {/* Product Info */}
        <div className="product-info-section">
          <h1 className="product-detail-title">
            {isUzbek ? product.nameUz : product.name}
          </h1>
          
          {/* Rating */}
          <div className="product-rating-detail">
            <div className="stars">
              {[...Array(5)].map((_, i) => (
                <i 
                  key={i} 
                  className={`fas fa-star ${i < Math.floor(product.rating) ? 'filled' : ''}`}
                ></i>
              ))}
            </div>
            <span className="rating-text">
              {product.rating} ({product.reviewsCount} {isUzbek ? 'sharh' : 'отзывов'})
            </span>
          </div>

          {/* Price */}
          <div className="product-detail-price">
            <span className="current-price">
              {product.price.toLocaleString()} so'm
            </span>
            {product.originalPrice && (
              <span className="original-price">
                {product.originalPrice.toLocaleString()} so'm
              </span>
            )}
          </div>

          {/* Stock Status */}
          <div className={`stock-status ${product.inStock ? 'in-stock' : 'out-of-stock'}`}>
            <i className={`fas ${product.inStock ? 'fa-check-circle' : 'fa-times-circle'}`}></i>
            {product.inStock ? 
              (isUzbek ? "Mavjud" : "В наличии") : 
              (isUzbek ? "Tugagan" : "Нет в наличии")
            }
          </div>

          {/* Quantity Selector */}
          {product.inStock && (
            <div className="quantity-selector">
              <label>{isUzbek ? "Miqdor:" : "Количество:"}</label>
              <div className="quantity-controls">
                <button 
                  onClick={() => setQuantity(Math.max(1, quantity - 1))}
                  disabled={quantity <= 1}
                >
                  <i className="fas fa-minus"></i>
                </button>
                <span className="quantity-value">{quantity}</span>
                <button onClick={() => setQuantity(quantity + 1)}>
                  <i className="fas fa-plus"></i>
                </button>
              </div>
            </div>
          )}

          {/* Tabs */}
          <div className="product-tabs">
            <div className="tab-headers">
              <button 
                className={`tab-header ${activeTab === 'description' ? 'active' : ''}`}
                onClick={() => setActiveTab('description')}
              >
                {isUzbek ? "Tavsif" : "Описание"}
              </button>
              <button 
                className={`tab-header ${activeTab === 'specifications' ? 'active' : ''}`}
                onClick={() => setActiveTab('specifications')}
              >
                {isUzbek ? "Xususiyatlar" : "Характеристики"}
              </button>
              <button 
                className={`tab-header ${activeTab === 'reviews' ? 'active' : ''}`}
                onClick={() => setActiveTab('reviews')}
              >
                {isUzbek ? "Sharhlar" : "Отзывы"} ({product.reviewsCount})
              </button>
            </div>

            <div className="tab-content">
              {activeTab === 'description' && (
                <div className="description-content">
                  <p>{isUzbek ? product.descriptionUz : product.description}</p>
                </div>
              )}

              {activeTab === 'specifications' && (
                <div className="specifications-content">
                  {product.specifications && Object.entries(product.specifications).map(([key, value]) => (
                    <div key={key} className="spec-item">
                      <span className="spec-key">{key}:</span>
                      <span className="spec-value">{value}</span>
                    </div>
                  ))}
                </div>
              )}

              {activeTab === 'reviews' && (
                <div className="reviews-content">
                  {product.reviews && product.reviews.length > 0 ? (
                    product.reviews.map((review) => (
                      <div key={review.id} className="review-item">
                        <div className="review-header">
                          <span className="reviewer-name">{review.userName}</span>
                          <div className="review-rating">
                            {[...Array(5)].map((_, i) => (
                              <i 
                                key={i} 
                                className={`fas fa-star ${i < review.rating ? 'filled' : ''}`}
                              ></i>
                            ))}
                          </div>
                          <span className="review-date">{review.date}</span>
                        </div>
                        <p className="review-comment">{review.comment}</p>
                      </div>
                    ))
                  ) : (
                    <p>{isUzbek ? "Hali sharhlar yo'q" : "Пока нет отзывов"}</p>
                  )}
                </div>
              )}
            </div>
          </div>
        </div>
      </main>

      {/* Footer */}
      <footer className="detail-footer">
        <div className="footer-actions">
          <button 
            className="btn-wishlist"
            onClick={handleWishlistToggle}
          >
            <i className={isInWishlist ? "fas fa-heart" : "far fa-heart"}></i>
          </button>
          <button 
            className="btn btn-primary add-to-cart-btn"
            onClick={handleAddToCart}
            disabled={!product.inStock}
          >
            <i className="fas fa-shopping-cart"></i>
            {product.inStock ? t('add_to_cart') : 
              (isUzbek ? "Tugagan" : "Нет в наличии")
            }
          </button>
        </div>
      </footer>
    </div>
  );
}
