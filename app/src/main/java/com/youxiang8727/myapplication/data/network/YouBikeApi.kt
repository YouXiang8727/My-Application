package com.youxiang8727.myapplication.data.network

import com.youxiang8727.myapplication.data.network.dto.YouBikeDataDto
import retrofit2.Response
import retrofit2.http.GET

interface YouBikeApi {
    companion object {
        const val BASE_URL = "https://tcgbusfs.blob.core.windows.net/"
    }
    @GET("dotapp/youbike/v2/youbike_immediate.json")
    suspend fun getYouBikeData(): Response<List<YouBikeDataDto>>
}