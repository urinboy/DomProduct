package uz.urinboydev.domproduct.app.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.urinboydev.domproduct.app.constants.ApiConstants
import uz.urinboydev.domproduct.app.utils.PreferenceManager // PreferenceManager ni import qilish
import java.util.concurrent.TimeUnit

object ApiClient {

    private var retrofit: Retrofit? = null
    private lateinit var preferenceManager: PreferenceManager // PreferenceManager ni saqlash uchun

    // Bu metodni ilova boshlanganda bir marta chaqirish kerak
    fun initialize(context: android.content.Context) {
        preferenceManager = PreferenceManager(context)
    }

    // Header interceptor - default headerlarni qo'shish
    private fun getHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header(ApiConstants.CONTENT_TYPE, "application/json")
                .header(ApiConstants.ACCEPT, "application/json")
                .header(ApiConstants.ACCEPT_LANGUAGE, "uz") // Backend Accept-Language ni kutadi

            // Agar token mavjud bo'lsa, Authorization header ni qo'shish
            val token = preferenceManager.getToken()
            if (!token.isNullOrEmpty()) {
                requestBuilder.header(ApiConstants.AUTHORIZATION, ApiConstants.BEARER + token)
            }

            val request = requestBuilder.method(original.method, original.body).build()
            chain.proceed(request)
        }
    }

    // OkHttp Client yaratish
    private fun getOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(getHeaderInterceptor()) // Header interceptor ni qo'shish
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // Retrofit instance yaratish
    private fun getRetrofitInstance(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(ApiConstants.FULL_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    // ApiService instance olish
    fun getApiService(): ApiService {
        return getRetrofitInstance().create(ApiService::class.java)
    }
}