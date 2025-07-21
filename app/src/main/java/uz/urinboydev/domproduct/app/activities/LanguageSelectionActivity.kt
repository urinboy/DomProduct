package uz.urinboydev.domproduct.app.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import uz.urinboydev.domproduct.app.R
import uz.urinboydev.domproduct.app.databinding.ActivityLanguageSelectionBinding
import uz.urinboydev.domproduct.app.utils.PreferenceManager
import uz.urinboydev.domproduct.app.utils.LanguageManager

class LanguageSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLanguageSelectionBinding
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var languageManager: LanguageManager

    // Hozir tanlangan til - default emas, saqlanganni olish
    private var selectedLanguageCode = ""

    companion object {
        private const val TAG = "LanguageSelection"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate started")

        try {
            // ViewBinding setup
            binding = ActivityLanguageSelectionBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Managers setup
            preferenceManager = PreferenceManager(this)
            languageManager = LanguageManager(this)

            // Status bar ni yashirish
            supportActionBar?.hide()

            Log.d(TAG, "Setup completed")

            // Avval saqlangan tilni olish
            loadCurrentLanguage()

            // UI setup
            setupClickListeners()

            // Tanlangan tilni ko'rsatish
            updateLanguageCards()

            Log.d(TAG, "onCreate finished successfully")

        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate: ${e.message}", e)
        }
    }

    /**
     * Hozir saqlangan tilni yuklash
     */
    private fun loadCurrentLanguage() {
        // LanguageManager dan hozirgi tilni olish
        selectedLanguageCode = languageManager.getCurrentLanguage()

        Log.d(TAG, "Current saved language: $selectedLanguageCode")

        // Agar hech qanday til tanlanmagan bo'lsa, default o'zbek
        if (selectedLanguageCode.isEmpty()) {
            selectedLanguageCode = LanguageManager.DEFAULT_LANGUAGE
            Log.d(TAG, "No language saved, using default: $selectedLanguageCode")
        }
    }

    private fun setupClickListeners() {
        Log.d(TAG, "Setting up click listeners")

        try {
            // O'zbek tili
            binding.uzbekLanguageCard.setOnClickListener {
                Log.d(TAG, "Uzbek language clicked")
                selectLanguage("uz")
            }

            // Rus tili
            binding.russianLanguageCard.setOnClickListener {
                Log.d(TAG, "Russian language clicked")
                selectLanguage("ru")
            }

            // Davom etish tugmasi
            binding.continueButton.setOnClickListener {
                Log.d(TAG, "Continue button clicked")
                saveLanguageAndContinue()
            }

            // O'tkazib yuborish
            binding.skipButton.setOnClickListener {
                Log.d(TAG, "Skip button clicked")
                saveLanguageAndContinue()
            }

            Log.d(TAG, "Click listeners setup completed")

        } catch (e: Exception) {
            Log.e(TAG, "Error setting up click listeners: ${e.message}", e)
        }
    }

    private fun selectLanguage(languageCode: String) {
        Log.d(TAG, "Selecting language: $languageCode (previous: $selectedLanguageCode)")

        try {
            selectedLanguageCode = languageCode

            // Kartalarni yangilash
            updateLanguageCards()

            Log.d(TAG, "Language selected: $languageCode")

        } catch (e: Exception) {
            Log.e(TAG, "Error selecting language: ${e.message}", e)
        }
    }

    private fun updateLanguageCards() {
        Log.d(TAG, "Updating language cards for: $selectedLanguageCode")

        try {
            // O'zbek tili kardi
            if (selectedLanguageCode == "uz") {
                // Tanlangan - ko'k background
                binding.uzbekLanguageCard.setCardBackgroundColor(Color.parseColor("#2196F3"))
                binding.uzbekCheckIcon.visibility = View.VISIBLE
                binding.uzbekLanguageText.setTextColor(Color.WHITE)
                binding.uzbekLanguageSubtext.setTextColor(Color.WHITE)

                // Rus tili - tanlanmagan
                binding.russianLanguageCard.setCardBackgroundColor(Color.WHITE)
                binding.russianCheckIcon.visibility = View.GONE
                binding.russianLanguageText.setTextColor(Color.BLACK)
                binding.russianLanguageSubtext.setTextColor(Color.GRAY)

            } else if (selectedLanguageCode == "ru") {
                // Rus tili tanlangan
                binding.russianLanguageCard.setCardBackgroundColor(Color.parseColor("#2196F3"))
                binding.russianCheckIcon.visibility = View.VISIBLE
                binding.russianLanguageText.setTextColor(Color.WHITE)
                binding.russianLanguageSubtext.setTextColor(Color.WHITE)

                // O'zbek tili - tanlanmagan
                binding.uzbekLanguageCard.setCardBackgroundColor(Color.WHITE)
                binding.uzbekCheckIcon.visibility = View.GONE
                binding.uzbekLanguageText.setTextColor(Color.BLACK)
                binding.uzbekLanguageSubtext.setTextColor(Color.GRAY)
            }

            Log.d(TAG, "Language cards updated successfully for: $selectedLanguageCode")

        } catch (e: Exception) {
            Log.e(TAG, "Error updating language cards: ${e.message}", e)
        }
    }

    private fun saveLanguageAndContinue() {
        Log.d(TAG, "Saving language and continuing: $selectedLanguageCode")

        try {
            // Tilni saqlash
            preferenceManager.saveAppSettings("selected_language", selectedLanguageCode)
            preferenceManager.saveAppSettings("language_selected", true)

            // Tilni o'rnatish
            languageManager.setLanguage(selectedLanguageCode)

            Log.d(TAG, "Language saved: $selectedLanguageCode")

            // Keyingi ekranga o'tish
            val intent = if (preferenceManager.isLoggedIn()) {
                Log.d(TAG, "User is logged in, going to MainActivity")
                Intent(this, MainActivity::class.java)
            } else {
                Log.d(TAG, "User not logged in, going to LoginActivity")
                Intent(this, LoginActivity::class.java)
            }

            startActivity(intent)
            finish()

            // Simple transition
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            Log.d(TAG, "Navigation completed")

        } catch (e: Exception) {
            Log.e(TAG, "Error saving language and continuing: ${e.message}", e)

            // Agar xato bo'lsa, hech bo'lmasa keyingi ekranga o'tish
            try {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e2: Exception) {
                Log.e(TAG, "Critical error: ${e2.message}", e2)
            }
        }
    }

    // Back button ni disable qilish
    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        Log.d(TAG, "Back button pressed - ignoring")
        // Language selection da back button ishlamasin
    }
}
