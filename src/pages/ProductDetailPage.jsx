import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { allProducts } from '../data/products';
import { useToast } from '../components/Toast';
import { useCart } from '../contexts/CartContext';
import { useWishlist } from '../contexts/WishlistContext';
import Meta from '../components/Meta';

const ProductDetailPage = () => {
    const { t } = useTranslation();
    const { id } = useParams();
    const navigate = useNavigate();
    const { showToast } = useToast();
    const { addToCart } = useCart();
    const { toggleWishlist, isItemInWishlist } = useWishlist();

    const product = allProducts.find(p => p.id === parseInt(id));
    const isLiked = product && isItemInWishlist(product.id);

    const handleGoBack = () => {
        if (window.history.length > 2) {
            navigate(-1);
        } else {
            navigate('/');
        }
    };

    const handleShare = () => {
        navigator.clipboard.writeText(window.location.href);
        showToast(t('link_copied'), 'info');
    };

    const handleLike = () => {
        if (!product) return;
        toggleWishlist(product);
    };

    if (!product) {
        return (
            <div className="product-detail-container">
                <Meta title={t('product_not_found')} />
                <div className="error">{t('product_not_found')}</div>
            </div>
        );
    }

    return (
        <div className="product-detail-page">
            <Meta 
                title={`${product.name} - ${t('app_name')}`}
                description={product.description}
            />
            <header className="detail-header">
                <button onClick={handleGoBack} className="header-btn">
                    <i className="fas fa-arrow-left"></i>
                </button>
                <div className="header-actions">
                    <button onClick={handleShare} className="header-btn">
                        <i className="fas fa-share-alt"></i>
                    </button>
                    <button onClick={handleLike} className={`header-btn ${isLiked ? 'liked' : ''}`}>
                        <i className={isLiked ? "fas fa-heart" : "far fa-heart"}></i>
                    </button>
                </div>
            </header>

            <main className="detail-content">
                <div className="product-image-gallery">
                    <img src={product.image} alt={product.name} className="main-product-image" />
                </div>
                <div className="product-info-section">
                    <h1 className="product-detail-title">{t(product.name)}</h1>
                    <div className="product-detail-price">
                        <span className="current-price">{product.price.toLocaleString('uz-UZ')} UZS</span>
                        {product.originalPrice && (
                            <span className="original-price">{product.originalPrice.toLocaleString('uz-UZ')} UZS</span>
                        )}
                    </div>
                    <div className="product-description">
                        <h3 className="description-title">{t('product_description')}</h3>
                        <p>{t(product.description)}</p>
                    </div>
                </div>
            </main>

            <footer className="detail-footer">
                <button className="btn btn-primary btn-block add-to-cart-btn" onClick={() => addToCart(product)}>
                    <i className="fas fa-shopping-cart"></i> {t('add_to_cart')}
                </button>
            </footer>
        </div>
    );
};

export default ProductDetailPage;