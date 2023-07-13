package com.android.adminportal.ui.activities.userdetails

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.android.adminportal.BR
import com.android.adminportal.R
import com.android.adminportal.data.model.Device
import com.android.adminportal.data.model.User
import com.android.adminportal.databinding.ActivityUserDetailsBinding
import com.android.adminportal.ui.activities.assigndevice.AssignDeviceActivity
import com.android.adminportal.ui.activities.signup.SignUpActivity
import com.android.adminportal.ui.base.BaseActivity
import com.android.adminportal.ui.dialogs.DevicesDialogSheet
import com.android.adminportal.ui.dialogs.ProgressDialog
import com.android.adminportal.utils.ktx.decode
import com.android.adminportal.utils.ktx.launchActivityRTL
import com.android.adminportal.utils.ktx.launchActivityWithDataRTL
import com.android.adminportal.utils.ktx.showToast
import com.android.adminportal.utils.others.AppUtils
import com.android.adminportal.utils.viewState.ResponseResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserDetailsActivity : BaseActivity<ActivityUserDetailsBinding, UserDetailsViewModel>() {

    private val viewModel: UserDetailsViewModel by viewModels()
    private lateinit var devicesSheet: DevicesDialogSheet


    @Inject
    lateinit var progress: ProgressDialog


    override fun setBindingVariable(): Int {
        return BR.userDetailsVM
    }

    override fun setLayoutId(): Int {
        return R.layout.activity_user_details
    }

    override fun setViewModel(): UserDetailsViewModel {
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        observeGetUserResult(viewModel.progress)
    }

    private fun init() {
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.toolbar.tvTitle.text = "Employee Details"
        viewDataBinding.deviceCount.paintFlags = viewDataBinding.deviceCount.paintFlags or Paint.UNDERLINE_TEXT_FLAG



        viewDataBinding.btnAssign.setOnClickListener{
            val bundle = Bundle()
            bundle.putSerializable("user", intent.getSerializableExtra("user") as User)
            launchActivityWithDataRTL(AssignDeviceActivity::class.java, bundle)
        }
    }

    /*
    * Observe progress..
    * */
    private fun observeGetUserResult(getUsersResult: LiveData<ResponseResult<User>>) {
        getUsersResult.observe(this) {

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
                    onGetUserSuccess(it.data)
                }

            }
        }
    }

    private fun onGetUserSuccess(user: User) {
        try {
            if (user.devices.isNotEmpty()) {

                val devices = user.devices.decode(Array<Device>::class.java)

                /*
                * Init sheet
                * */
                devicesSheet = DevicesDialogSheet(devices.toMutableList())
                viewDataBinding.holderDevices.setOnClickListener {
                    if (!devicesSheet.isAdded) {
                        devicesSheet.setStyle(
                            DialogFragment.STYLE_NORMAL,
                            R.style.TransparentDialog
                        )
                        devicesSheet.show(supportFragmentManager, null)
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }

    override fun onResume() {
        super.onResume()

        /*
        * Set data..
        * */
        viewModel.setUserData(intent.getSerializableExtra("user") as User)
    }

}