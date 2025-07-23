package uz.urinboydev.domproduct.app.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uz.urinboydev.domproduct.app.models.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalCartManager @Inject constructor(private val context: Context) {

    private val PREF_NAME = "LocalCartPrefs"
    private val KEY_CART_ITEMS = "cart_items"

    private val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    data class LocalCartItem(
        val productId: Int,
        val productName: String,
        val productPrice: Double,
        val productImage: String?,
        var quantity: Int
    )

    fun addItem(product: Product, quantity: Int = 1) {
        val currentItems = getItems().toMutableList()
        val existingItem = currentItems.find { it.productId == product.id }

        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            currentItems.add(
                LocalCartItem(
                    productId = product.id,
                    productName = product.name,
                    productPrice = product.getCurrentPrice(),
                    productImage = product.image,
                    quantity = quantity
                )
            )
        }
        saveItems(currentItems)
    }

    fun updateQuantity(productId: Int, newQuantity: Int) {
        val currentItems = getItems().toMutableList()
        val existingItem = currentItems.find { it.productId == productId }

        if (existingItem != null) {
            if (newQuantity > 0) {
                existingItem.quantity = newQuantity
            } else {
                currentItems.remove(existingItem)
            }
            saveItems(currentItems)
        }
    }

    fun removeItem(productId: Int) {
        val currentItems = getItems().toMutableList()
        currentItems.removeIf { it.productId == productId }
        saveItems(currentItems)
    }

    fun clearCart() {
        saveItems(emptyList())
    }

    fun getItems(): List<LocalCartItem> {
        val json = prefs.getString(KEY_CART_ITEMS, null)
        val type = object : TypeToken<List<LocalCartItem>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun getItemCount(): Int {
        return getItems().sumOf { it.quantity }
    }

    private fun saveItems(items: List<LocalCartItem>) {
        val json = gson.toJson(items)
        prefs.edit().putString(KEY_CART_ITEMS, json).apply()
    }
}