// Примерные данные товаров для демонстрации
export const products = [
  {
    id: 1,
    name: "Smartfon Samsung Galaxy S24",
    nameUz: "Samsung Galaxy S24 Smartfon",
    price: 8999000,
    originalPrice: 9999000,
    discount: 10,
    category: "smartphones",
    categoryUz: "smartfonlar",
    brand: "Samsung",
    images: [
      "/api/placeholder/400/400",
      "/api/placeholder/400/400",
      "/api/placeholder/400/400"
    ],
    description: "Yangi Samsung Galaxy S24 - eng so'nggi texnologiyalar bilan",
    descriptionRu: "Новый Samsung Galaxy S24 - с новейшими технологиями",
    specifications: {
      display: "6.1 dyuym Dynamic AMOLED",
      processor: "Snapdragon 8 Gen 3",
      memory: "8GB RAM, 256GB ROM",
      camera: "50MP asosiy kamera",
      battery: "4000 mAh"
    },
    rating: 4.8,
    reviewsCount: 124,
    inStock: true,
    tags: ["yangi", "mashhur", "chegirma"]
  },
  {
    id: 2,
    name: "iPhone 15 Pro",
    nameUz: "iPhone 15 Pro",
    price: 12999000,
    originalPrice: 13999000,
    discount: 7,
    category: "smartphones", 
    categoryUz: "smartfonlar",
    brand: "Apple",
    images: [
      "/api/placeholder/400/400",
      "/api/placeholder/400/400"
    ],
    description: "Apple iPhone 15 Pro - professional darajadagi smartfon",
    descriptionRu: "Apple iPhone 15 Pro - смартфон профессионального уровня",
    specifications: {
      display: "6.1 dyuym ProMotion",
      processor: "A17 Pro chip",
      memory: "8GB RAM, 256GB ROM", 
      camera: "48MP Pro kamera tizimi",
      battery: "3274 mAh"
    },
    rating: 4.9,
    reviewsCount: 89,
    inStock: true,
    tags: ["yangi", "premium"]
  },
  {
    id: 3,
    name: "MacBook Air M2",
    nameUz: "MacBook Air M2",
    price: 14999000,
    originalPrice: 16999000,
    discount: 12,
    category: "laptops",
    categoryUz: "noutbuklar", 
    brand: "Apple",
    images: [
      "/api/placeholder/400/400"
    ],
    description: "MacBook Air M2 chip bilan - eng yengil va kuchli noutbuk",
    descriptionRu: "MacBook Air с чипом M2 - самый легкий и мощный ноутбук",
    specifications: {
      display: "13.6 dyuym Liquid Retina",
      processor: "Apple M2",
      memory: "8GB unified memory, 256GB SSD",
      graphics: "8-core GPU",
      battery: "18 soatgacha"
    },
    rating: 4.7,
    reviewsCount: 156,
    inStock: true,
    tags: ["yangi", "professional"]
  },
  {
    id: 4,
    name: "AirPods Pro 2",
    nameUz: "AirPods Pro 2",
    price: 2999000,
    originalPrice: 3299000,
    discount: 9,
    category: "audio",
    categoryUz: "audio",
    brand: "Apple", 
    images: [
      "/api/placeholder/400/400"
    ],
    description: "AirPods Pro 2 - eng ilg'or shovqin bekor qilish texnologiyasi",
    descriptionRu: "AirPods Pro 2 - с передовой технологией шумоподавления",
    specifications: {
      driver: "Custom high-excursion driver",
      chip: "H2 chip",
      anc: "2x kuchliroq ANC",
      battery: "6 soat + 24 soat case bilan",
      connection: "Bluetooth 5.3"
    },
    rating: 4.6,
    reviewsCount: 78,
    inStock: true,
    tags: ["audio", "wireless"]
  },
  {
    id: 5,
    name: "PlayStation 5",
    nameUz: "PlayStation 5",
    price: 7999000,
    originalPrice: 8499000,
    discount: 6,
    category: "gaming",
    categoryUz: "o'yin",
    brand: "Sony",
    images: [
      "/api/placeholder/400/400"
    ],
    description: "PlayStation 5 - yangi avlod o'yin konsoli",
    descriptionRu: "PlayStation 5 - игровая консоль нового поколения",
    specifications: {
      processor: "AMD Zen 2",
      graphics: "AMD RDNA 2",
      memory: "16GB GDDR6",
      storage: "825GB SSD",
      resolution: "4K UHD"
    },
    rating: 4.8,
    reviewsCount: 203,
    inStock: false,
    tags: ["o'yin", "konsol"]
  }
];

export const categories = [
  {
    id: "smartphones",
    name: "Smartfonlar",
    nameRu: "Смартфоны",
    icon: "📱",
    itemCount: 156
  },
  {
    id: "laptops", 
    name: "Noutbuklar",
    nameRu: "Ноутбуки",
    icon: "💻",
    itemCount: 89
  },
  {
    id: "audio",
    name: "Audio",
    nameRu: "Аудио",
    icon: "🎧",
    itemCount: 124
  },
  {
    id: "gaming",
    name: "O'yin",
    nameRu: "Игры",
    icon: "🎮",
    itemCount: 67
  },
  {
    id: "accessories",
    name: "Aksessuarlar", 
    nameRu: "Аксессуары",
    icon: "📋",
    itemCount: 234
  }
];

// Helper функции
export const getProductById = (id) => {
  return products.find(product => product.id === parseInt(id));
};

export const getProductsByCategory = (categoryId) => {
  return products.filter(product => product.category === categoryId);
};

export const searchProducts = (query) => {
  const lowerQuery = query.toLowerCase();
  return products.filter(product => 
    product.name.toLowerCase().includes(lowerQuery) ||
    product.nameUz.toLowerCase().includes(lowerQuery) ||
    product.description.toLowerCase().includes(lowerQuery) ||
    product.brand.toLowerCase().includes(lowerQuery) ||
    product.tags.some(tag => tag.toLowerCase().includes(lowerQuery))
  );
};

export const getFeaturedProducts = () => {
  return products.filter(product => product.tags.includes('mashhur'));
};

export const getDiscountedProducts = () => {
  return products.filter(product => product.discount > 0);
};
