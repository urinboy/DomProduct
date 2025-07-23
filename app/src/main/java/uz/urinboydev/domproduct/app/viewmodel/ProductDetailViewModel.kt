package uz.urinboydev.domproduct.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.urinboydev.domproduct.app.api.ApiHelper
import uz.urinboydev.domproduct.app.models.CartItem
import uz.urinboydev.domproduct.app.models.Product
import uz.urinboydev.domproduct.app.utils.Resource
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val apiHelper: ApiHelper
) : ViewModel() {

    private val _productDetail = MutableLiveData<Resource<Product>>()
    val productDetail: LiveData<Resource<Product>> = _productDetail

    private val _addToCartStatus = MutableLiveData<Resource<CartItem>>()
    val addToCartStatus: LiveData<Resource<CartItem>> = _addToCartStatus

    private val _productsByCategory = MutableLiveData<Resource<List<Product>>>()
    val productsByCategory: LiveData<Resource<List<Product>>> = _productsByCategory

    fun getProduct(productId: Int) {
        viewModelScope.launch {
            _productDetail.postValue(Resource.Loading)
            try {
                val response = apiHelper.getApi().getProductDetail(productId)
                if (response.isSuccessful) {
                    response.body()?.let { _productDetail.postValue(Resource.Success(it)) }
                } else {
                    _productDetail.postValue(Resource.Error(apiHelper.getErrorMessageFromRawBody(response.errorBody()?.string())))
                }
            } catch (e: Exception) {
                _productDetail.postValue(Resource.Error(e.message ?: "Tarmoq xatosi"))
            }
        }
    }

    fun addToCart(token: String, productId: Int, quantity: Int) {
        viewModelScope.launch {
            _addToCartStatus.postValue(Resource.Loading)
            try {
                val response = apiHelper.getApi().addToCart(token, productId, quantity)
                if (response.isSuccessful) {
                    response.body()?.let { _addToCartStatus.postValue(Resource.Success(it)) }
                } else {
                    _addToCartStatus.postValue(Resource.Error(apiHelper.getErrorMessageFromRawBody(response.errorBody()?.string())))
                }
            } catch (e: Exception) {
                _addToCartStatus.postValue(Resource.Error(e.message ?: "Tarmoq xatosi"))
            }
        }
    }

    fun getProductsByCategory(categoryId: Int) {
        viewModelScope.launch {
            _productsByCategory.postValue(Resource.Loading)
            try {
                val response = apiHelper.getApi().getProductsByCategory(categoryId)
                if (response.isSuccessful) {
                    response.body()?.let { _productsByCategory.postValue(Resource.Success(it)) }
                } else {
                    _productsByCategory.postValue(Resource.Error(apiHelper.getErrorMessageFromRawBody(response.errorBody()?.string())))
                }
            } catch (e: Exception) {
                _productsByCategory.postValue(Resource.Error(e.message ?: "Tarmoq xatosi"))
            }
        }
    }
}