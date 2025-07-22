package uz.urinboydev.domproduct.app.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson // Gson ni import qilish
import kotlinx.coroutines.launch
import uz.urinboydev.domproduct.app.R
import uz.urinboydev.domproduct.app.api.ApiHelper
import uz.urinboydev.domproduct.app.databinding.ActivityLoginBinding
import uz.urinboydev.domproduct.app.models.LoginRequest
import uz.urinboydev.domproduct.app.utils.AuthManager
import uz.urinboydev.domproduct.app.utils.PreferenceManager
import uz.urinboydev.domproduct.app.utils.LanguageManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var languageManager: LanguageManager

    companion object {
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate started")

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)
        languageManager = LanguageManager(this)

        supportActionBar?.hide()

        setupClickListeners()

        Log.d(TAG, "onCreate completed")
    }

    private fun setupClickListeners() {
        binding.loginButton.setOnClickListener {
            performLogin()
        }

        binding.registerText.setOnClickListener {
            openRegisterActivity()
        }

        binding.forgotPasswordText.setOnClickListener {
            showForgotPasswordDialog()
        }

        binding.changeLanguageText.setOnClickListener {
            openLanguageSelection()
        }

        binding.guestButton.setOnClickListener {
            continueAsGuest()
        }
    }

    private fun performLogin() {
        Log.d(TAG, "Performing login")

        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        // Input validation
        if (!validateInput(email, password)) {
            Log.d(TAG, "Input validation failed.")
            return
        }

        // Show loading state
        showLoading(true)

        // API call
        lifecycleScope.launch {
            try {
                val loginRequest = LoginRequest(email, password)
                // Yuborilayotgan requestni loglash
                Log.d(TAG, "Sending LoginRequest: ${Gson().toJson(loginRequest)}")

                val response = ApiHelper.getApi().login(loginRequest)

                // API javobini loglash
                Log.d(TAG, "API Response Status Code: ${response.code()}")
                // response.errorBody()?.string() ni bir marta o'qish kerak, aks holda keyingi chaqiruvlarda null bo'ladi.
                val rawBody = response.errorBody()?.string() ?: response.body()?.toString()
                Log.d(TAG, "API Response Raw Body: $rawBody")

                if (ApiHelper.isSuccessful(response)) {
                    val authResponse = response.body()?.data
                    if (authResponse != null) {
                        preferenceManager.saveToken(authResponse.token)
                        preferenceManager.saveUser(authResponse.user)

                        if (binding.rememberMeCheckBox.isChecked) {
                            preferenceManager.saveAppSettings("remember_login", true)
                        } else {
                            preferenceManager.saveAppSettings("remember_login", false)
                        }

                        AuthManager.logout(this@LoginActivity) // Mehmon rejimini o'chirish
                        Log.d(TAG, "Login successful for user: ${authResponse.user.email}")
                        showMessage(getString(R.string.login_success))
                        navigateToMainActivity()
                    } else {
                        Log.e(TAG, "AuthResponse data is null after successful API call.")
                        showMessage(getString(R.string.login_failed_generic)) // Umumiy xato xabari
                    }
                } else {
                    // Xato body ni qayta o'qish uchun yangi response yaratish kerak bo'ladi,
                    // yoki getErrorMessage va getValidationErrors metodlarini errorBody() ni iste'mol qilmasdan yozish kerak.
                    // Hozircha rawBody dan foydalanamiz
                    val errorMessage = ApiHelper.getErrorMessageFromRawBody(rawBody) // Yangi metod
                    Log.e(TAG, "Login failed with error: $errorMessage")
                    // Backenddan kelgan validation xatolarini ko'rsatish
                    ApiHelper.getValidationErrorsFromRawBody(rawBody)?.let { errors ->
                        showValidationErrors(errors)
                    }
                    // Agar maxsus xabar bo'lmasa, umumiy xabar ko'rsatish
                    showMessage(errorMessage.ifEmpty { getString(R.string.login_failed_generic) })
                }

            } catch (e: Exception) {
                Log.e(TAG, "Login network error: ${e.message}", e)
                showMessage(getString(R.string.network_error))
            } finally {
                showLoading(false)
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        var isValid = true

        // Oldingi xatolarni tozalash
        binding.emailInputLayout.error = null
        binding.passwordInputLayout.error = null

        if (email.isEmpty()) {
            binding.emailInputLayout.error = getString(R.string.email_required)
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailInputLayout.error = getString(R.string.email_invalid)
            isValid = false
        }

        if (password.isEmpty()) {
            binding.passwordInputLayout.error = getString(R.string.password_required)
            isValid = false
        } else if (password.length < 6) { // Parol uzunligi kamida 6 ta belgi bo'lishi kerak
            binding.passwordInputLayout.error = getString(R.string.password_min_length)
            isValid = false
        }

        return isValid
    }

    // Backenddan kelgan validation xatolarini ko'rsatish uchun
    // Endi Map<String, List<String>> ni to'g'ridan-to'g'ri qabul qiladi
    private fun showValidationErrors(errors: Map<String, List<String>>) {
        // Avvalgi xatolarni tozalash
        binding.emailInputLayout.error = null
        binding.passwordInputLayout.error = null

        errors["email"]?.firstOrNull()?.let {
            binding.emailInputLayout.error = it
            Log.d(TAG, "Validation error for email: $it")
        }
        errors["password"]?.firstOrNull()?.let {
            binding.passwordInputLayout.error = it
            Log.d(TAG, "Validation error for password: $it")
        }
        // Agar boshqa umumiy xatolar bo'lsa, ularni ham ko'rsatish
        errors["error"]?.firstOrNull()?.let {
            showMessage(it)
            Log.d(TAG, "General validation error: $it")
        }
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            binding.loginButton.visibility = View.GONE
            binding.guestButton.visibility = View.GONE
            binding.loginProgressBar.visibility = View.VISIBLE
        } else {
            binding.loginButton.visibility = View.VISIBLE
            binding.guestButton.visibility = View.VISIBLE
            binding.loginProgressBar.visibility = View.GONE
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun continueAsGuest() {
        Log.d(TAG, "Continue as guest clicked")
        AuthManager.loginAsGuest(this)
        showMessage(getString(R.string.guest_mode_enabled))
        navigateToMainActivity()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
        // overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right) // Deprecated, olib tashlandi
    }

    private fun openRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        // overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right) // Deprecated, olib tashlandi
    }

    private fun showForgotPasswordDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.forgot_password))
            .setMessage("Parolni tiklash funksiyasi hali tayyor emas. Iltimos, keyinroq urinib ko'ring yoki qo'llab-quvvatlash xizmati bilan bog'laning.")
            .setPositiveButton("Tushunarli") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun openLanguageSelection() {
        val intent = Intent(this, LanguageSelectionActivity::class.java)
        startActivity(intent)
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        finish()
    }
}