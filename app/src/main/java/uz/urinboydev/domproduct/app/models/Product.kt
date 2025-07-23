package uz.urinboydev.domproduct.app.models

data class Product(
    val id: Int,
    val name: String,
    val slug: String,
    val description: String?,
    val shortDescription: String?,
    val price: Double,
    val salePrice: Double?,
    val sku: String?,
    val stockQuantity: Int,
    val weight: Double?,
    val dimensions: String?,
    val image: String?,
    val gallery: List<String>?,
    val categoryId: Int,
    val isFeatured: Boolean,
    val isActive: Boolean,
    val metaTitle: String?,
    val metaDescription: String?,
    val createdAt: String,
    val updatedAt: String,
    val averageRating: Double?,
    val reviewsCount: Int?
) {
    fun getCurrentPrice(): Double {
        return salePrice ?: price
    }

    fun hasDiscount(): Boolean {
        return salePrice != null && salePrice < price
    }

    fun getDiscountPercentage(): Int {
        if (hasDiscount()) {
            return ((price - salePrice!!) / price * 100).toInt()
        }
        return 0
    }

    fun isInStock(): Boolean {
        return stockQuantity > 0
    }
}