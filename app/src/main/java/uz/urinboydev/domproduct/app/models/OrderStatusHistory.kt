package uz.urinboydev.domproduct.app.models

import com.google.gson.annotations.SerializedName

data class OrderStatusHistory(
    @SerializedName("id")
    val id: Int,

    @SerializedName("order_id")
    val orderId: Int,

    @SerializedName("status")
    val status: OrderStatus,

    @SerializedName("comment")
    val comment: String? = null,

    @SerializedName("created_at")
    val createdAt: String
)