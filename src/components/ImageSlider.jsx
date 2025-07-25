import React from 'react';
import { Swiper, SwiperSlide } from 'swiper/react';
import { Pagination, Autoplay } from 'swiper/modules';
import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import { sliders } from '../data/sliders'; // Import sliders data

import 'swiper/css';
import 'swiper/css/pagination';
import 'swiper/css/autoplay';

const ImageSlider = () => {
    const { t } = useTranslation();

    return (
        <div className="slider-container">
            <Swiper
                modules={[Pagination, Autoplay]}
                spaceBetween={15}
                slidesPerView={1}
                pagination={{ clickable: true, dynamicBullets: true }}
                autoplay={{
                    delay: 3000,
                    disableOnInteraction: false,
                }}
                loop={true}
                className="mySwiper"
            >
                {sliders.map(slide => (
                    <SwiperSlide key={slide.id}>
                        <Link to={slide.link}>
                            <img src={slide.src} alt={slide.alt} className="slider-image" />
                            <div className="slider-content">
                                <h3 className="slider-title">{t(`slider_title_${slide.id}`)}</h3>
                                <p className="slider-description">{t(`slider_description_${slide.id}`)}</p>
                            </div>
                        </Link>
                    </SwiperSlide>
                ))}
            </Swiper>
        </div>
    );
};

export default ImageSlider;