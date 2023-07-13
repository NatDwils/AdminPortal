package com.android.adminportal.ui.activities.resetpassword

import android.os.Bundle
import androidx.activity.viewModels
import com.android.adminportal.BR
import com.android.adminportal.R
import com.android.adminportal.databinding.ActivityResetPasswordBinding
import com.android.adminportal.ui.base.BaseActivity
import com.android.adminportal.ui.dialogs.ProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ResetPasswordActivity : BaseActivity<ActivityResetPasswordBinding, ResetPasswordViewModel>() {

    private val viewModel: ResetPasswordViewModel by viewModels()

    @Inject
    lateinit var progress: ProgressDialog

    override fun setBindingVariable(): Int {
        return BR.resetVM
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
//        observeRegisterResult(viewModel.registerResult)
    }

    private fun init() {
        /*
        * Reset password
        * */

    }
}