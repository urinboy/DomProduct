'use client';

import { useState } from 'react';
import { useTranslation } from 'react-i18next';
import Link from 'next/link';
import { useAuth } from '../../src/contexts/AuthContext';
import { useCart } from '../../src/contexts/CartContext';
import { useWishlist } from '../../src/contexts/WishlistContext';
import { useToast } from '../../src/components/Toast/ToastProvider';
import './profile.css';

interface LoginFormData {
  email: string;
  password: string;
}

interface User {
  id: number;
  email: string;
  name: string;
  phone?: string;
}

/* Profil sahifasi - avvalgi loyihadagi dizaynni takrorlash */
const ProfilePage = () => {
  const { t, i18n } = useTranslation();
  const { user, login, logout } = useAuth();
  const { items: cartItems } = useCart();
  const { items: wishlistItems } = useWishlist();
  const { showSuccess, showError, showInfo } = useToast();
  
  const [loginForm, setLoginForm] = useState<LoginFormData>({
    email: '',
    password: ''
  });
  const [isLoading, setIsLoading] = useState(false);

  const isUzbek = i18n.language === 'uz';

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!loginForm.email || !loginForm.password) {
      showError(isUzbek ? 'Iltimos, barcha maydonlarni to\'ldiring' : 'Пожалуйста, заполните все поля');
      return;
    }
    
    setIsLoading(true);
    
    try {
      // Demo login - avvalgi loyihadagi kabi
      const demoUser: User = {
        id: 1,
        email: loginForm.email,
        name: loginForm.email.split('@')[0],
        phone: '+998 90 123 45 67'
      };
      
      login(demoUser);
      showSuccess(isUzbek ? `Xush kelibsiz, ${demoUser.name}!` : `Добро пожаловать, ${demoUser.name}!`);
    } catch (error) {
      console.error('Login error:', error);
      showError(isUzbek ? 'Kirish muvaffaqiyatsiz. Ma\'lumotlarni tekshiring.' : 'Вход неуспешен. Проверьте данные.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleLogout = () => {
    logout();
    showInfo(isUzbek ? 'Tizimdan muvaffaqiyatli chiqildi' : 'Вы успешно вышли из системы');
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setLoginForm({
      ...loginForm,
      [e.target.name]: e.target.value
    });
  };

  // Agar foydalanuvchi kirmagan bo'lsa login formani ko'rsatish
  if (!user) {
    return (
      <div id="profilePage">
        <div className="login-container" style={{
          maxWidth: '450px',
          margin: '2rem auto',
          background: 'linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%)',
          borderRadius: '20px',
          padding: '2.5rem',
          boxShadow: '0 10px 30px rgba(0,0,0,0.1)',
          border: '1px solid #dee2e6'
        }}>
          <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
            <div style={{
              width: '80px',
              height: '80px',
              background: 'linear-gradient(135deg, #087c36, #0a9d44)',
              borderRadius: '50%',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              margin: '0 auto 1rem',
              boxShadow: '0 4px 15px rgba(8, 124, 54, 0.3)'
            }}>
              <i className="fas fa-user" style={{ fontSize: '30px', color: 'white' }}></i>
            </div>
            <h2 style={{ 
              margin: 0, 
              color: '#087c36', 
              fontSize: '24px',
              fontWeight: '600'
            }}>
              {isUzbek ? "Hisobingizga kiring" : "Войдите в аккаунт"}
            </h2>
            <p style={{ 
              margin: '8px 0 0', 
              color: '#6c757d', 
              fontSize: '14px' 
            }}>
              {isUzbek ? "Xaridlaringizni kuzatib boring" : "Следите за своими покупками"}
            </p>
          </div>

          <form onSubmit={handleLogin}>
            <div className="form-group" style={{ marginBottom: '1.5rem' }}>
              <label htmlFor="email" style={{ 
                display: 'block',
                marginBottom: '8px',
                color: '#495057',
                fontSize: '14px',
                fontWeight: '500'
              }}>
                {isUzbek ? "Email manzil" : "Email адрес"}
              </label>
              <div style={{ position: 'relative' }}>
                <i className="fas fa-envelope" style={{
                  position: 'absolute',
                  left: '15px',
                  top: '50%',
                  transform: 'translateY(-50%)',
                  color: '#087c36',
                  fontSize: '16px'
                }}></i>
                <input
                  type="email"
                  id="email"
                  name="email"
                  value={loginForm.email}
                  onChange={handleInputChange}
                  required
                  placeholder={isUzbek ? "Email manzilingizni kiriting" : "Введите ваш email"}
                  style={{
                    width: '100%',
                    padding: '12px 20px 12px 45px',
                    border: '2px solid #e9ecef',
                    borderRadius: '10px',
                    fontSize: '14px',
                    outline: 'none',
                    transition: 'all 0.3s ease',
                    background: 'white'
                  }}
                  onFocus={(e) => e.target.style.borderColor = '#087c36'}
                  onBlur={(e) => e.target.style.borderColor = '#e9ecef'}
                />
              </div>
            </div>
            
            <div className="form-group" style={{ marginBottom: '1.5rem' }}>
              <label htmlFor="password" style={{ 
                display: 'block',
                marginBottom: '8px',
                color: '#495057',
                fontSize: '14px',
                fontWeight: '500'
              }}>
                {isUzbek ? "Parol" : "Пароль"}
              </label>
              <div style={{ position: 'relative' }}>
                <i className="fas fa-lock" style={{
                  position: 'absolute',
                  left: '15px',
                  top: '50%',
                  transform: 'translateY(-50%)',
                  color: '#087c36',
                  fontSize: '16px'
                }}></i>
                <input
                  type="password"
                  id="password"
                  name="password"
                  value={loginForm.password}
                  onChange={handleInputChange}
                  required
                  placeholder={isUzbek ? "Parolingizni kiriting" : "Введите ваш пароль"}
                  style={{
                    width: '100%',
                    padding: '12px 20px 12px 45px',
                    border: '2px solid #e9ecef',
                    borderRadius: '10px',
                    fontSize: '14px',
                    outline: 'none',
                    transition: 'all 0.3s ease',
                    background: 'white'
                  }}
                  onFocus={(e) => e.target.style.borderColor = '#087c36'}
                  onBlur={(e) => e.target.style.borderColor = '#e9ecef'}
                />
              </div>
            </div>
            
            <button 
              type="submit" 
              className="login-button"
              disabled={isLoading}
              style={{
                width: '100%',
                padding: '14px',
                background: isLoading ? '#6c757d' : 'linear-gradient(135deg, #087c36, #0a9d44)',
                color: 'white',
                border: 'none',
                borderRadius: '10px',
                fontSize: '16px',
                fontWeight: '600',
                cursor: isLoading ? 'not-allowed' : 'pointer',
                transition: 'all 0.3s ease',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                gap: '10px'
              }}
            >
              {isLoading && <i className="fas fa-spinner fa-spin"></i>}
              {isLoading ? 
                (isUzbek ? "Kirilmoqda..." : "Вход...") : 
                (isUzbek ? "Kirish" : "Войти")
              }
            </button>

            <div style={{ 
              textAlign: 'center', 
              marginTop: '1.5rem',
              padding: '1rem',
              background: 'rgba(8, 124, 54, 0.1)',
              borderRadius: '8px',
              border: '1px solid rgba(8, 124, 54, 0.2)'
            }}>
              <p style={{ 
                margin: 0, 
                fontSize: '13px', 
                color: '#087c36',
                fontWeight: '500'
              }}>
                <i className="fas fa-info-circle" style={{ marginRight: '5px' }}></i>
                {isUzbek ? 
                  "Demo: Istalgan email va parol bilan kiring" : 
                  "Демо: Войдите с любым email и паролем"
                }
              </p>
            </div>
          </form>
        </div>
      </div>
    );
  }

  return (
    <div id="profilePage">
      {/* Yaxshilangan profil header */}
      <div className="profile-header" style={{
        background: 'linear-gradient(135deg, #087c36 0%, #0a9d44 100%)',
        borderRadius: '20px',
        padding: '2rem',
        marginBottom: '2rem',
        color: 'white',
        position: 'relative',
        overflow: 'hidden'
      }}>
        <div style={{
          position: 'absolute',
          top: '-50px',
          right: '-50px',
          width: '150px',
          height: '150px',
          background: 'rgba(255,255,255,0.1)',
          borderRadius: '50%'
        }}></div>
        <div style={{
          position: 'absolute',
          bottom: '-30px',
          left: '-30px',
          width: '100px',
          height: '100px',
          background: 'rgba(255,255,255,0.1)',
          borderRadius: '50%'
        }}></div>
        
        <div style={{ position: 'relative', zIndex: 1, display: 'flex', alignItems: 'center', gap: '1.5rem' }}>
          <div className="profile-avatar" style={{
            width: '80px',
            height: '80px',
            background: 'rgba(255,255,255,0.2)',
            borderRadius: '50%',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            fontSize: '30px',
            border: '3px solid rgba(255,255,255,0.3)'
          }}>
            <i className="fas fa-user"></i>
          </div>
          <div className="profile-info">
            <h2 style={{ margin: '0 0 5px 0', fontSize: '24px', fontWeight: '600' }}>
              {user.name || user.email.split('@')[0]}
            </h2>
            <p style={{ margin: '0 0 3px 0', opacity: 0.9, fontSize: '14px' }}>
              <i className="fas fa-envelope" style={{ marginRight: '8px' }}></i>
              {user.email}
            </p>
            {user.phone && (
              <p style={{ margin: 0, opacity: 0.9, fontSize: '14px' }}>
                <i className="fas fa-phone" style={{ marginRight: '8px' }}></i>
                {user.phone}
              </p>
            )}
          </div>
        </div>
      </div>

      {/* Yaxshilangan statistika */}
      <div className="profile-stats" style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(120px, 1fr))',
        gap: '1rem',
        marginBottom: '2rem'
      }}>
        <div className="profile-stat" style={{
          background: 'linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%)',
          borderRadius: '15px',
          padding: '1.5rem 1rem',
          textAlign: 'center',
          border: '1px solid #dee2e6',
          position: 'relative',
          overflow: 'hidden'
        }}>
          <div style={{
            position: 'absolute',
            top: '10px',
            right: '10px',
            color: '#087c36',
            fontSize: '20px'
          }}>
            <i className="fas fa-shopping-cart"></i>
          </div>
          <div className="profile-stat-value" style={{
            fontSize: '24px',
            fontWeight: '700',
            color: '#087c36',
            marginBottom: '5px'
          }}>
            {cartItems.reduce((sum: number, item: any) => sum + item.quantity, 0)}
          </div>
          <div className="profile-stat-label" style={{
            fontSize: '12px',
            color: '#6c757d',
            fontWeight: '500',
            textTransform: 'uppercase'
          }}>
            {t('cart')}
          </div>
        </div>

        <div className="profile-stat" style={{
          background: 'linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%)',
          borderRadius: '15px',
          padding: '1.5rem 1rem',
          textAlign: 'center',
          border: '1px solid #dee2e6',
          position: 'relative',
          overflow: 'hidden'
        }}>
          <div style={{
            position: 'absolute',
            top: '10px',
            right: '10px',
            color: '#dc3545',
            fontSize: '20px'
          }}>
            <i className="fas fa-heart"></i>
          </div>
          <div className="profile-stat-value" style={{
            fontSize: '24px',
            fontWeight: '700',
            color: '#dc3545',
            marginBottom: '5px'
          }}>
            {wishlistItems.length}
          </div>
          <div className="profile-stat-label" style={{
            fontSize: '12px',
            color: '#6c757d',
            fontWeight: '500',
            textTransform: 'uppercase'
          }}>
            {t('wishlist')}
          </div>
        </div>

        <div className="profile-stat" style={{
          background: 'linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%)',
          borderRadius: '15px',
          padding: '1.5rem 1rem',
          textAlign: 'center',
          border: '1px solid #dee2e6',
          position: 'relative',
          overflow: 'hidden'
        }}>
          <div style={{
            position: 'absolute',
            top: '10px',
            right: '10px',
            color: '#6f42c1',
            fontSize: '20px'
          }}>
            <i className="fas fa-box"></i>
          </div>
          <div className="profile-stat-value" style={{
            fontSize: '24px',
            fontWeight: '700',
            color: '#6f42c1',
            marginBottom: '5px'
          }}>
            0
          </div>
          <div className="profile-stat-label" style={{
            fontSize: '12px',
            color: '#6c757d',
            fontWeight: '500',
            textTransform: 'uppercase'
          }}>
            {isUzbek ? "Buyurtmalar" : "Заказы"}
          </div>
        </div>
      </div>

      {/* Yaxshilangan profil menu */}
      <div className="profile-menu" style={{
        background: 'white',
        borderRadius: '15px',
        padding: '1rem',
        marginBottom: '2rem',
        boxShadow: '0 4px 15px rgba(0,0,0,0.1)',
        border: '1px solid #dee2e6'
      }}>
        <Link href="/cart" className="profile-menu-item" style={{
          display: 'flex',
          alignItems: 'center',
          padding: '1rem',
          textDecoration: 'none',
          color: 'inherit',
          borderRadius: '10px',
          marginBottom: '0.5rem',
          transition: 'all 0.3s ease',
          border: '1px solid transparent'
        }}
        onMouseOver={(e) => {
          e.currentTarget.style.background = '#f8f9fa';
          e.currentTarget.style.borderColor = '#087c36';
        }}
        onMouseOut={(e) => {
          e.currentTarget.style.background = 'transparent';
          e.currentTarget.style.borderColor = 'transparent';
        }}>
          <div style={{
            width: '45px',
            height: '45px',
            background: 'linear-gradient(135deg, #087c36, #0a9d44)',
            borderRadius: '10px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: 'white',
            marginRight: '1rem'
          }}>
            <i style={{ color: '#ffffff' }} className="fas fa-shopping-cart"></i>
          </div>
          <div className="menu-text" style={{ flex: 1 }}>
            <div className="menu-title" style={{ 
              fontWeight: '600', 
              marginBottom: '3px',
              color: '#212529'
            }}>
              {t('cart')}
            </div>
            <div className="menu-description" style={{ 
              fontSize: '13px', 
              color: '#6c757d' 
            }}>
              {isUzbek ? "Savatchadagi mahsulotlaringiz" : "Товары в корзине"}
            </div>
          </div>
          <i className="fas fa-chevron-right" style={{ color: '#087c36' }}></i>
        </Link>

        <Link href="/wishlist" className="profile-menu-item" style={{
          display: 'flex',
          alignItems: 'center',
          padding: '1rem',
          textDecoration: 'none',
          color: 'inherit',
          borderRadius: '10px',
          marginBottom: '0.5rem',
          transition: 'all 0.3s ease',
          border: '1px solid transparent'
        }}
        onMouseOver={(e) => {
          e.currentTarget.style.background = '#f8f9fa';
          e.currentTarget.style.borderColor = '#dc3545';
        }}
        onMouseOut={(e) => {
          e.currentTarget.style.background = 'transparent';
          e.currentTarget.style.borderColor = 'transparent';
        }}>
          <div style={{
            width: '45px',
            height: '45px',
            background: 'linear-gradient(135deg, #dc3545, #e85d75)',
            borderRadius: '10px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: 'white',
            marginRight: '1rem'
          }}>
            <i style={{ color: '#ffffff' }} className="fas fa-heart"></i>
          </div>
          <div className="menu-text" style={{ flex: 1 }}>
            <div className="menu-title" style={{ 
              fontWeight: '600', 
              marginBottom: '3px',
              color: '#212529'
            }}>
              {t('wishlist')}
            </div>
            <div className="menu-description" style={{ 
              fontSize: '13px', 
              color: '#6c757d' 
            }}>
              {isUzbek ? "Sevimli mahsulotlaringiz" : "Избранные товары"}
            </div>
          </div>
          <i className="fas fa-chevron-right" style={{ color: '#dc3545' }}></i>
        </Link>

        <div className="profile-menu-item" style={{
          display: 'flex',
          alignItems: 'center',
          padding: '1rem',
          borderRadius: '10px',
          marginBottom: '0.5rem',
          transition: 'all 0.3s ease',
          border: '1px solid transparent',
          cursor: 'pointer'
        }}
        onMouseOver={(e) => {
          e.currentTarget.style.background = '#f8f9fa';
          e.currentTarget.style.borderColor = '#6f42c1';
        }}
        onMouseOut={(e) => {
          e.currentTarget.style.background = 'transparent';
          e.currentTarget.style.borderColor = 'transparent';
        }}>
          <div style={{
            width: '45px',
            height: '45px',
            background: 'linear-gradient(135deg, #6f42c1, #8e5dd9)',
            borderRadius: '10px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: 'white',
            marginRight: '1rem'
          }}>
            <i style={{ color: '#ffffff' }} className="fas fa-box"></i>
          </div>
          <div className="menu-text" style={{ flex: 1 }}>
            <div className="menu-title" style={{ 
              fontWeight: '600', 
              marginBottom: '3px',
              color: '#212529'
            }}>
              {isUzbek ? "Buyurtmalarim" : "Мои заказы"}
            </div>
            <div className="menu-description" style={{ 
              fontSize: '13px', 
              color: '#6c757d' 
            }}>
              {isUzbek ? "Buyurtmalar tarixi" : "История заказов"}
            </div>
          </div>
          <i className="fas fa-chevron-right" style={{ color: '#6f42c1' }}></i>
        </div>

        <div className="profile-menu-item" style={{
          display: 'flex',
          alignItems: 'center',
          padding: '1rem',
          borderRadius: '10px',
          marginBottom: '0.5rem',
          transition: 'all 0.3s ease',
          border: '1px solid transparent',
          cursor: 'pointer'
        }}
        onMouseOver={(e) => {
          e.currentTarget.style.background = '#f8f9fa';
          e.currentTarget.style.borderColor = '#fd7e14';
        }}
        onMouseOut={(e) => {
          e.currentTarget.style.background = 'transparent';
          e.currentTarget.style.borderColor = 'transparent';
        }}>
          <div style={{
            width: '45px',
            height: '45px',
            background: 'linear-gradient(135deg, #fd7e14, #ff922b)',
            borderRadius: '10px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: 'white',
            marginRight: '1rem'
          }}>
            <i style={{ color: '#ffffff' }} className="fas fa-map-marker-alt"></i>
          </div>
          <div className="menu-text" style={{ flex: 1 }}>
            <div className="menu-title" style={{ 
              fontWeight: '600', 
              marginBottom: '3px',
              color: '#212529'
            }}>
              {isUzbek ? "Manzillarim" : "Мои адреса"}
            </div>
            <div className="menu-description" style={{ 
              fontSize: '13px', 
              color: '#6c757d' 
            }}>
              {isUzbek ? "Yetkazish manzillari" : "Адреса доставки"}
            </div>
          </div>
          <i className="fas fa-chevron-right" style={{ color: '#fd7e14' }}></i>
        </div>

        <div className="profile-menu-item" style={{
          display: 'flex',
          alignItems: 'center',
          padding: '1rem',
          borderRadius: '10px',
          marginBottom: '0.5rem',
          transition: 'all 0.3s ease',
          border: '1px solid transparent',
          cursor: 'pointer'
        }}
        onMouseOver={(e) => {
          e.currentTarget.style.background = '#f8f9fa';
          e.currentTarget.style.borderColor = '#20c997';
        }}
        onMouseOut={(e) => {
          e.currentTarget.style.background = 'transparent';
          e.currentTarget.style.borderColor = 'transparent';
        }}>
          <div style={{
            width: '45px',
            height: '45px',
            background: 'linear-gradient(135deg, #20c997, #40dfb8)',
            borderRadius: '10px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: 'white',
            marginRight: '1rem'
          }}>
            <i style={{ color: '#ffffff' }} className="fas fa-credit-card"></i>
          </div>
          <div className="menu-text" style={{ flex: 1 }}>
            <div className="menu-title" style={{ 
              fontWeight: '600', 
              marginBottom: '3px',
              color: '#212529'
            }}>
              {isUzbek ? "To'lov usullari" : "Способы оплаты"}
            </div>
            <div className="menu-description" style={{ 
              fontSize: '13px', 
              color: '#6c757d' 
            }}>
              {isUzbek ? "Kartalar va to'lov" : "Карты и платежи"}
            </div>
          </div>
          <i className="fas fa-chevron-right" style={{ color: '#20c997' }}></i>
        </div>

        <div className="profile-menu-item" style={{
          display: 'flex',
          alignItems: 'center',
          padding: '1rem',
          borderRadius: '10px',
          marginBottom: '0.5rem',
          transition: 'all 0.3s ease',
          border: '1px solid transparent',
          cursor: 'pointer'
        }}
        onMouseOver={(e) => {
          e.currentTarget.style.background = '#f8f9fa';
          e.currentTarget.style.borderColor = '#6c757d';
        }}
        onMouseOut={(e) => {
          e.currentTarget.style.background = 'transparent';
          e.currentTarget.style.borderColor = 'transparent';
        }}>
          <div style={{
            width: '45px',
            height: '45px',
            background: 'linear-gradient(135deg, #6c757d, #868e96)',
            borderRadius: '10px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: 'white',
            marginRight: '1rem'
          }}>
            <i style={{ color: '#ffffff' }} className="fas fa-cog"></i>
          </div>
          <div className="menu-text" style={{ flex: 1 }}>
            <div className="menu-title" style={{ 
              fontWeight: '600', 
              marginBottom: '3px',
              color: '#212529'
            }}>
              {isUzbek ? "Sozlamalar" : "Настройки"}
            </div>
            <div className="menu-description" style={{ 
              fontSize: '13px', 
              color: '#6c757d' 
            }}>
              {isUzbek ? "Hisobni boshqarish" : "Управление аккаунтом"}
            </div>
          </div>
          <i className="fas fa-chevron-right" style={{ color: '#6c757d' }}></i>
        </div>

        <div className="profile-menu-item" style={{
          display: 'flex',
          alignItems: 'center',
          padding: '1rem',
          borderRadius: '10px',
          transition: 'all 0.3s ease',
          border: '1px solid transparent',
          cursor: 'pointer'
        }}
        onMouseOver={(e) => {
          e.currentTarget.style.background = '#f8f9fa';
          e.currentTarget.style.borderColor = '#0dcaf0';
        }}
        onMouseOut={(e) => {
          e.currentTarget.style.background = 'transparent';
          e.currentTarget.style.borderColor = 'transparent';
        }}>
          <div style={{
            width: '45px',
            height: '45px',
            background: 'linear-gradient(135deg, #0dcaf0, #3dd5f3)',
            borderRadius: '10px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: 'white',
            marginRight: '1rem'
          }}>
            <i style={{ color: '#ffffff' }} className="fas fa-headset"></i>
          </div>
          <div className="menu-text" style={{ flex: 1 }}>
            <div className="menu-title" style={{ 
              fontWeight: '600', 
              marginBottom: '3px',
              color: '#212529'
            }}>
              {isUzbek ? "Yordam" : "Поддержка"}
            </div>
            <div className="menu-description" style={{ 
              fontSize: '13px', 
              color: '#6c757d' 
            }}>
              {isUzbek ? "Savol va javoblar" : "Вопросы и ответы"}
            </div>
          </div>
          <i className="fas fa-chevron-right" style={{ color: '#0dcaf0' }}></i>
        </div>
      </div>

      {/* Yaxshilangan logout tugma */}
      <button className="logout-button" onClick={handleLogout} style={{
        width: '100%',
        padding: '15px',
        background: 'linear-gradient(135deg, #dc3545, #e85d75)',
        color: 'white',
        border: 'none',
        borderRadius: '15px',
        fontSize: '16px',
        fontWeight: '600',
        cursor: 'pointer',
        transition: 'all 0.3s ease',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        gap: '10px',
        boxShadow: '0 4px 15px rgba(220, 53, 69, 0.3)'
      }}
      onMouseOver={(e) => {
        e.currentTarget.style.transform = 'translateY(-2px)';
        e.currentTarget.style.boxShadow = '0 6px 20px rgba(220, 53, 69, 0.4)';
      }}
      onMouseOut={(e) => {
        e.currentTarget.style.transform = 'translateY(0)';
        e.currentTarget.style.boxShadow = '0 4px 15px rgba(220, 53, 69, 0.3)';
      }}>
        <i className="fas fa-sign-out-alt"></i>
        {isUzbek ? "Chiqish" : "Выйти"}
      </button>
    </div>
  );
};

export default ProfilePage;
