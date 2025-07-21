package uz.urinboydev.domproduct.app.models

data class AddToCartRequest(
    val product_id: Int,
    val quantity: Int = 1
)