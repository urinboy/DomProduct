package uz.urinboydev.domproduct.app.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uz.urinboydev.domproduct.app.R
import uz.urinboydev.domproduct.app.adapters.CartAdapter
import uz.urinboydev.domproduct.app.activities.MainActivity
import uz.urinboydev.domproduct.app.api.ApiHelper
import uz.urinboydev.domproduct.app.databinding.FragmentCartBinding
import uz.urinboydev.domproduct.app.models.CartItem
import uz.urinboydev.domproduct.app.utils.AuthManager
import uz.urinboydev.domproduct.app.utils.LocalCartManager
import uz.urinboydev.domproduct.app.utils.PreferenceManager
import uz.urinboydev.domproduct.app.utils.Resource
import uz.urinboydev.domproduct.app.viewmodel.CartViewModel
import javax.inject.Inject
import java.text.NumberFormat
import java.util.*

@AndroidEntryPoint
class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var preferenceManager: PreferenceManager

    @Inject
    lateinit var localCartManager: LocalCartManager

    @Inject
    lateinit var apiHelper: ApiHelper // ApiHelper ni inject qilish

    @Inject
    lateinit var authManager: AuthManager

    private val cartViewModel: CartViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter

    companion object {
        private const val TAG = "CartFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        loadCartData()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            onQuantityChange = { cartItem, newQuantity ->
                updateCartItemQuantity(cartItem, newQuantity)
            },
            onRemoveItem = { cartItem ->
                removeCartItem(cartItem)
            }
        )
        binding.cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }
    }

    private fun setupClickListeners() {
        binding.checkoutButton.setOnClickListener {
            if (authManager.isLoggedIn()) {
                showMessage("Buyurtma berish sahifasiga o'tish")
                // TODO: Navigate to CheckoutActivity
            } else {
                authManager.showLoginPrompt(requireContext(), getString(R.string.checkout_feature))
            }
        }

        binding.clearCartButton.setOnClickListener {
            clearCart()
        }
    }

    private fun loadCartData() {
        if (authManager.isLoggedIn()) {
            // Load from server cart
            loadServerCart()
        } else {
            // Load from local cart
            loadLocalCart()
        }
    }

    private fun loadServerCart() {
        val token = preferenceManager.getToken()
        if (token.isNullOrEmpty()) {
            showMessage("Tizimga kiring yoki mehmon sifatida davom eting.")
            binding.progressBar.visibility = View.GONE
            updateUI(emptyList())
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        cartViewModel.getCart(apiHelper.createAuthHeader(token)).observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        it.data?.let {
                            updateUI(it)
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showMessage("Savatni yuklashda xato: ${it.message}")
                        Log.e(TAG, "Failed to load server cart: ${it.message}")
                        updateUI(emptyList())
                    }
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun loadLocalCart() {
        val items = localCartManager.getItems()
        updateUI(items.map { LocalCartItemToCartItem(it) })
        binding.progressBar.visibility = View.GONE
    }

    private fun updateUI(cartItems: List<CartItem>) {
        if (cartItems.isEmpty()) {
            binding.emptyCartMessage.visibility = View.VISIBLE
            binding.cartContentGroup.visibility = View.GONE
        } else {
            binding.emptyCartMessage.visibility = View.GONE
            binding.cartContentGroup.visibility = View.VISIBLE
            cartAdapter.submitList(cartItems)
            updateCartSummary(cartItems)
        }
        // Update MainActivity's cart badge
        (activity as? MainActivity)?.updateCartBadge()
    }

    private fun updateCartSummary(cartItems: List<CartItem>) {
        val subtotal = cartItems.sumOf { it.getTotalPrice() }
        binding.subtotalTextView.text = formatPrice(subtotal)
        binding.totalTextView.text = formatPrice(subtotal) // Hozircha delivery fee yo'q
    }

    private fun updateCartItemQuantity(cartItem: CartItem, newQuantity: Int) {
        if (authManager.isLoggedIn()) {
            // Update server cart
            val token = preferenceManager.getToken()
            if (token.isNullOrEmpty()) {
                showMessage("Tizimga kiring yoki mehmon sifatida davom eting.")
                return
            }
            cartViewModel.updateCartItem(cartItem.id, apiHelper.createAuthHeader(token), newQuantity).observe(viewLifecycleOwner) {
                it?.let {
                    when (it) {
                        is Resource.Success -> {
                            showMessage("Savat yangilandi.")
                            loadServerCart() // Refresh cart
                        }
                        is Resource.Error -> {
                            showMessage("Savatni yangilashda xato: ${it.message}")
                            Log.e(TAG, "Failed to update server cart: ${it.message}")
                        }
                        is Resource.Loading -> {
                            // Show loading
                        }
                    }
                }
            }
        } else {
            // Update local cart
            localCartManager.updateQuantity(cartItem.productId, newQuantity)
            loadLocalCart() // Refresh cart
            showMessage("Savat yangilandi.")
        }
    }

    private fun removeCartItem(cartItem: CartItem) {
        if (authManager.isLoggedIn()) {
            // Remove from server cart
            val token = preferenceManager.getToken()
            if (token.isNullOrEmpty()) {
                showMessage("Tizimga kiring yoki mehmon sifatida davom eting.")
                return
            }
            cartViewModel.removeFromCart(cartItem.id, apiHelper.createAuthHeader(token)).observe(viewLifecycleOwner) {
                it?.let {
                    when (it) {
                        is Resource.Success -> {
                            showMessage("Mahsulot savatdan o'chirildi.")
                            loadServerCart() // Refresh cart
                        }
                        is Resource.Error -> {
                            showMessage("Mahsulotni o'chirishda xato: ${it.message}")
                            Log.e(TAG, "Failed to remove server cart item: ${it.message}")
                        }
                        is Resource.Loading -> {
                            // Show loading
                        }
                    }
                }
            }
        } else {
            // Remove from local cart
            localCartManager.removeItem(cartItem.productId)
            loadLocalCart() // Refresh cart
            showMessage("Mahsulot savatdan o'chirildi.")
        }
    }

    private fun clearCart() {
        if (authManager.isLoggedIn()) {
            // Clear server cart
            val token = preferenceManager.getToken()
            if (token.isNullOrEmpty()) {
                showMessage("Tizimga kiring yoki mehmon sifatida davom eting.")
                return
            }
            cartViewModel.clearCart(apiHelper.createAuthHeader(token)).observe(viewLifecycleOwner) {
                it?.let {
                    when (it) {
                        is Resource.Success -> {
                            showMessage("Savat tozalandi.")
                            loadServerCart() // Refresh cart
                            localCartManager.clearCart() // Lokal savatni ham tozalash
                        }
                        is Resource.Error -> {
                            showMessage("Savatni tozalashda xato: ${it.message}")
                            Log.e(TAG, "Failed to clear server cart: ${it.message}")
                        }
                        is Resource.Loading -> {
                            // Show loading
                        }
                    }
                }
            }
        } else {
            // Clear local cart
            localCartManager.clearCart()
            loadLocalCart() // Refresh cart
            showMessage("Savat tozalandi.")
        }
    }

    private fun formatPrice(price: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale("uz", "UZ"))
        return "${formatter.format(price.toInt())} SO'M"
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Helper function to convert LocalCartItem to CartItem for adapter
    private fun LocalCartItemToCartItem(localCartItem: LocalCartManager.LocalCartItem): CartItem {
        return CartItem(
            id = 0, // Lokal savatda ID bo'lmasligi mumkin, 0 yoki boshqa default qiymat
            productId = localCartItem.productId,
            quantity = localCartItem.quantity,
            price = localCartItem.productPrice,
            createdAt = "", // Default qiymat
            updatedAt = "", // Default qiymat
            product = uz.urinboydev.domproduct.app.models.Product(
                id = localCartItem.productId,
                name = localCartItem.productName,
                slug = "",
                price = localCartItem.productPrice,
                image = localCartItem.productImage,
                categoryId = 0 // Default qiymat
            )
        )
    }
}
