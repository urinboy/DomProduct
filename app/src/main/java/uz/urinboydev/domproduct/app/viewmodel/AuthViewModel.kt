package uz.urinboydev.domproduct.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.urinboydev.domproduct.app.api.ApiHelper
import uz.urinboydev.domproduct.app.models.LoginRequest
import uz.urinboydev.domproduct.app.models.LoginResponse
import uz.urinboydev.domproduct.app.models.RegisterRequest
import uz.urinboydev.domproduct.app.models.RegisterResponse
import uz.urinboydev.domproduct.app.utils.Resource
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val apiHelper: ApiHelper
) : ViewModel() {

    private val _loginResponse = MutableLiveData<Resource<LoginResponse>>()
    val loginResponse: LiveData<Resource<LoginResponse>> = _loginResponse

    private val _registerResponse = MutableLiveData<Resource<RegisterResponse>>()
    val registerResponse: LiveData<Resource<RegisterResponse>> = _registerResponse

    fun login(request: LoginRequest) {
        viewModelScope.launch {
            _loginResponse.postValue(Resource.Loading)
            try {
                val response = apiHelper.getApi().login(request)
                if (response.isSuccessful) {
                    response.body()?.let { _loginResponse.postValue(Resource.Success(it)) }
                } else {
                    _loginResponse.postValue(Resource.Error(apiHelper.getErrorMessageFromRawBody(response.errorBody()?.string())))
                }
            } catch (e: Exception) {
                _loginResponse.postValue(Resource.Error(e.message ?: "Tarmoq xatosi"))
            }
        }
    }

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            _registerResponse.postValue(Resource.Loading)
            try {
                val response = apiHelper.getApi().register(request)
                if (response.isSuccessful) {
                    response.body()?.let { _registerResponse.postValue(Resource.Success(it)) }
                } else {
                    _registerResponse.postValue(Resource.Error(apiHelper.getErrorMessageFromRawBody(response.errorBody()?.string())))
                }
            } catch (e: Exception) {
                _registerResponse.postValue(Resource.Error(e.message ?: "Tarmoq xatosi"))
            }
        }
    }
}