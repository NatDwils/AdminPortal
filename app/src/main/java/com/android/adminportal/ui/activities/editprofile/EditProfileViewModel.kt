package com.android.adminportal.ui.activities.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.adminportal.data.model.User
import com.android.adminportal.data.remote.repository.ProfileRepository
import com.android.adminportal.utils.viewState.ResponseResult
import com.android.adminportal.utils.others.AppUtils
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
   var repository: ProfileRepository
) : ViewModel() {

    private val _updateUserResult = MutableLiveData<ResponseResult<User>>()
    val updateUserResult: LiveData<ResponseResult<User>>
        get() = _updateUserResult

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun fetchUserDetails(user: User?) {
        viewModelScope.launch {
            repository.fetchUserDetails(user) { userDetails ->
                _user.postValue(userDetails)
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            if (AppUtils.isInternetAvailable()) {
                _updateUserResult.postValue(ResponseResult.Loading)
                repository.updateUser(user) { result ->
                    _updateUserResult.postValue(result)
                }
            } else {
                _updateUserResult.postValue(ResponseResult.Error("Please check internet connection."))
            }
        }
    }
}