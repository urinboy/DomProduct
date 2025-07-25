import React, { useState, useMemo } from 'react';
import { useSearchParams, Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { allProducts } from '../data/products';
import { categories } from '../data/categories'; // Import categories for translation
import { useCart } from '../contexts/CartContext';
import { useWishlist } from '../contexts/WishlistContext';
import Meta from '../components/Meta';

const ProductsPage = () => {
    const { t } = useTranslation();
    const [searchParams] = useSearchParams();
    const selectedCategorySlug = searchParams.get('category');
    const { addToCart } = useCart();
    const { toggleWishlist, isItemInWishlist } = useWishlist();

    const [searchTerm, setSearchTerm] = useState('');
    const [sortOrder, setSortOrder] = useState('');

    const displayedProducts = useMemo(() => {
        let products = selectedCategorySlug
            ? allProducts.filter(p => p.category === selectedCategorySlug)
            : allProducts;

        if (searchTerm) {
            products = products.filter(p =>
                p.name.toLowerCase().includes(searchTerm.toLowerCase())
            );
        }

        if (sortOrder) {
            products = [...products].sort((a, b) => {
                switch (sortOrder) {
                    case 'price-asc': return a.price - b.price;
                    case 'price-desc': return b.price - a.price;
                    case 'name-asc': return a.name.localeCompare(b.name);
                    default: return 0;
                }
            });
        }

        return products;
    }, [selectedCategorySlug, searchTerm, sortOrder]);

    const getCategoryName = (slug) => {
        const category = categories.find(cat => cat.slug === slug);
        return category ? t(`category_${category.slug}`) : t('all_products');
    };

    return (
        <div id="productsPage">
            <Meta title={getCategoryName(selectedCategorySlug)} />
            <h2 style={{ marginBottom: '1rem' }}>
                {getCategoryName(selectedCategorySlug)}
            </h2>

            <div className="filters-container">
                <div className="search-form-group">
                    <i className="fas fa-search"></i>
                    <input
                        type="text"
                        placeholder={t('search_products_placeholder')}
                        className="filter-input"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </div>
                <div className="search-form-group">
                    <i className="fas fa-sort-amount-down"></i>
                    <select
                        className="filter-select"
                        value={sortOrder}
                        onChange={(e) => setSortOrder(e.target.value)}
                    >
                        <option value="">{t('sort_by')}</option>
                        <option value="price-asc">{t('price_asc')}</option>
                        <option value="price-desc">{t('price_desc')}</option>
                        <option value="name-asc">{t('name_asc')}</option>
                    </select>
                </div>
            </div>

            {displayedProducts.length > 0 ? (
                <div className="product-grid" id="productsGrid">
                    {displayedProducts.map(product => (
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
            ) : (
                <div className="cart-empty" style={{ padding: '2rem' }}>
                    <i className="fas fa-box-open cart-empty-icon"></i>
                    <div className="cart-empty-title">{t('no_products_found_title')}</div>
                    <p className="cart-empty-message">
                        {t('no_products_found_message')}
                    </p>
                </div>
            )}
        </div>
    );
};

export default ProductsPage;