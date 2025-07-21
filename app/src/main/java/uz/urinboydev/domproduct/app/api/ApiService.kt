package uz.urinboydev.domproduct.app.api

import retrofit2.Response
import retrofit2.http.*
import uz.urinboydev.domproduct.app.models.*
import uz.urinboydev.domproduct.app.constants.ApiConstants

interface ApiService {

    // ===== AUTHENTICATION =====
    @POST(ApiConstants.LOGIN)
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<AuthResponse>>

    @POST(ApiConstants.REGISTER)
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<AuthResponse>>

    @POST(ApiConstants.LOGOUT)
    suspend fun logout(@Header("Authorization") token: String): Response<ApiResponse<String>>

    @GET(ApiConstants.ME)
    suspend fun getMe(@Header("Authorization") token: String): Response<ApiResponse<User>>

    // ===== CATEGORIES =====
    @GET(ApiConstants.CATEGORIES)
    suspend fun getCategories(): Response<ApiResponse<List<Category>>>

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
        @Query("search") search: String? = null,
        @Query("sort") sort: String? = null
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
}