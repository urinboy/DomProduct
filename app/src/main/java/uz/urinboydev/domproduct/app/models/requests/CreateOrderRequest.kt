package uz.urinboydev.domproduct.app.models.requests

import com.google.gson.annotations.SerializedName

data class CreateOrderRequest(
    @SerializedName("address_id")
    val addressId: Int,

    @SerializedName("delivery_date")
    val deliveryDate: String? = null,

    @SerializedName("delivery_time_slot")
    val deliveryTimeSlot: String? = null,

    @SerializedName("payment_method")
    val paymentMethod: String = "cash",

    @SerializedName("notes")
    val notes: String? = null
)