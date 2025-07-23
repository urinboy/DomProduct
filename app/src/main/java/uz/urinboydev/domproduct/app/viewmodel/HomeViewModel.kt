package uz.urinboydev.domproduct.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.urinboydev.domproduct.app.api.ApiHelper
import uz.urinboydev.domproduct.app.models.Category
import uz.urinboydev.domproduct.app.models.Product
import uz.urinboydev.domproduct.app.utils.Resource
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiHelper: ApiHelper
) : ViewModel() {

    private val _categories = MutableLiveData<Resource<List<Category>>>()
    val categories: LiveData<Resource<List<Category>>> = _categories

    private val _featuredProducts = MutableLiveData<Resource<List<Product>>>()
    val featuredProducts: LiveData<Resource<List<Product>>> = _featuredProducts

    private val _latestProducts = MutableLiveData<Resource<List<Product>>>()
    val latestProducts: LiveData<Resource<List<Product>>> = _latestProducts

    fun getCategories() {
        viewModelScope.launch {
            _categories.postValue(Resource.Loading)
            try {
                val response = apiHelper.getApi().getCategories()
                if (response.isSuccessful) {
                    response.body()?.let { _categories.postValue(Resource.Success(it)) }
                } else {
                    _categories.postValue(Resource.Error(apiHelper.getErrorMessageFromRawBody(response.errorBody()?.string())))
                }
            } catch (e: Exception) {
                _categories.postValue(Resource.Error(e.message ?: "Tarmoq xatosi"))
            }
        }
    }

    fun getFeaturedProducts() {
        viewModelScope.launch {
            _featuredProducts.postValue(Resource.Loading)
            try {
                val response = apiHelper.getApi().getProducts() // Assuming API has a way to filter featured
                if (response.isSuccessful) {
                    response.body()?.let { products ->
                        val featured = products.filter { it.isFeatured }
                        _featuredProducts.postValue(Resource.Success(featured))
                    }
                } else {
                    _featuredProducts.postValue(Resource.Error(apiHelper.getErrorMessageFromRawBody(response.errorBody()?.string())))
                }
            } catch (e: Exception) {
                _featuredProducts.postValue(Resource.Error(e.message ?: "Tarmoq xatosi"))
            }
        }
    }

    fun getLatestProducts() {
        viewModelScope.launch {
            _latestProducts.postValue(Resource.Loading)
            try {
                val response = apiHelper.getApi().getProducts() // Assuming API returns latest by default or has a filter
                if (response.isSuccessful) {
                    response.body()?.let { products ->
                        // For simplicity, assuming the API returns latest first or we sort here
                        val latest = products.sortedByDescending { it.createdAt }.take(10) // Take top 10 latest
                        _latestProducts.postValue(Resource.Success(latest))
                    }
                } else {
                    _latestProducts.postValue(Resource.Error(apiHelper.getErrorMessageFromRawBody(response.errorBody()?.string())))
                }
            } catch (e: Exception) {
                _latestProducts.postValue(Resource.Error(e.message ?: "Tarmoq xatosi"))
            }
        }
    }
}