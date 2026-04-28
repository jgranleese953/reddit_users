package com.example.imageloader.api

import android.content.Context
import com.example.imageloader.BuildConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Url
import java.util.concurrent.TimeUnit

const val cacheSize = (1 * 1024 * 1024).toLong() // 10 MB

interface ImageAPIService {
    @GET
    suspend fun downloadImage(@Url url: String): Response<ResponseBody>
}

private fun provideHttpClient(context: Context): OkHttpClient {
    return OkHttpClient
        .Builder()
        .cache(Cache(context.cacheDir, cacheSize))
        .addInterceptor(provideLogging())
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()
}

private fun provideLogging(): HttpLoggingInterceptor =
    HttpLoggingInterceptor().setLevel(if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BASIC)

private fun provideRetrofit(
    okHttpClient: OkHttpClient
): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://www.gravatar.com/avatar/")
        .client(okHttpClient)
        .build()
}

fun getImageAPIService(context: Context): ImageAPIService =
    provideRetrofit(provideHttpClient(context)).create(ImageAPIService::class.java)