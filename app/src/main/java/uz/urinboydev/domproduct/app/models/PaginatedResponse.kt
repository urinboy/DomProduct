package uz.urinboydev.domproduct.app.models

import com.google.gson.annotations.SerializedName

data class PaginatedResponse<T>(
    @SerializedName("data")
    val data: List<T>,

    @SerializedName("current_page")
    val currentPage: Int,

    @SerializedName("last_page")
    val lastPage: Int,

    @SerializedName("per_page")
    val perPage: Int,

    @SerializedName("total")
    val total: Int
)