package uz.urinboydev.domproduct.app.api

import uz.urinboydev.domproduct.app.constants.ApiConstants
import uz.urinboydev.domproduct.app.models.ApiResponse

object ApiHelper {

    private val apiService = ApiClient.getApiService()

    // Token bilan header yaratish
    fun createAuthHeader(token: String): String {
        return ApiConstants.BEARER + token
    }

    // ApiService olish
    fun getApi(): ApiService {
        return apiService
    }

    // Response muvaffaqiyatlimi tekshirish
    fun <T> isSuccessful(response: retrofit2.Response<ApiResponse<T>>): Boolean {
        return response.isSuccessful && response.body()?.success == true
    }

    // Error message olish
    fun <T> getErrorMessage(response: retrofit2.Response<ApiResponse<T>>): String {
        return when {
            // Validation errors bo'lsa
            response.body()?.errors != null -> {
                val errors = response.body()!!.errors!!
                val firstError = errors.values.firstOrNull()?.firstOrNull()
                firstError ?: response.body()!!.message
            }
            // Backend dan kelgan message
            response.body()?.message != null -> response.body()!!.message
            // HTTP errors
            response.code() == 422 -> "Ma'lumotlarni to'g'ri kiriting"
            response.code() == 401 -> "Ruxsat berilmagan"
            response.code() == 500 -> "Server xatoligi"
            response.errorBody() != null -> "Server bilan aloqa xatoligi"
            !response.isSuccessful -> "Tarmoq xatoligi"
            else -> "Noma'lum xato"
        }
    }

    // Validation errorlarini olish
    fun <T> getValidationErrors(response: retrofit2.Response<ApiResponse<T>>): Map<String, List<String>>? {
        return response.body()?.errors
    }
}