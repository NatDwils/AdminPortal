package com.android.adminportal.ui.activities.resetpassword

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import com.android.adminportal.BR
import com.android.adminportal.R
import com.android.adminportal.data.local.MyPref.DataPreference
import com.android.adminportal.data.local.MyPref.DataPreferenceKeys
import com.android.adminportal.databinding.ActivityResetPasswordBinding
import com.android.adminportal.ui.activities.dashboard.DashboardActivity
import com.android.adminportal.ui.activities.login.LoginActivity
import com.android.adminportal.ui.activities.signup.SignUpActivity
import com.android.adminportal.ui.base.BaseActivity
import com.android.adminportal.ui.dialogs.ProgressDialog
import com.android.adminportal.utils.ktx.isValidEmailAddress
import com.android.adminportal.utils.ktx.launchActivityRTL
import com.android.adminportal.utils.ktx.showToast
import com.android.adminportal.utils.viewState.ResponseResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ResetPasswordActivity : BaseActivity<ActivityResetPasswordBinding, ResetPasswordViewModel>() {

    private val viewModel: ResetPasswordViewModel by viewModels()

    @Inject
    lateinit var progress: ProgressDialog

    override fun setBindingVariable(): Int {
        return BR.resetPasswordVM
    }

    override fun setLayoutId(): Int {
        return R.layout.activity_reset_password
    }

    override fun setViewModel(): ResetPasswordViewModel {
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        observeResetResult(viewModel.resetResult)
    }

    private fun init() {
        /*
        * Login button click...
        * */
        viewDataBinding.btnReset.setOnClickListener {
            /*
            * Validate email..
            * */
            if (viewDataBinding.etEmail.text.toString()
                    .isValidEmailAddress() && !viewDataBinding.etEmail.text.toString()
                    .isNullOrBlank()
            ) {
                /*
                * Do reset....
                * */
                viewModel.reset(
                    email = viewDataBinding.etEmail.text.toString()
                )

            } else {
                showToast("Email should be valid or not empty.")
            }
        }

    }


    private fun observeResetResult(resetResult: LiveData<ResponseResult<String>>) {
        resetResult.observe(this) {

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
                    onResetSuccessful(it.data)
                    showToast("Reset email sent")

                }

            }
        }
    }

    private fun onResetSuccessful(data: String) {
        DataPreference.addValue(DataPreferenceKeys.USER_SESSION, data)
        launchActivityRTL(LoginActivity::class.java)
        finish()
    }
}