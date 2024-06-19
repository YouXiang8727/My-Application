package com.youxiang8727.myapplication.data.repository

import android.content.Context
import com.youxiang8727.myapplication.data.network.YouBikeApi
import com.youxiang8727.myapplication.data.network.dto.toYouBikeDataList
import com.youxiang8727.myapplication.domain.common.ApiResult
import com.youxiang8727.myapplication.domain.model.YouBikeData
import com.youxiang8727.myapplication.domain.repository.YouBikeApiRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class YouBikeApiRepositoryImpl @Inject constructor(
    private val youBikeApi: YouBikeApi,
    private val context: Context
): YouBikeApiRepository {
    override suspend fun getYouBikeData(): ApiResult<List<YouBikeData>> {
        val response = youBikeApi.getYouBikeData()
        if (response.isSuccessful) {
            val youBikeData = response.body()
            return if (youBikeData == null) {
                ApiResult.Error("data is null")
            }else {
                ApiResult.Success(
                    youBikeData.toYouBikeDataList(context)
                )
            }
        }else {
            val errorMessage = response.errorBody()?.string()
            response.errorBody()?.close()
            return ApiResult.Error(errorMessage)
        }
    }
}