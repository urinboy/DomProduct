

import React from 'react';
import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import Meta from '../components/Meta';

const OrdersPage = () => {
    const { t } = useTranslation();

    // Dummy data should eventually come from a context or API
    const dummyOrders = [
        { id: 1, date: '2023-01-15', total: '4,700,000 UZS', status: 'delivered', items: ['Smartfon X1', 'Aqlli soat'] },
        { id: 2, date: '2023-02-20', total: '8,000,000 UZS', status: 'pending', items: ['Noutbuk Pro'] },
        { id: 3, date: '2023-03-10', total: '750,000 UZS', status: 'cancelled', items: ['Simsiz quloqchin'] },
        { id: 4, date: '2023-04-01', total: '6,000,000 UZS', status: 'delivered', items: ['Televizor 4K', 'O\'yin konsoli'] },
    ];

    const getStatusColor = (status) => {
        switch (status) {
            case 'delivered': return 'var(--success-color)';
            case 'pending': return 'var(--warning-color)';
            case 'cancelled': return 'var(--danger-color)';
            default: return 'var(--gray-600)';
        }
    };

    const getStatusIcon = (status) => {
        switch (status) {
            case 'delivered': return 'fa-check-circle';
            case 'pending': return 'fa-clock';
            case 'cancelled': return 'fa-times-circle';
            default: return 'fa-info-circle';
        }
    };

    return (
        <div id="ordersPage">
            <Meta title={t('my_orders')} />
            <h2 style={{ marginBottom: '1rem' }}>{t('my_orders')}</h2>
            {dummyOrders.length === 0 ? (
                <div className="orders-empty cart-empty">
                    <i className="fas fa-clipboard-list cart-empty-icon"></i>
                    <div className="cart-empty-title">{t('no_orders_yet')}</div>
                    <p className="cart-empty-message">{t('no_orders_message')}</p>
                    <Link to="/products" className="btn btn-primary">{t('go_to_products')}</Link>
                </div>
            ) : (
                <div id="ordersList">
                    {dummyOrders.map(order => (
                        <div className="order-card" key={order.id} style={{ borderLeftColor: getStatusColor(order.status) }}>
                            <div className="order-card-header">
                                <span className="order-id">{t('order')} #{order.id}</span>
                                <span className="order-date">{order.date}</span>
                            </div>
                            <div className="order-card-body">
                                <div className="order-detail-item">
                                    <span className="order-detail-label">{t('status')}</span>
                                    <span className="order-detail-value" style={{ color: getStatusColor(order.status) }}>
                                        <i className={`fas ${getStatusIcon(order.status)}`}></i> {t(`status_${order.status}`)}
                                    </span>
                                </div>
                                <div className="order-detail-item">
                                    <span className="order-detail-label">{t('total_amount')}</span>
                                    <span className="order-detail-value total-price">{order.total}</span>
                                </div>
                            </div>
                            <div className="order-card-footer">
                                <h4 className="order-items-title">{t('products')}:</h4>
                                <ul className="order-items-list">
                                    {order.items.map((item, index) => (
                                        <li key={index}>{item}</li>
                                    ))}
                                </ul>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default OrdersPage;
