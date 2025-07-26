'use client';

import { AuthProvider } from '../contexts/AuthContext';
import { CartProvider } from '../contexts/CartContext';
import { WishlistProvider } from '../contexts/WishlistContext';
import { ToastProvider } from '../components/Toast/ToastProvider';
import AppLayout from '../components/AppLayout';
import '../lib/i18n';

/* Barcha provider va layout komponentlarini birlashtirish */
export default function Providers({ children }: { children: React.ReactNode }) {
  return (
    <ToastProvider>
      <AuthProvider>
        <CartProvider>
          <WishlistProvider>
            <AppLayout>
              {children}
            </AppLayout>
          </WishlistProvider>
        </CartProvider>
      </AuthProvider>
    </ToastProvider>
  );
}
