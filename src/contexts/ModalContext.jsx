import React, { createContext, useContext, useState, useCallback } from 'react';
import ReactDOM from 'react-dom';

const ModalContext = createContext();

export const ModalProvider = ({ children }) => {
    const [modalContent, setModalContent] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const openModal = useCallback((content) => {
        setModalContent(content);
        setIsModalOpen(true);
        document.body.style.overflow = 'hidden'; // Prevent scrolling
    }, []);

    const closeModal = useCallback(() => {
        setIsModalOpen(false);
        setModalContent(null);
        document.body.style.overflow = ''; // Restore scrolling
    }, []);

    const value = { openModal, closeModal };

    return (
        <ModalContext.Provider value={value}>
            {children}
            {isModalOpen &&
                ReactDOM.createPortal(
                    <div className="modal active">
                        <div className="modal-content">
                            <button className="modal-close" onClick={closeModal}>&times;</button>
                            {modalContent}
                        </div>
                    </div>,
                    document.body
                )}
        </ModalContext.Provider>
    );
};

export const useModal = () => useContext(ModalContext);
