package uz.urinboydev.domproduct.app.models.requests

import com.google.gson.annotations.SerializedName

data class CreateReviewRequest(
    @SerializedName("product_id")
    val productId: Int,

    @SerializedName("rating")
    val rating: Int,

    @SerializedName("comment")
    val comment: String? = null
)