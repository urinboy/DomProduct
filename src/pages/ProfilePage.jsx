

import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../contexts/AuthContext';
import { useModal } from '../contexts/ModalContext';
import { useToast } from '../components/Toast';
import LoginModalContent from '../components/LoginModal';
import ConfirmModal from '../components/ConfirmModal';
import Meta from '../components/Meta';
import EditProfileModal from '../components/EditProfileModal';

const ProfilePage = () => {
    const { t } = useTranslation();
    const { isLoggedIn, user, logout } = useAuth();
    const { openModal, closeModal } = useModal();
    const { showToast } = useToast();
    const navigate = useNavigate();

    const handleLogout = () => {
        openModal(
            <ConfirmModal 
                title={t('logout_title')}
                message={t('logout_message')}
                confirmText={t('logout')}
                cancelText={t('cancel')}
                confirmButtonClass="btn-primary"
                onConfirm={() => {
                    logout();
                    showToast(t('logout_success'), 'info');
                    closeModal();
                }}
                onCancel={closeModal}
            />
        );
    };

    const handleEditProfile = () => {
        openModal(<EditProfileModal />);
    };

    return (
        <div id="profilePage">
            <Meta title={t('my_profile')} />
            <h2 style={{ marginBottom: '1rem' }}>{t('profile')}</h2>
            {!isLoggedIn || !user ? (
                <div className="profile-guest-card">
                    <i className="fas fa-user-circle cart-empty-icon"></i>
                    <div className="cart-empty-title">{t('login_to_profile')}</div>
                    <p className="cart-empty-message">{t('login_to_profile_message')}</p>
                    <div className="profile-actions">
                        <button className="btn btn-primary" onClick={() => openModal(<LoginModalContent />)}>{t('login')}</button>
                    </div>
                </div>
            ) : (
                <div className="profile-container">
                    <div className="profile-user-card">
                        <div className="profile-avatar">{user.firstName && user.firstName.charAt(0)}</div>
                        <h3 className="profile-name">{`${user.firstName} ${user.lastName}`}</h3>
                    </div>
                    <div className="profile-menu">
                        <button className="profile-menu-item" onClick={handleEditProfile}>
                            <i className="fas fa-cog profile-menu-icon"></i>
                            <span>{t('settings')}</span>
                            <i className="fas fa-chevron-right"></i>
                        </button>
                        <button className="profile-menu-item" onClick={() => navigate('/orders')}>
                            <i className="fas fa-clipboard-list profile-menu-icon"></i>
                            <span>{t('my_orders')}</span>
                            <i className="fas fa-chevron-right"></i>
                        </button>
                        <button className="profile-menu-item" onClick={() => navigate('/wishlist')}>
                            <i className="fas fa-heart profile-menu-icon"></i>
                            <span>{t('my_wishlist')}</span>
                            <i className="fas fa-chevron-right"></i>
                        </button>
                        <button className="profile-menu-item danger" onClick={handleLogout}>
                            <i className="fas fa-sign-out-alt profile-menu-icon"></i>
                            <span>{t('logout')}</span>
                            <i className="fas fa-chevron-right"></i>
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default ProfilePage;
