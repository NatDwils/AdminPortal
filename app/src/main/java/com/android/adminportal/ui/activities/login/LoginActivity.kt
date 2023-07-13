package com.android.adminportal.ui.activities.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import com.android.adminportal.BR
import com.android.adminportal.R
import com.android.adminportal.data.local.MyPref.DataPreference
import com.android.adminportal.data.local.MyPref.DataPreferenceKeys
import com.android.adminportal.databinding.ActivityLoginBinding
import com.android.adminportal.ui.activities.dashboard.DashboardActivity
import com.android.adminportal.ui.activities.resetpassword.ResetPasswordActivity
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
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var progress: ProgressDialog

    override fun setBindingVariable(): Int {
        return BR.loginVM
    }

    override fun setLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun setViewModel(): LoginViewModel {
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        observeLoginResult(viewModel.loginResult)
    }

    private fun init() {
        /*
        * Login button click...
        * */
        viewDataBinding.btnLogin.setOnClickListener {
            /*
            * Validate email and password..
            * */
            if (viewDataBinding.etEmail.text.toString()
                    .isValidEmailAddress() && !viewDataBinding.etPassword.text.toString()
                    .isNullOrBlank()
            ) {
                /*
                * Do login....
                * */
                viewModel.login(
                    email = viewDataBinding.etEmail.text.toString(),
                    password = viewDataBinding.etPassword.text.toString()
                )

            } else {
                showToast("Email and password should be valid or not empty.")
            }
        }

        /*
        * sign up click
        * */
        viewDataBinding.btnSignup.setOnClickListener {
            launchActivityRTL(SignUpActivity::class.java)
        }

        /*
       * forget password up click
       * */
        viewDataBinding.btnReset.setOnClickListener {
            launchActivityRTL(ResetPasswordActivity::class.java)
        }

    }

    /*
    * Observe login result..
    * */
    private fun observeLoginResult(loginResult: LiveData<ResponseResult<String>>) {
        loginResult.observe(this) {

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
                    onLoginSuccessful(it.data)
                }

            }
        }
    }

    private fun onLoginSuccessful(data: String) {
        DataPreference.addValue(DataPreferenceKeys.USER_SESSION, data)
        launchActivityRTL(DashboardActivity::class.java)
        finish()
    }
}