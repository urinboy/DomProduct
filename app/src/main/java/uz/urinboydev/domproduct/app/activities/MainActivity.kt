package uz.urinboydev.domproduct.app.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import uz.urinboydev.domproduct.app.R
import uz.urinboydev.domproduct.app.databinding.ActivityMainBinding
import uz.urinboydev.domproduct.app.fragments.HomeFragment
import uz.urinboydev.domproduct.app.fragments.CategoriesFragment
import uz.urinboydev.domproduct.app.fragments.CartFragment
import uz.urinboydev.domproduct.app.fragments.ProfileFragment
import uz.urinboydev.domproduct.app.utils.PreferenceManager
import uz.urinboydev.domproduct.app.utils.AuthManager
import uz.urinboydev.domproduct.app.utils.AuthState
import uz.urinboydev.domproduct.app.utils.LocalCartManager

class MainActivity : AppCompatActivity(), OnProductAddedToCart {

    private lateinit var binding: ActivityMainBinding
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var localCartManager: LocalCartManager

    // Fragments
    private lateinit var homeFragment: HomeFragment
    private lateinit var categoriesFragment: CategoriesFragment
    private lateinit var cartFragment: CartFragment
    private lateinit var profileFragment: ProfileFragment

    // Current selected tab
    private var selectedTab = 0

    companion object {
        private const val TAG = "MainActivity"
        private const val TAB_HOME = 0
        private const val TAB_CATEGORIES = 1
        private const val TAB_CART = 2
        private const val TAB_PROFILE = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate started")

        // ViewBinding setup
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Managers setup
        preferenceManager = PreferenceManager(this)
        localCartManager = LocalCartManager(this)

        // Remove default action bar
        supportActionBar?.hide()

        // Initialize fragments
        initializeFragments()

        // Setup bottom navigation
        setupBottomNavigation()

        // Setup toolbar actions
        setupToolbarActions()

        // Show default fragment (Home)
        showFragment(homeFragment)
        updateTabSelection(TAB_HOME)

        // Update cart badge
        updateCartBadge()

        // Show user greeting
        showUserGreeting()

        Log.d(TAG, "onCreate completed")
    }

    private fun initializeFragments() {
        Log.d(TAG, "Initializing fragments")

        // HomeFragment ga listener uzatish
        homeFragment = HomeFragment.newInstance(this)
        categoriesFragment = CategoriesFragment()
        cartFragment = CartFragment()
        profileFragment = ProfileFragment() // ProfileFragment endi to'ldiriladi
    }

    private fun setupBottomNavigation() {
        // Home tab click
        binding.navHome.setOnClickListener {
            Log.d(TAG, "Home tab clicked")
            showFragment(homeFragment)
            updateTabSelection(TAB_HOME)
        }

        // Categories tab click
        binding.navCategories.setOnClickListener {
            Log.d(TAG, "Categories tab clicked")
            showFragment(categoriesFragment)
            updateTabSelection(TAB_CATEGORIES)
        }

        // Cart tab click
        binding.navCart.setOnClickListener {
            Log.d(TAG, "Cart tab clicked")
            // Savatga kirish uchun login talab qilinishi mumkin, agar server bilan sinxronizatsiya bo'lsa
            // Hozircha LocalCartManager ishlatilgani uchun login shart emas.
            // Agar server savati bo'lsa:
            // if (AuthManager.requireLogin(this)) {
            //     AuthManager.showLoginPrompt(this, getString(R.string.checkout_feature))
            //     return@setOnClickListener
            // }
            showFragment(cartFragment)
            updateTabSelection(TAB_CART)
        }

        // Profile tab click
        binding.navProfile.setOnClickListener {
            Log.d(TAG, "Profile tab clicked")
            // Profilga kirish uchun login talab qilish
            if (AuthManager.requireLogin(this)) {
                AuthManager.showLoginPrompt(this, getString(R.string.profile_feature))
                return@setOnClickListener
            }
            showFragment(profileFragment)
            updateTabSelection(TAB_PROFILE)
        }
    }

    private fun setupToolbarActions() {
        // Search icon click
        binding.searchIcon.setOnClickListener {
            Log.d(TAG, "Search clicked")
            // TODO: Open search activity
            showMessage("Qidiruv funksiyasi tez orada...")
        }

        // Cart icon click
        binding.cartIconContainer.setOnClickListener {
            Log.d(TAG, "Cart clicked")
            // Switch to cart tab
            showFragment(cartFragment)
            updateTabSelection(TAB_CART)
        }
    }

