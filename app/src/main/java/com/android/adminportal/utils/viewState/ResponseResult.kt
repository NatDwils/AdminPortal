package com.android.adminportal.utils.viewState

sealed class ResponseResult<out T> {
    data class Success<out T>(val data: T) : ResponseResult<T>()
    data class Error(val exception: String) : ResponseResult<Nothing>()
    object Loading : ResponseResult<Nothing>()
}