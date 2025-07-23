package uz.urinboydev.domproduct.app.models

data class ApiErrorResponse(
    val message: String,
    val errors: Map<String, List<String>>?
)