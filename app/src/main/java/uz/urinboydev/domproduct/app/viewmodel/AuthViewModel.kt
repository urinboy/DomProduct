package uz.urinboydev.domproduct.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import uz.urinboydev.domproduct.app.api.ApiHelper
import uz.urinboydev.domproduct.app.models.LoginRequest
import uz.urinboydev.domproduct.app.models.RegisterRequest
import uz.urinboydev.domproduct.app.repository.AuthRepository
import uz.urinboydev.domproduct.app.utils.Resource
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val apiHelper: ApiHelper
) : ViewModel() {

    fun login(request: LoginRequest) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val response = authRepository.login(request)
            val rawBody = if (!response.isSuccessful) response.errorBody()?.string() else response.body().toString()
            if (apiHelper.isSuccessful(response)) {
                emit(Resource.success(data = response.body()?.data))
            } else {
                val errors = apiHelper.getValidationErrorsFromRawBody(rawBody)
                emit(Resource.error(data = null, message = apiHelper.getErrorMessageFromRawBody(rawBody), errors = errors))
            }
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Xatolik yuz berdi!"))
        }
    }

    fun register(request: RegisterRequest) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val response = authRepository.register(request)
            val rawBody = if (!response.isSuccessful) response.errorBody()?.string() else response.body().toString()
            if (apiHelper.isSuccessful(response)) {
                emit(Resource.success(data = response.body()?.data))
            } else {
                val errors = apiHelper.getValidationErrorsFromRawBody(rawBody)
                emit(Resource.error(data = null, message = apiHelper.getErrorMessageFromRawBody(rawBody), errors = errors))
            }
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Xatolik yuz berdi!"))
        }
    }

    fun getMe(token: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val response = authRepository.getMe(token)
            val rawBody = if (!response.isSuccessful) response.errorBody()?.string() else response.body().toString()
            if (apiHelper.isSuccessful(response)) {
                emit(Resource.success(data = response.body()?.data))
            } else {
                val errors = apiHelper.getValidationErrorsFromRawBody(rawBody)
                emit(Resource.error(data = null, message = apiHelper.getErrorMessageFromRawBody(rawBody), errors = errors))
            }
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Xatolik yuz berdi!"))
        }
    }

    fun logout(token: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val response = authRepository.logout(token)
            val rawBody = if (!response.isSuccessful) response.errorBody()?.string() else response.body().toString()
            if (apiHelper.isSuccessful(response)) {
                emit(Resource.success(data = response.body()?.message))
            } else {
                val errors = apiHelper.getValidationErrorsFromRawBody(rawBody)
                emit(Resource.error(data = null, message = apiHelper.getErrorMessageFromRawBody(rawBody), errors = errors))
            }
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Xatolik yuz berdi!"))
        }
    }
}
