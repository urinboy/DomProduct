

import React from 'react';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { useWishlist } from '../contexts/WishlistContext';
import { useCart } from '../contexts/CartContext';
import Meta from '../components/Meta';

const WishlistPage = () => {
    const { t } = useTranslation();
    const { wishlistItems, toggleWishlist } = useWishlist();
    const { addToCart } = useCart();

    return (
        <div id="wishlistPage">
            <Meta title={t('my_wishlist')} />
            <h2 style={{ marginBottom: '1rem' }}>{t('my_wishlist')}</h2>
            {wishlistItems.length === 0 ? (
                <div className="cart-empty">
                    <i className="fas fa-heart-broken cart-empty-icon"></i>
                    <div className="cart-empty-title">{t('wishlist_empty_title')}</div>
                    <p className="cart-empty-message">{t('wishlist_empty_message')}</p>
                    <Link to="/products" className="btn btn-primary">{t('go_to_products')}</Link>
                </div>
            ) : (
                <div className="product-grid">
                    {wishlistItems.map(product => (
                        <div key={product.id} className="product-card">
                             <div className="product-image">
                                <img src={product.image} alt={product.name} />
                                <button className="wishlist-btn-remove" onClick={() => toggleWishlist(product)}>
                                    <i className="fas fa-times"></i>
                                </button>
                            </div>
                            <div className="product-info">
                                <div className="product-title">{t(product.name)}</div>
                                <div className="product-price">
                                    <span className="current-price">{product.price.toLocaleString('uz-UZ')} UZS</span>
                                </div>
                                <button className="btn btn-primary btn-sm btn-block" onClick={() => addToCart(product)}>
                                    {t('add_to_cart')}
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default WishlistPage;

