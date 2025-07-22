package uz.urinboydev.domproduct.app.repository

import uz.urinboydev.domproduct.app.api.ApiService
import uz.urinboydev.domproduct.app.models.requests.CreateAddressRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddressRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getAddresses(token: String) = apiService.getAddresses(token)

    suspend fun createAddress(token: String, request: CreateAddressRequest) = apiService.createAddress(token, request)

    suspend fun updateAddress(id: Int, token: String, request: CreateAddressRequest) = apiService.updateAddress(id, token, request)

    suspend fun deleteAddress(id: Int, token: String) = apiService.deleteAddress(id, token)

    suspend fun setDefaultAddress(id: Int, token: String) = apiService.setDefaultAddress(id, token)
}
