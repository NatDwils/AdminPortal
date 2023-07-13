package com.android.adminportal.ui.activities.assigndevice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.adminportal.data.model.Device
import com.android.adminportal.data.model.User
import com.android.adminportal.data.remote.repository.DeviceRepository
import com.android.adminportal.utils.viewState.ResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AssignDeviceViewModel @Inject constructor(var repository: DeviceRepository) : ViewModel()  {

    private val _assignResult = MutableLiveData<ResponseResult<User>>()
    val assignResult: LiveData<ResponseResult<User>>
    get() = _assignResult

    fun assignDevice(device: Device, user: User) {
        _assignResult.postValue(ResponseResult.Loading)
        repository.assignDevice(device, user) {
            _assignResult.postValue(it)
        }
    }



}