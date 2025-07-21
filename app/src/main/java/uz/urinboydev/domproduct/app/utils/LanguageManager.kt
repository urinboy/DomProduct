package uz.urinboydev.domproduct.app.utils

import android.content.Context
import android.content.res.Configuration
import uz.urinboydev.domproduct.app.models.LanguageItem
import java.util.*

class LanguageManager(private val context: Context) {

    private val preferenceManager = PreferenceManager(context)

    companion object {
        const val UZBEK = "uz"
        const val RUSSIAN = "ru"
        const val DEFAULT_LANGUAGE = UZBEK

        private const val TAG = "LanguageManager"
    }

    /**
     * Tilni o'rnatish
     */
    fun setLanguage(languageCode: String) {
        android.util.Log.d(TAG, "Setting language to: $languageCode")

        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)

        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        // SharedPreferences ga saqlash
        preferenceManager.saveAppSettings("selected_language", languageCode)

        android.util.Log.d(TAG, "Language set and saved: $languageCode")
    }

    /**
     * Joriy tilni olish
     */
    fun getCurrentLanguage(): String {
        val savedLanguage = preferenceManager.getAppSettings("selected_language", "")

        // Agar saqlangan til bo'sh bo'lsa, system locale ni tekshirish
        if (savedLanguage.isEmpty()) {
            val systemLanguage = getSystemLanguage()
            android.util.Log.d(TAG, "No saved language, checking system: $systemLanguage")

            // Agar system language bizning supported languages dan biri bo'lsa
            return if (systemLanguage in listOf(UZBEK, RUSSIAN)) {
                android.util.Log.d(TAG, "Using system language: $systemLanguage")
                systemLanguage
            } else {
                android.util.Log.d(TAG, "System language not supported, using default: $DEFAULT_LANGUAGE")
                DEFAULT_LANGUAGE
            }
        }

        android.util.Log.d(TAG, "Current language from preferences: $savedLanguage")
        return savedLanguage
    }

    /**
     * System tilini olish
     */
    private fun getSystemLanguage(): String {
        val systemLocale = Locale.getDefault()
        return systemLocale.language
    }

    /**
     * Til tanlanganmi tekshirish
     */
    fun isLanguageSelected(): Boolean {
        return preferenceManager.getAppSettings("language_selected", false)
    }

    /**
     * Tilni tanlanganlik holatini o'rnatish
     */
    fun setLanguageSelected(selected: Boolean) {
        preferenceManager.saveAppSettings("language_selected", selected)
    }

    /**
     * Mavjud tillar ro'yxati
     */
    fun getAvailableLanguages(): List<LanguageItem> {
        return listOf(
            LanguageItem(UZBEK, "O'zbekcha", "🇺🇿"),
            LanguageItem(RUSSIAN, "Русский", "🇷🇺")
        )
    }

    /**
     * Til nomini olish
     */
    fun getLanguageName(languageCode: String): String {
        return when (languageCode) {
            UZBEK -> "O'zbekcha"
            RUSSIAN -> "Русский"
            else -> "O'zbekcha"
        }
    }

    /**
     * Til flagini olish
     */
    fun getLanguageFlag(languageCode: String): String {
        return when (languageCode) {
            UZBEK -> "🇺🇿"
            RUSSIAN -> "🇷🇺"
            else -> "🇺🇿"
        }
    }

    /**
     * Joriy til ma'lumotlarini olish
     */
    fun getCurrentLanguageInfo(): LanguageItem {
        val currentCode = getCurrentLanguage()
        return LanguageItem(
            code = currentCode,
            name = getLanguageName(currentCode),
            flag = getLanguageFlag(currentCode)
        )
    }

    /**
     * Application context uchun tilni o'rnatish
     */
    fun attachBaseContext(context: Context): Context {
        val language = getCurrentLanguage()
        return updateResources(context, language)
    }

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)

        return context.createConfigurationContext(configuration)
    }

    /**
     * Tilni o'zgartirish va activity'ni restart qilish
     */
    fun changeLanguageAndRestart(context: Context, languageCode: String) {
        android.util.Log.d(TAG, "Changing language and restarting: $languageCode")

        setLanguage(languageCode)

        // Agar context Activity bo'lsa, restart qilish
        if (context is android.app.Activity) {
            context.recreate()
        }
    }
}
