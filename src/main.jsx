import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.jsx';
import './i18n'; // Import i18n configuration

// Global CSS stillarini import qilish
import './assets/css/variables.css';
import './assets/css/base.css';
import './assets/css/components.css';
import './assets/css/pages.css';
import './assets/css/responsive.css';
import './assets/css/slider.css'; // Import slider styles

import { BrowserRouter } from 'react-router-dom';
import { ToastProvider } from './components/Toast';
import { LoadingProvider } from './components/Loading';
import { ModalProvider } from './contexts/ModalContext';
import { AuthProvider } from './contexts/AuthContext'; // Import AuthProvider
import { CartProvider } from './contexts/CartContext';
import { WishlistProvider } from './contexts/WishlistContext'; // Import WishlistProvider
import ScrollToTop from './components/ScrollToTop';

ReactDOM.createRoot(document.getElementById('root')).render(
    <BrowserRouter>
        <AuthProvider>
            <ToastProvider>
                <CartProvider>
                    <WishlistProvider>
                        <LoadingProvider>
                            <ModalProvider>
                                <ScrollToTop />
                                <App />
                            </ModalProvider>
                        </LoadingProvider>
                    </WishlistProvider>
                </CartProvider>
            </ToastProvider>
        </AuthProvider>
    </BrowserRouter>
);
