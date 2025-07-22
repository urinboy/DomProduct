package uz.urinboydev.domproduct.app.repository

import uz.urinboydev.domproduct.app.api.ApiService
import uz.urinboydev.domproduct.app.models.requests.CreateReviewRequest
import uz.urinboydev.domproduct.app.models.requests.UpdateReviewRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getProductReviews(
        productId: Int,
        page: Int = 1,
        perPage: Int = 20
    ) = apiService.getProductReviews(productId, page, perPage)

    suspend fun createReview(token: String, request: CreateReviewRequest) = apiService.createReview(token, request)

    suspend fun updateReview(id: Int, token: String, request: UpdateReviewRequest) = apiService.updateReview(id, token, request)

    suspend fun deleteReview(id: Int, token: String) = apiService.deleteReview(id, token)
}
