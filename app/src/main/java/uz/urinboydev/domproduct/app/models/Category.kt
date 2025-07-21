package uz.urinboydev.domproduct.app.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("slug")
    val slug: String? = null,  // Optional qilish

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("image")
    val image: String? = null,

    @SerializedName("parent_id")
    val parentId: Int? = null,

    @SerializedName("sort_order")
    val sortOrder: Int = 0,

    @SerializedName("is_active")
    val isActive: Boolean = true,

    @SerializedName("children_count")
    val childrenCount: Int = 0,

    @SerializedName("products_count")
    val productsCount: Int = 0,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
) : Parcelable