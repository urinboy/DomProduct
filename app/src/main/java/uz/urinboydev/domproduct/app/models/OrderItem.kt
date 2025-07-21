package uz.urinboydev.domproduct.app.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderItem(
    @SerializedName("id")
    val id: Int,

    @SerializedName("order_id")
    val orderId: Int,

    @SerializedName("product_id")
    val productId: Int,

    @SerializedName("product")
    val product: Product? = null,  // Product allaqachon Parcelable

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("price")
    val price: Double,

    @SerializedName("total")
    val total: Double
) : Parcelable