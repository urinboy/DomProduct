'use client';

import { useState } from 'react';
import { useTranslation } from 'react-i18next';
import Link from 'next/link';
import { useCart } from '../../src/contexts/CartContext';
import './cart.css';

/* Savat sahifasi - avvalgi loyihadagi dizaynni takrorlash */
const CartPage = () => {
  const { t, i18n } = useTranslation();
  const { items, updateQuantity, removeFromCart, clearCart, getTotalPrice } = useCart();
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [itemToRemove, setItemToRemove] = useState<number | null>(null);
  
  const isUzbek = i18n.language === 'uz';

  const subtotal = getTotalPrice();
  const shipping = subtotal > 0 ? 25000 : 0;
  const total = subtotal + shipping;

  const handleRemoveItem = (productId: number) => {
    setItemToRemove(productId);
    setShowConfirmModal(true);
  };

  const confirmRemoveItem = () => {
    if (itemToRemove) {
      removeFromCart(itemToRemove);
      setItemToRemove(null);
    }
    setShowConfirmModal(false);
  };

  const handleQuantityChange = (productId: number, newQuantity: number) => {
    if (newQuantity <= 0) {
      handleRemoveItem(productId);
    } else {
      updateQuantity(productId, newQuantity);
    }
  };

  const handleClearCart = () => {
    clearCart();
  };

  if (items.length === 0) {
    return (
      <div id="cartPage">
        <div className="cart-empty">
          <i className="fas fa-shopping-cart cart-empty-icon"></i>
          <div className="cart-empty-title">
            {isUzbek ? "Savatchangiz bo'sh" : "Ваша корзина пуста"}
          </div>
          <p className="cart-empty-message">
            {isUzbek ? 
              "Mahsulotlarni ko'rib chiqing va yoqgan narsalaringizni savatga qo'shing" : 
              "Просмотрите товары и добавьте понравившиеся в корзину"
            }
          </p>
          <Link href="/products" className="btn btn-primary">
            {t('products')}
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div id="cartPage">
      {/* Sahifa header */}
      <div className="cart-header">
        <h2>{t('cart')} ({items.length})</h2>
        <button 
          onClick={handleClearCart} 
          className="clear-cart-btn"
          title={isUzbek ? "Savatni tozalash" : "Очистить корзину"}
        >
          <i className="fas fa-trash"></i>
          {isUzbek ? "Barchasini o'chirish" : "Очистить все"}
        </button>
      </div>

      <div className="cart-content">
        {/* Savat elementlari */}
        <div className="cart-items-section">
          <div id="cartItems">
            {items.map((item: any) => (
              <div className="cart-item" key={item.id}>
                {/* Mahsulot rasmi */}
                <div className="cart-item-image">
                  <Link href={`/products/${item.id}`}>
                    <img 
                      src={item.images?.[0] || "/api/placeholder/100/100"} 
                      alt={isUzbek ? item.nameUz : item.name}
                    />
                  </Link>
                </div>

                {/* Mahsulot ma'lumotlari */}
                <div className="cart-item-info">
                  <Link href={`/products/${item.id}`} className="cart-item-title">
                    {isUzbek ? item.nameUz : item.name}
                  </Link>
                  
                  <div className="cart-item-price">
                    <span className="current-price">
                      {item.price.toLocaleString()} so'm
                    </span>
                    {item.originalPrice && (
                      <span className="original-price">
                        {item.originalPrice.toLocaleString()} so'm
                      </span>
                    )}
                  </div>

                  {/* Miqdor boshqaruvi */}
                  <div className="quantity-controls">
                    <button 
                      className="quantity-btn"
                      onClick={() => handleQuantityChange(item.id, item.quantity - 1)}
                      disabled={item.quantity <= 1}
                    >
                      <i className="fas fa-minus"></i>
                    </button>
                    <span className="quantity-display">{item.quantity}</span>
                    <button 
                      className="quantity-btn"
                      onClick={() => handleQuantityChange(item.id, item.quantity + 1)}
                    >
                      <i className="fas fa-plus"></i>
                    </button>
                  </div>

                  {/* Jami narx */}
                  <div className="cart-item-total">
                    {(item.price * item.quantity).toLocaleString()} so'm
                  </div>
                </div>

                {/* O'chirish tugmasi */}
                <button 
                  className="remove-item-btn"
                  onClick={() => handleRemoveItem(item.id)}
                  title={isUzbek ? "Savatdan o'chirish" : "Удалить из корзины"}
                >
                  <i className="fas fa-times"></i>
                </button>
              </div>
            ))}
          </div>
        </div>

        {/* Buyurtma xulosasi */}
        <div className="cart-summary-section">
          <div id="cartSummary" className="cart-summary">
            <h3 className="summary-title">
              {isUzbek ? "Buyurtma xulosasi" : "Итого заказа"}
            </h3>
            
            <div className="summary-details">
              <div className="summary-row">
                <span className="summary-label">
                  {isUzbek ? "Mahsulotlar narxi:" : "Стоимость товаров:"}
                </span>
                <span className="summary-value">
                  {subtotal.toLocaleString()} so'm
                </span>
              </div>
              
              <div className="summary-row">
                <span className="summary-label">
                  {isUzbek ? "Yetkazib berish:" : "Доставка:"}
                </span>
                <span className="summary-value">
                  {shipping === 0 ? 
                    (isUzbek ? "Bepul" : "Бесплатно") : 
                    `${shipping.toLocaleString()} so'm`
                  }
                </span>
              </div>
              
              <div className="summary-divider"></div>
              
              <div className="summary-row summary-total-row">
                <span className="summary-label">
                  {isUzbek ? "Jami:" : "Итого:"}
                </span>
                <span className="summary-total-value">
                  {total.toLocaleString()} so'm
                </span>
              </div>
            </div>

            {/* Buyurtma berish tugmasi */}
            <button className="btn btn-primary btn-block checkout-btn">
              <i className="fas fa-credit-card"></i>
              {isUzbek ? "Buyurtma berish" : "Оформить заказ"}
            </button>

            {/* Xaridni davom ettirish */}
            <Link href="/products" className="continue-shopping">
              <i className="fas fa-arrow-left"></i>
              {isUzbek ? "Xaridni davom ettirish" : "Продолжить покупки"}
            </Link>
          </div>
        </div>
      </div>

      {/* Tasdiqlash modali */}
      {showConfirmModal && (
        <div className="modal-overlay" onClick={() => setShowConfirmModal(false)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3>{isUzbek ? "Tasdiqlash" : "Подтверждение"}</h3>
              <button 
                className="modal-close"
                onClick={() => setShowConfirmModal(false)}
              >
                <i className="fas fa-times"></i>
              </button>
            </div>
            <div className="modal-body">
              <p>
                {isUzbek ? 
                  "Rostdan ham bu mahsulotni savatdan o'chirmoqchimisiz?" : 
                  "Вы действительно хотите удалить этот товар из корзины?"
                }
              </p>
            </div>
            <div className="modal-actions">
              <button 
                className="btn btn-secondary"
                onClick={() => setShowConfirmModal(false)}
              >
                {isUzbek ? "Bekor qilish" : "Отмена"}
              </button>
              <button 
                className="btn btn-danger"
                onClick={confirmRemoveItem}
              >
                {isUzbek ? "O'chirish" : "Удалить"}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default CartPage;
