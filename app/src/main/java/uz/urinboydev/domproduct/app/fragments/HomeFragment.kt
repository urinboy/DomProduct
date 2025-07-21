package uz.urinboydev.domproduct.app.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import uz.urinboydev.domproduct.app.R
import uz.urinboydev.domproduct.app.activities.MainActivity
import uz.urinboydev.domproduct.app.activities.OnProductAddedToCart
import uz.urinboydev.domproduct.app.adapters.CategoryAdapter
import uz.urinboydev.domproduct.app.adapters.FeaturedProductAdapter
import uz.urinboydev.domproduct.app.api.ApiHelper
import uz.urinboydev.domproduct.app.databinding.FragmentHomeBinding
import uz.urinboydev.domproduct.app.models.Category
import uz.urinboydev.domproduct.app.models.Product
import uz.urinboydev.domproduct.app.utils.AuthManager
import uz.urinboydev.domproduct.app.utils.LocalCartManager
import uz.urinboydev.domproduct.app.utils.PreferenceManager

class HomeFragment private constructor() : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var preferenceManager: PreferenceManager
    private lateinit var localCartManager: LocalCartManager
    private var onProductAddedListener: OnProductAddedToCart? = null

    // Adapters
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var featuredProductAdapter: FeaturedProductAdapter

    // Data lists
    private val categories = mutableListOf<Category>()
    private val featuredProducts = mutableListOf<Product>()

    companion object {
        private const val TAG = "HomeFragment"

        // Factory method with callback
        fun newInstance(listener: OnProductAddedToCart): HomeFragment {
            val fragment = HomeFragment()
            fragment.onProductAddedListener = listener
            return fragment
        }

        // Default constructor for cases without callback
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated started")

        // Initialize managers
        preferenceManager = PreferenceManager(requireContext())
        localCartManager = LocalCartManager(requireContext())

        // Setup RecyclerViews
        setupRecyclerViews()

        // Setup click listeners
        setupClickListeners()

        // Load data from API
        loadHomeData()

        Log.d(TAG, "onViewCreated completed")
    }

    private fun setupRecyclerViews() {
        Log.d(TAG, "Setting up RecyclerViews")

        // Categories RecyclerView (horizontal)
        categoryAdapter = CategoryAdapter(categories) { category ->
            onCategoryClick(category)
        }

        binding.categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
            setHasFixedSize(true)
        }

        // Featured Products RecyclerView (horizontal)
        featuredProductAdapter = FeaturedProductAdapter(featuredProducts) { product, action ->
            when (action) {
                ProductAction.CLICK -> onProductClick(product)
                ProductAction.ADD_TO_CART -> onAddToCartClick(product)
            }
        }

        binding.featuredProductsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = featuredProductAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupClickListeners() {
        Log.d(TAG, "Setting up click listeners")

        // View All Categories
        binding.viewAllCategoriesText.setOnClickListener {
            navigateToCategories()
        }

        // View All Featured Products
        binding.viewAllFeaturedText.setOnClickListener {
            navigateToFeaturedProducts()
        }

        // Deal of the Day card click
        binding.dealOfDayCard.setOnClickListener {
            showDealOfDay()
        }
    }

    private fun loadHomeData() {
        Log.d(TAG, "Loading home data")

        // Show loading state
        showLoading(true)

        // Load categories and featured products in parallel
        lifecycleScope.launch {
            try {
                // Load both simultaneously
                val categoriesJob = launch { loadCategories() }
                val productsJob = launch { loadFeaturedProducts() }

                // Wait for both to complete
                categoriesJob.join()
                productsJob.join()

                Log.d(TAG, "Home data loading completed")

            } catch (e: Exception) {
                Log.e(TAG, "Error loading home data: ${e.message}", e)
                showError("Ma'lumotlarni yuklashda xato yuz berdi")
            } finally {
                showLoading(false)
            }
        }
    }

    private suspend fun loadCategories() {
        try {
            Log.d(TAG, "Loading categories from API")

            val response = ApiHelper.getApi().getMainCategories()

            if (ApiHelper.isSuccessful(response)) {
                val categoriesData = response.body()?.data ?: emptyList()

                // Update data on main thread
                requireActivity().runOnUiThread {
                    categories.clear()
                    categories.addAll(categoriesData)
                    categoryAdapter.notifyDataSetChanged()
                }

                Log.d(TAG, "Categories loaded successfully: ${categoriesData.size}")

            } else {
                val errorMessage = ApiHelper.getErrorMessage(response)
                Log.e(TAG, "Failed to load categories: $errorMessage")

                // Load dummy data on error
                requireActivity().runOnUiThread {
                    loadDummyCategories()
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, "Exception loading categories: ${e.message}", e)

            // Load dummy data on exception
            requireActivity().runOnUiThread {
                loadDummyCategories()
            }
        }
    }

    private suspend fun loadFeaturedProducts() {
        try {
            Log.d(TAG, "Loading featured products from API")

            val response = ApiHelper.getApi().getFeaturedProducts()

            if (ApiHelper.isSuccessful(response)) {
                val productsData = response.body()?.data ?: emptyList()

                // Update data on main thread
                requireActivity().runOnUiThread {
                    featuredProducts.clear()
                    featuredProducts.addAll(productsData)
                    featuredProductAdapter.notifyDataSetChanged()
                }

                Log.d(TAG, "Featured products loaded successfully: ${productsData.size}")

            } else {
                val errorMessage = ApiHelper.getErrorMessage(response)
                Log.e(TAG, "Failed to load featured products: $errorMessage")

                // Load dummy data on error
                requireActivity().runOnUiThread {
                    loadDummyProducts()
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, "Exception loading featured products: ${e.message}", e)

            // Load dummy data on exception
            requireActivity().runOnUiThread {
                loadDummyProducts()
            }
        }
    }

    private fun loadDummyCategories() {
        Log.d(TAG, "Loading dummy categories for testing")

        categories.clear()
        categories.addAll(listOf(
            Category(1, "Oziq-ovqat", "groceries", "Oziq-ovqat mahsulotlari", null, null, 1, true, "", ""),
            Category(2, "Uy-ro'zg'or", "household", "Uy-ro'zg'or buyumlari", null, null, 2, true, "", ""),
            Category(3, "Ayollar kiyimi", "womens-clothing", "Ayollar uchun kiyim", null, null, 3, true, "", ""),
            Category(4, "Erkaklar kiyimi", "mens-clothing", "Erkaklar uchun kiyim", null, null, 4, true, "", ""),
            Category(5, "Elektronika", "electronics", "Elektron jihozlar", null, null, 5, true, "", ""),
            Category(6, "Kitoblar", "books", "Kitoblar va darsliklar", null, null, 6, true, "", "")
        ))

        categoryAdapter.notifyDataSetChanged()
    }

    private fun loadDummyProducts() {
        Log.d(TAG, "Loading dummy products for testing")

        featuredProducts.clear()
        featuredProducts.addAll(listOf(
            Product(1, "Qizil olma", "apple", "Fresh red apple", "Yangi qizil olma", 4500.0, null, "APPLE001", 100, "1kg", null, null, null, 1, true, true, null, null, "", "", null, null, 4.5, 25),
            Product(2, "Beige palto", "beige-coat", "Stylish beige coat", "Zamonaviy beige palto", 125000.0, 99000.0, "COAT001", 50, null, null, null, null, 3, true, true, null, null, "", "", null, null, 4.2, 18),
            Product(3, "Futbolka", "t-shirt", "Comfortable t-shirt", "Qulay futbolka", 35000.0, null, "SHIRT001", 200, null, null, null, null, 4, true, true, null, null, "", "", null, null, 4.0, 32),
            Product(4, "Samsung telefon", "samsung-phone", "Latest Samsung phone", "Yangi Samsung telefon", 2500000.0, 2200000.0, "PHONE001", 25, null, null, null, null, 5, true, true, null, null, "", "", null, null, 4.7, 156),
            Product(5, "Dasturlash kitobi", "programming-book", "Learn programming", "Dasturlashni o'rganing", 75000.0, null, "BOOK001", 80, null, null, null, null, 6, true, true, null, null, "", "", null, null, 4.8, 42)
        ))

        featuredProductAdapter.notifyDataSetChanged()
    }

    private fun onCategoryClick(category: Category) {
        Log.d(TAG, "Category clicked: ${category.name}")

        // Navigate to ProductsFragment with category filter
        navigateToCategories()
        showMessage("Kategoriya: ${category.name}")
    }

    private fun onProductClick(product: Product) {
        Log.d(TAG, "Product clicked: ${product.name}")

        // TODO: Navigate to ProductDetailActivity
        showMessage("Mahsulot tafsiloti: ${product.name}")
    }

    private fun onAddToCartClick(product: Product) {
        Log.d(TAG, "Add to cart clicked: ${product.name}")

        try {
            // Add to local cart
            localCartManager.addItem(product, 1)

            // Notify MainActivity to update cart badge
            onProductAddedListener?.onProductAddedToCart()

            // Show success message
            showMessage("${product.name} savatga qo'shildi")

            Log.d(TAG, "Product added to cart successfully: ${product.name}")

        } catch (e: Exception) {
            Log.e(TAG, "Error adding product to cart: ${e.message}", e)
            showError("Savatga qo'shishda xato")
        }
    }

    private fun navigateToCategories() {
        (requireActivity() as? MainActivity)?.navigateToCategories()
    }

    private fun navigateToFeaturedProducts() {
        // TODO: Navigate to products with featured filter
        showMessage("Tavsiya etiladigan mahsulotlar")
    }

    private fun showDealOfDay() {
        // TODO: Implement deal of the day functionality
        showMessage("Kun taklifi tez orada...")
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            binding.homeScrollView.visibility = View.GONE
            binding.loadingProgressBar.visibility = View.VISIBLE
        } else {
            binding.homeScrollView.visibility = View.VISIBLE
            binding.loadingProgressBar.visibility = View.GONE
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    // Public method to refresh data
    fun refreshData() {
        Log.d(TAG, "Refreshing home data")
        loadHomeData()
    }

    override fun onResume() {
        super.onResume()
        // Update cart badge when returning to fragment
        onProductAddedListener?.onProductAddedToCart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// Enum for product actions
enum class ProductAction {
    CLICK,
    ADD_TO_CART
}