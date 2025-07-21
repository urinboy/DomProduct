package uz.urinboydev.domproduct.app.constants

object ApiConstants {
    // Base URL
    const val BASE_URL = "https://dompro.itorda.uz/"
    const val API_VERSION = "api/v1/"
    const val FULL_URL = BASE_URL + API_VERSION

    // Auth endpoints
    const val LOGIN = "auth/login"
    const val REGISTER = "auth/register"
    const val LOGOUT = "auth/logout"
    const val ME = "auth/me"

    // Categories endpoints
    const val CATEGORIES = "categories"
    const val CATEGORIES_MAIN = "categories/main"

    // Products endpoints
    const val PRODUCTS = "products"
    const val PRODUCTS_FEATURED = "products/featured"
    const val PRODUCTS_SEARCH = "products/search"
    const val PRODUCTS_BY_CATEGORY = "products/category/{categoryId}"

    // Cart endpoints
    const val CART = "cart"

    // Cities endpoints
    const val CITIES = "cities"
    const val CITIES_DELIVERY = "cities/delivery-available"

    // Addresses endpoints
    const val ADDRESSES = "addresses"
    const val ADDRESSES_SET_DEFAULT = "addresses/{id}/set-default"

    // Orders endpoints
    const val ORDERS = "orders"
    const val ORDERS_CANCEL = "orders/{id}/cancel"
    const val ORDERS_REORDER = "orders/{id}/reorder"
    const val ORDERS_STATUS_HISTORY = "orders/{id}/status-history"

    // Reviews endpoints
    const val REVIEWS = "reviews"
    const val PRODUCTS_REVIEWS = "products/{productId}/reviews"

    // Pagination
    const val DEFAULT_PAGE_SIZE = 20
    const val MAX_PAGE_SIZE = 100

    // Order statuses
    const val ORDER_STATUS_PENDING = "pending"
    const val ORDER_STATUS_CONFIRMED = "confirmed"
    const val ORDER_STATUS_PROCESSING = "processing"
    const val ORDER_STATUS_SHIPPED = "shipped"
    const val ORDER_STATUS_DELIVERED = "delivered"
    const val ORDER_STATUS_CANCELLED = "cancelled"

    // Payment methods
    const val PAYMENT_METHOD_CASH = "cash"
    const val PAYMENT_METHOD_CARD = "card"
    const val PAYMENT_METHOD_TRANSFER = "transfer"

    // Headers
    const val CONTENT_TYPE = "application/json"
    const val ACCEPT = "application/json"
    const val ACCEPT_LANGUAGE = "Accept-Language"
    const val AUTHORIZATION = "Authorization"
    const val BEARER = "Bearer "

    // SharedPreferences keys
    const val PREF_NAME = "DomProductPrefs"
    const val PREF_TOKEN = "auth_token"
    const val PREF_USER_ID = "user_id"
    const val PREF_USER_NAME = "user_name"
    const val PREF_USER_EMAIL = "user_email"
    const val PREF_USER_PHONE = "user_phone"
    const val PREF_USER_ROLE = "user_role"
    const val PREF_USER_AVATAR = "user_avatar"
    const val PREF_USER_CITY_ID = "user_city_id"

    // Request codes
    const val REQUEST_LOGIN = 1001
    const val REQUEST_REGISTER = 1002

    // Error messages
    const val ERROR_NETWORK = "Internet aloqasini tekshiring"
    const val ERROR_SERVER = "Server xatoligi"
    const val ERROR_UNKNOWN = "Noma'lum xato"

}