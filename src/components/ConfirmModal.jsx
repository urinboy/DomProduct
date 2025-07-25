
import React from 'react';
import { useTranslation } from 'react-i18next';

const ConfirmModal = ({ title, message, onConfirm, onCancel, confirmText, cancelText, confirmButtonClass }) => {
    const { t } = useTranslation();

    return (
        <div style={{ textAlign: 'center' }}>
            <h3 className="confirm-modal-title" style={{ marginBottom: '1rem' }}>{title}</h3>
            <p className="confirm-modal-message" style={{ marginBottom: '1.5rem' }}>{message}</p>
            <div className="confirm-modal-footer" style={{ display: 'flex', justifyContent: 'center', gap: '1rem' }}>
                <button onClick={onCancel} className="btn btn-secondary">
                    {cancelText || t('cancel')}
                </button>
                <button onClick={onConfirm} className={`btn ${confirmButtonClass || 'btn-danger'}`}>
                    {confirmText || t('confirm')}
                </button>
            </div>
        </div>
    );
};

export default ConfirmModal;
