package uz.urinboydev.domproduct.app.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("slug")
    val slug: String,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("short_description")
    val shortDescription: String? = null,

    @SerializedName("price")
    val price: Double,

    @SerializedName("sale_price")
    val salePrice: Double? = null,

    @SerializedName("sku")
    val sku: String? = null,

    @SerializedName("stock_quantity")
    val stockQuantity: Int = 0,

    @SerializedName("weight")
    val weight: String? = null,

    @SerializedName("dimensions")
    val dimensions: String? = null,

    @SerializedName("image")
    val image: String? = null,

    @SerializedName("gallery")
    val gallery: List<String>? = null,

    @SerializedName("category_id")
    val categoryId: Int,

    @SerializedName("is_featured")
    val isFeatured: Boolean = false,

    @SerializedName("is_active")
    val isActive: Boolean = true,

    @SerializedName("meta_title")
    val metaTitle: String? = null,

    @SerializedName("meta_description")
    val metaDescription: String? = null,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    // Calculated fields
    @SerializedName("average_rating")
    val averageRating: Double? = 0.0,

    @SerializedName("reviews_count")
    val reviewsCount: Int? = 0
) : Parcelable {
    // Helper methods
    fun getCurrentPrice(): Double {
        return salePrice ?: price
    }

    fun hasDiscount(): Boolean {
        return salePrice != null && salePrice < price
    }

    fun getDiscountPercentage(): Int {
        return if (hasDiscount()) {
            ((price - getCurrentPrice()) / price * 100).toInt()
        } else 0
    }

    fun isInStock(): Boolean {
        return stockQuantity > 0
    }
}