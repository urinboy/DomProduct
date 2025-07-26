'use client';

import { useTranslation } from 'react-i18next';
import Link from 'next/link';
import { products, categories } from '../src/data/products';
import LanguageSwitcher from '../src/components/LanguageSwitcher';
import { useCart } from '../src/contexts/CartContext';
import { useWishlist } from '../src/contexts/WishlistContext';

export default function HomePage() {
  const { t, i18n } = useTranslation();
  const { addToCart, getCartItemsCount } = useCart();
  const { addToWishlist, isInWishlist } = useWishlist();
  
  const featuredProducts = products.slice(0, 4);
  const isUzbek = i18n.language === 'uz';

  const handleAddToCart = (product: any) => {
    addToCart(product);
    // Здесь можно добавить Toast уведомление
  };

  const handleToggleWishlist = (product: any) => {
    if (isInWishlist(product.id)) {
      // removeFromWishlist(product.id);
    } else {
      addToWishlist(product);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center">
              <h1 className="text-2xl font-bold text-gray-900">DomProduct</h1>
            </div>
            
            <nav className="hidden md:flex space-x-8">
              <Link href="/" className="text-blue-600 font-medium">{t('home')}</Link>
              <Link href="/products" className="text-gray-700 hover:text-blue-600">{t('products')}</Link>
              <Link href="/categories" className="text-gray-700 hover:text-blue-600">{t('categories')}</Link>
              <Link href="/cart" className="text-gray-700 hover:text-blue-600">
                {t('cart')} ({getCartItemsCount()})
              </Link>
            </nav>
            
            <div className="flex items-center space-x-4">
              <LanguageSwitcher />
              <Link 
                href="/cart"
                className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors"
              >
                {t('cart')} ({getCartItemsCount()})
              </Link>
            </div>
          </div>
        </div>
      </header>

      {/* Hero Section */}
      <section className="bg-gradient-to-r from-blue-600 to-purple-600 text-white py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h2 className="text-4xl md:text-6xl font-bold mb-6">
            {isUzbek ? "Eng Yaxshi Mahsulotlar" : "Лучшие Товары"}
          </h2>
          <p className="text-xl md:text-2xl mb-8 opacity-90">
            {isUzbek ? "Sifatli va arzon narxlarda xarid qiling" : "Покупайте качественные товары по доступным ценам"}
          </p>
          <Link 
            href="/products"
            className="bg-white text-blue-600 px-8 py-3 rounded-lg text-lg font-semibold hover:bg-gray-100 transition-colors inline-block"
          >
            {t('products')}
          </Link>
        </div>
      </section>

      {/* Categories */}
      <section className="py-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <h3 className="text-3xl font-bold text-gray-900 mb-8 text-center">{t('categories')}</h3>
          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-6">
            {categories.map((category) => (
              <Link 
                key={category.id}
                href={`/products?category=${category.id}`}
                className="bg-white p-6 rounded-lg shadow-md hover:shadow-lg transition-shadow text-center"
              >
                <div className="text-4xl mb-3">{category.icon}</div>
                <h4 className="font-semibold text-gray-900">
                  {isUzbek ? category.name : category.nameRu}
                </h4>
                <p className="text-gray-600 text-sm">{category.itemCount} {isUzbek ? 'mahsulot' : 'товаров'}</p>
              </Link>
            ))}
          </div>
        </div>
      </section>

      {/* Featured Products */}
      <section className="py-16 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <h3 className="text-3xl font-bold text-gray-900 mb-8 text-center">
            {isUzbek ? "Mashhur Mahsulotlar" : "Популярные Товары"}
          </h3>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
            {featuredProducts.map((product) => (
              <div key={product.id} className="bg-gray-50 rounded-lg overflow-hidden shadow-md hover:shadow-lg transition-shadow">
                <div className="aspect-square bg-gray-200 relative">
                  <img 
                    src={product.images[0]} 
                    alt={isUzbek ? product.nameUz : product.name}
                    className="w-full h-full object-cover"
                  />
                  {product.discount > 0 && (
                    <span className="absolute top-2 left-2 bg-red-500 text-white px-2 py-1 text-xs rounded">
                      -{product.discount}%
                    </span>
                  )}
                  <button
                    onClick={() => handleToggleWishlist(product)}
                    className={`absolute top-2 right-2 p-2 rounded-full ${
                      isInWishlist(product.id) ? 'bg-red-500 text-white' : 'bg-white text-gray-600'
                    } hover:bg-red-500 hover:text-white transition-colors`}
                  >
                    ❤
                  </button>
                </div>
                <div className="p-4">
                  <h4 className="font-semibold text-gray-900 mb-2">
                    {isUzbek ? product.nameUz : product.name}
                  </h4>
                  <div className="flex items-center space-x-2 mb-3">
                    {product.originalPrice && (
                      <span className="text-gray-500 line-through text-sm">
                        {product.originalPrice.toLocaleString()} so'm
                      </span>
                    )}
                    <span className="text-blue-600 font-bold">
                      {product.price.toLocaleString()} so'm
                    </span>
                  </div>
                  <div className="flex items-center justify-between">
                    <div className="flex items-center">
                      <span className="text-yellow-400">★</span>
                      <span className="text-gray-600 text-sm ml-1">{product.rating}</span>
                    </div>
                    <button
                      onClick={() => handleAddToCart(product)}
                      className="bg-blue-600 text-white px-4 py-2 rounded text-sm hover:bg-blue-700 transition-colors"
                    >
                      {t('add_to_cart')}
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-gray-900 text-white py-12">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
            <div>
              <h5 className="text-xl font-bold mb-4">DomProduct</h5>
              <p className="text-gray-400">
                {isUzbek ? "Eng yaxshi onlayn do'kon" : "Лучший интернет-магазин"}
              </p>
            </div>
            <div>
              <h6 className="font-semibold mb-4">{isUzbek ? "Havolalar" : "Ссылки"}</h6>
              <ul className="space-y-2 text-gray-400">
                <li><Link href="/products" className="hover:text-white">{t('products')}</Link></li>
                <li><Link href="/categories" className="hover:text-white">{t('categories')}</Link></li>
                <li><Link href="/cart" className="hover:text-white">{t('cart')}</Link></li>
              </ul>
            </div>
            <div>
              <h6 className="font-semibold mb-4">{isUzbek ? "Yordam" : "Помощь"}</h6>
              <ul className="space-y-2 text-gray-400">
                <li><a href="#" className="hover:text-white">{isUzbek ? "Bog'lanish" : "Контакты"}</a></li>
                <li><a href="#" className="hover:text-white">{isUzbek ? "Yetkazish" : "Доставка"}</a></li>
                <li><a href="#" className="hover:text-white">{isUzbek ? "Qaytarish" : "Возврат"}</a></li>
              </ul>
            </div>
            <div>
              <h6 className="font-semibold mb-4">{isUzbek ? "Aloqa" : "Контакты"}</h6>
              <p className="text-gray-400">+998 90 123 45 67</p>
              <p className="text-gray-400">info@domproduct.uz</p>
            </div>
          </div>
          <div className="border-t border-gray-800 mt-8 pt-8 text-center text-gray-400">
            <p>&copy; 2024 DomProduct. {isUzbek ? "Barcha huquqlar himoyalangan." : "Все права защищены."}</p>
          </div>
        </div>
      </footer>
    </div>
  );
}
