
import React from 'react';
import { useTranslation } from 'react-i18next';
import { useCart } from '../contexts/CartContext';
import { useModal } from '../contexts/ModalContext';
import ConfirmModal from '../components/ConfirmModal';
import { Link } from 'react-router-dom';
import Meta from '../components/Meta';

const CartPage = () => {
    const { t } = useTranslation();
    const { cartItems, incrementQuantity, decrementQuantity, removeFromCart } = useCart();
    const { openModal, closeModal } = useModal();

    const subtotal = cartItems.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    const shipping = subtotal > 0 ? 25000 : 0;
    const total = subtotal + shipping;

    const handleDelete = (productId) => {
        openModal(
            <ConfirmModal 
                title={t('confirm_remove_title')}
                message={t('confirm_remove_message')}
                onConfirm={() => {
                    removeFromCart(productId);
                    closeModal();
                }}
                onCancel={closeModal}
            />
        );
    };

    return (
        <div id="cartPage">
            <Meta title={t('shopping_cart')} />
            <h2 style={{ marginBottom: '1rem' }}>{t('shopping_cart')}</h2>
            {cartItems.length === 0 ? (
                <div className="cart-empty">
                    <i className="fas fa-shopping-cart cart-empty-icon"></i>
                    <div className="cart-empty-title">{t('cart_empty_title')}</div>
                    <p className="cart-empty-message">{t('cart_empty_message')}</p>
                    <Link to="/products" className="btn btn-primary">{t('go_to_products')}</Link>
                </div>
            ) : (
                <>
                    <div id="cartItems">
                        {cartItems.map(item => (
                            <div className="cart-item" key={item.id}>
                                <div className="cart-item-image">
                                    <img src={item.image} alt={item.name} />
                                </div>
                                <div className="cart-item-info">
                                    <div className="cart-item-title">{t(item.name)}</div>
                                    <div className="cart-item-price">{item.price.toLocaleString('uz-UZ')} UZS</div>
                                    <div className="quantity-controls">
                                        <button className="quantity-btn" onClick={() => decrementQuantity(item.id)}>-</button>
                                        <span className="quantity-display">{item.quantity}</span>
                                        <button className="quantity-btn" onClick={() => incrementQuantity(item.id)}>+</button>
                                    </div>
                                </div>
                                <button className="btn btn-danger" onClick={() => handleDelete(item.id)}>{t('remove')}</button>
                            </div>
                        ))}
                    </div>
                    <div id="cartSummary" className="cart-summary">
                        <h3 className="summary-title">{t('order_summary')}</h3>
                        <div className="summary-row">
                            <span className="summary-label">{t('subtotal')}:</span>
                            <span className="summary-value">{subtotal.toLocaleString('uz-UZ')} UZS</span>
                        </div>
                        <div className="summary-row">
                            <span className="summary-label">{t('shipping')}:</span>
                            <span className="summary-value">{shipping.toLocaleString('uz-UZ')} UZS</span>
                        </div>
                        <div className="summary-divider"></div>
                        <div className="summary-row summary-total-row">
                            <span className="summary-label">{t('total')}:</span>
                            <span className="summary-total-value">{total.toLocaleString('uz-UZ')} UZS</span>
                        </div>
                        <button className="btn btn-primary btn-block" style={{ marginTop: '1.5rem' }}>
                            <i className="fas fa-check-circle" style={{ marginRight: '0.5rem' }}></i>
                            {t('checkout')}
                        </button>
                    </div>
                </>
            )}
        </div>
    );
};

export default CartPage;
