package com.youxiang8727.myapplication.domain.usecase

import com.youxiang8727.myapplication.domain.common.ApiResult
import com.youxiang8727.myapplication.domain.model.YouBikeData
import com.youxiang8727.myapplication.domain.repository.YouBikeApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

data class GetYouBikeDataUseCase @Inject constructor(
    private val youBikeApiRepository: YouBikeApiRepository
) {
    operator fun invoke(): Flow<ApiResult<List<YouBikeData>>> = flow {
        emit(ApiResult.Loading())
        emit(youBikeApiRepository.getYouBikeData())
    }
}
