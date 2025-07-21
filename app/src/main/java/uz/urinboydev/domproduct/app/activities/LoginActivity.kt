package uz.urinboydev.domproduct.app.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
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

        // ViewBinding setup
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Managers setup
        preferenceManager = PreferenceManager(this)
        languageManager = LanguageManager(this)

        // Status bar ni yashirish
        supportActionBar?.hide()

        // Setup UI
        setupClickListeners()

        Log.d(TAG, "onCreate completed")
    }

    private fun setupClickListeners() {
        // Login button
        binding.loginButton.setOnClickListener {
            performLogin()
        }

        // Register link
        binding.registerText.setOnClickListener {
            openRegisterActivity()
        }

        // Forgot password
        binding.forgotPasswordText.setOnClickListener {
            showForgotPasswordDialog()
        }

        // Change language
        binding.changeLanguageText.setOnClickListener {
            openLanguageSelection()
        }

        // Guest button - YANGI!
        binding.guestButton.setOnClickListener {
            continueAsGuest()
        }
    }

    private fun performLogin() {
        Log.d(TAG, "Performing login")

        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        // Validation
        if (!validateInput(email, password)) {
            return
        }

        // Show loading
        showLoading(true)

        // API call
        lifecycleScope.launch {
            try {
                val loginRequest = LoginRequest(email, password)
                val response = ApiHelper.getApi().login(loginRequest)

                if (ApiHelper.isSuccessful(response)) {
                    // Login successful
                    val authResponse = response.body()?.data
                    if (authResponse != null) {
                        // Save user data
                        preferenceManager.saveToken(authResponse.token)
                        preferenceManager.saveUser(authResponse.user)

                        // Save remember me
                        if (binding.rememberMeCheckBox.isChecked) {
                            preferenceManager.saveAppSettings("remember_login", true)
                        }

                        Log.d(TAG, "Login successful for user: ${authResponse.user.email}")

                        // Show success message
                        showMessage(getString(R.string.login_success))

                        // Navigate to MainActivity
                        navigateToMainActivity()
                    }
                } else {
                    // Login failed
                    val errorMessage = ApiHelper.getErrorMessage(response)
                    Log.e(TAG, "Login failed: $errorMessage")
                    showMessage(errorMessage)
                }

            } catch (e: Exception) {
                Log.e(TAG, "Login error: ${e.message}", e)
                showMessage(getString(R.string.network_error))
            } finally {
                showLoading(false)
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        var isValid = true

        // Email validation
        if (email.isEmpty()) {
            binding.emailInputLayout.error = getString(R.string.email_required)
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailInputLayout.error = getString(R.string.email_invalid)
            isValid = false
        } else {
            binding.emailInputLayout.error = null
        }

        // Password validation
        if (password.isEmpty()) {
            binding.passwordInputLayout.error = getString(R.string.password_required)
            isValid = false
        } else if (password.length < 6) {
            binding.passwordInputLayout.error = getString(R.string.password_min_length)
            isValid = false
        } else {
            binding.passwordInputLayout.error = null
        }

        return isValid
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            binding.loginButton.visibility = View.GONE
            binding.loginProgressBar.visibility = View.VISIBLE
        } else {
            binding.loginButton.visibility = View.VISIBLE
            binding.loginProgressBar.visibility = View.GONE
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun continueAsGuest() {
        Log.d(TAG, "Continue as guest clicked")

        // Guest mode ni enable qilish
        AuthManager.loginAsGuest(this)

        // Success message
        showMessage(getString(R.string.guest_mode_enabled))

        // MainActivity ga o'tish
        navigateToMainActivity()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

//    private fun navigateToMainActivity() {
//        val intent = Intent(this, MainActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        startActivity(intent)
//        finish()
//        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
//    }

    private fun openRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
//    private fun openRegisterActivity() {
//        // Register Activity yasash kerak
//        showMessage("Register oynasi hali tayyor emas")
//    }

    private fun showForgotPasswordDialog() {
        // Forgot password dialog
        showMessage("Parolni tiklash hali tayyor emas")
    }

    private fun openLanguageSelection() {
        val intent = Intent(this, LanguageSelectionActivity::class.java)
        startActivity(intent)
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // Login ekranidan back button bilan chiqish
        finish()
    }
}