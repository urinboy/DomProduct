package uz.urinboydev.domproduct.app.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import uz.urinboydev.domproduct.app.R
import uz.urinboydev.domproduct.app.activities.OnProductAddedToCart
import uz.urinboydev.domproduct.app.adapters.CategoryAdapter
import uz.urinboydev.domproduct.app.adapters.ProductAdapter
import uz.urinboydev.domproduct.app.api.ApiHelper
import uz.urinboydev.domproduct.app.databinding.FragmentHomeBinding
import uz.urinboydev.domproduct.app.models.Category
import uz.urinboydev.domproduct.app.models.Product
import uz.urinboydev.domproduct.app.utils.AuthManager
import uz.urinboydev.domproduct.app.utils.LocalCartManager
import uz.urinboydev.domproduct.app.utils.PreferenceManager

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var featuredProductAdapter: ProductAdapter
    private lateinit var latestProductAdapter: ProductAdapter

    private lateinit var localCartManager: LocalCartManager
    private lateinit var preferenceManager: PreferenceManager
    private var productAddedToCartListener: OnProductAddedToCart? = null

    companion object {
        private const val TAG = "HomeFragment"

        fun newInstance(listener: OnProductAddedToCart): HomeFragment {
            val fragment = HomeFragment()
            fragment.productAddedToCartListener = listener
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnProductAddedToCart) {
            productAddedToCartListener = context
        } else {
            Log.e(TAG, "$context must implement OnProductAddedToCart")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        localCartManager = LocalCartManager(requireContext())
        preferenceManager = PreferenceManager(requireContext())

        setupRecyclerViews()
        loadData()
    }

    private fun setupRecyclerViews() {
        // Main Categories
        categoryAdapter = CategoryAdapter(emptyList()) { category ->
            Toast.makeText(requireContext(), "Kategoriya: ${category.name}", Toast.LENGTH_SHORT).show()
            // TODO: Kategoriya bo'yicha mahsulotlar sahifasiga o'tish
        }
        binding.mainCategoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }

        // Featured Products
        featuredProductAdapter = ProductAdapter(emptyList(), { product ->
            Toast.makeText(requireContext(), "Mahsulot: ${product.name}", Toast.LENGTH_SHORT).show()
            // TODO: Mahsulot detail sahifasiga o'tish
        }, { product ->
            // Add to cart click
            addToCart(product)
        })
        binding.featuredProductsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = featuredProductAdapter
        }

        // Latest Products
        latestProductAdapter = ProductAdapter(emptyList(), { product ->
            Toast.makeText(requireContext(), "Mahsulot: ${product.name}", Toast.LENGTH_SHORT).show()
            // TODO: Mahsulot detail sahifasiga o'tish
        }, { product ->
            // Add to cart click
            addToCart(product)
        })
        binding.latestProductsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = latestProductAdapter
            isNestedScrollingEnabled = false // NestedScrollView ichida bo'lgani uchun
        }
    }

    private fun loadData() {
        loadMainCategories()
        loadFeaturedProducts()
        loadLatestProducts()
    }

    private fun loadMainCategories() {
        binding.mainCategoriesProgressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = ApiHelper.getApi().getMainCategories()
                val rawBody = response.errorBody()?.string() ?: response.body()?.toString() // rawBody ni olish
                Log.d(TAG, "Main Categories API Response Raw Body: $rawBody")

                if (ApiHelper.isSuccessful(response)) {
                    val categories = response.body()?.data
                    categories?.let {
                        categoryAdapter.updateData(it)
                    }
                } else {
                    val errorMessage = ApiHelper.getErrorMessageFromRawBody(rawBody) // O'zgartirildi
                    Toast.makeText(requireContext(), "Kategoriyalarni yuklashda xato: $errorMessage", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Failed to load main categories: $errorMessage")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading main categories: ${e.message}", e)
                Toast.makeText(requireContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show()
            } finally {
                binding.mainCategoriesProgressBar.visibility = View.GONE
            }
        }
    }

    private fun loadFeaturedProducts() {
        binding.featuredProductsProgressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = ApiHelper.getApi().getFeaturedProducts()
                val rawBody = response.errorBody()?.string() ?: response.body()?.toString() // rawBody ni olish
                Log.d(TAG, "Featured Products API Response Raw Body: $rawBody")

                if (ApiHelper.isSuccessful(response)) {
                    val products = response.body()?.data
                    products?.let {
                        featuredProductAdapter.updateData(it)
                    }
                } else {
                    val errorMessage = ApiHelper.getErrorMessageFromRawBody(rawBody) // O'zgartirildi
                    Toast.makeText(requireContext(), "Tavsiya etilgan mahsulotlarni yuklashda xato: $errorMessage", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Failed to load featured products: $errorMessage")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading featured products: ${e.message}", e)
                Toast.makeText(requireContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show()
            } finally {
                binding.featuredProductsProgressBar.visibility = View.GONE
            }
        }
    }

    private fun loadLatestProducts() {
        binding.latestProductsProgressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = ApiHelper.getApi().getProducts(
                    page = 1,
                    perPage = 10,
                    sortBy = "created_at",
                    sortOrder = "desc"
                )
                val rawBody = response.errorBody()?.string() ?: response.body()?.toString() // rawBody ni olish
                Log.d(TAG, "Latest Products API Response Raw Body: $rawBody")

                if (ApiHelper.isSuccessful(response)) {
                    val products = response.body()?.data?.data // PaginatedResponse ichidagi data
                    products?.let {
                        latestProductAdapter.updateData(it)
                    }
                } else {
                    val errorMessage = ApiHelper.getErrorMessageFromRawBody(rawBody) // O'zgartirildi
                    Toast.makeText(requireContext(), "Yangi mahsulotlarni yuklashda xato: $errorMessage", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Failed to load latest products: $errorMessage")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading latest products: ${e.message}", e)
                Toast.makeText(requireContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show()
            } finally {
                binding.latestProductsProgressBar.visibility = View.GONE
            }
        }
    }

    private fun addToCart(product: Product) {
        if (!AuthManager.isLoggedIn(requireContext())) {
            AuthManager.showLoginPrompt(requireContext(), getString(R.string.checkout_feature))
            return
        }

        // Show loading (optional for individual item add to cart)
        // binding.addToCartProgressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val token = preferenceManager.getToken()
                if (token.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "Tizimga kirilmagan.", Toast.LENGTH_SHORT).show()
                    // binding.addToCartProgressBar.visibility = View.GONE
                    return@launch
                }

                val request = uz.urinboydev.domproduct.app.models.AddToCartRequest(
                    productId = product.id,
                    quantity = 1
                )

                val authHeader = ApiHelper.createAuthHeader(token)
                val response = ApiHelper.getApi().addToCart(authHeader, request)
                val rawBody = response.errorBody()?.string() ?: response.body()?.toString() // rawBody ni olish
                Log.d(TAG, "Add to cart API Response Raw Body: $rawBody")

                if (ApiHelper.isSuccessful(response)) {
                    Toast.makeText(requireContext(), "Mahsulot savatga qo'shildi!", Toast.LENGTH_SHORT).show()
                    localCartManager.addItem(product.id, 1, product.price)
                    productAddedToCartListener?.onProductAddedToCart() // Callback to MainActivity
                } else {
                    val errorMessage = ApiHelper.getErrorMessageFromRawBody(rawBody) // O'zgartirildi
                    Toast.makeText(requireContext(), "Savatga qo'shishda xato: $errorMessage", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Failed to add to cart: $errorMessage")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error adding to cart: ${e.message}", e)
                Toast.makeText(requireContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show()
            } finally {
                // binding.addToCartProgressBar.visibility = View.GONE
            }
        }
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