package uz.urinboydev.domproduct.app.models

data class RegisterRequest(
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val password_confirmation: String
)