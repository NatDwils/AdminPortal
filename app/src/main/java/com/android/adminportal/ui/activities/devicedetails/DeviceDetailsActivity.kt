package com.android.adminportal.ui.activities.devicedetails

import android.os.Bundle
import androidx.activity.viewModels
import com.android.adminportal.BR
import com.android.adminportal.R
import com.android.adminportal.data.model.Device
import com.android.adminportal.databinding.ActivityDeviceDetailsBinding
import com.android.adminportal.ui.base.BaseActivity
import com.android.adminportal.ui.dialogs.ProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DeviceDetailsActivity : BaseActivity<ActivityDeviceDetailsBinding, DeviceDetailsViewModel>() {

    private val viewModel: DeviceDetailsViewModel by viewModels()

    @Inject
    lateinit var progress: ProgressDialog

    override fun setBindingVariable(): Int {
        return BR.deviceDetailsVM
    }

    override fun setLayoutId(): Int {
        return R.layout.activity_device_details
    }

    override fun setViewModel(): DeviceDetailsViewModel {
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.toolbar.tvTitle.text = "Device Details"

        /*
        * Set data..
        * */
        viewModel.setDeviceData(intent.getSerializableExtra("device") as Device)
    }
}