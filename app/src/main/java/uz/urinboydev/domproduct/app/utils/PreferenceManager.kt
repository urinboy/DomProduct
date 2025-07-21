package uz.urinboydev.domproduct.app.utils

import android.content.Context
import android.content.SharedPreferences
import uz.urinboydev.domproduct.app.constants.ApiConstants
import uz.urinboydev.domproduct.app.models.User

class PreferenceManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(ApiConstants.PREF_NAME, Context.MODE_PRIVATE)

    // ===== TOKEN MANAGEMENT =====
    fun saveToken(token: String) {
        sharedPreferences.edit()
            .putString(ApiConstants.PREF_TOKEN, token)
            .apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(ApiConstants.PREF_TOKEN, null)
    }

    fun clearToken() {
        sharedPreferences.edit()
            .remove(ApiConstants.PREF_TOKEN)
            .apply()
    }

    fun hasToken(): Boolean {
        return !getToken().isNullOrEmpty()
    }

    // ===== USER DATA MANAGEMENT =====
    fun saveUser(user: User) {
        sharedPreferences.edit()
            .putInt(ApiConstants.PREF_USER_ID, user.id)
            .putString(ApiConstants.PREF_USER_NAME, user.name)
            .putString(ApiConstants.PREF_USER_EMAIL, user.email)
            .apply()
    }

    fun getUserId(): Int {
        return sharedPreferences.getInt(ApiConstants.PREF_USER_ID, -1)
    }

    fun getUserName(): String? {
        return sharedPreferences.getString(ApiConstants.PREF_USER_NAME, null)
    }

    fun getUserEmail(): String? {
        return sharedPreferences.getString(ApiConstants.PREF_USER_EMAIL, null)
    }

    // ===== AUTHENTICATION STATUS =====
    fun isLoggedIn(): Boolean {
        return hasToken() && getUserId() != -1
    }

    // ===== LOGOUT =====
    fun logout() {
        sharedPreferences.edit().clear().apply()
    }

    // ===== APP SETTINGS =====
    fun saveAppSettings(key: String, value: String) {
        sharedPreferences.edit()
            .putString(key, value)
            .apply()
    }

    fun getAppSettings(key: String, defaultValue: String = ""): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun saveAppSettings(key: String, value: Boolean) {
        sharedPreferences.edit()
            .putBoolean(key, value)
            .apply()
    }

    fun getAppSettings(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun saveAppSettings(key: String, value: Int) {
        sharedPreferences.edit()
            .putInt(key, value)
            .apply()
    }

    fun getAppSettings(key: String, defaultValue: Int = 0): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    // ===== CART SETTINGS =====
    fun saveCartCount(count: Int) {
        sharedPreferences.edit()
            .putInt("cart_count", count)
            .apply()
    }

    fun getCartCount(): Int {
        return sharedPreferences.getInt("cart_count", 0)
    }
}