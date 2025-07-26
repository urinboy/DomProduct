'use client';

import { AuthProvider } from '../src/contexts/AuthContext';
import { CartProvider } from '../src/contexts/CartContext';
import { WishlistProvider } from '../src/contexts/WishlistContext';
import AppLayout from '../src/components/AppLayout';
import '../src/lib/i18n';

/* Barcha provider va layout komponentlarini birlashtirish */
export default function Providers({ children }: { children: React.ReactNode }) {
  return (
    <AuthProvider>
      <CartProvider>
        <WishlistProvider>
          <AppLayout>
            {children}
          </AppLayout>
        </WishlistProvider>
      </CartProvider>
    </AuthProvider>
  );
}
