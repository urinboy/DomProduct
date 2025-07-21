package uz.urinboydev.domproduct.app.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.urinboydev.domproduct.app.constants.ApiConstants
import java.util.concurrent.TimeUnit

object ApiClient {

    private var retrofit: Retrofit? = null

    // Header interceptor - default headerlarni qo'shish
    private fun getHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header(ApiConstants.CONTENT_TYPE, "application/json")
                .header(ApiConstants.ACCEPT, "application/json")
                .header(ApiConstants.ACCEPT_LANGUAGE, "uz") // Backend Accept-Language ni kutadi
                .method(original.method, original.body)

            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    // OkHttp Client yaratish
    private fun getOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(getHeaderInterceptor())
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