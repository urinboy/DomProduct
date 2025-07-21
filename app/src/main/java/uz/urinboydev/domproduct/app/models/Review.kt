package uz.urinboydev.domproduct.app.models

import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("id")
    val id: Int,

    @SerializedName("product_id")
    val productId: Int,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("rating")
    val rating: Int,

    @SerializedName("comment")
    val comment: String? = null,

    @SerializedName("is_approved")
    val isApproved: Boolean = false,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    // User relationship
    @SerializedName("user")
    val user: User? = null
)
