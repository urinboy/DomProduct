import Link from 'next/link';

// Next.js 15.4.4 Global Not Found sahifasi
export default function NotFound() {
  return (
    <div className="not-found-container">
      <div className="not-found-content">
        <div className="not-found-icon">🔍</div>
        <h1>404 - Sahifa topilmadi</h1>
        <p>Kechirasiz, qidirayotgan sahifangiz mavjud emas yoki ko'chirilgan.</p>
        
        <div className="not-found-actions">
          <Link href="/" className="btn btn-primary">
            🏠 Bosh sahifaga qaytish
          </Link>
          
          <Link href="/products" className="btn btn-secondary">
            🛍️ Mahsulotlarni ko'rish
          </Link>
          
          <Link href="/categories" className="btn btn-outline">
            📂 Kategoriyalar
          </Link>
        </div>
        
        <div className="search-suggestion">
          <p>Yoki qidiruv orqali kerakli narsangizni toping:</p>
          <Link href="/search" className="search-link">
            🔍 Qidiruv sahifasi
          </Link>
        </div>
      </div>
    </div>
  );
}
