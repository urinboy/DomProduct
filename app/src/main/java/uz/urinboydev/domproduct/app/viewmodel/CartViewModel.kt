package uz.urinboydev.domproduct.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import uz.urinboydev.domproduct.app.api.ApiHelper
import uz.urinboydev.domproduct.app.models.AddToCartRequest
import uz.urinboydev.domproduct.app.models.UpdateCartRequest
import uz.urinboydev.domproduct.app.repository.CartRepository
import uz.urinboydev.domproduct.app.utils.Resource
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val apiHelper: ApiHelper
) : ViewModel() {

    fun getCart(token: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val response = cartRepository.getCart(token)
            val rawBody = if (!response.isSuccessful) response.errorBody()?.string() else response.body().toString()
            if (apiHelper.isSuccessful(response)) {
                emit(Resource.success(data = response.body()?.data))
            } else {
                emit(Resource.error(data = null, message = apiHelper.getErrorMessageFromRawBody(rawBody)))
            }
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Xatolik yuz berdi!"))
        }
    }

    fun addToCart(token: String, productId: Int, quantity: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val request = AddToCartRequest(productId, quantity)
            val response = cartRepository.addToCart(token, request)
            val rawBody = if (!response.isSuccessful) response.errorBody()?.string() else response.body().toString()
            if (apiHelper.isSuccessful(response)) {
                emit(Resource.success(data = response.body()?.data))
            } else {
                emit(Resource.error(data = null, message = apiHelper.getErrorMessageFromRawBody(rawBody)))
            }
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Xatolik yuz berdi!"))
        }
    }

    fun updateCartItem(id: Int, token: String, quantity: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val request = UpdateCartRequest(quantity)
            val response = cartRepository.updateCartItem(id, token, request)
            val rawBody = if (!response.isSuccessful) response.errorBody()?.string() else response.body().toString()
            if (apiHelper.isSuccessful(response)) {
                emit(Resource.success(data = response.body()?.data))
            } else {
                emit(Resource.error(data = null, message = apiHelper.getErrorMessageFromRawBody(rawBody)))
            }
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Xatolik yuz berdi!"))
        }
    }

    fun removeFromCart(id: Int, token: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val response = cartRepository.removeFromCart(id, token)
            val rawBody = if (!response.isSuccessful) response.errorBody()?.string() else response.body().toString()
            if (apiHelper.isSuccessful(response)) {
                emit(Resource.success(data = response.body()?.message))
            } else {
                emit(Resource.error(data = null, message = apiHelper.getErrorMessageFromRawBody(rawBody)))
            }
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Xatolik yuz berdi!"))
        }
    }

    fun clearCart(token: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val response = cartRepository.clearCart(token)
            val rawBody = if (!response.isSuccessful) response.errorBody()?.string() else response.body().toString()
            if (apiHelper.isSuccessful(response)) {
                emit(Resource.success(data = response.body()?.message))
            } else {
                emit(Resource.error(data = null, message = apiHelper.getErrorMessageFromRawBody(rawBody)))
            }
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Xatolik yuz berdi!"))
        }
    }
}
