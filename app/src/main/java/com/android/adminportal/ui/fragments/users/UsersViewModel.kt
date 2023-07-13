package com.android.adminportal.ui.fragments.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.adminportal.data.model.User
import com.android.adminportal.data.local.room.AppDatabase
import com.android.adminportal.data.remote.repository.DeviceRepository
import com.android.adminportal.data.remote.repository.UserRepository
import com.android.adminportal.utils.viewState.ResponseResult
import com.android.adminportal.utils.others.AppUtils
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(var repository: UserRepository) : ViewModel() {
    private val _getUsersResult = MutableLiveData<ResponseResult<List<User>>>()
    val getUsersResult: LiveData<ResponseResult<List<User>>>
        get() = _getUsersResult

    @Inject
    lateinit var database: AppDatabase

    fun fetchAllUsers() {
        viewModelScope.launch {
            _getUsersResult.postValue(ResponseResult.Loading)

            /*
            * Check internet connection...
            * */
            if (AppUtils.isInternetAvailable()) {
                /*
                * Load data from network when internet is there...
                * */
                repository.fetchAllUsers { result ->
                    _getUsersResult.postValue(result)
                    if (result is ResponseResult.Success) {
                        insertUsersInDatabase(result.data)
                    }
                }
            }else {
                /*
                * Load data from database...
                * */
                database.usersDao().getAllUsers().onEach {
                    _getUsersResult.postValue(
                        ResponseResult.Success(it)
                    )
                }.collect()
            }
        }
    }

    private fun insertUsersInDatabase(list: List<User>) {
        viewModelScope.launch {
            database.usersDao().deleteAll()
            database.usersDao().insertUsers(list)
        }
    }

}
