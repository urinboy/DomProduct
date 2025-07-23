package uz.urinboydev.domproduct.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.urinboydev.domproduct.app.api.ApiHelper
import uz.urinboydev.domproduct.app.models.Category
import uz.urinboydev.domproduct.app.utils.Resource
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val apiHelper: ApiHelper
) : ViewModel() {

    private val _categories = MutableLiveData<Resource<List<Category>>>()
    val categories: LiveData<Resource<List<Category>>> = _categories

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
}