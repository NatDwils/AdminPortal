package com.android.adminportal.ui.activities.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.adminportal.data.model.User
import com.android.adminportal.data.remote.repositoryImpl.AuthRepository
import com.android.adminportal.utils.viewState.ResponseResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    var repository: AuthRepository
) : ViewModel() {

    private val _registerResult = MutableLiveData<ResponseResult<User>>()
    val registerResult: LiveData<ResponseResult<User>>
        get() = _registerResult

    fun register(user: User, password: String) {
        _registerResult.postValue(ResponseResult.Loading)
        repository.register(user, password) { result ->
            _registerResult.postValue(result)
        }
    }


}