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
import uz.urinboydev.domproduct.app.activities.ProductDetailActivity
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

    // Loading state
    private var isLoadingCategories = false
    private var isLoadingProducts = false

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

                // Load dummy data on error
                loadDummyData()
            } finally {
                showLoading(false)
            }
        }
    }

    private suspend fun loadCategories() {
        if (isLoadingCategories) return
        isLoadingCategories = true

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
        } finally {
            isLoadingCategories = false
        }
    }

    private suspend fun loadFeaturedProducts() {
        if (isLoadingProducts) return
        isLoadingProducts = true

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
        } finally {
            isLoadingProducts = false
        }
    }

    private fun loadDummyData() {
        loadDummyCategories()
        loadDummyProducts()
    }

    private fun loadDummyCategories() {
        Log.d(TAG, "Loading dummy categories for testing")

        categories.clear()
        categories.addAll(listOf(
            Category(
                id = 1,
                name = "Oziq-ovqat",
                slug = "groceries",
                description = "Oziq-ovqat mahsulotlari",
                image = null,
                parentId = null,
                sortOrder = 1,
                isActive = true,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z"
            ),
            Category(
                id = 2,
                name = "Uy-ro'zg'or",
                slug = "household",
                description = "Uy-ro'zg'or buyumlari",
                image = null,
                parentId = null,
                sortOrder = 2,
                isActive = true,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z"
            ),
            Category(
                id = 3,
                name = "Ayollar kiyimi",
                slug = "womens-clothing",
                description = "Ayollar uchun kiyim",
                image = null,
                parentId = null,
                sortOrder = 3,
                isActive = true,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z"
            ),
            Category(
                id = 4,
                name = "Erkaklar kiyimi",
                slug = "mens-clothing",
                description = "Erkaklar uchun kiyim",
                image = null,
                parentId = null,
                sortOrder = 4,
                isActive = true,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z"
            ),
            Category(
                id = 5,
                name = "Elektronika",
                slug = "electronics",
                description = "Elektron jihozlar",
                image = null,
                parentId = null,
                sortOrder = 5,
                isActive = true,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z"
            ),
            Category(
                id = 6,
                name = "Kitoblar",
                slug = "books",
                description = "Kitoblar va darsliklar",
                image = null,
                parentId = null,
                sortOrder = 6,
                isActive = true,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z"
            )
        ))

        categoryAdapter.notifyDataSetChanged()
    }

    private fun loadDummyProducts() {
        Log.d(TAG, "Loading dummy products for testing")

        featuredProducts.clear()
        featuredProducts.addAll(listOf(
            Product(
                id = 1,
                name = "Qizil olma",
                slug = "apple",
                description = "Yangi qizil olma, mazali va foydali",
                shortDescription = "Fresh red apple",
                price = 4500.0,
                salePrice = null,
                sku = "APPLE001",
                stockQuantity = 100,
                weight = "1kg",
                dimensions = null,
                image = "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=400",
                gallery = null,
                categoryId = 1,
                isFeatured = true,
                isActive = true,
                metaTitle = null,
                metaDescription = null,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z",
                averageRating = 4.5,
                reviewsCount = 25
            ),
            Product(
                id = 2,
                name = "Beige palto",
                slug = "beige-coat",
                description = "Zamonaviy beige palto, yumshoq va issiq",
                shortDescription = "Stylish beige coat",
                price = 125000.0,
                salePrice = 99000.0,
                sku = "COAT001",
                stockQuantity = 50,
                weight = null,
                dimensions = null,
                image = "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=400",
                gallery = null,
                categoryId = 3,
                isFeatured = true,
                isActive = true,
                metaTitle = null,
                metaDescription = null,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z",
                averageRating = 4.2,
                reviewsCount = 18
            ),
            Product(
                id = 3,
                name = "Futbolka",
                slug = "t-shirt",
                description = "Qulay va yumshoq paxta futbolka",
                shortDescription = "Comfortable t-shirt",
                price = 35000.0,
                salePrice = null,
                sku = "SHIRT001",
                stockQuantity = 200,
                weight = null,
                dimensions = null,
                image = "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400",
                gallery = null,
                categoryId = 4,
                isFeatured = true,
                isActive = true,
                metaTitle = null,
                metaDescription = null,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z",
                averageRating = 4.0,
                reviewsCount = 32
            ),
            Product(
                id = 4,
                name = "Samsung telefon",
                slug = "samsung-phone",
                description = "Eng yangi Samsung smartfoni, barcha imkoniyatlar bilan",
                shortDescription = "Latest Samsung phone",
                price = 2500000.0,
                salePrice = 2200000.0,
                sku = "PHONE001",
                stockQuantity = 25,
                weight = null,
                dimensions = null,
                image = "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=400",
                gallery = null,
                categoryId = 5,
                isFeatured = true,
                isActive = true,
                metaTitle = null,
                metaDescription = null,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z",
                averageRating = 4.7,
                reviewsCount = 156
            ),
            Product(
                id = 5,
                name = "Dasturlash kitobi",
                slug = "programming-book",
                description = "Dasturlashni o'rganish uchun eng yaxshi kitob",
                shortDescription = "Learn programming",
                price = 75000.0,
                salePrice = null,
                sku = "BOOK001",
                stockQuantity = 80,
                weight = null,
                dimensions = null,
                image = "https://images.unsplash.com/photo-1532012197267-da84d127e765?w=400",
                gallery = null,
                categoryId = 6,
                isFeatured = true,
                isActive = true,
                metaTitle = null,
                metaDescription = null,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z",
                averageRating = 4.8,
                reviewsCount = 42
            )
        ))

        featuredProductAdapter.notifyDataSetChanged()
    }

    private fun onCategoryClick(category: Category) {
        Log.d(TAG, "Category clicked: ${category.name}")

        // Navigate to CategoriesFragment with specific category
        (requireActivity() as? MainActivity)?.navigateToCategories()
        showMessage("Kategoriya: ${category.name}")
    }

    private fun onProductClick(product: Product) {
        Log.d(TAG, "Product clicked: ${product.name}")

        // Navigate to ProductDetailActivity
        ProductDetailActivity.start(requireContext(), product)
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
        Log.d(TAG, "Navigating to categories")
        (requireActivity() as? MainActivity)?.navigateToCategories()
    }

    private fun navigateToFeaturedProducts() {
        Log.d(TAG, "Navigating to featured products")
        showMessage("Tavsiya etiladigan mahsulotlar")
        // TODO: Navigate to products with featured filter
        // (requireActivity() as? MainActivity)?.navigateToProducts(featured = true)
    }

    private fun showDealOfDay() {
        Log.d(TAG, "Deal of day clicked")
        showMessage("Kun taklifi tez orada...")
        // TODO: Implement deal of the day functionality
        // Could navigate to a special deals page or show a dialog
    }

    private fun showLoading(show: Boolean) {
        if (_binding == null) return

        if (show) {
            binding.homeScrollView.visibility = View.GONE
            binding.loadingProgressBar.visibility = View.VISIBLE
        } else {
            binding.homeScrollView.visibility = View.VISIBLE
            binding.loadingProgressBar.visibility = View.GONE
        }
    }

    private fun showError(message: String) {
        if (context == null) return
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        Log.e(TAG, "Error: $message")
    }

    private fun showMessage(message: String) {
        if (context == null) return
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    // Public method to refresh data
    fun refreshData() {
        Log.d(TAG, "Refreshing home data")
        if (_binding != null && !isLoadingCategories && !isLoadingProducts) {
            loadHomeData()
        }
    }

    // Public method to update cart badge when returning to fragment
    fun updateCartBadge() {
        onProductAddedListener?.onProductAddedToCart()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called")

        // Update cart badge when returning to fragment
        updateCartBadge()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView called")
        _binding = null
    }
}

// Enum for product actions
enum class ProductAction {
    CLICK,
    ADD_TO_CART
}