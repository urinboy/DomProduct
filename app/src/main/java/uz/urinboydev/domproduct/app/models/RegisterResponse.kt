package uz.urinboydev.domproduct.app.models

data class RegisterResponse(
    val token: String,
    val user: User
)