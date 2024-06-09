package com.nqmgaming.shoseshop.di

import com.nqmgaming.shoseshop.data.remote.ShoesApi
import com.nqmgaming.shoseshop.data.repository.ShoesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideShoesApi(): ShoesApi {
        return Retrofit.Builder()
            .baseUrl("https://shoes-shop-api-unl0.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }).build()
            )
            .build()
            .create(ShoesApi::class.java)
    }
    @Provides
    fun provideShoesRepository(shoesApi: ShoesApi): ShoesRepository {
        return ShoesRepository(shoesApi)
    }
}