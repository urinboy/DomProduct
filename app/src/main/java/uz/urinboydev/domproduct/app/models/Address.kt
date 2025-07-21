package uz.urinboydev.domproduct.app.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("city_id")
    val cityId: Int,

    @SerializedName("city")
    val city: City? = null,

    @SerializedName("district")
    val district: String? = null,

    @SerializedName("landmark")
    val landmark: String? = null,

    @SerializedName("phone")
    val phone: String? = null,

    @SerializedName("latitude")
    val latitude: Double? = null,

    @SerializedName("longitude")
    val longitude: Double? = null,

    @SerializedName("is_default")
    val isDefault: Boolean = false,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
) : Parcelable