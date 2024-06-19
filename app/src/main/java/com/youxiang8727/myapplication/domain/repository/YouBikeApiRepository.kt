package com.youxiang8727.myapplication.domain.repository

import com.youxiang8727.myapplication.domain.common.ApiResult
import com.youxiang8727.myapplication.domain.model.YouBikeData

interface YouBikeApiRepository {
    suspend fun getYouBikeData(): ApiResult<List<YouBikeData>>
}