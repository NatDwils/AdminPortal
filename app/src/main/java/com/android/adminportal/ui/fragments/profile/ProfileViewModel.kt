package com.android.adminportal.ui.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.adminportal.data.model.User
import com.android.adminportal.data.local.room.AppDatabase
import com.android.adminportal.data.remote.repository.ProfileRepository
import com.android.adminportal.utils.viewState.ResponseResult
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(var repository:ProfileRepository) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user
    private val _progress = MutableLiveData<ResponseResult<String>>()
    val progress: LiveData<ResponseResult<String>> = _progress

    @Inject
    lateinit var database: AppDatabase

    fun fetchUserDetails(user: User?) {
        viewModelScope.launch {
            repository.fetchUserDetails(user) { userDetails ->
                _user.postValue(userDetails)
            }
        }
    }

}