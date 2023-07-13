package com.android.adminportal.ui.activities.resetpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.adminportal.data.remote.repositoryImpl.AuthRepository
import com.android.adminportal.utils.viewState.ResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    var repository: AuthRepository
) : ViewModel() {

    private val _resetResult = MutableLiveData<ResponseResult<String>>()
    val resetResult: LiveData<ResponseResult<String>>
        get() = _resetResult

    /*
    * Reset with firebase auth
    * */
    fun reset(email: String) {
        viewModelScope.launch {
            _resetResult.postValue(ResponseResult.Loading)

            repository.reset(email) { result ->
                _resetResult.postValue(result)
            }
        }
    }
}