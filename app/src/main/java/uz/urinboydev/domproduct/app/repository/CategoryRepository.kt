package uz.urinboydev.domproduct.app.repository

import uz.urinboydev.domproduct.app.api.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getCategories(parentId: Int? = null) = apiService.getCategories(parentId)

    suspend fun getMainCategories() = apiService.getMainCategories()

    suspend fun getCategory(id: Int) = apiService.getCategory(id)
}
