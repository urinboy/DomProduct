package uz.urinboydev.domproduct.app.api

import retrofit2.Response
import retrofit2.http.*
import uz.urinboydev.domproduct.app.constants.ApiConstants // Bu qator mavjudligiga ishonch hosil qiling
import uz.urinboydev.domproduct.app.models.*
import uz.urinboydev.domproduct.app.models.requests.*

interface ApiService {

    // ===== AUTHENTICATION =====
    @POST(ApiConstants.LOGIN) // ApiConstants dan foydalanilgan
    suspend fun login(@Body loginRequest: LoginRequest): Response<ApiResponse<AuthResponse>>

    @POST(ApiConstants.REGISTER) // ApiConstants dan foydalanilgan
    suspend fun register(@Body registerRequest: RegisterRequest): Response<ApiResponse<AuthResponse>>

    @POST(ApiConstants.LOGOUT) // ApiConstants dan foydalanilgan
    suspend fun logout(@Header("Authorization") token: String): Response<ApiResponse<String>>

    @GET(ApiConstants.ME) // ApiConstants dan foydalanilgan
    suspend fun getMe(@Header("Authorization") token: String): Response<ApiResponse<User>>

    // ===== CATEGORIES =====
    @GET(ApiConstants.CATEGORIES)
    suspend fun getCategories(
        @Query("parent_id") parentId: Int? = null
    ): Response<ApiResponse<List<Category>>>

    @GET(ApiConstants.CATEGORIES_MAIN)
    suspend fun getMainCategories(): Response<ApiResponse<List<Category>>>

    @GET("${ApiConstants.CATEGORIES}/{id}")
    suspend fun getCategory(@Path("id") id: Int): Response<ApiResponse<Category>>

    // ===== PRODUCTS =====
    @GET(ApiConstants.PRODUCTS)
    suspend fun getProducts(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("category_id") categoryId: Int? = null,
        @Query("min_price") minPrice: Double? = null,
        @Query("max_price") maxPrice: Double? = null,
        @Query("sort_by") sortBy: String? = null,
        @Query("sort_order") sortOrder: String? = null
    ): Response<ApiResponse<PaginatedResponse<Product>>>

    @GET("${ApiConstants.PRODUCTS}/{id}")
    suspend fun getProduct(@Path("id") id: Int): Response<ApiResponse<Product>>

    @GET(ApiConstants.PRODUCTS_FEATURED)
    suspend fun getFeaturedProducts(): Response<ApiResponse<List<Product>>>

    @GET(ApiConstants.PRODUCTS_SEARCH)
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20
    ): Response<ApiResponse<PaginatedResponse<Product>>>

    @GET("products/category/{categoryId}")
    suspend fun getProductsByCategory(
        @Path("categoryId") categoryId: Int,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20
    ): Response<ApiResponse<PaginatedResponse<Product>>>

    // ===== CART =====
    @GET(ApiConstants.CART)
    suspend fun getCart(@Header("Authorization") token: String): Response<ApiResponse<List<CartItem>>>

    @POST(ApiConstants.CART)
    suspend fun addToCart(
        @Header("Authorization") token: String,
        @Body request: AddToCartRequest
    ): Response<ApiResponse<CartItem>>

    @PUT("${ApiConstants.CART}/{id}")
    suspend fun updateCartItem(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
        @Body request: UpdateCartRequest
    ): Response<ApiResponse<CartItem>>

    @DELETE("${ApiConstants.CART}/{id}")
    suspend fun removeFromCart(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<ApiResponse<String>>

    @DELETE(ApiConstants.CART)
    suspend fun clearCart(@Header("Authorization") token: String): Response<ApiResponse<String>>

    // ===== CITIES =====
    @GET(ApiConstants.CITIES)
    suspend fun getCities(): Response<ApiResponse<List<City>>>

    @GET(ApiConstants.CITIES_DELIVERY)
    suspend fun getDeliveryAvailableCities(): Response<ApiResponse<List<City>>>

    @GET("${ApiConstants.CITIES}/{id}")
    suspend fun getCity(@Path("id") id: Int): Response<ApiResponse<City>>

    @GET("${ApiConstants.CITIES}/{id}/delivery-fee")
    suspend fun getDeliveryFee(@Path("id") id: Int): Response<ApiResponse<DeliveryFeeResponse>>

    // ===== ADDRESSES =====
    @GET("addresses")
    suspend fun getAddresses(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<Address>>>

    @POST("addresses")
    suspend fun createAddress(
        @Header("Authorization") token: String,
        @Body request: CreateAddressRequest
    ): Response<ApiResponse<Address>>

    @PUT("addresses/{id}")
    suspend fun updateAddress(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
        @Body request: CreateAddressRequest
    ): Response<ApiResponse<Address>>

    @DELETE("addresses/{id}")
    suspend fun deleteAddress(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<ApiResponse<String>>

    @POST("addresses/{id}/set-default")
    suspend fun setDefaultAddress(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Address>>

    // ===== ORDERS =====
    @GET("orders")
    suspend fun getOrders(
        @Header("Authorization") token: String,
        @Query("status") status: String? = null,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20
    ): Response<ApiResponse<PaginatedResponse<Order>>>

    @POST("orders")
    suspend fun createOrder(
        @Header("Authorization") token: String,
        @Body request: CreateOrderRequest
    ): Response<ApiResponse<Order>>

    @GET("orders/{id}")
    suspend fun getOrderDetails(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Order>>

    @POST("orders/{id}/cancel")
    suspend fun cancelOrder(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
        @Body request: CancelOrderRequest
    ): Response<ApiResponse<Order>>

    @POST("orders/{id}/reorder")
    suspend fun reorder(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Order>>

    @GET("orders/{id}/status-history")
    suspend fun getOrderStatusHistory(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<OrderStatusHistory>>>

    // ===== REVIEWS =====
    @GET("products/{productId}/reviews")
    suspend fun getProductReviews(
        @Path("productId") id: Int,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20
    ): Response<ApiResponse<PaginatedResponse<ReviewWithUser>>>

    @POST("reviews")
    suspend fun createReview(
        @Header("Authorization") token: String,
        @Body request: CreateReviewRequest
    ): Response<ApiResponse<Review>>

    @PUT("reviews/{id}")
    suspend fun updateReview(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
        @Body request: UpdateReviewRequest
    ): Response<ApiResponse<Review>>

    @DELETE("reviews/{id}")
    suspend fun deleteReview(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<ApiResponse<String>>
}