package uz.urinboydev.domproduct.app.models

import com.google.gson.annotations.SerializedName

data class ApiErrorResponse(
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("errors")
    val errors: Map<String, List<String>>? = null // Validation xatolari uchun
)