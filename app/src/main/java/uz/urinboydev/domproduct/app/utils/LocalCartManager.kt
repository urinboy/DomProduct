package uz.urinboydev.domproduct.app.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uz.urinboydev.domproduct.app.models.Product

class LocalCartManager(private val context: Context) {

    private val preferenceManager = PreferenceManager(context)
    private val gson = Gson()

    companion object {
        private const val LOCAL_CART_KEY = "local_cart_items"
    }

    /**
     * Local cart item data class
     */
    data class LocalCartItem(
        val productId: Int,
        val productName: String,
        val productPrice: Double,
        val productImage: String?,
        var quantity: Int,
        val addedAt: Long = System.currentTimeMillis()
    ) {
        fun getTotalPrice(): Double = productPrice * quantity
    }

    /**
     * Cart ga mahsulot qo'shish
     */
    fun addItem(product: Product, quantity: Int = 1) {
        val items = getItems().toMutableList()

        // Agar mahsulot allaqachon bor bo'lsa, quantity ni update qilish
        val existingItem = items.find { it.productId == product.id }

        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            val newItem = LocalCartItem(
                productId = product.id,
                productName = product.name,
                productPrice = product.getCurrentPrice(),
                productImage = product.image,
                quantity = quantity
            )
            items.add(newItem)
        }

        saveItems(items)
        updateCartBadge()
    }

    /**
     * Cart item quantity ni yangilash
     */
    fun updateQuantity(productId: Int, quantity: Int) {
        val items = getItems().toMutableList()
        val item = items.find { it.productId == productId }

        if (item != null) {
            if (quantity <= 0) {
                items.remove(item)
            } else {
                item.quantity = quantity
            }
            saveItems(items)
            updateCartBadge()
        }
    }

    /**
     * Cart dan mahsulot o'chirish
     */
    fun removeItem(productId: Int) {
        val items = getItems().toMutableList()
        items.removeAll { it.productId == productId }
        saveItems(items)
        updateCartBadge()
    }

    /**
     * Cart ni tozalash
     */
    fun clearCart() {
        preferenceManager.saveAppSettings(LOCAL_CART_KEY, "")
        updateCartBadge()
    }

    /**
     * Cart items olish
     */
    fun getItems(): List<LocalCartItem> {
        val itemsJson = preferenceManager.getAppSettings(LOCAL_CART_KEY, "")

        if (itemsJson.isEmpty()) {
            return emptyList()
        }

        return try {
            val type = object : TypeToken<List<LocalCartItem>>() {}.type
            gson.fromJson<List<LocalCartItem>>(itemsJson, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Cart items soni
     */
    fun getItemCount(): Int {
        return getItems().sumOf { it.quantity }
    }

    /**
     * Cart total price
     */
    fun getTotalPrice(): Double {
        return getItems().sumOf { it.getTotalPrice() }
    }

    /**
     * Mahsulot cart da bormi?
     */
    fun isProductInCart(productId: Int): Boolean {
        return getItems().any { it.productId == productId }
    }

    /**
     * Mahsulot quantity sini olish
     */
    fun getProductQuantity(productId: Int): Int {
        return getItems().find { it.productId == productId }?.quantity ?: 0
    }

    /**
     * Items ni saqlash
     */
    private fun saveItems(items: List<LocalCartItem>) {
        val itemsJson = gson.toJson(items)
        preferenceManager.saveAppSettings(LOCAL_CART_KEY, itemsJson)
    }

    /**
     * Cart badge ni yangilash
     */
    private fun updateCartBadge() {
        val count = getItemCount()
        preferenceManager.saveCartCount(count)
    }

    /**
     * Server cart bilan sync qilish (login qilganda)
     */
    suspend fun syncToServer(token: String): Boolean {
        // TODO: Implement server sync
        // 1. Local items ni server ga yuborish
        // 2. Server response ni handle qilish
        // 3. Local cart ni tozalash
        return true
    }

    /**
     * Guest cart summary
     */
    fun getCartSummary(): CartSummary {
        val items = getItems()
        return CartSummary(
            itemCount = items.size,
            totalQuantity = getItemCount(),
            totalPrice = getTotalPrice(),
            items = items
        )
    }

    data class CartSummary(
        val itemCount: Int,
        val totalQuantity: Int,
        val totalPrice: Double,
        val items: List<LocalCartItem>
    )
}