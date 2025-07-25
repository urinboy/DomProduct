
import React, { useState, useEffect } from 'react';
import { Routes, Route, NavLink, useNavigate, useLocation } from 'react-router-dom';
import { useTranslation } from 'react-i18next'; // Import useTranslation
import HomePage from './pages/HomePage';
import ProductsPage from './pages/ProductsPage';
import CartPage from './pages/CartPage';
import OrdersPage from './pages/OrdersPage';
import ProfilePage from './pages/ProfilePage';
import WishlistPage from './pages/WishlistPage';
// import LoginModalContent from './components/LoginModal';
import { useLoading } from './components/Loading';
import { useCart } from './contexts/CartContext';
import ProductDetailPage from './pages/ProductDetailPage';
import SearchResultsPage from './pages/SearchResultsPage';
import SearchOverlay from './components/SearchOverlay';
import SplashScreen from './components/SplashScreen';
import LanguageSwitcher from './components/LanguageSwitcher'; // Import LanguageSwitcher

import CategoriesPage from './pages/CategoriesPage'; // Import CategoriesPage

function App() {
    const { t } = useTranslation(); // Initialize translation hook
    const navigate = useNavigate();
    const location = useLocation();
    const { showLoading, hideLoading } = useLoading();
    const { cartItems } = useCart();
    const [isSearchOpen, setIsSearchOpen] = useState(false);
    const [isAppLoading, setIsAppLoading] = useState(true);

    useEffect(() => {
        const timer = setTimeout(() => setIsAppLoading(false), 2000);
        return () => clearTimeout(timer);
    }, []);

    useEffect(() => {
        if (!isAppLoading) {
            showLoading();
        }
    }, [location.pathname, showLoading, isAppLoading]);

    useEffect(() => {
        if (!isAppLoading) {
            hideLoading();
        }
    }, [location.pathname, hideLoading, isAppLoading]);

    const handleSearch = (query) => {
        setIsSearchOpen(false);
        navigate(`/search?q=${encodeURIComponent(query)}`);
    };

    const totalCartItems = cartItems.reduce((sum, item) => sum + item.quantity, 0);

    if (isAppLoading) {
        return <SplashScreen />;
    }

    const isDetailPage = location.pathname.startsWith('/products/') && location.pathname.split('/').length === 3;

    return (
        <div className="app-container" id="app">
            {isSearchOpen && <SearchOverlay onClose={() => setIsSearchOpen(false)} onSearch={handleSearch} />}

            {!isDetailPage && (
                <header className="header">
                    <div className="header-content">
                        <h1 className="logo">
                            <img src="/logos/white.png" alt="White Logo" className='logo-image'/>
                            {/* <i className="fas fa-shopping-cart"></i> Dom Product 🛍️ */}
                        </h1>
                        <div className="search-container desktop-search">
                            <i className="fas fa-search search-icon"></i>
                            <input 
                                type="text" 
                                className="search-input" 
                                placeholder={t('search_placeholder')} 
                                onKeyDown={(e) => e.key === 'Enter' && handleSearch(e.target.value)} 
                            />
                        </div>
                        <div className="header-actions">
                            <button className="icon-btn mobile-search-btn" onClick={() => setIsSearchOpen(true)}>
                                <i className="fas fa-search"></i>
                            </button>
                            <LanguageSwitcher /> 
                        </div>
                    </div>
                </header>
            )}

            <main className={`main-content ${isDetailPage ? 'detail-page-content' : ''}`}>
                <Routes>
                    <Route path="/" element={<HomePage />} />
                    <Route path="/products" element={<ProductsPage />} />
                    <Route path="/products/:id" element={<ProductDetailPage />} />
                    <Route path="/cart" element={<CartPage />} />
                    <Route path="/orders" element={<OrdersPage />} />
                    <Route path="/wishlist" element={<WishlistPage />} />
                    <Route path="/profile" element={<ProfilePage />} />
                    <Route path="/search" element={<SearchResultsPage />} />
                    <Route path="/categories" element={<CategoriesPage />} />
                </Routes>
            </main>

            {!isDetailPage && (
                <nav className="bottom-nav">
                    <NavLink to="/" className="nav-item">
                        <i className="fas fa-home nav-icon"></i>
                        <span className="nav-text">{t('home')}</span>
                    </NavLink>
                    <NavLink to="/products" className="nav-item">
                        <i className="fas fa-cubes nav-icon"></i>
                        <span className="nav-text">{t('products')}</span>
                    </NavLink>
                    <NavLink to="/wishlist" className="nav-item">
                        <i className="fas fa-heart nav-icon"></i>
                        <span className="nav-text">{t('wishlist')}</span>
                    </NavLink>
                    <NavLink to="/cart" className="nav-item cart-nav-item">
                        <i className="fas fa-shopping-cart nav-icon"></i>
                        {totalCartItems > 0 && <span className="badge" id="cartBadge">{totalCartItems}</span>}
                        <span className="nav-text">{t('cart')}</span>
                    </NavLink>
                    <NavLink to="/profile" className="nav-item">
                        <i className="fas fa-user nav-icon"></i>
                        <span className="nav-text">{t('profile')}</span>
                    </NavLink>
                </nav>
            )}
        </div>
    );
}

export default App;
