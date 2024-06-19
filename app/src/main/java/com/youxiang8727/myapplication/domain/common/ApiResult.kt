package com.youxiang8727.myapplication.domain.common

enum class ApiStatus{
    SUCCESS,
    ERROR,
    LOADING
}

sealed class ApiResult <out T> (
    val status: ApiStatus, val data: T?, val message:String?
) {

    data class Success<out R>(val _data: R?): ApiResult<R>(
        status = ApiStatus.SUCCESS,
        data = _data,
        message = null
    )

    data class Error(val exception: String? = "error message is null"): ApiResult<Nothing>(
        status = ApiStatus.ERROR,
        data = null,
        message = exception
    )

    data class Loading<out R>(
        val _data: R? = null,
        val _message: String? = null
    ): ApiResult<R>(
        status = ApiStatus.LOADING,
        data = _data,
        message = _message
    )
}