import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';

// Ресурсы переводов
const resources = {
  uz: {
    translation: {
      // Навигация
      "home": "Bosh sahifa",
      "products": "Mahsulotlar",
      "categories": "Turkumlar",
      "cart": "Savat",
      "orders": "Buyurtmalar",
      "profile": "Profil",
      "wishlist": "Sevimlilar",
      "search": "Qidirish",
      "login": "Kirish",
      "register": "Ro'yxatdan o'tish",
      "logout": "Chiqish",
      
      // Общие термины
      "loading": "Yuklanmoqda...",
      "price": "Narxi",
      "add_to_cart": "Savatga qo'shish",
      "buy_now": "Hozir sotib olish",
      "view_details": "Batafsil ko'rish",
      "search_placeholder": "Mahsulot izlash...",
      "no_results": "Hech narsa topilmadi",
      
      // Карточка товара
      "product_added_to_cart": "Mahsulot savatga qo'shildi",
      "product_added_to_wishlist": "Mahsulot sevimlilarga qo'shildi",
      "product_removed_from_wishlist": "Mahsulot sevimlilardan olib tashlandi",
      
      // Корзина
      "cart_empty": "Savat bo'sh",
      "cart_total": "Jami",
      "checkout": "To'lov",
      "remove_from_cart": "Savatdan olib tashlash",
      
      // Профиль
      "edit_profile": "Profilni tahrirlash",
      "save_changes": "O'zgarishlarni saqlash",
      "name": "Ism",
      "email": "Email",
      "phone": "Telefon",
      
      // Заказы
      "order_history": "Buyurtmalar tarixi",
      "order_status": "Buyurtma holati",
      "order_date": "Buyurtma sanasi",
      
      // Статусы заказа
      "pending": "Kutilmoqda",
      "processing": "Jarayonda",
      "shipped": "Yuborildi",
      "delivered": "Yetkazildi",
      "cancelled": "Bekor qilindi"
    }
  },
  ru: {
    translation: {
      // Навигация
      "home": "Главная",
      "products": "Товары",
      "categories": "Категории",
      "cart": "Корзина",
      "orders": "Заказы",
      "profile": "Профиль",
      "wishlist": "Избранное",
      "search": "Поиск",
      "login": "Войти",
      "register": "Регистрация",
      "logout": "Выйти",
      
      // Общие термины
      "loading": "Загрузка...",
      "price": "Цена",
      "add_to_cart": "В корзину",
      "buy_now": "Купить сейчас",
      "view_details": "Подробнее",
      "search_placeholder": "Поиск товаров...",
      "no_results": "Ничего не найдено",
      
      // Карточка товара
      "product_added_to_cart": "Товар добавлен в корзину",
      "product_added_to_wishlist": "Товар добавлен в избранное",
      "product_removed_from_wishlist": "Товар удален из избранного",
      
      // Корзина
      "cart_empty": "Корзина пуста",
      "cart_total": "Итого",
      "checkout": "Оформить заказ",
      "remove_from_cart": "Удалить из корзины",
      
      // Профиль
      "edit_profile": "Редактировать профиль",
      "save_changes": "Сохранить изменения",
      "name": "Имя",
      "email": "Email",
      "phone": "Телефон",
      
      // Заказы
      "order_history": "История заказов",
      "order_status": "Статус заказа",
      "order_date": "Дата заказа",
      
      // Статусы заказа
      "pending": "Ожидает",
      "processing": "Обрабатывается",
      "shipped": "Отправлен",
      "delivered": "Доставлен",
      "cancelled": "Отменен"
    }
  }
};

i18n
  .use(initReactI18next)
  .init({
    resources,
    lng: 'uz',
    fallbackLng: 'uz',
    
    interpolation: {
      escapeValue: false
    }
  });

export default i18n;
