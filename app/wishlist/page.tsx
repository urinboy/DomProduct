'use client';

import { useTranslation } from 'react-i18next';
import Link from 'next/link';
import { useWishlist } from '../../src/contexts/WishlistContext';
import { useCart } from '../../src/contexts/CartContext';
import { useToast } from '../../src/components/Toast/ToastProvider';
import './wishlist.css';

/* Sevimlilar sahifasi - zamonaviy dizayn */
const WishlistPage = () => {
  const { t, i18n } = useTranslation();
  const { items, removeFromWishlist, clearWishlist } = useWishlist();
  const { addToCart, items: cartItems } = useCart();
  const { showSuccess, showInfo, showWarning } = useToast();
  
  const isUzbek = i18n.language === 'uz';

  const handleAddToCart = (product: any) => {
    addToCart(product);
    showSuccess(
      isUzbek ? `${product.nameUz || product.name} savatga qo'shildi` : `${product.name} добавлен в корзину`
    );
  };

  const handleRemoveFromWishlist = (productId: number) => {
    const product = items.find((item: any) => item.id === productId);
    removeFromWishlist(productId);
    if (product) {
      showInfo(
        isUzbek ? `${product.nameUz || product.name} sevimlilardan olib tashlandi` : `${product.name} удален из избранного`
      );
    }
  };

  const handleClearWishlist = () => {
    if (items.length > 0) {
      clearWishlist();
      showWarning(
        isUzbek ? "Barcha sevimlilar tozalandi" : "Все избранное очищено"
      );
    }
  };

  const isInCart = (productId: number) => {
    return cartItems.some((item: any) => item.id === productId);
  };

  if (items.length === 0) {
    return (
      <div id="wishlistPage">
        <div className="empty-state">
          <i className="fas fa-heart"></i>
          <h2>
            {isUzbek ? "Sevimlilar ro'yxati bo'sh" : "Список избранного пуст"}
          </h2>
          <p>
            {isUzbek ? 
              "Yoqgan mahsulotlaringizni sevimlilar ro'yxatiga qo'shing" : 
              "Добавьте понравившиеся товары в избранное"
            }
          </p>
          <Link href="/products" className="btn">
            {isUzbek ? "Mahsulotlarni ko'rish" : "Посмотреть товары"}
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div id="wishlistPage">
      <div className="wishlist-header">
        <h1>
          {isUzbek ? "Sevimlilar" : "Избранное"}
        </h1>
        <button 
          className="clear-wishlist-btn"
          onClick={handleClearWishlist}
        >
          <i className="fas fa-trash"></i>
          {isUzbek ? "Barchasini o'chirish" : "Очистить все"}
        </button>
      </div>

      <div className="wishlist-grid">
        {items.map((product: any) => (
          <div key={product.id} className="wishlist-item">
            <div className="wishlist-item-image">
              <img 
                src={product.image} 
                alt={isUzbek ? (product.nameUz || product.name) : product.name}
              />
            </div>
            
            <div className="wishlist-item-info">
              <h3 className="wishlist-item-title">
                {isUzbek ? (product.nameUz || product.name) : product.name}
              </h3>
              <p className="wishlist-item-price">
                {product.price.toLocaleString()} {isUzbek ? "so'm" : "сум"}
              </p>
            </div>
            
            <div className="wishlist-item-actions">
              <button 
                className="btn btn-primary"
                onClick={() => handleAddToCart(product)}
                disabled={!product.inStock}
              >
                <i className="fas fa-shopping-cart"></i>
                {product.inStock ? 
                  (isUzbek ? "Savatga" : "В корзину") :
                  (isUzbek ? "Tugagan" : "Нет в наличии")
                }
              </button>
              
              <button 
                className="btn btn-danger"
                onClick={() => handleRemoveFromWishlist(product.id)}
                title={isUzbek ? "Sevimlilardan olib tashlash" : "Удалить из избранного"}
              >
                <i className="fas fa-trash"></i>
                {isUzbek ? "O'chirish" : "Удалить"}
              </button>
            </div>
          </div>
        ))}
      </div>

      <div style={{ textAlign: 'center', marginTop: '3rem', padding: '2rem' }}>
        <p style={{ 
          fontSize: '1.1rem', 
          color: '#666', 
          marginBottom: '1.5rem' 
        }}>
          {isUzbek ? "Boshqa mahsulotlarni ham ko'ring" : "Посмотрите другие товары"}
        </p>
        <Link href="/products" className="btn btn-primary" style={{ padding: '1rem 2rem' }}>
          {isUzbek ? "Mahsulotlarga o'tish" : "Перейти к товарам"}
        </Link>
      </div>
    </div>
  );
};

export default WishlistPage;
