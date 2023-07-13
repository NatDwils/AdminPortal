package com.android.adminportal.ui.activities.devicedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.adminportal.data.model.Device
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeviceDetailsViewModel @Inject constructor() : ViewModel() {

    private val _device = MutableLiveData<Device>()
    val device: LiveData<Device> = _device

    fun setDeviceData(device: Device) {
        _device.postValue(device)
    }
}