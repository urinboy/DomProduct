package uz.urinboydev.domproduct.app.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import uz.urinboydev.domproduct.app.models.CartItem
import uz.urinboydev.domproduct.app.models.Category
import uz.urinboydev.domproduct.app.models.LoginRequest
import uz.urinboydev.domproduct.app.models.LoginResponse
import uz.urinboydev.domproduct.app.models.Product
import uz.urinboydev.domproduct.app.models.RegisterRequest
import uz.urinboydev.domproduct.app.models.RegisterResponse
import uz.urinboydev.domproduct.app.models.User

interface ApiService {

    // Auth
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @GET("auth/me")
    suspend fun getProfile(@Header("Authorization") token: String): Response<User>

    @PUT("auth/profile")
    suspend fun updateProfile(@Header("Authorization") token: String, @Body user: User): Response<User>

    // Products
    @GET("products")
    suspend fun getProducts(): Response<List<Product>>

    @GET("products/{id}")
    suspend fun getProductDetail(@Path("id") productId: Int): Response<Product>

    @GET("products")
    suspend fun getProductsByCategory(@Query("category_id") categoryId: Int): Response<List<Product>>

    // Categories
    @GET("categories")
    suspend fun getCategories(): Response<List<Category>>

    // Cart
    @GET("cart")
    suspend fun getCart(@Header("Authorization") token: String): Response<List<CartItem>>

    @POST("cart/add")
    suspend fun addToCart(
        @Header("Authorization") token: String,
        @Query("product_id") productId: Int,
        @Query("quantity") quantity: Int
    ): Response<CartItem>

    @PUT("cart/update/{id}")
    suspend fun updateCartItem(
        @Path("id") cartItemId: Int,
        @Header("Authorization") token: String,
        @Query("quantity") quantity: Int
    ): Response<CartItem>

    @POST("cart/remove/{id}")
    suspend fun removeFromCart(
        @Path("id") cartItemId: Int,
        @Header("Authorization") token: String
    ): Response<Void>

    @POST("cart/clear")
    suspend fun clearCart(@Header("Authorization") token: String): Response<Void>
}