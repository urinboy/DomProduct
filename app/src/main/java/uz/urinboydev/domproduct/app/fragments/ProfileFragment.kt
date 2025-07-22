package uz.urinboydev.domproduct.app.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import uz.urinboydev.domproduct.app.R
import uz.urinboydev.domproduct.app.activities.LoginActivity
import uz.urinboydev.domproduct.app.api.ApiHelper
import uz.urinboydev.domproduct.app.constants.ApiConstants
import uz.urinboydev.domproduct.app.databinding.FragmentProfileBinding
import uz.urinboydev.domproduct.app.models.User
import uz.urinboydev.domproduct.app.utils.AuthManager
import uz.urinboydev.domproduct.app.utils.AuthState
import uz.urinboydev.domproduct.app.utils.PreferenceManager

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var preferenceManager: PreferenceManager

    companion object {
        private const val TAG = "ProfileFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferenceManager = PreferenceManager(requireContext())

        setupUI()
        setupClickListeners()
        loadUserProfile()
    }

    private fun setupUI() {
        // Foydalanuvchi login qilganmi yoki yo'qligiga qarab UI ni ko'rsatish/yashirish
        when (AuthManager.getAuthState(requireContext())) {
            AuthState.LOGGED_IN -> {
                binding.profileHeaderCard.visibility = View.VISIBLE
                binding.profileDetailsCard.visibility = View.VISIBLE
                binding.profileNavCard.visibility = View.VISIBLE
                binding.logoutButton.visibility = View.VISIBLE
                binding.notLoggedInContainer.visibility = View.GONE
            }
            else -> { // GUEST yoki NOT_AUTHENTICATED holatlari
                binding.profileHeaderCard.visibility = View.GONE
                binding.profileDetailsCard.visibility = View.GONE
                binding.profileNavCard.visibility = View.GONE
                binding.logoutButton.visibility = View.GONE
                binding.notLoggedInContainer.visibility = View.VISIBLE
            }
        }
    }

    private fun setupClickListeners() {
        binding.logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        binding.loginFromProfileButton.setOnClickListener {
            navigateToLogin()
        }

        // Boshqa navigatsiya tugmalari (hozircha Toast ko'rsatadi)
        binding.myOrdersButton.setOnClickListener {
            showMessage("Buyurtmalarim sahifasi hali tayyor emas.")
            // TODO: navigateToOrdersHistory()
        }
        binding.myAddressesButton.setOnClickListener {
            showMessage("Manzillarim sahifasi hali tayyor emas.")
            // TODO: navigateToAddresses()
        }
        binding.editProfileButton.setOnClickListener {
            showMessage("Profilni tahrirlash sahifasi hali tayyor emas.")
            // TODO: navigateToEditProfile()
        }
    }

    private fun loadUserProfile() {
        // Agar foydalanuvchi login qilmagan bo'lsa, ma'lumotlarni yuklamaslik
        if (!AuthManager.isLoggedIn(requireContext())) {
            showLoading(false)
            return
        }

        showLoading(true)
        lifecycleScope.launch {
            try {
                val token = preferenceManager.getToken()
                if (token.isNullOrEmpty()) {
                    Log.e(TAG, "Token not found, cannot load profile.")
                    showMessage("Tizimga kirilmagan.")
                    showLoading(false)
                    AuthManager.logout(requireContext()) // Token yo'q bo'lsa, logout qilish
                    setupUI() // UI ni yangilash
                    return@launch
                }

                val authHeader = ApiHelper.createAuthHeader(token)
                val response = ApiHelper.getApi().getMe(authHeader)
                val rawBody = response.errorBody()?.string() ?: response.body()?.toString() // rawBody ni olish
                Log.d(TAG, "User Profile API Response Raw Body: $rawBody")


                if (ApiHelper.isSuccessful(response)) {
                    val user = response.body()?.data
                    if (user != null) {
                        displayUserProfile(user)
                        // Ma'lumotlar yangilangan bo'lsa, PreferenceManagerga ham saqlash
                        preferenceManager.saveUser(user)
                        Log.d(TAG, "User profile loaded successfully: ${user.email}")
                    } else {
                        showMessage("Foydalanuvchi ma'lumotlari topilmadi.")
                        Log.e(TAG, "User data is null in getMe response.")
                        AuthManager.logout(requireContext()) // Xato bo'lsa, logout qilish
                        setupUI() // UI ni yangilash
                    }
                } else {
                    val errorMessage = ApiHelper.getErrorMessageFromRawBody(rawBody) // O'zgartirildi
                    showMessage("Profilni yuklashda xato: $errorMessage")
                    Log.e(TAG, "Failed to load profile: $errorMessage")
                    // Agar 401 Unauthorized bo'lsa, token muddati tugagan bo'lishi mumkin
                    if (response.code() == 401) {
                        AuthManager.logout(requireContext())
                        setupUI()
                        navigateToLogin() // Login sahifasiga yo'naltirish
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading user profile: ${e.message}", e)
                showMessage(getString(R.string.network_error))
            } finally {
                showLoading(false)
            }
        }
    }

    private fun displayUserProfile(user: User) {
        binding.profileName.text = user.name
        binding.profileEmail.text = user.email
        binding.detailName.text = user.name
        binding.detailEmail.text = user.email
        binding.detailPhone.text = user.phone ?: "Ma'lumot yo'q"
        binding.detailRole.text = user.role
        // Shahar nomini olish uchun qo'shimcha API chaqiruvi kerak bo'lishi mumkin
        // Hozircha shahar ID si mavjud bo'lsa, uni ko'rsatamiz yoki "Noma'lum"
        binding.detailCity.text = user.cityId?.toString() ?: "Noma'lum"

        // Avatar rasmini yuklash (Glide yordamida)
        if (!user.avatar.isNullOrEmpty()) {
            Glide.with(this)
                .load(ApiConstants.BASE_URL + user.avatar) // Backenddan keladigan to'liq URL
                .placeholder(R.drawable.ic_person) // Yuklanayotganda ko'rsatiladigan rasm
                .error(R.drawable.ic_person) // Xato bo'lsa ko'rsatiladigan rasm
                .into(binding.profileAvatar)
        } else {
            binding.profileAvatar.setImageResource(R.drawable.ic_person)
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.logout_confirmation_title))
            .setMessage(getString(R.string.logout_confirmation_message))
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                performLogout()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun performLogout() {
        showLoading(true)
        lifecycleScope.launch {
            try {
                val token = preferenceManager.getToken()
                if (!token.isNullOrEmpty()) {
                    val authHeader = ApiHelper.createAuthHeader(token)
                    val response = ApiHelper.getApi().logout(authHeader)
                    val rawBody = response.errorBody()?.string() ?: response.body()?.toString() // rawBody ni olish
                    Log.d(TAG, "Logout API Response Raw Body: $rawBody")

                    if (ApiHelper.isSuccessful(response)) {
                        Log.d(TAG, "Logout successful from backend.")
                    } else {
                        val errorMessage = ApiHelper.getErrorMessageFromRawBody(rawBody) // O'zgartirildi
                        Log.e(TAG, "Backend logout failed: $errorMessage")
                        // Backendda xato bo'lsa ham, ilovadan chiqishimiz kerak
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error during backend logout: ${e.message}", e)
            } finally {
                // Har doim lokal ma'lumotlarni tozalash va LoginActivity ga o'tish
                AuthManager.logout(requireContext())
                showMessage("Tizimdan chiqildi.")
                navigateToLogin()
                showLoading(false)
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(activity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }

    private fun showLoading(show: Boolean) {
        if (_binding == null) return // Fragment allaqachon yo'q qilingan bo'lsa

        if (show) {
            binding.profileProgressBar.visibility = View.VISIBLE
            binding.profileHeaderCard.visibility = View.GONE
            binding.profileDetailsCard.visibility = View.GONE
            binding.profileNavCard.visibility = View.GONE
            binding.logoutButton.visibility = View.GONE
            binding.notLoggedInContainer.visibility = View.GONE
        } else {
            binding.profileProgressBar.visibility = View.GONE
            setupUI() // Loading tugagach UI ni holatiga qarab ko'rsatish
        }
    }

    private fun showMessage(message: String) {
        if (context == null) return
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        // Fragmentga qaytganda profil ma'lumotlarini yangilash
        loadUserProfile()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        productAddedToCartListener = null
    }
}