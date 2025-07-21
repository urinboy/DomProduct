package uz.urinboydev.domproduct.app.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("delivery_available")
    val deliveryAvailable: Boolean = false,

    @SerializedName("delivery_fee")
    val deliveryFee: Double = 0.0,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
) : Parcelable