package com.android.adminportal.ui.fragments.devices

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.adminportal.data.model.Device
import com.android.adminportal.data.local.room.AppDatabase
import com.android.adminportal.data.remote.repository.DeviceRepository
import com.android.adminportal.utils.viewState.ResponseResult
import com.android.adminportal.utils.others.AppUtils
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DevicesViewModel @Inject constructor( var repository: DeviceRepository) : ViewModel() {
    private val _getDevicesResult = MutableLiveData<ResponseResult<List<Device>>>()
    val getDevicesResult: LiveData<ResponseResult<List<Device>>>
        get() = _getDevicesResult

    private val _devicesDeleteResult = MutableLiveData<ResponseResult<String>>()
    val devicesDeleteResult: LiveData<ResponseResult<String>>
        get() = _devicesDeleteResult

    @Inject
    lateinit var database: AppDatabase

    fun fetchAllDevices() {
        viewModelScope.launch {
            _getDevicesResult.postValue(ResponseResult.Loading)
            /*
            * Check internet connection...
            * */
            if (AppUtils.isInternetAvailable()) {
                /*
                * Load data from network when internet is there...
                * */
                repository.fetchAllDevices { result ->
                    _getDevicesResult.postValue(result)
                    if (result is ResponseResult.Success) {
                        val devices = result.data.filter { !it.isOfflineDeleted }
                        insertDevicesInDatabase(devices)
                    }
                }

            } else {
                /*
                * Load data from database...
                * */
                database.devicesDao().getAllDevices().onEach {
                    _getDevicesResult.postValue(
                        ResponseResult.Success(it.filter { item -> !item.isOfflineDeleted })
                    )
                }.collect()
            }
        }
    }

    private fun insertDevicesInDatabase(list: List<Device>) {
        viewModelScope.launch {
            database.devicesDao().deleteAll()
            database.devicesDao().insertDevices(list)
        }
    }

    fun deleteDevice(device: Device?) {
        if (AppUtils.isInternetAvailable()) {
            repository.deleteDevice(device) { result ->
                _devicesDeleteResult.postValue(result)
            }
        } else {
            viewModelScope.launch {
                device?.isOfflineDeleted = true
                database.devicesDao().updateDevice(device)
                _devicesDeleteResult.postValue(ResponseResult.Success(""))
            }
        }
    }
}