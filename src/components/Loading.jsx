
import React, { useState, createContext, useContext, useCallback } from 'react';
import { motion, AnimatePresence } from 'framer-motion';

const LoadingContext = createContext();

export const LoadingProvider = ({ children }) => {
    const [loading, setLoading] = useState(false);

    const showLoading = useCallback(() => {
        setLoading(true);
    }, []);

    const hideLoading = useCallback(() => {
        setLoading(false);
    }, []);

    const value = { showLoading, hideLoading };

    return (
        <LoadingContext.Provider value={value}>
            {children}
            <AnimatePresence>
                {loading && (
                    <motion.div
                        className="loading-overlay"
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        exit={{ opacity: 0 }}
                        transition={{ duration: 0.5 }}
                    >
                        <div className="loading-spinner"></div>
                    </motion.div>
                )}
            </AnimatePresence>
        </LoadingContext.Provider>
    );
};

export const useLoading = () => useContext(LoadingContext);
