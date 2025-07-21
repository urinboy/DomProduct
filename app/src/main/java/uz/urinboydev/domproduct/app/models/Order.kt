package uz.urinboydev.domproduct.app.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    @SerializedName("id")
    val id: Int,

    @SerializedName("order_number")
    val orderNumber: String,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("status")
    val status: OrderStatus,

    @SerializedName("subtotal")
    val subtotal: Double,

    @SerializedName("delivery_fee")
    val deliveryFee: Double,

    @SerializedName("total")
    val total: Double,

    @SerializedName("delivery_date")
    val deliveryDate: String? = null,

    @SerializedName("delivery_time_slot")
    val deliveryTimeSlot: String? = null,

    @SerializedName("payment_method")
    val paymentMethod: String,

    @SerializedName("payment_status")
    val paymentStatus: String,

    @SerializedName("notes")
    val notes: String? = null,

    @SerializedName("address")
    val address: Address? = null,  // Address allaqachon Parcelable

    @SerializedName("items")
    val items: List<OrderItem> = emptyList(),  // OrderItem ham Parcelable bo'lishi kerak

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
) : Parcelable {

    fun getStatusText(): String {
        return when (status) {
            OrderStatus.PENDING -> "Kutilmoqda"
            OrderStatus.CONFIRMED -> "Tasdiqlangan"
            OrderStatus.PROCESSING -> "Tayyorlanmoqda"
            OrderStatus.SHIPPED -> "Yo'lda"
            OrderStatus.DELIVERED -> "Yetkazilgan"
            OrderStatus.CANCELLED -> "Bekor qilingan"
        }
    }

    fun getStatusColor(): String {
        return when (status) {
            OrderStatus.PENDING -> "#FF9800"
            OrderStatus.CONFIRMED -> "#2196F3"
            OrderStatus.PROCESSING -> "#9C27B0"
            OrderStatus.SHIPPED -> "#00BCD4"
            OrderStatus.DELIVERED -> "#4CAF50"
            OrderStatus.CANCELLED -> "#F44336"
        }
    }
}

enum class OrderStatus {
    @SerializedName("pending")
    PENDING,

    @SerializedName("confirmed")
    CONFIRMED,

    @SerializedName("processing")
    PROCESSING,

    @SerializedName("shipped")
    SHIPPED,

    @SerializedName("delivered")
    DELIVERED,

    @SerializedName("cancelled")
    CANCELLED
}