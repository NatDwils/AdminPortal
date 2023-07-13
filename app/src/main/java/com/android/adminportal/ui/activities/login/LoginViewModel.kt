package com.android.adminportal.ui.activities.login

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
class LoginViewModel @Inject constructor(
    var repository: AuthRepository
) : ViewModel() {

    private val _loginResult = MutableLiveData<ResponseResult<String>>()
    val loginResult: LiveData<ResponseResult<String>>
        get() = _loginResult

    /*
    * Login by firebase auth (email & password auth)
    * */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.postValue(ResponseResult.Loading)

            repository.login(email, password) { result ->
                _loginResult.postValue(result)
            }
        }
    }


}