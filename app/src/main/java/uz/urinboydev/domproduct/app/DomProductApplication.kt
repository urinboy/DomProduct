package uz.urinboydev.domproduct.app

import android.app.Application
import android.content.Context

import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DomProductApplication : Application() {

    // Hilt avtomatik boshqaradi, shuning uchun bu qatorlar keraksiz
    // private lateinit var languageManager: LanguageManager

    override fun onCreate() {
        super.onCreate()

        // ApiClient ni initialize qilish (Hilt orqali amalga oshiriladi)
        // ApiClient.initialize(this)

        // Language Manager setup (Hilt orqali amalga oshiriladi)
        // languageManager = LanguageManager(this)

        // Saqlangan tilni o'rnatish (Hilt orqali olingan LanguageManager ishlatiladi)
        // val currentLanguage = languageManager.getCurrentLanguage()
        // languageManager.setLanguage(currentLanguage)
    }

    override fun attachBaseContext(base: Context) {
        // Hilt orqali olingan LanguageManager ishlatiladi
        // val languageManager = LanguageManager(base)
        super.attachBaseContext(base)
    }
}