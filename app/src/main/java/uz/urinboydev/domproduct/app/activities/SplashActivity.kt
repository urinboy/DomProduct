package uz.urinboydev.domproduct.app.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import uz.urinboydev.domproduct.app.databinding.ActivitySplashBinding
import uz.urinboydev.domproduct.app.utils.PreferenceManager
import uz.urinboydev.domproduct.app.utils.LanguageManager

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var languageManager: LanguageManager

    // Splash screen ko'rsatish vaqti (millisekund)
    private val SPLASH_DELAY = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding setup
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Managers setup
        preferenceManager = PreferenceManager(this)
        languageManager = LanguageManager(this)

        // Saqlangan tilni o'rnatish
        setupLanguage()

        // Status bar ni yashirish (fullscreen)
        supportActionBar?.hide()

        // Splash animatsiyasini boshlash
        startSplashAnimation()

        // Handler bilan keyingi ekranga o'tish
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, SPLASH_DELAY)
    }

    private fun setupLanguage() {
        val currentLanguage = languageManager.getCurrentLanguage()
        languageManager.setLanguage(currentLanguage)
    }

    private fun startSplashAnimation() {
        // Logo animatsiyasi
        binding.logoImageView.apply {
            alpha = 0f
            scaleX = 0.5f
            scaleY = 0.5f

            animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(1000)
                .start()
        }

        // App nomi animatsiyasi
        binding.appNameTextView.apply {
            alpha = 0f
            translationY = 50f

            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(1000)
                .setStartDelay(500)
                .start()
        }

        // Loading indicator animatsiyasi
        binding.loadingProgressBar.apply {
            alpha = 0f

            animate()
                .alpha(1f)
                .setDuration(500)
                .setStartDelay(1000)
                .start()
        }

        // Slogan animatsiyasi
        binding.sloganTextView.apply {
            alpha = 0f
            translationY = 30f

            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(800)
                .setStartDelay(1200)
                .start()
        }
    }

//    private fun navigateToNextScreen() {
//        // Test uchun to'g'ridan-to'g'ri LanguageSelection ga o'tish
//        val intent = Intent(this, LanguageSelectionActivity::class.java)
//        startActivity(intent)
//        finish()
//    }

//    private fun navigateToNextScreen() {
//        val intent = when {
//            // Test uchun to'g'ridan-to'g'ri MainActivity
//            !languageManager.isLanguageSelected() -> {
//                Intent(this, LanguageSelectionActivity::class.java)
//            }
//            // MainActivity ga o'tkazish (test uchun)
//            else -> {
//                Intent(this, MainActivity::class.java)
//            }
//        }
//
//        startActivity(intent)
//        finish()
//    }

    private fun navigateToNextScreen() {
        val intent = when {
            // 1. Til tanlanmaganmi? -> Language Selection
            !languageManager.isLanguageSelected() -> {
                Intent(this, LanguageSelectionActivity::class.java)
            }
            // 2. User login qilganmi? -> MainActivity
            preferenceManager.isLoggedIn() -> {
                Intent(this, MainActivity::class.java)
            }
            // 3. Aks holda -> LoginActivity
            else -> {
                Intent(this, LoginActivity::class.java)
            }
        }

        startActivity(intent)
        finish()

        // Transition animatsiyasi
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    // Back button ni disable qilish
    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // Splash screen da back button ishlamasin
    }
}