package uz.urinboydev.domproduct.app.repository

import uz.urinboydev.domproduct.app.api.ApiService
import uz.urinboydev.domproduct.app.models.LoginRequest
import uz.urinboydev.domproduct.app.models.RegisterRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun login(request: LoginRequest) = apiService.login(request)

    suspend fun register(request: RegisterRequest) = apiService.register(request)

    suspend fun getMe(token: String) = apiService.getMe(token)

    suspend fun logout(token: String) = apiService.logout(token)
}
