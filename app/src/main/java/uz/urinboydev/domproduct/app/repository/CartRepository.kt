package uz.urinboydev.domproduct.app.repository

import uz.urinboydev.domproduct.app.api.ApiService
import uz.urinboydev.domproduct.app.models.AddToCartRequest
import uz.urinboydev.domproduct.app.models.UpdateCartRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getCart(token: String) = apiService.getCart(token)

    suspend fun addToCart(token: String, request: AddToCartRequest) = apiService.addToCart(token, request)

    suspend fun updateCartItem(id: Int, token: String, request: UpdateCartRequest) = apiService.updateCartItem(id, token, request)

    suspend fun removeFromCart(id: Int, token: String) = apiService.removeFromCart(id, token)

    suspend fun clearCart(token: String) = apiService.clearCart(token)
}
