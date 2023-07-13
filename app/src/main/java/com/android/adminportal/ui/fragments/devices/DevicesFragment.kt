package com.android.adminportal.ui.fragments.devices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.android.adminportal.BR
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.android.adminportal.R
import com.android.adminportal.data.model.Device
import com.android.adminportal.databinding.FragmentDevicesBinding
import com.android.adminportal.ui.activities.devicedetails.DeviceDetailsActivity
import com.android.adminportal.ui.adapters.DeviceListAdapter
import com.android.adminportal.ui.base.BaseFragment
import com.android.adminportal.ui.dialogs.LongClickActionsDialog
import com.android.adminportal.ui.dialogs.ProgressDialog
import com.android.adminportal.utils.ktx.launchActivityWithDataRTL
import com.android.adminportal.utils.ktx.showToast
import com.android.adminportal.utils.viewState.ResponseResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DevicesFragment : BaseFragment<FragmentDevicesBinding, DevicesViewModel>() {

    private val viewModel: DevicesViewModel by viewModels()

    @Inject
    lateinit var progress: ProgressDialog

    override fun setBindingVariable(): Int {
        return BR.devicesVM
    }

    override fun setLayoutId(): Int {
        return R.layout.fragment_devices
    }

    override fun setViewModel(): DevicesViewModel {
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
        observeGetDevicesResult(viewModel.getDevicesResult)
        observeDeleteDeviceResult(viewModel.devicesDeleteResult)
    }

    private fun init() {
        viewDataBinding.toolbar.tvTitle.text = "Devices"
        viewModel.fetchAllDevices()
    }

    /*
    * Observe getAllDevices result..
    * */
    private fun observeGetDevicesResult(getDevicesResult: LiveData<ResponseResult<List<Device>>>) {
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
                    onGetAllDevicesSuccessful(it.data)
                }

            }
        }
    }

    /*
    * Observe delete device result..
    * */
    private fun observeDeleteDeviceResult(data: LiveData<ResponseResult<String>>) {
        data.observe(this) {

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
                    onDeleteDevicesSuccessful()
                }

            }
        }
    }

    private fun onDeleteDevicesSuccessful(){
        viewModel.fetchAllDevices()
    }


    private fun onGetAllDevicesSuccessful(data: List<Device>) {
        /*
         * Set adapter & data...
         * */
        val adapter = DeviceListAdapter()
        adapter.setData(data)
        viewDataBinding.listDevices.adapter = adapter

        /*
        * Set item clicks
        * */
        adapter.setItemClickListener(object : DeviceListAdapter.OnItemClickListener {
            override fun onItemClickListener(device: Device?) {
                val bundle = Bundle()
                bundle.putSerializable("device", device)
                baseActivity?.launchActivityWithDataRTL(DeviceDetailsActivity::class.java, bundle)
            }

            override fun onItemLongClickListener(device: Device?) {
                LongClickActionsDialog(mutableListOf()).display(childFragmentManager)
                    .setOnItemClickListener { parent, view, position, id ->
                        when (position) {
                            0 -> {
                                viewModel.deleteDevice(device)
                            }

                            1 -> {

                            }
                        }
                    }
            }
        })

        /*
        * Set search with emp. id of user..
        * */
        viewDataBinding.search.etSearch.doOnTextChanged { text, start, before, count ->
            adapter.filter.filter(text)
        }
    }

}