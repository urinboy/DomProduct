package uz.urinboydev.domproduct.app.repository

import uz.urinboydev.domproduct.app.api.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getProducts(
        page: Int = 1,
        perPage: Int = 20,
        categoryId: Int? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        sortBy: String? = null,
        sortOrder: String? = null
    ) = apiService.getProducts(page, perPage, categoryId, minPrice, maxPrice, sortBy, sortOrder)

    suspend fun getProduct(id: Int) = apiService.getProduct(id)

    suspend fun getFeaturedProducts() = apiService.getFeaturedProducts()

    suspend fun searchProducts(
        query: String,
        page: Int = 1,
        perPage: Int = 20
    ) = apiService.searchProducts(query, page, perPage)

    suspend fun getProductsByCategory(
        categoryId: Int,
        page: Int = 1,
        perPage: Int = 20
    ) = apiService.getProductsByCategory(categoryId, page, perPage)
}
