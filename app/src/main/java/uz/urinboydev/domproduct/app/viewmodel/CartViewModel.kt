package uz.urinboydev.domproduct.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.urinboydev.domproduct.app.api.ApiHelper
import uz.urinboydev.domproduct.app.models.CartItem
import uz.urinboydev.domproduct.app.utils.Resource
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val apiHelper: ApiHelper
) : ViewModel() {

    private val _cartItems = MutableLiveData<Resource<List<CartItem>>>()
    val cartItems: LiveData<Resource<List<CartItem>>> = _cartItems

    fun getCart(token: String) {
        viewModelScope.launch {
            _cartItems.postValue(Resource.Loading)
            try {
                val response = apiHelper.getApi().getCart(token)
                if (response.isSuccessful) {
                    response.body()?.let { _cartItems.postValue(Resource.Success(it)) }
                } else {
                    _cartItems.postValue(Resource.Error(apiHelper.getErrorMessageFromRawBody(response.errorBody()?.string())))
                }
            } catch (e: Exception) {
                _cartItems.postValue(Resource.Error(e.message ?: "Tarmoq xatosi"))
            }
        }
    }

    fun addToCart(token: String, productId: Int, quantity: Int) {
        viewModelScope.launch {
            // _cartItems.postValue(Resource.Loading) // Optional: show loading state
            try {
                val response = apiHelper.getApi().addToCart(token, productId, quantity)
                if (response.isSuccessful) {
                    // Refresh cart after successful addition
                    getCart(token)
                } else {
                    _cartItems.postValue(Resource.Error(apiHelper.getErrorMessageFromRawBody(response.errorBody()?.string())))
                }
            } catch (e: Exception) {
                _cartItems.postValue(Resource.Error(e.message ?: "Tarmoq xatosi"))
            }
        }
    }

    fun updateCartItem(cartItemId: Int, token: String, quantity: Int) {
        viewModelScope.launch {
            // _cartItems.postValue(Resource.Loading) // Optional: show loading state
            try {
                val response = apiHelper.getApi().updateCartItem(cartItemId, token, quantity)
                if (response.isSuccessful) {
                    getCart(token)
                } else {
                    _cartItems.postValue(Resource.Error(apiHelper.getErrorMessageFromRawBody(response.errorBody()?.string())))
                }
            } catch (e: Exception) {
                _cartItems.postValue(Resource.Error(e.message ?: "Tarmoq xatosi"))
            }
        }
    }

    fun removeFromCart(cartItemId: Int, token: String) {
        viewModelScope.launch {
            // _cartItems.postValue(Resource.Loading) // Optional: show loading state
            try {
                val response = apiHelper.getApi().removeFromCart(cartItemId, token)
                if (response.isSuccessful) {
                    getCart(token)
                } else {
                    _cartItems.postValue(Resource.Error(apiHelper.getErrorMessageFromRawBody(response.errorBody()?.string())))
                }
            } catch (e: Exception) {
                _cartItems.postValue(Resource.Error(e.message ?: "Tarmoq xatosi"))
            }
        }
    }

    fun clearCart(token: String) {
        viewModelScope.launch {
            // _cartItems.postValue(Resource.Loading) // Optional: show loading state
            try {
                val response = apiHelper.getApi().clearCart(token)
                if (response.isSuccessful) {
                    getCart(token)
                } else {
                    _cartItems.postValue(Resource.Error(apiHelper.getErrorMessageFromRawBody(response.errorBody()?.string())))
                }
            } catch (e: Exception) {
                _cartItems.postValue(Resource.Error(e.message ?: "Tarmoq xatosi"))
            }
        }
    }
}