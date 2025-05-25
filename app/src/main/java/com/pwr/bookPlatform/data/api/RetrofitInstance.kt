package com.pwr.bookPlatform.data.api

import com.pwr.bookPlatform.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = BuildConfig.API_BASE_URL

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    val bookApi: BookApi by lazy {
        retrofit.create(BookApi::class.java)
    }

    val reviewApi: ReviewApi by lazy {
        retrofit.create(ReviewApi::class.java)
    }

    val postApi: PostApi by lazy {
        retrofit.create(PostApi::class.java)
    }
}

