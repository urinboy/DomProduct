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