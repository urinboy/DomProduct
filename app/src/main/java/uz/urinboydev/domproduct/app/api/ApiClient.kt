package uz.urinboydev.domproduct.app.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.urinboydev.domproduct.app.constants.ApiConstants
import uz.urinboydev.domproduct.app.utils.PreferenceManager
import java.util.concurrent.TimeUnit

// ApiClient endi Hilt moduli orqali taqdim etiladi
// Bu fayl faqat ApiService interfeysini o'z ichiga oladi
// ApiClient.kt fayli endi keraksiz, shuning uchun uni o'chirib tashlaymiz
// va ApiService.kt faylini o'zgartiramiz.
