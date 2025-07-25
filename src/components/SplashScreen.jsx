import React from 'react';

const SplashScreen = () => {
    return (
        <div className="splash-screen">
            <div className="splash-icon-container">
                {/* Agar public papkada favicon.png bo'lsa, avtomatik ishlatiladi */}
                <img src="/loadings/light-splash.gif" alt="Loading Light Splash" className="splash-icon" />
            </div>
        </div>
    );
};

export default SplashScreen;
