package uz.urinboydev.domproduct.app.activities

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.launch
import uz.urinboydev.domproduct.app.R
import uz.urinboydev.domproduct.app.adapters.FeaturedProductAdapter
import uz.urinboydev.domproduct.app.adapters.ProductImageAdapter
import uz.urinboydev.domproduct.app.api.ApiHelper
import uz.urinboydev.domproduct.app.databinding.ActivityProductDetailBinding
import uz.urinboydev.domproduct.app.fragments.ProductAction
import uz.urinboydev.domproduct.app.models.Product
import uz.urinboydev.domproduct.app.utils.LocalCartManager
import uz.urinboydev.domproduct.app.utils.PreferenceManager
import java.text.NumberFormat
import java.util.*

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var localCartManager: LocalCartManager
    private lateinit var preferenceManager: PreferenceManager

    // Adapters
    private lateinit var imageAdapter: ProductImageAdapter
    private lateinit var relatedProductsAdapter: FeaturedProductAdapter

    // Data
    private var currentProduct: Product? = null
    private var selectedQuantity = 1
    private val productImages = mutableListOf<String>()
    private val relatedProducts = mutableListOf<Product>()

    companion object {
        private const val TAG = "ProductDetailActivity"
        const val EXTRA_PRODUCT_ID = "product_id"

        // Static method for easy navigation - only use product ID
        fun start(context: android.content.Context, product: Product) {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra(EXTRA_PRODUCT_ID, product.id)
            context.startActivity(intent)
        }

        fun start(context: android.content.Context, productId: Int) {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra(EXTRA_PRODUCT_ID, productId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate started")

        // ViewBinding setup
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Managers setup
        localCartManager = LocalCartManager(this)
        preferenceManager = PreferenceManager(this)

        // Setup UI
        setupToolbar()
        setupRecyclerViews()
        setupClickListeners()
        setupQuantitySelector()

        // Load product data
        loadProductData()

        // Update cart badge
        updateCartBadge()

        Log.d(TAG, "onCreate completed")
    }

    private fun setupToolbar() {
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.cartIconContainer.setOnClickListener {
            // Navigate to cart
            navigateToCart()
        }
    }

    private fun setupRecyclerViews() {
        // Image ViewPager
        imageAdapter = ProductImageAdapter(productImages)
        binding.imageViewPager.adapter = imageAdapter

        // Related Products RecyclerView
        relatedProductsAdapter = FeaturedProductAdapter(relatedProducts) { product, action ->
            when (action) {
                ProductAction.CLICK -> openProductDetail(product)
                ProductAction.ADD_TO_CART -> addRelatedProductToCart(product)
            }
        }

        binding.relatedProductsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ProductDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = relatedProductsAdapter
        }
    }

    private fun setupClickListeners() {
        // Add to Cart Button
        binding.addToCartButton.setOnClickListener {
            addToCart()
        }

        // See All Reviews
        binding.seeAllReviews.setOnClickListener {
            // TODO: Navigate to reviews activity
            showMessage("Barcha sharhlar")
        }
    }

    private fun setupQuantitySelector() {
        binding.decreaseButton.setOnClickListener {
            if (selectedQuantity > 1) {
                selectedQuantity--
                updateQuantityDisplay()
            }
        }

        binding.increaseButton.setOnClickListener {
            val maxQuantity = currentProduct?.stockQuantity ?: 1
            if (selectedQuantity < maxQuantity) {
                selectedQuantity++
                updateQuantityDisplay()
            }
        }
    }

    private fun loadProductData() {
        showLoading(true)

        // Get product ID from intent
        val productId = intent.getIntExtra(EXTRA_PRODUCT_ID, -1)

        if (productId != -1) {
            // Load product by ID
            loadProductById(productId)
        } else {
            showError("Mahsulot topilmadi")
            finish()
            return
        }
    }

    private fun loadProductById(productId: Int) {
        lifecycleScope.launch {
            try {
                Log.d(TAG, "Loading product by ID: $productId")

                val response = ApiHelper.getApi().getProduct(productId)

                if (ApiHelper.isSuccessful(response)) {
                    val product = response.body()?.data
                    if (product != null) {
                        currentProduct = product
                        displayProduct(product)
                        loadAdditionalData(productId)
                    } else {
                        showError("Mahsulot ma'lumotlari topilmadi")
                        finish()
                    }
                } else {
                    val errorMessage = ApiHelper.getErrorMessage(response)
                    Log.e(TAG, "Failed to load product: $errorMessage")
                    showError("Mahsulotni yuklashda xato")
                    finish()
                }

            } catch (e: Exception) {
                Log.e(TAG, "Exception loading product: ${e.message}", e)
                showError("Internet aloqasini tekshiring")
                finish()
            } finally {
                showLoading(false)
            }
        }
    }

    private fun displayProduct(product: Product) {
        Log.d(TAG, "Displaying product: ${product.name}")

        // Product name and toolbar title
        binding.productName.text = product.name
        binding.toolbarTitle.text = product.name

        // Price
        val currentPrice = product.getCurrentPrice()
        binding.currentPrice.text = formatPrice(currentPrice)

        // Original price and discount
        if (product.hasDiscount()) {
            binding.originalPrice.visibility = View.VISIBLE
            binding.originalPrice.text = formatPrice(product.price)
            binding.originalPrice.paintFlags = binding.originalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            binding.discountBadge.visibility = View.VISIBLE
            binding.discountBadge.text = "-${product.getDiscountPercentage()}%"
        } else {
            binding.originalPrice.visibility = View.GONE
            binding.discountBadge.visibility = View.GONE
        }

        // Rating and reviews - NULL SAFETY FIX
        val avgRating = product.averageRating ?: 0.0
        val reviewCount = product.reviewsCount ?: 0

        if (avgRating > 0) {
            binding.ratingContainer.visibility = View.VISIBLE
            binding.productRating.text = "$avgRating ⭐"
            binding.reviewCount.text = "($reviewCount ta sharh)"
        } else {
            binding.ratingContainer.visibility = View.GONE
        }

        // Stock status
        if (product.isInStock()) {
            binding.stockStatus.text = "Mavjud"
            binding.stockStatus.setTextColor(getColor(R.color.success_color))
            binding.addToCartButton.isEnabled = true
        } else {
            binding.stockStatus.text = "Tugagan"
            binding.stockStatus.setTextColor(getColor(R.color.error_color))
            binding.addToCartButton.isEnabled = false
            binding.addToCartButton.text = "Tugagan"
        }

        // Description
        binding.productDescription.text = product.description ?: product.shortDescription ?: "Mahsulot haqida ma'lumot yo'q"

        // Setup images
        setupProductImages(product)

        // Update quantity and price
        updateQuantityDisplay()

        showLoading(false)
    }

    private fun setupProductImages(product: Product) {
        productImages.clear()

        // Add main image
        if (!product.image.isNullOrEmpty()) {
            productImages.add(product.image)
        }

        // Add gallery images
        product.gallery?.let { gallery ->
            productImages.addAll(gallery)
        }

        // If no images, add placeholder
        if (productImages.isEmpty()) {
            productImages.add("") // Empty string for placeholder
        }

        imageAdapter.notifyDataSetChanged()
        setupDotsIndicator()
    }

    private fun setupDotsIndicator() {
        // Create dots for image indicators
        binding.dotsIndicator.removeAllViews()

        if (productImages.size > 1) {
            for (i in productImages.indices) {
                val dot = View(this).apply {
                    layoutParams = android.widget.LinearLayout.LayoutParams(16, 16).apply {
                        setMargins(4, 0, 4, 0)
                    }
                    // Create simple dot drawable
                    setBackgroundResource(if (i == 0) R.color.primary_color else android.R.color.darker_gray)
                }
                binding.dotsIndicator.addView(dot)
            }

            // ViewPager page change listener
            binding.imageViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    updateDotsIndicator(position)
                }
            })
        }
    }

    private fun updateDotsIndicator(position: Int) {
        for (i in 0 until binding.dotsIndicator.childCount) {
            val dot = binding.dotsIndicator.getChildAt(i)
            dot.setBackgroundResource(if (i == position) R.color.primary_color else android.R.color.darker_gray)
        }
    }

    private fun loadAdditionalData(productId: Int) {
        // Load related products
        lifecycleScope.launch {
            try {
                currentProduct?.let { product ->
                    // Load products from same category
                    val response = ApiHelper.getApi().getProductsByCategory(product.categoryId)

                    if (ApiHelper.isSuccessful(response)) {
                        val categoryProducts = response.body()?.data?.data ?: emptyList()

                        // Filter out current product and take first 5
                        val filtered = categoryProducts.filter { it.id != productId }.take(5)

                        relatedProducts.clear()
                        relatedProducts.addAll(filtered)
                        relatedProductsAdapter.notifyDataSetChanged()

                        Log.d(TAG, "Related products loaded: ${filtered.size}")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading related products: ${e.message}", e)
                // Load dummy related products for demo
                loadDummyRelatedProducts()
            }
        }
    }

    private fun loadDummyRelatedProducts() {
        relatedProducts.clear()

        // FIXED: Correct Product constructor arguments
        relatedProducts.addAll(listOf(
            Product(
                id = 99,
                name = "O'xshash mahsulot 1",
                slug = "similar-1",
                description = "Similar product description",
                shortDescription = "O'xshash mahsulot",
                price = 25000.0,
                salePrice = null,
                sku = "SIM001",
                stockQuantity = 10,
                weight = null,
                dimensions = null,
                image = "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400",
                gallery = null,
                categoryId = 1,
                isFeatured = false,
                isActive = true,
                metaTitle = null,
                metaDescription = null,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z",
                averageRating = 4.2,
                reviewsCount = 15
            ),
            Product(
                id = 100,
                name = "O'xshash mahsulot 2",
                slug = "similar-2",
                description = "Similar product description",
                shortDescription = "O'xshash mahsulot",
                price = 35000.0,
                salePrice = 30000.0,
                sku = "SIM002",
                stockQuantity = 20,
                weight = null,
                dimensions = null,
                image = "https://images.unsplash.com/photo-1572635196237-14b3f281503f?w=400",
                gallery = null,
                categoryId = 1,
                isFeatured = false,
                isActive = true,
                metaTitle = null,
                metaDescription = null,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z",
                averageRating = 4.5,
                reviewsCount = 8
            ),
            Product(
                id = 101,
                name = "O'xshash mahsulot 3",
                slug = "similar-3",
                description = "Similar product description",
                shortDescription = "O'xshash mahsulot",
                price = 15000.0,
                salePrice = null,
                sku = "SIM003",
                stockQuantity = 30,
                weight = null,
                dimensions = null,
                image = "https://images.unsplash.com/photo-1586495777744-4413f21062fa?w=400",
                gallery = null,
                categoryId = 1,
                isFeatured = false,
                isActive = true,
                metaTitle = null,
                metaDescription = null,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z",
                averageRating = 4.0,
                reviewsCount = 22
            )
        ))

        relatedProductsAdapter.notifyDataSetChanged()
    }

    private fun updateQuantityDisplay() {
        binding.quantityText.text = selectedQuantity.toString()

        // Update total price
        currentProduct?.let { product ->
            val totalPrice = product.getCurrentPrice() * selectedQuantity
            binding.totalPrice.text = formatPrice(totalPrice)
        }

        // Update decrease button state
        binding.decreaseButton.isEnabled = selectedQuantity > 1
        binding.decreaseButton.alpha = if (selectedQuantity > 1) 1.0f else 0.5f

        // Update increase button state
        val maxQuantity = currentProduct?.stockQuantity ?: 1
        binding.increaseButton.isEnabled = selectedQuantity < maxQuantity
        binding.increaseButton.alpha = if (selectedQuantity < maxQuantity) 1.0f else 0.5f
    }

    private fun addToCart() {
        currentProduct?.let { product ->
            if (!product.isInStock()) {
                showMessage("Mahsulot tugagan")
                return
            }

            try {
                // Add to local cart using LocalCartManager
                localCartManager.addItem(product, selectedQuantity)

                // Update cart badge
                updateCartBadge()

                // Show success message
                showMessage("${product.name} savatga qo'shildi!")

                // Update button temporarily
                val originalText = binding.addToCartButton.text
                binding.addToCartButton.text = "Qo'shildi ✓"
                binding.addToCartButton.isEnabled = false

                // Reset button after 2 seconds
                binding.addToCartButton.postDelayed({
                    binding.addToCartButton.text = originalText
                    binding.addToCartButton.isEnabled = product.isInStock()
                }, 2000)

                Log.d(TAG, "Product added to cart: ${product.name}, quantity: $selectedQuantity")

            } catch (e: Exception) {
                Log.e(TAG, "Error adding to cart: ${e.message}", e)
                showMessage("Xatolik yuz berdi")
            }
        }
    }

    private fun updateCartBadge() {
        val cartCount = localCartManager.getItemCount()

        if (cartCount > 0) {
            binding.cartBadge.visibility = View.VISIBLE
            binding.cartBadge.text = if (cartCount > 99) "99+" else cartCount.toString()
        } else {
            binding.cartBadge.visibility = View.GONE
        }
    }

    private fun formatPrice(price: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale("uz", "UZ"))
        return "${formatter.format(price.toInt())} SO'M"
    }

    private fun navigateToCart() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("NAVIGATE_TO", "CART")
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
    }

    private fun openProductDetail(product: Product) {
        start(this, product)
    }

    private fun addRelatedProductToCart(product: Product) {
        try {
            localCartManager.addItem(product, 1)
            updateCartBadge()
            showMessage("${product.name} savatga qo'shildi!")
        } catch (e: Exception) {
            Log.e(TAG, "Error adding related product to cart: ${e.message}", e)
            showMessage("Xatolik yuz berdi")
        }
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            binding.loadingProgressBar.visibility = View.VISIBLE
            binding.contentScrollView.visibility = View.GONE
        } else {
            binding.loadingProgressBar.visibility = View.GONE
            binding.contentScrollView.visibility = View.VISIBLE
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        Log.e(TAG, "Error: $message")
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}