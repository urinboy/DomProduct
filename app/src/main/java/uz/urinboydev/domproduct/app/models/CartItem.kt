package uz.urinboydev.domproduct.app.models

import com.google.gson.annotations.SerializedName

data class CartItem(
    @SerializedName("id")
    val id: Int,

    @SerializedName("product_id")
    val productId: Int,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("price")
    val price: Double,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    // Product relationship
    @SerializedName("product")
    val product: Product? = null
) {
    fun getTotalPrice(): Double {
        return price * quantity
    }
}
