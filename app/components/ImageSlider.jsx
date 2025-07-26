'use client';

import { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';

/* Avvalgi loyihadagi ImageSlider komponentini takrorlash */
const ImageSlider = () => {
  const { t } = useTranslation();
  const [currentSlide, setCurrentSlide] = useState(0);

  // Slider uchun rasmlar
  const slides = [
    {
      id: 1,
      image: '/api/placeholder/800/400',
      title: 'Yangi mahsulotlar',
      titleRu: 'Новые товары',
      subtitle: 'Eng so\'nggi texnologiyalar bilan tanishing',
      subtitleRu: 'Знакомьтесь с новейшими технологиями',
      buttonText: 'Ko\'rish',
      buttonTextRu: 'Смотреть'
    },
    {
      id: 2,
      image: '/api/placeholder/800/400',
      title: 'Chegirmalar',
      titleRu: 'Скидки',
      subtitle: '50% gacha chegirma barcha mahsulotlarga',
      subtitleRu: 'Скидки до 50% на все товары',
      buttonText: 'Xarid qilish',
      buttonTextRu: 'Купить'
    },
    {
      id: 3,
      image: '/api/placeholder/800/400',
      title: 'Tezkor yetkazish',
      titleRu: 'Быстрая доставка',
      subtitle: 'Buyurtmangizni 24 soat ichida oling',
      subtitleRu: 'Получите заказ в течение 24 часов',
      buttonText: 'Buyurtma berish',
      buttonTextRu: 'Заказать'
    }
  ];

  // Avtomatik slider
  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentSlide((prev) => (prev + 1) % slides.length);
    }, 5000); // 5 soniya

    return () => clearInterval(interval);
  }, [slides.length]);

  // Keyingi slide
  const nextSlide = () => {
    setCurrentSlide((prev) => (prev + 1) % slides.length);
  };

  // Oldingi slide
  const prevSlide = () => {
    setCurrentSlide((prev) => (prev - 1 + slides.length) % slides.length);
  };

  // Muayyan slide ga o'tish
  const goToSlide = (index) => {
    setCurrentSlide(index);
  };

  return (
    <div className="image-slider">
      <div className="slider-container">
        {slides.map((slide, index) => (
          <div
            key={slide.id}
            className={`slide ${index === currentSlide ? 'active' : ''}`}
            style={{
              backgroundImage: `url(${slide.image})`,
              backgroundSize: 'cover',
              backgroundPosition: 'center',
              backgroundRepeat: 'no-repeat'
            }}
          >
            <div className="slide-content">
              <h2 className="slide-title">
                {t('language') === 'uz' ? slide.title : slide.titleRu}
              </h2>
              <p className="slide-subtitle">
                {t('language') === 'uz' ? slide.subtitle : slide.subtitleRu}
              </p>
              <button className="slide-button btn btn-primary">
                {t('language') === 'uz' ? slide.buttonText : slide.buttonTextRu}
              </button>
            </div>
          </div>
        ))}

        {/* Navigation tugmalari */}
        <button className="slider-nav prev" onClick={prevSlide}>
          <i className="fas fa-chevron-left"></i>
        </button>
        <button className="slider-nav next" onClick={nextSlide}>
          <i className="fas fa-chevron-right"></i>
        </button>

        {/* Dots indikatorlar */}
        <div className="slider-dots">
          {slides.map((_, index) => (
            <button
              key={index}
              className={`dot ${index === currentSlide ? 'active' : ''}`}
              onClick={() => goToSlide(index)}
            />
          ))}
        </div>
      </div>

      <style jsx>{`
        .image-slider {
          position: relative;
          width: 100%;
          height: 400px;
          margin-bottom: 2rem;
          border-radius: var(--radius-lg);
          overflow: hidden;
          box-shadow: var(--shadow-lg);
        }

        .slider-container {
          position: relative;
          width: 100%;
          height: 100%;
        }

        .slide {
          position: absolute;
          top: 0;
          left: 0;
          width: 100%;
          height: 100%;
          opacity: 0;
          transition: opacity 0.5s ease-in-out;
          display: flex;
          align-items: center;
          justify-content: center;
        }

        .slide.active {
          opacity: 1;
        }

        .slide::before {
          content: '';
          position: absolute;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          background: rgba(0, 0, 0, 0.4);
          z-index: 1;
        }

        .slide-content {
          position: relative;
          z-index: 2;
          text-align: center;
          color: white;
          max-width: 600px;
          padding: 2rem;
        }

        .slide-title {
          font-size: 2.5rem;
          font-weight: bold;
          margin-bottom: 1rem;
          text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.7);
        }

        .slide-subtitle {
          font-size: 1.2rem;
          margin-bottom: 2rem;
          text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.7);
        }

        .slide-button {
          padding: 1rem 2rem;
          font-size: 1.1rem;
          border: none;
          border-radius: var(--radius-md);
          cursor: pointer;
          transition: all 0.3s;
        }

        .slider-nav {
          position: absolute;
          top: 50%;
          transform: translateY(-50%);
          background: rgba(255, 255, 255, 0.2);
          border: none;
          color: white;
          font-size: 1.5rem;
          padding: 1rem;
          cursor: pointer;
          border-radius: 50%;
          transition: all 0.3s;
          z-index: 3;
        }

        .slider-nav:hover {
          background: rgba(255, 255, 255, 0.3);
          transform: translateY(-50%) scale(1.1);
        }

        .slider-nav.prev {
          left: 1rem;
        }

        .slider-nav.next {
          right: 1rem;
        }

        .slider-dots {
          position: absolute;
          bottom: 1rem;
          left: 50%;
          transform: translateX(-50%);
          display: flex;
          gap: 0.5rem;
          z-index: 3;
        }

        .dot {
          width: 12px;
          height: 12px;
          border-radius: 50%;
          border: none;
          background: rgba(255, 255, 255, 0.5);
          cursor: pointer;
          transition: all 0.3s;
        }

        .dot.active {
          background: white;
          transform: scale(1.2);
        }

        @media (max-width: 768px) {
          .image-slider {
            height: 300px;
          }

          .slide-title {
            font-size: 2rem;
          }

          .slide-subtitle {
            font-size: 1rem;
          }

          .slide-content {
            padding: 1rem;
          }

          .slider-nav {
            padding: 0.5rem;
            font-size: 1.2rem;
          }
        }
      `}</style>
    </div>
  );
};

export default ImageSlider;
