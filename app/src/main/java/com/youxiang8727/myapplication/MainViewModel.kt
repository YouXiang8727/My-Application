package com.youxiang8727.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youxiang8727.myapplication.domain.common.ApiResult
import com.youxiang8727.myapplication.domain.model.YouBikeData
import com.youxiang8727.myapplication.domain.repository.YouBikeApiRepository
import com.youxiang8727.myapplication.domain.usecase.GetYouBikeDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getYouBikeDataUseCase: GetYouBikeDataUseCase
): ViewModel() {
    private val _youBikeApiResult: MutableStateFlow<ApiResult<List<YouBikeData>>> =
        MutableStateFlow(ApiResult.Loading)

    val youBikeApiResult = _youBikeApiResult.asStateFlow()

    fun getYouBikeData() {
        viewModelScope.launch {
            getYouBikeDataUseCase().collect {
                _youBikeApiResult.value = it
            }
        }
    }

    init {
        getYouBikeData()
    }
}