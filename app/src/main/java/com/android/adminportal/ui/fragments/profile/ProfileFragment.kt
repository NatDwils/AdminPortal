package com.android.adminportal.ui.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.android.adminportal.BR
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import com.android.adminportal.R
import com.android.adminportal.data.model.User
import com.android.adminportal.data.local.MyPref.DataPreference
import com.android.adminportal.data.local.MyPref.DataPreferenceKeys
import com.android.adminportal.data.local.room.AppDatabase
import com.android.adminportal.databinding.FragmentProfileBinding
import com.android.adminportal.ui.activities.editprofile.EditProfileActivity
import com.android.adminportal.ui.activities.login.LoginActivity
import com.android.adminportal.ui.base.BaseFragment
import com.android.adminportal.ui.dialogs.ProgressDialog
import com.android.adminportal.utils.ktx.decode
import com.android.adminportal.utils.ktx.launchActivityRTL
import com.android.adminportal.utils.ktx.showToast
import com.android.adminportal.utils.ktx.visible
import com.android.adminportal.utils.viewState.ResponseResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    private val viewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var progress: ProgressDialog

    @Inject
    lateinit var database: AppDatabase

    override fun setBindingVariable(): Int {
        return BR.profileVM
    }

    override fun setLayoutId(): Int {
        return R.layout.fragment_profile
    }

    override fun setViewModel(): ProfileViewModel {
        return viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observeProgress(viewModel.progress)
    }

    private fun init() {
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.toolbar.tvTitle.text = "Profile"
        viewDataBinding.toolbar.btnEdit.visible()

        /*
        * Edit click..
        * */
        viewDataBinding.toolbar.btnEdit.setOnClickListener {
            baseActivity?.launchActivityRTL(EditProfileActivity::class.java)
        }

        /*
        * Logout button click..
        * */
        viewDataBinding.btnLogout.setOnClickListener {
            logout()
        }

    }

    override fun onResume() {
        super.onResume()
        /*
        * Fetch user details
        * */
        val user = DataPreference.getValue(DataPreferenceKeys.USER_SESSION, String::class.java)
        viewModel.fetchUserDetails(user?.decode(User::class.java))

    }

    /*
    * Observe progress..
    * */
    private fun observeProgress(getDevicesResult: LiveData<ResponseResult<String>>) {
        getDevicesResult.observe(this) {

            progress.isCancelable = false

            when (it) {
                is ResponseResult.Error -> {
                    if (progress.isAdded) progress.dismissAllowingStateLoss()
                    baseActivity?.showToast(it.exception)
                }
                is ResponseResult.Loading -> {
                    if (!progress.isAdded) progress.show(
                        childFragmentManager, ""
                    )

                }
                is ResponseResult.Success -> {
                    if (progress.isAdded) progress.dismissAllowingStateLoss()
                }

            }
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            firebaseAuth.signOut()
            database.devicesDao().deleteAll()
            database.usersDao().deleteAll()
            DataPreference.clearSharedPreference()
            baseActivity?.launchActivityRTL(LoginActivity::class.java)
            activity?.finish()
        }
    }
}