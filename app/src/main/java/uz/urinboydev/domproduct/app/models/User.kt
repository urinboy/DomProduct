package uz.urinboydev.domproduct.app.models

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String?,
    val email_verified_at: String?,
    val phone_verified_at: String?,
    val role: String,
    val status: String,
    val created_at: String,
    val updated_at: String
)