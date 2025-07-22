package uz.urinboydev.domproduct.app.api

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response
import uz.urinboydev.domproduct.app.models.ApiErrorResponse
import uz.urinboydev.domproduct.app.constants.ApiConstants

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiHelper @Inject constructor(private val apiService: ApiService) {

    fun getApi(): ApiService {
        return apiService
    }

    fun isSuccessful(response: Response<*>): Boolean {
        return response.isSuccessful && response.body() != null
    }

    fun getErrorMessageFromRawBody(rawBody: String?): String {
        return try {
            rawBody?.let {
                val type = object : TypeToken<ApiErrorResponse>() {}.type
                val errorResponse: ApiErrorResponse? = Gson().fromJson(it, type)
                errorResponse?.message ?: errorResponse?.errors?.values?.flatten()?.firstOrNull() ?: "Noma'lum xato"
            } ?: "Noma'lum xato"
        } catch (e: Exception) {
            Log.e("ApiHelper", "Error parsing error message from raw body: ${e.message}", e)
            "Xato xabarini tahlil qilishda muammo: ${e.message}"
        }
    }

    fun getValidationErrorsFromRawBody(rawBody: String?): Map<String, List<String>>? {
        return try {
            rawBody?.let {
                val type = object : TypeToken<ApiErrorResponse>() {}.type
                val errorResponse: ApiErrorResponse? = Gson().fromJson(it, type)
                errorResponse?.errors
            } ?: null
        } catch (e: Exception) {
            Log.e("ApiHelper", "Error parsing validation errors from raw body: ${e.message}", e)
            null
        }
    }

    fun createAuthHeader(token: String): String {
        return "${ApiConstants.BEARER}$token"
    }
}
