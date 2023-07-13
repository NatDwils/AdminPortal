package com.android.adminportal.ui.activities.assigndevice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import com.android.adminportal.BR
import com.android.adminportal.R
import com.android.adminportal.data.local.MyPref.DataPreference
import com.android.adminportal.data.local.MyPref.DataPreferenceKeys
import com.android.adminportal.data.model.Device
import com.android.adminportal.data.model.User
import com.android.adminportal.databinding.ActivityAssignDeviceBinding
import com.android.adminportal.databinding.ActivitySignupBinding
import com.android.adminportal.ui.activities.login.LoginActivity
import com.android.adminportal.ui.activities.signup.SignUpViewModel
import com.android.adminportal.ui.activities.userdetails.UserDetailsActivity
import com.android.adminportal.ui.base.BaseActivity
import com.android.adminportal.ui.dialogs.ProgressDialog
import com.android.adminportal.utils.ktx.encode
import com.android.adminportal.utils.ktx.launchActivityRTL
import com.android.adminportal.utils.ktx.launchActivityWithClearTaskRTL
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
//        observeAssignResult(viewModel.assignResult)
    }

    private fun init() {
        /*
        * Register user
        * */
        viewDataBinding.btnAssign.setOnClickListener {
            val device = Device(
                    deviceId = viewDataBinding.etDeviceId.text.toString(),
                    deviceName = viewDataBinding.etDeviceName.text.toString(),
                    deviceType = viewDataBinding.etDeviceType.text.toString(),
                    model = viewDataBinding.etModel.text.toString(),
                    manufacturer = viewDataBinding.etManufracturer.text.toString(),
                    otherDetails = viewDataBinding.etOther.text.toString()
                )
            }
    }

    /*
    * Observe register result..
    * */
    private fun observeAssignResult(result: LiveData<ResponseResult<User>>) {
        result.observe(this) {

            progress.isCancelable = false

            when (it) {
                is ResponseResult.Error -> {
                    if (progress.isAdded) progress.dismissAllowingStateLoss()
                    showToast(it.exception)
                }
                is ResponseResult.Loading -> {
                    if (!progress.isAdded) progress.show(
                        supportFragmentManager, ""
                    )

                }
                is ResponseResult.Success -> {
                    if (progress.isAdded) progress.dismissAllowingStateLoss()
                    onAssignSuccess(it.data)
                }

            }
        }
    }

    private fun onAssignSuccess(user: User) {
        DataPreference.addValue(DataPreferenceKeys.USER_SESSION, user.encode(User::class.java))
        launchActivityRTL(UserDetailsActivity::class.java)
        finish()
    }

}