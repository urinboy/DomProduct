package uz.urinboydev.domproduct.app.models.requests

import com.google.gson.annotations.SerializedName

data class CancelOrderRequest(
    @SerializedName("reason")
    val reason: String
)