package uz.urinboydev.domproduct.app

import android.app.Application
import android.content.Context
import uz.urinboydev.domproduct.app.api.ApiClient // ApiClient ni import qilish
import uz.urinboydev.domproduct.app.utils.LanguageManager

class DomProductApplication : Application() {

    private lateinit var languageManager: LanguageManager

    override fun onCreate() {
        super.onCreate()

        // ApiClient ni initialize qilish
        ApiClient.initialize(this)

        // Language Manager setup
        languageManager = LanguageManager(this)

        // Saqlangan tilni o'rnatish
        val currentLanguage = languageManager.getCurrentLanguage()
        languageManager.setLanguage(currentLanguage)
    }

    override fun attachBaseContext(base: Context) {
        val languageManager = LanguageManager(base)
        super.attachBaseContext(languageManager.attachBaseContext(base))
    }
}