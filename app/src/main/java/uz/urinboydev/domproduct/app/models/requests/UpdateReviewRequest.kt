package uz.urinboydev.domproduct.app.models.requests

import com.google.gson.annotations.SerializedName

data class UpdateReviewRequest(
    @SerializedName("rating")
    val rating: Int,

    @SerializedName("comment")
    val comment: String? = null
)