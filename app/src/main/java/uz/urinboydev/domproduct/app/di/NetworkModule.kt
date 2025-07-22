package uz.urinboydev.domproduct.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.urinboydev.domproduct.app.api.ApiService
import uz.urinboydev.domproduct.app.constants.ApiConstants
import uz.urinboydev.domproduct.app.utils.PreferenceManager
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAuthInterceptor(preferenceManager: PreferenceManager): Interceptor {
        return Interceptor {
            chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header(ApiConstants.CONTENT_TYPE, "application/json")
                .header(ApiConstants.ACCEPT, "application/json")
                .header(ApiConstants.ACCEPT_LANGUAGE, "uz")

            val token = preferenceManager.getToken()
            if (!token.isNullOrEmpty()) {
                requestBuilder.header(ApiConstants.AUTHORIZATION, ApiConstants.BEARER + token)
            }

            val request = requestBuilder.method(original.method, original.body).build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: Interceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.FULL_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
