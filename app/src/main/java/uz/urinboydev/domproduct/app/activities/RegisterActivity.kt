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
import uz.urinboydev.domproduct.app.databinding.ActivityRegisterBinding
import uz.urinboydev.domproduct.app.models.RegisterRequest
import uz.urinboydev.domproduct.app.utils.PreferenceManager
import uz.urinboydev.domproduct.app.utils.LanguageManager

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var languageManager: LanguageManager

    companion object {
        private const val TAG = "RegisterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate started")

        // ViewBinding setup
        binding = ActivityRegisterBinding.inflate(layoutInflater)
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
        // Register button
        binding.registerButton.setOnClickListener {
            performRegister()
        }

        // Login link
        binding.loginText.setOnClickListener {
            openLoginActivity()
        }

        // Terms link
        binding.termsLinkText.setOnClickListener {
            showTermsDialog()
        }

        // Change language
        binding.changeLanguageText.setOnClickListener {
            openLanguageSelection()
        }
    }

    private fun performRegister() {
        Log.d(TAG, "Performing register")

        val name = binding.nameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val phone = binding.phoneEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()

        // Validation
        if (!validateInput(name, email, phone, password, confirmPassword)) {
            return
        }

        // Terms checkbox check
        if (!binding.termsCheckBox.isChecked) {
            showMessage(getString(R.string.terms_required))
            return
        }

        // Show loading
        showLoading(true)

        // API call
        lifecycleScope.launch {
            try {
                val registerRequest = RegisterRequest(
                    name = name,
                    email = email,
                    phone = phone,
                    password = password,
                    passwordConfirmation = confirmPassword,
                    cityId = null // Hozircha null, keyinroq city selection qo'shiladi
                )
                val response = ApiHelper.getApi().register(registerRequest)

                if (ApiHelper.isSuccessful(response)) {
                    // Register successful
                    val authResponse = response.body()?.data
                    if (authResponse != null) {
                        // Save user data
                        preferenceManager.saveToken(authResponse.token)
                        preferenceManager.saveUser(authResponse.user)

                        Log.d(TAG, "Register successful for user: ${authResponse.user.email}")

                        // Show success message
                        showMessage(getString(R.string.register_success))

                        // Navigate to MainActivity
                        navigateToMainActivity()
                    }
                } else {
                    // Register failed
                    val errorMessage = ApiHelper.getErrorMessage(response)
                    Log.e(TAG, "Register failed: $errorMessage")

                    // Validation errorlarini ko'rsatish
                    showValidationErrors(response)
                    showMessage(errorMessage)
                }

            } catch (e: Exception) {
                Log.e(TAG, "Register error: ${e.message}", e)
                showMessage(getString(R.string.network_error))
            } finally {
                showLoading(false)
            }
        }
    }

    private fun validateInput(name: String, email: String, phone: String, password: String, confirmPassword: String): Boolean {
        var isValid = true

        // Name validation
        if (name.isEmpty()) {
            binding.nameInputLayout.error = getString(R.string.name_required)
            isValid = false
        } else if (name.length < 2) {
            binding.nameInputLayout.error = getString(R.string.name_min_length)
            isValid = false
        } else {
            binding.nameInputLayout.error = null
        }

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

        // Phone validation
        if (phone.isEmpty()) {
            binding.phoneInputLayout.error = getString(R.string.phone_required)
            isValid = false
        } else if (phone.length < 9) {
            binding.phoneInputLayout.error = getString(R.string.phone_min_length)
            isValid = false
        } else {
            binding.phoneInputLayout.error = null
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

        // Confirm password validation
        if (confirmPassword.isEmpty()) {
            binding.confirmPasswordInputLayout.error = getString(R.string.confirm_password_required)
            isValid = false
        } else if (password != confirmPassword) {
            binding.confirmPasswordInputLayout.error = getString(R.string.passwords_not_match)
            isValid = false
        } else {
            binding.confirmPasswordInputLayout.error = null
        }

        return isValid
    }

    private fun showValidationErrors(response: retrofit2.Response<uz.urinboydev.domproduct.app.models.ApiResponse<uz.urinboydev.domproduct.app.models.AuthResponse>>) {
        val validationErrors = ApiHelper.getValidationErrors(response)
        validationErrors?.let { errors ->
            errors["name"]?.firstOrNull()?.let {
                binding.nameInputLayout.error = it
            }
            errors["email"]?.firstOrNull()?.let {
                binding.emailInputLayout.error = it
            }
            errors["phone"]?.firstOrNull()?.let {
                binding.phoneInputLayout.error = it
            }
            errors["password"]?.firstOrNull()?.let {
                binding.passwordInputLayout.error = it
            }
        }
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            binding.registerButton.visibility = View.GONE
            binding.registerProgressBar.visibility = View.VISIBLE
        } else {
            binding.registerButton.visibility = View.VISIBLE
            binding.registerProgressBar.visibility = View.GONE
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

    private fun openLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

    private fun showTermsDialog() {
        // Terms & Conditions dialog
        showMessage("Shartlar va qoidalar hali tayyor emas")
    }

    private fun openLanguageSelection() {
        val intent = Intent(this, LanguageSelectionActivity::class.java)
        startActivity(intent)
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // Register ekranidan Login ga qaytish
        openLoginActivity()
    }
}