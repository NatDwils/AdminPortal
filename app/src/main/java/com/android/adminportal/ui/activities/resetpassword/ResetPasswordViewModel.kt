package com.android.adminportal.ui.activities.resetpassword

import androidx.lifecycle.ViewModel
import com.android.adminportal.data.remote.repositoryImpl.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    var repository: AuthRepository
) : ViewModel() {



}