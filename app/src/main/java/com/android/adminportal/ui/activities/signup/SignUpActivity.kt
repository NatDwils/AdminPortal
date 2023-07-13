package com.android.adminportal.ui.activities.signup

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import com.android.adminportal.BR
import com.android.adminportal.R
import com.android.adminportal.data.model.User
import com.android.adminportal.data.local.MyPref.DataPreference
import com.android.adminportal.data.local.MyPref.DataPreferenceKeys
import com.android.adminportal.databinding.ActivitySignupBinding
import com.android.adminportal.ui.activities.dashboard.DashboardActivity
import com.android.adminportal.ui.activities.login.LoginActivity
import com.android.adminportal.ui.base.BaseActivity
import com.android.adminportal.ui.dialogs.ProgressDialog
import com.android.adminportal.utils.ktx.*
import com.android.adminportal.utils.viewState.ResponseResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpActivity : BaseActivity<ActivitySignupBinding, SignUpViewModel>() {

    private val viewModel: SignUpViewModel by viewModels()

    @Inject
    lateinit var progress: ProgressDialog

    override fun setBindingVariable(): Int {
        return BR.signUpVM
    }

    override fun setLayoutId(): Int {
        return R.layout.activity_signup
    }

    override fun setViewModel(): SignUpViewModel {
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        observeRegisterResult(viewModel.registerResult)
    }

    private fun init() {
        /*
        * Register user
        * */
        viewDataBinding.btnSignup.setOnClickListener {
            if (validateName()!! && validateEmployeeId()!! && validatePhoneNo()!! && validateEmail()!! && validatePassword()!!) {
                val user = User(
                    name = viewDataBinding.etName.text.toString(),
                    email = viewDataBinding.etEmail.text.toString(),
                    employeeId = viewDataBinding.etEmpId.text.toString(),
                    admin = false,
                    mobileNumber = viewDataBinding.etPhoneNo.text.toString()
                )

                viewModel.register(user, viewDataBinding.etPassword.text.toString())
            }
        }

        /*
        * already have account
        * */
        viewDataBinding.btnLogin.setOnClickListener {
            onBackPressed()
        }
    }

    /*
    * Observe register result..
    * */
    private fun observeRegisterResult(result: LiveData<ResponseResult<User>>) {
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
                    onRegisterSuccess(it.data)
                }

            }
        }
    }

    private fun onRegisterSuccess(user: User) {
        DataPreference.addValue(DataPreferenceKeys.USER_SESSION, user.encode(User::class.java))
        launchActivityWithClearTaskRTL(LoginActivity::class.java)
        finish()
    }

    /*
    * validate name
    * */
    private fun validateName(): Boolean? {
        val value: String = viewDataBinding.etName.text.toString()
        return if (value.isEmpty()) {
            showToast("Name can't be empty.")
            false
        } else {
            true
        }
    }

    /*
    * validate email
    * */
    private fun validateEmail(): Boolean? {
        val value: String = viewDataBinding.etEmail.text.toString()
        return if (!value.isValidEmailAddress()) {
            showToast("Invalid email.")
            false
        } else {
            true
        }
    }

    /*
    * validate phone no.
    * */
    private fun validatePhoneNo(): Boolean? {
        val value: String = viewDataBinding.etPhoneNo.text.toString()
        return if (value.isEmpty()) {
            showToast("Phone no. can't be empty.")
            false
        } else if (value.isValidPhoneNumber()) {
            showToast("Phone no. not valid.")
            false
        } else {
            true
        }
    }

    /*
    * validate employee id..
    * */
    private fun validateEmployeeId(): Boolean? {
        val value: String = viewDataBinding.etEmpId.text.toString()
        return if (value.isEmpty()) {
            showToast("Employee Id can't be empty.")
            false
        } else if (value.length < 6) {
            showToast("Employee Id not valid.")
            false
        } else {
            true
        }
    }

    /*
    * validate password..
    * */
    private fun validatePassword(): Boolean? {
        val value: String = viewDataBinding.etPassword.text.toString()
        return if (value.isEmpty()) {
            showToast("Password can't be empty.")
            false
        } else {
            true
        }
    }

}