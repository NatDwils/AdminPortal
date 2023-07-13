package com.android.adminportal.ui.activities.userdetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.adminportal.data.model.Device
import com.android.adminportal.data.model.User
import com.android.adminportal.data.local.room.AppDatabase
import com.android.adminportal.data.remote.repository.UserRepository
import com.android.adminportal.utils.ktx.encode
import com.android.adminportal.utils.viewState.ResponseResult
import com.android.adminportal.utils.others.AppUtils
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(var repository: UserRepository) : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: MutableLiveData<User?> = _user

    private val _progress = MutableLiveData<ResponseResult<User>>()
    val progress: LiveData<ResponseResult<User>> = _progress


    @Inject
    lateinit var database: AppDatabase

    fun setUserData(user: User) {
        getUserDevices(user)
    }

    fun getUserDevices(user: User) {
        if (user.id.isNotEmpty()) {
            viewModelScope.launch {

                _progress.postValue(ResponseResult.Loading)

                if (AppUtils.isInternetAvailable()) {
                    repository.getUserDevices(user) { result ->
                        if (result is ResponseResult.Success) {
                            updateUserDevices(result.data)
                            _user.postValue(result.data)
                            _progress.postValue(ResponseResult.Success(result.data))
                        } else if (result is ResponseResult.Error) {
                            _progress.postValue(ResponseResult.Error("Something went wrong !!"))
                        }
                    }
                } else {
                    database.usersDao().getUserById(userId = user.id).onEach {
                        /*
                        * Update binding and post user
                        * */
                        it?.let {
                            _user.postValue(it)
                            _progress.postValue(ResponseResult.Success(user))
                        }
                    }.collect()
                }
            }
        }
    }

    private fun updateUserDevices(user: User) {
        viewModelScope.launch {
            database.usersDao().updateUser(user)
        }
    }

}