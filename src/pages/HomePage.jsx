import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { allProducts } from '../data/products';
import { categories } from '../data/categories'; // Import categories
import ImageSlider from '../components/ImageSlider';
import { useCart } from '../contexts/CartContext';
import { useWishlist } from '../contexts/WishlistContext';
import Meta from '../components/Meta';

const HomePage = () => {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const { addToCart } = useCart();
    const { toggleWishlist, isItemInWishlist } = useWishlist();

    // Show a limited number of categories on the home page
    const featuredCategories = categories.slice(0, 5);
    const featuredProducts = allProducts.slice(0, 4);

    const handleCategoryClick = (categorySlug) => {
        navigate(`/products?category=${categorySlug}`);
    };

    return (
        <div id="homePage">
            <Meta />
            <ImageSlider />
            <div className="section-header">
                <h2>{t('categories')}</h2>
                <Link to="/categories" className="see-all-link">{t('see_all')}</Link>
            </div>
            <div className="category-grid" id="categoriesGrid">
                {featuredCategories.map(category => (
                    <div 
                        className="category-card" 
                        key={category.id}
                        onClick={() => handleCategoryClick(category.slug)}
                        style={{ cursor: 'pointer' }}
                    >
                        <i className={`${category.icon} category-icon`}></i> 
                        <span>{t(`category_${category.slug}`)}</span>
                    </div>
                ))}
            </div>
            <div className="section-header">
                <h2>{t('new_products')}</h2>
                <Link to="/products" className="see-all-link">{t('see_all')}</Link>
            </div>
            <div className="product-grid" id="featuredProducts">
                {featuredProducts.map(product => (
                    <div className="product-card" key={product.id}>
                        <Link to={`/products/${product.id}`} className="product-image-link">
                            <div className="product-image">
                                <img src={product.image} alt={product.name} />
                            </div>
                        </Link>
                        <div className="product-info">
                            <div className="product-title">{t(product.name)}</div>
                            <div className="product-price">
                                <span className="current-price">{product.price.toLocaleString('uz-UZ')} UZS</span>
                            </div>
                            <div className="product-card-actions">
                                <Link to={`/products/${product.id}`} className="btn btn-primary btn-sm">{t('details')}</Link>
                                <button className="btn-icon" onClick={() => addToCart(product)}>
                                    <i className="fas fa-shopping-cart"></i>
                                </button>
                                <button 
                                    className={`btn-icon ${isItemInWishlist(product.id) ? 'active' : ''}`}
                                    onClick={() => toggleWishlist(product)}
                                >
                                    <i className={`${isItemInWishlist(product.id) ? 'fas' : 'far'} fa-heart`}></i>
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
