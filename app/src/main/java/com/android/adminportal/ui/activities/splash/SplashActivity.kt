package com.android.adminportal.ui.activities.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.android.adminportal.BR
import com.android.adminportal.R
import com.android.adminportal.data.local.MyPref.DataPreference
import com.android.adminportal.data.local.MyPref.DataPreferenceKeys
import com.android.adminportal.databinding.ActivitySplashBinding
import com.android.adminportal.ui.activities.dashboard.DashboardActivity
import com.android.adminportal.ui.base.BaseActivity
import com.android.adminportal.ui.activities.login.LoginActivity
import com.android.adminportal.utils.ktx.launchActivityRTL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {

    private val viewModel: SplashViewModel by viewModels()
    private val delay: Long = 1500

    override fun setBindingVariable(): Int {
        return BR.splashVM
    }

    override fun setLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun setViewModel(): SplashViewModel {
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isUserAlreadyLogin()
    }

    private fun isUserAlreadyLogin() {
        val value = DataPreference.getValue(DataPreferenceKeys.USER_SESSION, String::class.java)

        Handler(Looper.getMainLooper()).postDelayed({
            if (value.isNullOrEmpty()) {
                launchActivityRTL(LoginActivity::class.java)
                finish()
            } else {
                launchActivityRTL(DashboardActivity::class.java)
                finish()
            }
        }, delay)
    }
}