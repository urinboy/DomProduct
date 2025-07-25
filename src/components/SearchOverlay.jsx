import React, { useState, useEffect, useRef } from 'react';
import { useTranslation } from 'react-i18next';

const SearchOverlay = ({ onClose, onSearch }) => {
    const { t } = useTranslation();
    const [query, setQuery] = useState('');
    const inputRef = useRef(null);

    useEffect(() => {
        inputRef.current?.focus();
    }, []);

    const handleSearch = (e) => {
        e.preventDefault();
        if (query.trim()) {
            onSearch(query.trim());
        }
    };

    return (
        <div className="search-overlay">
            <div className="search-overlay-content">
                <button className="close-btn" onClick={onClose}>
                    <i className="fas fa-times"></i>
                </button>
                <form onSubmit={handleSearch} className="search-form">
                    <input
                        ref={inputRef}
                        type="text"
                        value={query}
                        onChange={(e) => setQuery(e.target.value)}
                        placeholder={t('search_products_placeholder')}
                        className="search-input-overlay"
                    />
                    <button type="submit" className="search-btn-overlay">
                        <i className="fas fa-search"></i>
                    </button>
                </form>
            </div>
        </div>
    );
};

export default SearchOverlay;
