package uz.urinboydev.domproduct.app.repository

import uz.urinboydev.domproduct.app.api.ApiService
import uz.urinboydev.domproduct.app.models.requests.CancelOrderRequest
import uz.urinboydev.domproduct.app.models.requests.CreateOrderRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getOrders(
        token: String,
        status: String? = null,
        page: Int = 1,
        perPage: Int = 20
    ) = apiService.getOrders(token, status, page, perPage)

    suspend fun createOrder(token: String, request: CreateOrderRequest) = apiService.createOrder(token, request)

    suspend fun getOrderDetails(id: Int, token: String) = apiService.getOrderDetails(id, token)

    suspend fun cancelOrder(id: Int, token: String, request: CancelOrderRequest) = apiService.cancelOrder(id, token, request)

    suspend fun reorder(id: Int, token: String) = apiService.reorder(id, token)

    suspend fun getOrderStatusHistory(id: Int, token: String) = apiService.getOrderStatusHistory(id, token)
}
