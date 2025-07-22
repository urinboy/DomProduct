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

import dagger.hilt.android.AndroidEntryPoint

import javax.inject.Inject

import androidx.fragment.app.viewModels
import uz.urinboydev.domproduct.app.viewmodel.HomeViewModel

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var featuredProductAdapter: ProductAdapter
    private lateinit var latestProductAdapter: ProductAdapter

    @Inject
    lateinit var localCartManager: LocalCartManager

    @Inject
    lateinit var preferenceManager: PreferenceManager

    private val homeViewModel: HomeViewModel by viewModels()
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

        // localCartManager = LocalCartManager(requireContext())
        // preferenceManager = PreferenceManager(requireContext())

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
        homeViewModel.getMainCategories().observe(viewLifecycleOwner) {
            it?.let {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        binding.mainCategoriesProgressBar.visibility = View.GONE
                        it.data?.let {
                            categoryAdapter.updateData(it)
                        }
                    }
                    Resource.Status.ERROR -> {
                        binding.mainCategoriesProgressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Kategoriyalarni yuklashda xato: ${it.message}", Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "Failed to load main categories: ${it.message}")
                    }
                    Resource.Status.LOADING -> {
                        binding.mainCategoriesProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun loadFeaturedProducts() {
        binding.featuredProductsProgressBar.visibility = View.VISIBLE
        homeViewModel.getFeaturedProducts().observe(viewLifecycleOwner) {
            it?.let {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        binding.featuredProductsProgressBar.visibility = View.GONE
                        it.data?.let {
                            featuredProductAdapter.updateData(it)
                        }
                    }
                    Resource.Status.ERROR -> {
                        binding.featuredProductsProgressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Tavsiya etilgan mahsulotlarni yuklashda xato: ${it.message}", Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "Failed to load featured products: ${it.message}")
                    }
                    Resource.Status.LOADING -> {
                        binding.featuredProductsProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun loadLatestProducts() {
        binding.latestProductsProgressBar.visibility = View.VISIBLE
        homeViewModel.getLatestProducts().observe(viewLifecycleOwner) {
            it?.let {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        binding.latestProductsProgressBar.visibility = View.GONE
                        it.data?.let {
                            latestProductAdapter.updateData(it)
                        }
                    }
                    Resource.Status.ERROR -> {
                        binding.latestProductsProgressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Yangi mahsulotlarni yuklashda xato: ${it.message}", Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "Failed to load latest products: ${it.message}")
                    }
                    Resource.Status.LOADING -> {
                        binding.latestProductsProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun addToCart(product: Product) {
        if (!AuthManager.isLoggedIn(requireContext())) {
            AuthManager.showLoginPrompt(requireContext(), getString(R.string.checkout_feature))
            return
        }

        lifecycleScope.launch {
            val token = preferenceManager.getToken()
            if (token.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Tizimga kirilmagan.", Toast.LENGTH_SHORT).show()
                return@launch
            }

            homeViewModel.addToCart(ApiHelper.createAuthHeader(token), product.id, 1).observe(viewLifecycleOwner) {
                it?.let {
                    when (it.status) {
                        Resource.Status.SUCCESS -> {
                            Toast.makeText(requireContext(), "Mahsulot savatga qo'shildi!", Toast.LENGTH_SHORT).show()
                            localCartManager.addItem(product.id, 1, product.price)
                            productAddedToCartListener?.onProductAddedToCart() // Callback to MainActivity
                        }
                        Resource.Status.ERROR -> {
                            Toast.makeText(requireContext(), "Savatga qo'shishda xato: ${it.message}", Toast.LENGTH_SHORT).show()
                            Log.e(TAG, "Failed to add to cart: ${it.message}")
                        }
                        Resource.Status.LOADING -> {
                            // Optional: show loading for individual item add to cart
                        }
                    }
                }
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