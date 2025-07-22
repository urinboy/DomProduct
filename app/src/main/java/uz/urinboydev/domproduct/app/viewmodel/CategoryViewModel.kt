package uz.urinboydev.domproduct.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import uz.urinboydev.domproduct.app.api.ApiHelper
import uz.urinboydev.domproduct.app.repository.CategoryRepository
import uz.urinboydev.domproduct.app.utils.Resource
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val apiHelper: ApiHelper
) : ViewModel() {

    fun getCategories(parentId: Int? = null) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val response = categoryRepository.getCategories(parentId)
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

    fun getCategory(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val response = categoryRepository.getCategory(id)
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
