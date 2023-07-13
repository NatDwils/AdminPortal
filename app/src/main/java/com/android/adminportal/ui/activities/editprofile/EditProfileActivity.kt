package com.android.adminportal.ui.activities.editprofile

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import com.android.adminportal.BR
import com.android.adminportal.R
import com.android.adminportal.data.model.User
import com.android.adminportal.data.local.MyPref.DataPreference
import com.android.adminportal.data.local.MyPref.DataPreferenceKeys
import com.android.adminportal.databinding.ActivityEditProfileBinding
import com.android.adminportal.ui.base.BaseActivity
import com.android.adminportal.ui.dialogs.ProgressDialog
import com.android.adminportal.utils.ktx.*
import com.android.adminportal.utils.viewState.ResponseResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileActivity : BaseActivity<ActivityEditProfileBinding, EditProfileViewModel>() {

    private val viewModel: EditProfileViewModel by viewModels()

    @Inject
    lateinit var progress: ProgressDialog

    override fun setBindingVariable(): Int {
        return BR.editProfileVM
    }

    override fun setLayoutId(): Int {
        return R.layout.activity_edit_profile
    }

    override fun setViewModel(): EditProfileViewModel {
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        observeUpdateResult(viewModel.updateUserResult)
    }

    private fun init() {
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.toolbar.tvTitle.text = "Edit Profile"

        /*
        * Get user by pref..
        * */
        val user =
            DataPreference.getValue(DataPreferenceKeys.USER_SESSION, String::class.java)?.decode(User::class.java)

        viewModel.fetchUserDetails(user)

        /*
        * update user click..
        * */
        viewDataBinding.btnUpdate.setOnClickListener {
            if (validateName()!! && validatePhoneNo()!!) {
                user?.name = viewDataBinding.etName.text.toString()
                user?.mobileNumber = viewDataBinding.etPhoneNo.text.toString()
                viewModel.updateUser(user!!)
            }
        }
    }

    /*
    * Observe update result..
    * */
    private fun observeUpdateResult(result: LiveData<ResponseResult<User>>) {
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
                    onUpdateSuccess(it.data)
                }

            }
        }
    }

    private fun onUpdateSuccess(user: User) {
        DataPreference.addValue(DataPreferenceKeys.USER_SESSION, user.encode(User::class.java))
        showToast("Profile update successfully.")
        onBackPressed()
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
    * validate phone no.
    * */
    private fun validatePhoneNo(): Boolean? {
        val value: String = viewDataBinding.etPhoneNo.text.toString()
        return if (value.isEmpty()) {
            showToast("Phone no. can't be empty.")
            false
        } else if (value.length < 10) {
            showToast("Phone no. not valid.")
            false
        } else {
            true
        }
    }

}