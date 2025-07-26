'use client';

import { useState, useEffect } from 'react';
import Image from 'next/image';

interface ProductImageProps {
  src: string;
  alt: string;
  width?: number;
  height?: number;
  className?: string;
  priority?: boolean;
  fill?: boolean;
  sizes?: string;
  loading?: 'lazy' | 'eager';
  style?: React.CSSProperties;
  onMouseOver?: (e: React.MouseEvent) => void;
  onMouseOut?: (e: React.MouseEvent) => void;
}

// Mahsulot rasmi komponenti - fallback bilan
export default function ProductImage({ 
  src, 
  alt, 
  width = 400, 
  height = 400, 
  className = '',
  priority = false,
  fill = false,
  sizes = '(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw',
  loading = 'lazy',
  style,
  onMouseOver,
  onMouseOut
}: ProductImageProps) {
  // Agar src bo'sh yoki yo'q bo'lsa, darhol placeholder ishlat
  const placeholderSrc = '/images/domproduct-placeholder.png';
  
  const fallbackSVG = `data:image/svg+xml;base64,${btoa(`
    <svg width="${width}" height="${height}" xmlns="http://www.w3.org/2000/svg">
      <rect width="100%" height="100%" fill="#f8f9fa"/>
      <text x="50%" y="45%" dominant-baseline="middle" text-anchor="middle" 
            font-family="Arial, sans-serif" font-size="16" fill="#6c757d">
        DomProduct
      </text>
      <text x="50%" y="60%" dominant-baseline="middle" text-anchor="middle" 
            font-family="Arial, sans-serif" font-size="12" fill="#adb5bd">
        Rasm yuklanmadi
      </text>
    </svg>
  `)}`;

  // Xavfsiz src - agar bo'sh bo'lsa placeholder ishlatamiz
  const safeSrc = src && src.trim() !== '' ? src : placeholderSrc;
  const [imageSrc, setImageSrc] = useState(safeSrc);
  const [hasError, setHasError] = useState(false);

  // src o'zgarganda imageSrc ni yangilaymiz
  useEffect(() => {
    const newSafeSrc = src && src.trim() !== '' ? src : placeholderSrc;
    setImageSrc(newSafeSrc);
    setHasError(false);
  }, [src, placeholderSrc]);

  const handleError = () => {
    if (!hasError) {
      setHasError(true);
      // Birinchi fallback - placeholder rasm
      setImageSrc(placeholderSrc);
    } else {
      // Ikkinchi fallback - SVG
      setImageSrc(fallbackSVG);
    }
  };

  const handleLoad = () => {
    // Rasm muvaffaqiyatli yuklandi
    setHasError(false);
  };

  if (fill) {
    return (
      <Image
        src={imageSrc}
        alt={alt}
        fill
        sizes={sizes}
        className={className}
        priority={priority}
        onError={handleError}
        onLoad={handleLoad}
        style={{ objectFit: 'cover', ...style }}
        onMouseOver={onMouseOver}
        onMouseOut={onMouseOut}
      />
    );
  }

  return (
    <Image
      src={imageSrc}
      alt={alt}
      width={width}
      height={height}
      className={className}
      priority={priority}
      onError={handleError}
      onLoad={handleLoad}
      style={{ objectFit: 'cover', ...style }}
      onMouseOver={onMouseOver}
      onMouseOut={onMouseOut}
    />
  );
}
