package com.android.adminportal.ui.activities.dashboard

import android.os.Bundle
import androidx.activity.viewModels
import com.android.adminportal.BR
import com.android.adminportal.R
import com.android.adminportal.databinding.ActivityDashboardBinding
import com.android.adminportal.ui.base.BaseActivity
import com.android.adminportal.ui.fragments.devices.DevicesFragment
import com.android.adminportal.ui.fragments.home.HomeFragment
import com.android.adminportal.ui.fragments.profile.ProfileFragment
import com.android.adminportal.ui.fragments.users.UsersFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : BaseActivity<ActivityDashboardBinding, DashboardViewModel>() {

    private val viewModel: DashboardViewModel by viewModels()

    /*
    * Navigation Fragments
    * */
    private var homeFragment: HomeFragment? = null
    private var devicesFragment: DevicesFragment? = null
    private var usersFragment: UsersFragment? = null
    private var profileFragment: ProfileFragment? = null

    override fun setBindingVariable(): Int {
        return BR.dashboardVM
    }

    override fun setLayoutId(): Int {
        return R.layout.activity_dashboard
    }

    override fun setViewModel(): DashboardViewModel {
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init()
        init()
    }

    private fun init() {
        /*
        * Init nav items fragment..
        * */
        homeFragment = HomeFragment()
        profileFragment = ProfileFragment()
        usersFragment = UsersFragment()
        devicesFragment = DevicesFragment()

        /*
        * Load default fragment..
        * */
        loadDefaultFragment(homeFragment, R.id.container)


        /*
        * Bottom nav item click
        * */
        viewDataBinding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    if (homeFragment == null) {
                        homeFragment = HomeFragment()
                        loadDefaultFragment(homeFragment, R.id.container)
                    } else if (homeFragment?.isVisible == false) {
                        homeFragment = null
                        homeFragment = HomeFragment()
                        loadDefaultFragment(homeFragment, R.id.container)
                    }
                    true
                }

                R.id.navigation_profile -> {
                    if (profileFragment == null) {
                        profileFragment = ProfileFragment()
                        loadDefaultFragment(profileFragment, R.id.container)
                    } else if (profileFragment?.isVisible == false) {
                        profileFragment = null
                        profileFragment = ProfileFragment()
                        loadDefaultFragment(profileFragment, R.id.container)
                    }
                    true
                }

                R.id.navigation_users -> {
                    if (usersFragment == null) {
                        usersFragment = UsersFragment()
                        loadDefaultFragment(usersFragment, R.id.container)
                    } else if (usersFragment?.isVisible == false) {
                        usersFragment = null
                        usersFragment = UsersFragment()
                        loadDefaultFragment(usersFragment, R.id.container)
                    }
                    true
                }

                R.id.navigation_devices -> {
                    if (devicesFragment == null) {
                        devicesFragment = DevicesFragment()
                        loadDefaultFragment(devicesFragment, R.id.container)
                    } else if (devicesFragment?.isVisible == false) {
                        devicesFragment = null
                        devicesFragment = DevicesFragment()
                        loadDefaultFragment(devicesFragment, R.id.container)
                    }
                    true
                }


                else -> false
            }
        }

    }
}