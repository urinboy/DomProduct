package uz.urinboydev.domproduct.app.models.requests

import com.google.gson.annotations.SerializedName

data class CreateAddressRequest(
    @SerializedName("title")
    val title: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("city_id")
    val cityId: Int,

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
    val isDefault: Boolean = false
)