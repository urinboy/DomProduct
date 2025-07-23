package uz.urinboydev.domproduct.app.models

data class Category(
    val id: Int,
    val name: String,
    val slug: String,
    val image: String?,
    val parentId: Int?,
    val createdAt: String,
    val updatedAt: String
)