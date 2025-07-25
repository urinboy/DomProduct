import React, { useState, useEffect, createContext, useContext, useCallback } from 'react';
import ReactDOM from 'react-dom/client';

const generateId = (prefix = '') => {
    return prefix + Math.random().toString(36).substr(2, 9);
};

const sanitizeHtml = (html) => {
    const div = document.createElement('div');
    div.innerText = html;
    return div.innerHTML;
};

const ToastContext = createContext();

export const ToastProvider = ({ children }) => {
    const [toasts, setToasts] = useState([]);

    const showToast = useCallback((message, type = 'info', duration = 3000) => {
        const id = generateId('toast');
        setToasts(prevToasts => [...prevToasts, { id, message, type, duration }]);
    }, []);

    const dismissToast = useCallback((id) => {
        setToasts(prevToasts => prevToasts.filter(toast => toast.id !== id));
    }, []);

    const value = { showToast, dismissToast };

    return (
        <ToastContext.Provider value={value}>
            {children}
            <div className="toast-container">
                {toasts.map(toast => (
                    <ToastItem key={toast.id} {...toast} onDismiss={dismissToast} />
                ))}
            </div>
        </ToastContext.Provider>
    );
};

const ToastItem = ({ id, message, type, duration, onDismiss }) => {
    const [isVisible, setIsVisible] = useState(false);

    useEffect(() => {
        // Animate in
        setIsVisible(true);

        const timer = setTimeout(() => {
            setIsVisible(false);
            // Allow time for fade out animation before removing from DOM
            setTimeout(() => onDismiss(id), 300);
        }, duration);

        return () => clearTimeout(timer);
    }, [duration, id, onDismiss]);

    const getTypeIcon = (type) => {
        const icons = {
            success: 'check-circle',
            error: 'exclamation-circle',
            warning: 'exclamation-triangle',
            info: 'info-circle'
        };
        return icons[type] || icons.info;
    };

    const getTypeColor = (type) => {
        const colors = {
            success: 'var(--success-color)',
            error: 'var(--danger-color)',
            warning: 'var(--warning-color)',
            info: 'var(--info-color)'
        };
        return colors[type] || colors.info;
    };

    return (
        <div className={`toast ${type} ${isVisible ? 'show' : ''}`} style={{ borderLeftColor: getTypeColor(type) }}>
            <div className="toast-content">
                <i className={`fas fa-${getTypeIcon(type)} toast-icon`} style={{ color: getTypeColor(type) }}></i>
                <span className="toast-message">{sanitizeHtml(message)}</span>
                <button className="toast-close" onClick={() => onDismiss(id)}>&times;</button>
            </div>
        </div>
    );
};

export const useToast = () => useContext(ToastContext);
