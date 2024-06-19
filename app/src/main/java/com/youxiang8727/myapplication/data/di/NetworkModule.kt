package com.youxiang8727.myapplication.data.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.youxiang8727.myapplication.data.network.YouBikeApi
import com.youxiang8727.myapplication.data.repository.YouBikeApiRepositoryImpl
import com.youxiang8727.myapplication.domain.repository.YouBikeApiRepository
import com.youxiang8727.myapplication.domain.usecase.GetYouBikeDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        moshi: Moshi,
        client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(YouBikeApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()

    @Provides
    @Singleton
    fun provideYouBikeApi(
        retrofit: Retrofit
    ): YouBikeApi =
        retrofit.create(YouBikeApi::class.java)

    @Provides
    @Singleton
    fun provideYouBikeApiRepository(
        youBikeApi: YouBikeApi,
        @ApplicationContext context: Context
    ): YouBikeApiRepository =
        YouBikeApiRepositoryImpl(youBikeApi, context)

    @Provides
    @Singleton
    fun provideGetYouBikeDataUseCase(
        youBikeApiRepository: YouBikeApiRepository
    ): GetYouBikeDataUseCase =
        GetYouBikeDataUseCase(youBikeApiRepository)

}