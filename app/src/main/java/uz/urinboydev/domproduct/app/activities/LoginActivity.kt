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
import uz.urinboydev.domproduct.app.utils.Resource
import uz.urinboydev.domproduct.app.utils.LocalCartManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.activity.viewModels
import uz.urinboydev.domproduct.app.viewmodel.AuthViewModel

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var preferenceManager: PreferenceManager

    @Inject
    lateinit var languageManager: LanguageManager

    private val authViewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var authManager: AuthManager

    @Inject
    lateinit var localCartManager: LocalCartManager

    companion object {
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate started")

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        authViewModel.login(LoginRequest(email, password)).observe(this) {
            it?.let {
                when (it) {
                    is Resource.Success -> {
                        showLoading(false)
                        it.data?.let {
                            preferenceManager.saveToken(it.token)
                            preferenceManager.saveUser(it.user)

                            if (binding.rememberMeCheckBox.isChecked) {
                                preferenceManager.saveAppSettings("remember_login", true)
                            } else {
                                preferenceManager.saveAppSettings("remember_login", false)
                            }

                            authManager.logout(localCartManager) // Mehmon rejimini o'chirish
                            Log.d(TAG, "Login successful for user: ${it.user.email}")
                            showMessage(getString(R.string.login_success))
                            navigateToMainActivity()
                        }
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        Log.e(TAG, "Login failed with error: ${it.message}")
                        // Backenddan kelgan validation xatolarini ko'rsatish
                        // Hozircha bu yerda validation errorsni to'g'ridan-to'g'ri Resource ichidan olish imkoni yo'q.
                        // Agar kerak bo'lsa, Resource sinfini o'zgartirishimiz kerak bo'ladi.
                        showMessage(it.message ?: getString(R.string.login_failed_generic))
                    }
                    is Resource.Loading -> {
                        showLoading(true)
                    }
                }
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
        }

        return isValid
    }

    private fun showValidationErrors(errors: Map<String, List<String>>?) {
        // Avvalgi xatolarni tozalash
        binding.emailInputLayout.error = null
        binding.passwordInputLayout.error = null

        errors?.let {
            it["email"]?.firstOrNull()?.let {
                binding.emailInputLayout.error = it
                Log.d(TAG, "Validation error for email: $it")
            }
            it["password"]?.firstOrNull()?.let {
                binding.passwordInputLayout.error = it
                Log.d(TAG, "Validation error for password: $it")
            }
            // Agar boshqa umumiy xatolar bo'lsa, ularni ham ko'rsatish
            it["error"]?.firstOrNull()?.let {
                showMessage(it)
                Log.d(TAG, "General validation error: $it")
            }
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
        authManager.loginAsGuest(this)
        showMessage(getString(R.string.guest_mode_enabled))
        navigateToMainActivity()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun openRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
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

    
}