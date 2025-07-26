'use client';

import { useState } from 'react';
import { useTranslation } from 'react-i18next';
import Link from 'next/link';
import { useCart } from '../../src/contexts/CartContext';
import { useToast } from '../../src/components/Toast/ToastProvider';
import './cart.css';

/* Savat sahifasi - avvalgi loyihadagi dizaynni takrorlash */
const CartPage = () => {
  const { t, i18n } = useTranslation();
  const { items, updateQuantity, removeFromCart, clearCart, getCartTotal } = useCart();
  const { showSuccess, showError, showInfo } = useToast();
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [itemToRemove, setItemToRemove] = useState<number | null>(null);
  
  const isUzbek = i18n.language === 'uz';
  const subtotal = getCartTotal();
  const shipping = subtotal > 0 ? 25000 : 0;
  const total = subtotal + shipping;

  const handleRemoveItem = (productId: number) => {
    setItemToRemove(productId);
    setShowConfirmModal(true);
  };

  const confirmRemoveItem = () => {
    if (itemToRemove) {
      const item = items.find((item: any) => item.id === itemToRemove);
      removeFromCart(itemToRemove);
      if (item) {
        showSuccess(isUzbek ? 
          `${item.name} savatdan o'chirildi` : 
          `${item.name} удален из корзины`
        );
      }
      setItemToRemove(null);
    }
    setShowConfirmModal(false);
  };

  const handleQuantityChange = (productId: number, newQuantity: number) => {
    if (newQuantity <= 0) {
      handleRemoveItem(productId);
    } else {
      updateQuantity(productId, newQuantity);
      const item = items.find((item: any) => item.id === productId);
      if (item) {
        showInfo(isUzbek ? 
          `${item.name} soni yangilandi: ${newQuantity}` : 
          `Количество ${item.name} обновлено: ${newQuantity}`
        );
      }
    }
  };

  const handleClearCart = () => {
    clearCart();
    showInfo(isUzbek ? 'Savat tozalandi' : 'Корзина очищена');
  };

  const handleOrderSubmit = () => {
    showSuccess(isUzbek ? 
      'Buyurtmangiz qabul qilindi! Tez orada aloqaga chiqamiz.' : 
      'Ваш заказ принят! Скоро с вами свяжемся.'
    );
    // Buyurtma berish logikasi
    setTimeout(() => {
      clearCart();
    }, 2000);
  };

  if (items.length === 0) {
    return (
      <div style={{ 
        minHeight: '70vh', 
        display: 'flex', 
        alignItems: 'center', 
        justifyContent: 'center',
        padding: '2rem',
        background: '#f8f9fa'
      }}>
        <div style={{ 
          textAlign: 'center', 
          padding: '3rem',
          background: 'white',
          borderRadius: '20px',
          boxShadow: '0 10px 30px rgba(0,0,0,0.1)',
          maxWidth: '500px'
        }}>
          <div style={{ 
            fontSize: '4rem', 
            marginBottom: '1rem',
            color: '#ddd'
          }}>
            🛒
          </div>
          <h2 style={{ 
            color: '#666', 
            marginBottom: '1rem',
            fontSize: '1.8rem',
            fontWeight: '600'
          }}>
            {isUzbek ? "Savat bo'sh" : "Корзина пуста"}
          </h2>
          <p style={{ 
            color: '#999', 
            marginBottom: '2rem',
            fontSize: '1.1rem',
            lineHeight: '1.5'
          }}>
            {isUzbek ? 
              "Hozircha hech qanday mahsulot qo'shilmagan" : 
              "Пока не добавлено ни одного товара"
            }
          </p>
          <Link 
            href="/products" 
            style={{
              background: 'linear-gradient(135deg, #087c36, #0a9d44)',
              color: 'white',
              padding: '1rem 2rem',
              borderRadius: '25px',
              textDecoration: 'none',
              fontSize: '1rem',
              fontWeight: '600',
              display: 'inline-block',
              boxShadow: '0 4px 15px rgba(8, 124, 54, 0.3)',
              transition: 'all 0.3s ease'
            }}
            onMouseOver={(e) => {
              e.currentTarget.style.transform = 'translateY(-2px)';
              e.currentTarget.style.boxShadow = '0 6px 20px rgba(8, 124, 54, 0.4)';
            }}
            onMouseOut={(e) => {
              e.currentTarget.style.transform = 'translateY(0)';
              e.currentTarget.style.boxShadow = '0 4px 15px rgba(8, 124, 54, 0.3)';
            }}
          >
            {isUzbek ? "Xarid qilishni boshlash" : "Начать покупки"}
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div id="cartPage">
      <div className="cart-header">
        <h1>{isUzbek ? "Savat" : "Корзина"}</h1>
        <button 
          onClick={handleClearCart}
          style={{
            background: 'linear-gradient(135deg, #dc3545, #c82333)',
            color: 'white',
            border: 'none',
            padding: '0.75rem 1.5rem',
            borderRadius: '25px',
            fontSize: '0.9rem',
            fontWeight: '600',
            cursor: 'pointer',
            transition: 'all 0.3s ease',
            boxShadow: '0 4px 15px rgba(220, 53, 69, 0.3)'
          }}
          onMouseOver={(e) => {
            e.currentTarget.style.transform = 'translateY(-2px)';
            e.currentTarget.style.boxShadow = '0 6px 20px rgba(220, 53, 69, 0.4)';
          }}
          onMouseOut={(e) => {
            e.currentTarget.style.transform = 'translateY(0)';
            e.currentTarget.style.boxShadow = '0 4px 15px rgba(220, 53, 69, 0.3)';
          }}
        >
          {isUzbek ? "Barchasini o'chirish" : "Очистить все"}
        </button>
      </div>

      <div className="cart-content">
        <div className="cart-items">
          {items.map((item: any) => (
            <div key={item.id} className="cart-item">
              <div className="item-image">
                <img src={item.image} alt={item.name} />
              </div>
              
              <div className="item-details">
                <h3>{item.name}</h3>
                <p className="item-price">
                  {item.price.toLocaleString()} {isUzbek ? "so'm" : "сум"}
                </p>
              </div>
              
              <div className="item-controls">
                <div className="quantity-controls">
                  <button 
                    onClick={() => handleQuantityChange(item.id, item.quantity - 1)}
                    className="quantity-btn"
                  >
                    −
                  </button>
                  <span className="quantity">{item.quantity}</span>
                  <button 
                    onClick={() => handleQuantityChange(item.id, item.quantity + 1)}
                    className="quantity-btn"
                  >
                    +
                  </button>
                </div>
                
                <button 
                  onClick={() => handleRemoveItem(item.id)}
                  className="remove-btn"
                  title={isUzbek ? "O'chirish" : "Удалить"}
                >
                  🗑️
                </button>
              </div>
              
              <div className="item-total">
                {(item.price * item.quantity).toLocaleString()} {isUzbek ? "so'm" : "сум"}
              </div>
            </div>
          ))}
        </div>

        <div className="cart-summary">
          <div className="summary-card">
            <h3>{isUzbek ? "Buyurtma ma'lumotlari" : "Информация о заказе"}</h3>
            
            <div className="summary-row">
              <span>{isUzbek ? "Mahsulotlar narxi:" : "Стоимость товаров:"}</span>
              <span>{subtotal.toLocaleString()} {isUzbek ? "so'm" : "сум"}</span>
            </div>
            
            <div className="summary-row">
              <span>{isUzbek ? "Yetkazib berish:" : "Доставка:"}</span>
              <span>
                {shipping > 0 ? 
                  `${shipping.toLocaleString()} ${isUzbek ? "so'm" : "сум"}` :
                  (isUzbek ? "Bepul" : "Бесплатно")
                }
              </span>
            </div>
            
            <hr />
            
            <div className="summary-row total">
              <span>{isUzbek ? "Jami:" : "Итого:"}</span>
              <span>{total.toLocaleString()} {isUzbek ? "so'm" : "сум"}</span>
            </div>
            
            <button 
              className="btn btn-primary btn-full"
              onClick={handleOrderSubmit}
            >
              {isUzbek ? "Buyurtma berish" : "Оформить заказ"}
            </button>
            
            <Link href="/products" className="btn btn-outline btn-full">
              {isUzbek ? "Xaridni davom ettirish" : "Продолжить покупки"}
            </Link>
          </div>
        </div>
      </div>

      {/* Tasdiqlash modal oynasi */}
      {showConfirmModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <div className="modal-header">
              <h4>{isUzbek ? "Tasdiqlash" : "Подтверждение"}</h4>
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
