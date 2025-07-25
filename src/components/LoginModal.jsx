
import React from 'react';
import { useTranslation } from 'react-i18next';
import { useToast } from './Toast';
import { useModal } from '../contexts/ModalContext';
import { useAuth } from '../contexts/AuthContext';
import RegisterModalContent from './RegisterModal';

const LoginModalContent = () => {
    const { t } = useTranslation();
    const { showToast } = useToast();
    const { openModal, closeModal } = useModal();
    const { login } = useAuth();

    const handleLogin = (e) => {
        e.preventDefault();
        login();
        showToast(t('login_success'), 'success');
        closeModal();
    };

    const showRegister = (e) => {
        e.preventDefault();
        openModal(<RegisterModalContent />);
    };

    return (
        <div style={{ padding: '2.5rem' }}>
            <h3 style={{ marginBottom: '1rem' }}>{t('login')}</h3>
            <form id="loginForm" onSubmit={handleLogin}>
                <div className="form-group">
                    <label className="form-label">{t('email_or_phone')}</label>
                    <input type="text" className="form-input" id="loginInput" required defaultValue="test@user.com" />
                </div>
                <div className="form-group">
                    <label className="form-label">{t('password')}</label>
                    <input type="password" className="form-input" id="passwordInput" required defaultValue="12345" />
                </div>
                <button type="submit" className="btn btn-primary btn-block">{t('login')}</button>
            </form>
            <p style={{ textAlign: 'center', marginTop: '1rem' }}>
                {t('no_account')} <a href="#" onClick={showRegister}>{t('register_now')}</a>
            </p>
        </div>
    );
};

export default LoginModalContent;
