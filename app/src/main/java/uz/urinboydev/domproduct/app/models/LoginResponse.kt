package uz.urinboydev.domproduct.app.models

data class LoginResponse(
    val token: String,
    val user: User
)