    private fun showFragment(fragment: Fragment) {
        Log.d(TAG, "Showing fragment: ${fragment::class.simpleName}")

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun updateTabSelection(selectedTabIndex: Int) {
        selectedTab = selectedTabIndex

        // Reset all tabs to unselected state
        resetTabColors()

        // Highlight selected tab
        when (selectedTabIndex) {
            TAB_HOME -> {
                binding.iconHome.setColorFilter(Color.parseColor("#2E7D32"))
                binding.textHome.setTextColor(Color.parseColor("#2E7D32"))
            }
            TAB_CATEGORIES -> {
                binding.iconCategories.setColorFilter(Color.parseColor("#2E7D32"))
                binding.textCategories.setTextColor(Color.parseColor("#2E7D32"))
            }
            TAB_CART -> {
                binding.iconCart.setColorFilter(Color.parseColor("#2E7D32"))
                binding.textCart.setTextColor(Color.parseColor("#2E7D32"))
            }
            TAB_PROFILE -> {
                binding.iconProfile.setColorFilter(Color.parseColor("#2E7D32"))
                binding.textProfile.setTextColor(Color.parseColor("#2E7D32"))
            }
        }
    }

    private fun resetTabColors() {
        val unselectedColor = Color.parseColor("#757575")

        binding.iconHome.setColorFilter(unselectedColor)
        binding.textHome.setTextColor(unselectedColor)

        binding.iconCategories.setColorFilter(unselectedColor)
        binding.textCategories.setTextColor(unselectedColor)

        binding.iconCart.setColorFilter(unselectedColor)
        binding.textCart.setTextColor(unselectedColor)

        binding.iconProfile.setColorFilter(unselectedColor)
        binding.textProfile.setTextColor(unselectedColor)
    }

    fun updateCartBadge() { // Public qilib o'zgartirildi
        val cartCount = localCartManager.getItemCount()

        if (cartCount > 0) {
            binding.cartBadge.visibility = View.VISIBLE
            binding.cartBadge.text = if (cartCount > 99) "99+" else cartCount.toString()
        } else {
            binding.cartBadge.visibility = View.GONE
        }

        Log.d(TAG, "Cart badge updated: $cartCount")
    }

    private fun showUserGreeting() {
        // Toolbar'dagi foydalanuvchi holatini ko'rsatish
        when (AuthManager.getAuthState(this)) {
            AuthState.LOGGED_IN -> {
                val userName = preferenceManager.getUserName() ?: "Foydalanuvchi"
                binding.toolbarTitle.text = getString(R.string.welcome_user, userName) // Yangi string resursi kerak
                Log.d(TAG, "Logged in user: $userName")
            }
            AuthState.GUEST -> {
                binding.toolbarTitle.text = getString(R.string.welcome_guest) // Yangi string resursi kerak
                Log.d(TAG, "Guest mode active")
            }
            AuthState.NOT_AUTHENTICATED -> {
                binding.toolbarTitle.text = getString(R.string.app_name) // Default app nomi
                Log.d(TAG, "No authentication")
            }
        }
    }

    private fun showMessage(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }

    // Interface implementation for product added to cart
    override fun onProductAddedToCart() {
        Log.d(TAG, "Product added to cart - refreshing badge")
        updateCartBadge() // refreshCartBadge() o'rniga updateCartBadge()
    }

    // Navigation helper methods for fragments
    fun navigateToCategories() {
        showFragment(categoriesFragment)
        updateTabSelection(TAB_CATEGORIES)
    }

    fun navigateToCart() {
        showFragment(cartFragment)
        updateTabSelection(TAB_CART)
    }

    fun navigateToProfile() {
        if (AuthManager.requireLogin(this)) {
            AuthManager.showLoginPrompt(this, getString(R.string.profile_feature))
            return
        }
        showFragment(profileFragment)
        updateTabSelection(TAB_PROFILE)
    }

    override fun onResume() {
        super.onResume()
        updateCartBadge()
        showUserGreeting() // Har safar onResume da foydalanuvchi holatini yangilash
    }
}

// Interface for cart callbacks
interface OnProductAddedToCart {
    fun onProductAddedToCart()
}