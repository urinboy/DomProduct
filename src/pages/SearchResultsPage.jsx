import React from 'react';
import { useSearchParams, Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { allProducts } from '../data/products';
import Meta from '../components/Meta';

const SearchResultsPage = () => {
    const { t } = useTranslation();
    const [searchParams] = useSearchParams();
    const query = searchParams.get('q')?.toLowerCase() || '';

    const filteredProducts = query
        ? allProducts.filter(product =>
            product.name.toLowerCase().includes(query) ||
            product.description.toLowerCase().includes(query)
        )
        : [];

    return (
        <div className="products-page">
            <Meta title={`${t('search_results')}: ${query}`} description={`${t('products_found_for')} "${query}".`} />
            <h1 className="page-title">
                {t('search_results')}: "{query}"
            </h1>

            {filteredProducts.length > 0 ? (
                <div className="product-grid">
                    {filteredProducts.map(product => (
                        <div className="product-card" key={product.id}>
                            <Link to={`/products/${product.id}`} className="product-image-link">
                                <div className="product-image">
                                    <img src={product.image} alt={product.name} />
                                </div>
                            </Link>
                            <div className="product-info">
                                <div className="product-title">{t(product.name)}</div>
                                <div className="product-price">
                                    <span className="current-price">{product.price.toLocaleString('uz-UZ')} UZS</span>
                                </div>
                                <Link to={`/products/${product.id}`} className="btn btn-secondary btn-sm">{t('details')}</Link>
                            </div>
                        </div>
                    ))}
                </div>
            ) : (
                <p className="empty-message">{t('no_results_found')}</p>
            )}
        </div>
    );
};

export default SearchResultsPage;
