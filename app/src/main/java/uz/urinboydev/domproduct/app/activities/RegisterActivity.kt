package uz.urinboydev.domproduct.app.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import uz.urinboydev.domproduct.app.R
import uz.urinboydev.domproduct.app.databinding.ActivityRegisterBinding
import uz.urinboydev.domproduct.app.utils.PreferenceManager
import uz.urinboydev.domproduct.app.utils.LanguageManager

import dagger.hilt.android.AndroidEntryPoint

import javax.inject.Inject

import androidx.activity.viewModels
import uz.urinboydev.domproduct.app.utils.Resource
import uz.urinboydev.domproduct.app.viewmodel.AuthViewModel

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    @Inject
    lateinit var preferenceManager: PreferenceManager

    @Inject
    lateinit var languageManager: LanguageManager

    private val authViewModel: AuthViewModel by viewModels()

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
        // preferenceManager = PreferenceManager(this)
        // languageManager = LanguageManager(this)

        // Status bar ni yashirish
        supportActionBar?.hide()

        // Setup UI
        setupClickListeners()

        onBackPressedDispatcher.addCallback(this, object : androidx.activity.OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Register ekranidan Login ga qaytish
                openLoginActivity()
            }
        })

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
        authViewModel.register(registerRequest).observe(this) {
            it?.let {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        showLoading(false)
                        it.data?.let {
                            // Save user data
                            preferenceManager.saveToken(it.token)
                            preferenceManager.saveUser(it.user)

                            Log.d(TAG, "Register successful for user: ${it.user.email}")

                            // Show success message
                            showMessage(getString(R.string.register_success))

                            // Navigate to MainActivity
                            navigateToMainActivity()
                        }
                    }
                    Resource.Status.ERROR -> {
                        showLoading(false)
                        Log.e(TAG, "Register failed: ${it.message}")
                        // Validation errorlarini ko'rsatish
                        showValidationErrors(it.errors)
                        showMessage(it.message ?: getString(R.string.register_failed_generic))
                    }
                    Resource.Status.LOADING -> {
                        showLoading(true)
                    }
                }
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

    private fun showValidationErrors(errors: Map<String, List<String>>?) {
        // Avvalgi xatolarni tozalash
        binding.nameInputLayout.error = null
        binding.emailInputLayout.error = null
        binding.phoneInputLayout.error = null
        binding.passwordInputLayout.error = null
        binding.confirmPasswordInputLayout.error = null

        errors?.let {
            it["name"]?.firstOrNull()?.let {
                binding.nameInputLayout.error = it
            }
            it["email"]?.firstOrNull()?.let {
                binding.emailInputLayout.error = it
            }
            it["phone"]?.firstOrNull()?.let {
                binding.phoneInputLayout.error = it
            }
            it["password"]?.firstOrNull()?.let {
                binding.passwordInputLayout.error = it
            }
            it["password_confirmation"]?.firstOrNull()?.let {
                binding.confirmPasswordInputLayout.error = it
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
    }

    private fun openLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showTermsDialog() {
        // Terms & Conditions dialog
        showMessage("Shartlar va qoidalar hali tayyor emas")
    }

    private fun openLanguageSelection() {
        val intent = Intent(this, LanguageSelectionActivity::class.java)
        startActivity(intent)
    }

    
}