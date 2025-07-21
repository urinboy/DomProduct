package uz.urinboydev.domproduct.app.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class ReviewWithUser(
    @SerializedName("id")
    val id: Int,

    @SerializedName("product_id")
    val productId: Int,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("user")
    val user: @RawValue User? = null,  // User Parcelable emas, shuning uchun @RawValue

    @SerializedName("rating")
    val rating: Int,

    @SerializedName("comment")
    val comment: String? = null,

    @SerializedName("is_approved")
    val isApproved: Boolean = true,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
) : Parcelable