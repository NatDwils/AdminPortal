package com.android.adminportal.ui.activities.assigndevice

import androidx.lifecycle.ViewModel
import com.android.adminportal.data.remote.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AssignDeviceViewModel @Inject constructor(var repository: UserRepository) : ViewModel()  {


}