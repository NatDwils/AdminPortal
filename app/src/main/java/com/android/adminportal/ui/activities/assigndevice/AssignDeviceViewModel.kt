package com.android.adminportal.ui.activities.assigndevice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.adminportal.data.local.room.AppDatabase
import com.android.adminportal.data.model.Device
import com.android.adminportal.data.model.User
import com.android.adminportal.data.remote.repository.DeviceRepository
import com.android.adminportal.utils.others.AppUtils
import com.android.adminportal.utils.viewState.ResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssignDeviceViewModel @Inject constructor(var repository: DeviceRepository) : ViewModel()  {

    private val _assignResult = MutableLiveData<ResponseResult<User>>()
    val assignResult: LiveData<ResponseResult<User>>
    get() = _assignResult

    @Inject
    lateinit var database: AppDatabase

    fun assignDevice(device: Device, user: User) {
            if (AppUtils.isInternetAvailable()) {
                _assignResult.postValue(ResponseResult.Loading)
                repository.assignDevice(device, user) {
                    _assignResult.postValue(it)
                }
            } else {
//                viewModelScope.launch {
//                    device?.isOfflineAdded = true
//                    database.devicesDao().updateDevice(device)
//                    _assignResult.postValue(ResponseResult.Success(""))
//                }
                _assignResult.postValue(ResponseResult.Error("No internet connection. Please check your internet connectivity."))

            }
    }
}