
import React, { createContext, useState, useContext, useEffect } from 'react';
import { users } from '../data/users'; // Import users data

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [isLoggedIn, setIsLoggedIn] = useState(() => !!localStorage.getItem('isLoggedIn'));
    // Find a user to be the dummy user, for example the first one
    const [user, setUser] = useState(users[0]); 

    useEffect(() => {
        if (isLoggedIn) {
            localStorage.setItem('isLoggedIn', 'true');
        } else {
            localStorage.removeItem('isLoggedIn');
        }
    }, [isLoggedIn]);

    const login = () => {
        setIsLoggedIn(true);
    };

    const logout = () => {
        setIsLoggedIn(false);
    };

    const updateUser = (updatedData) => {
        setUser(prevUser => ({ ...prevUser, ...updatedData }));
        // Here you would typically also make an API call to update the backend
    };

    return (
        <AuthContext.Provider value={{ isLoggedIn, user, login, logout, updateUser }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
