
import React from 'react';
import { useTranslation } from 'react-i18next';
import { useModal } from '../contexts/ModalContext';
import LoginModalContent from './LoginModal';

const RegisterModalContent = () => {
    const { t } = useTranslation();
    const { openModal } = useModal();

    const showLogin = (e) => {
        e.preventDefault();
        openModal(<LoginModalContent />);
    };

    return (
        <div style={{ padding: '2.5rem' }}>
            <h3 style={{ marginBottom: '1rem' }}>{t('register')}</h3>
            <form id="registerForm">
                <div className="form-group">
                    <label className="form-label">{t('name')}</label>
                    <input type="text" className="form-input" id="nameInput" required />
                </div>
                <div className="form-group">
                    <label className="form-label">{t('email')}</label>
                    <input type="email" className="form-input" id="emailInput" required />
                </div>
                <div className="form-group">
                    <label className="form-label">{t('phone')}</label>
                    <input type="tel" className="form-input" id="phoneInput" required />
                </div>
                <div className="form-group">
                    <label className="form-label">{t('password')}</label>
                    <input type="password" className="form-input" id="newPasswordInput" required />
                </div>
                <div className="form-group">
                    <label className="form-label">{t('confirm_password')}</label>
                    <input type="password" className="form-input" id="confirmPasswordInput" required />
                </div>
                <button type="submit" className="btn btn-primary btn-block">{t('register')}</button>
            </form>
            <p style={{ textAlign: 'center', marginTop: '1rem' }}>
                {t('have_account')} <a href="#" onClick={showLogin}>{t('login_now')}</a>
            </p>
        </div>
    );
};

export default RegisterModalContent;
