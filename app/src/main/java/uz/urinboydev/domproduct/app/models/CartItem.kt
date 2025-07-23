package uz.urinboydev.domproduct.app.models

data class CartItem(
    val id: Int,
    val productId: Int,
    val quantity: Int,
    val price: Double,
    val createdAt: String,
    val updatedAt: String,
    val product: Product
) {
    fun getTotalPrice(): Double {
        return quantity * price
    }
}