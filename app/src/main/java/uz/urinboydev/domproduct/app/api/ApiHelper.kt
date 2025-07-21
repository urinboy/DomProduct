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
        return response.isSuccessful && response.body()?.status == "success"
    }

    // Error message olish
    fun <T> getErrorMessage(response: retrofit2.Response<ApiResponse<T>>): String {
        return when {
            response.body()?.message != null -> response.body()!!.message
            response.errorBody() != null -> "Server xatoligi"
            !response.isSuccessful -> "Network xatoligi"
            else -> "Noma'lum xato"
        }
    }
}