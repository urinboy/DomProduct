'use client';

import { useTranslation } from 'react-i18next';
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { useCart } from '../contexts/CartContext';

/* Avvalgi loyihadagi pastki navigation componentini to'liq takrorlash */
const BottomNavigation = () => {
  const { t } = useTranslation();
  const { getCartItemsCount } = useCart();
  const pathname = usePathname();

  // Savatcha ichidagi mahsulotlar sonini olish
  const totalCartItems = getCartItemsCount();

  // Mahsulot detail sahifasida navigation ko'rsatmaslik
  const isDetailPage = pathname?.startsWith('/products/') && pathname.split('/').length === 3;
  
  if (isDetailPage) {
    return null;
  }

  return (
    <nav className="bottom-nav">
      {/* Bosh sahifa */}
      <Link href="/" className={`nav-item ${pathname === '/' ? 'active' : ''}`}>
        <i className="fas fa-home nav-icon"></i>
        <span className="nav-text">{t('home')}</span>
      </Link>

      {/* Mahsulotlar sahifasi */}
      <Link href="/products" className={`nav-item ${pathname === '/products' ? 'active' : ''}`}>
        <i className="fas fa-cubes nav-icon"></i>
        <span className="nav-text">{t('products')}</span>
      </Link>

      {/* Sevimlilar sahifasi */}
      <Link href="/wishlist" className={`nav-item ${pathname === '/wishlist' ? 'active' : ''}`}>
        <i className="fas fa-heart nav-icon"></i>
        <span className="nav-text">{t('wishlist')}</span>
      </Link>

      {/* Savatcha sahifasi */}
      <Link href="/cart" className={`nav-item cart-nav-item ${pathname === '/cart' ? 'active' : ''}`}>
        <i className="fas fa-shopping-cart nav-icon"></i>
        {totalCartItems > 0 && (
          <span className="badge" id="cartBadge">{totalCartItems}</span>
        )}
        <span className="nav-text">{t('cart')}</span>
      </Link>

      {/* Profil sahifasi */}
      <Link href="/profile" className={`nav-item ${pathname === '/profile' ? 'active' : ''}`}>
        <i className="fas fa-user nav-icon"></i>
        <span className="nav-text">{t('profile')}</span>
      </Link>
    </nav>
  );
};

export default BottomNavigation;
