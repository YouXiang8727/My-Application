package com.youxiang8727.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youxiang8727.myapplication.domain.common.ApiResult
import com.youxiang8727.myapplication.domain.model.YouBikeData
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
        MutableStateFlow(ApiResult.Loading())

    val youBikeApiResult = _youBikeApiResult.asStateFlow()

    fun getYouBikeData() {
        viewModelScope.launch {
            getYouBikeDataUseCase().collect {
                when(it) {
                    is ApiResult.Loading -> {
                        _youBikeApiResult.value = it.copy(
                            _data = _youBikeApiResult.value.data,
                            _message = _youBikeApiResult.value.message
                        )
                    }
                    else -> {
                        _youBikeApiResult.value = it
                    }
                }
            }
        }
    }

    init {
        getYouBikeData()
    }
}