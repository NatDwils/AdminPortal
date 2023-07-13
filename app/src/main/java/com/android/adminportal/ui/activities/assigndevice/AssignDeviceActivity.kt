package com.android.adminportal.ui.activities.assigndevice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.android.adminportal.BR
import com.android.adminportal.R
import com.android.adminportal.databinding.ActivityAssignDeviceBinding
import com.android.adminportal.databinding.ActivitySignupBinding
import com.android.adminportal.ui.activities.signup.SignUpViewModel
import com.android.adminportal.ui.base.BaseActivity
import com.android.adminportal.ui.dialogs.ProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AssignDeviceActivity : BaseActivity<ActivityAssignDeviceBinding, AssignDeviceViewModel>() {

    private val viewModel: AssignDeviceViewModel by viewModels()

    @Inject
    lateinit var progress: ProgressDialog

    override fun setBindingVariable(): Int {
        return BR.assignDeviceVM
    }

    override fun setLayoutId(): Int {
        return R.layout.activity_assign_device
    }

    override fun setViewModel(): AssignDeviceViewModel {
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}