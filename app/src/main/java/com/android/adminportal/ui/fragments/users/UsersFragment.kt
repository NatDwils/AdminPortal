package com.android.adminportal.ui.fragments.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.android.adminportal.BR
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.android.adminportal.R
import com.android.adminportal.data.model.User
import com.android.adminportal.databinding.FragmentUsersBinding
import com.android.adminportal.ui.activities.userdetails.UserDetailsActivity
import com.android.adminportal.ui.adapters.UserListAdapter
import com.android.adminportal.ui.base.BaseFragment
import com.android.adminportal.ui.dialogs.ProgressDialog
import com.android.adminportal.utils.ktx.launchActivityWithDataRTL
import com.android.adminportal.utils.ktx.showToast
import com.android.adminportal.utils.viewState.ResponseResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UsersFragment : BaseFragment<FragmentUsersBinding, UsersViewModel>() {

    private val viewModel: UsersViewModel by viewModels()

    @Inject
    lateinit var progress: ProgressDialog

    override fun setBindingVariable(): Int {
        return BR.usersVM
    }

    override fun setLayoutId(): Int {
        return R.layout.fragment_users
    }

    override fun setViewModel(): UsersViewModel {
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
        observeGetUsersResult(viewModel.getUsersResult)
    }

    private fun init() {
        viewDataBinding.toolbar.tvTitle.text = "Employees"
        viewModel.fetchAllUsers()
    }

    /*
    * Observe getAllDevices result..
    * */
    private fun observeGetUsersResult(getUsersResult: LiveData<ResponseResult<List<User>>>) {
        getUsersResult.observe(this) {

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
                    onGetAllUsersSuccessful(it.data)
                }

            }
        }
    }

    private fun onGetAllUsersSuccessful(data: List<User>) {
        /*
        * Set adapter & data...
        * */
        val adapter = UserListAdapter()
        adapter.setData(data.filter { !it.admin })
        viewDataBinding.listUsers.adapter = adapter

        /*
        * Set item clicks
        * */
        adapter.setItemClickListener(object : UserListAdapter.OnItemClickListener {
            override fun onItemClickListener(user: User?) {
                val bundle = Bundle()
                bundle.putSerializable("user", user)
                baseActivity?.launchActivityWithDataRTL(UserDetailsActivity::class.java, bundle)
            }

            override fun onItemLongClickListener(user: User?) {
                //LongClickActionsDialog(mutableListOf()).display(childFragmentManager)
            }

        })

        /*
        * Set search with name of user..
        * */
        viewDataBinding.search.etSearch.doOnTextChanged { text, start, before, count ->
            adapter.filter.filter(text)
        }
    }


}