package com.android.adminportal.ui.activities.assigndevice

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import com.android.adminportal.BR
import com.android.adminportal.R
import com.android.adminportal.data.model.Device
import com.android.adminportal.data.model.User
import com.android.adminportal.databinding.ActivityAssignDeviceBinding
import com.android.adminportal.ui.base.BaseActivity
import com.android.adminportal.ui.dialogs.ProgressDialog
import com.android.adminportal.utils.ktx.showToast
import com.android.adminportal.utils.viewState.ResponseResult
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
        init()
        observeAssignResult(viewModel.assignResult)
    }

    private fun init() {

        val currentUser = intent.getSerializableExtra("user") as User
        val empId = currentUser.employeeId

        viewDataBinding.btnAssign.setOnClickListener {
            val device = Device(
                employeeId=empId,
                deviceId = viewDataBinding.etDeviceId.text.toString(),
                deviceName = viewDataBinding.etDeviceName.text.toString(),
                deviceType = viewDataBinding.etDeviceType.selectedItem.toString(),
                model = viewDataBinding.etModel.text.toString(),
                manufacturer = viewDataBinding.etManufracturer.text.toString(),
                otherDetails = viewDataBinding.etOther.text.toString()
            )

            viewModel.assignDevice(device, currentUser)
        }
    }


    /*
    * Observe assign result.
    * */
    private fun observeAssignResult(result: LiveData<ResponseResult<User>>) {
        result.observe(this) { response ->
            progress.isCancelable = false

            when (response) {
                is ResponseResult.Error -> {
                    if (progress.isAdded) progress.dismissAllowingStateLoss()
                    showToast(response.exception)
                }
                is ResponseResult.Loading -> {
                    if (!progress.isAdded) progress.show(
                        supportFragmentManager, ""
                    )
                }
                is ResponseResult.Success -> {
                    if (progress.isAdded) progress.dismissAllowingStateLoss()
                    onAssignSuccess(response.data)
                }
            }
        }
    }

    private fun onAssignSuccess(user: User) {
        finish()
    }

    private fun getSpinnerSelection(deviceType : String):Int{
        val deviceTypes= resources.getStringArray(R.array.device_Type)
        return deviceTypes.indexOf(deviceType)
    }
}