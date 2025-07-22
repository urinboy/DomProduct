package uz.urinboydev.domproduct.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import uz.urinboydev.domproduct.app.api.ApiHelper
import uz.urinboydev.domproduct.app.models.AddToCartRequest
import uz.urinboydev.domproduct.app.repository.CategoryRepository
import uz.urinboydev.domproduct.app.repository.ProductRepository
import uz.urinboydev.domproduct.app.repository.CartRepository
import uz.urinboydev.domproduct.app.utils.Resource
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val apiHelper: ApiHelper
) : ViewModel() {

    fun getMainCategories() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val response = categoryRepository.getMainCategories()
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

    fun getFeaturedProducts() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val response = productRepository.getFeaturedProducts()
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

    fun getLatestProducts() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val response = productRepository.getProducts(
                page = 1,
                perPage = 10,
                sortBy = "created_at",
                sortOrder = "desc"
            )
            val rawBody = if (!response.isSuccessful) response.errorBody()?.string() else response.body().toString()
            if (apiHelper.isSuccessful(response)) {
                emit(Resource.success(data = response.body()?.data?.data))
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
}
