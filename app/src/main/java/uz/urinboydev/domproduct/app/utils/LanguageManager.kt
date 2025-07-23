package uz.urinboydev.domproduct.app.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageManager @Inject constructor(private val context: Context) {

    private val PREF_NAME = "LanguagePrefs"
    private val KEY_LANGUAGE = "selected_language"
    private val KEY_LANGUAGE_SELECTED = "is_language_selected"

    private val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun setLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply()
        prefs.edit().putBoolean(KEY_LANGUAGE_SELECTED, true).apply()
    }

    fun getCurrentLanguage(): String {
        return prefs.getString(KEY_LANGUAGE, Locale.getDefault().language) ?: Locale.getDefault().language
    }

    fun isLanguageSelected(): Boolean {
        return prefs.getBoolean(KEY_LANGUAGE_SELECTED, false)
    }

    fun markLanguageSelected() {
        prefs.edit().putBoolean(KEY_LANGUAGE_SELECTED, true).apply()
    }

    fun markLanguageNotSelected() {
        prefs.edit().putBoolean(KEY_LANGUAGE_SELECTED, false).apply()
    }
}