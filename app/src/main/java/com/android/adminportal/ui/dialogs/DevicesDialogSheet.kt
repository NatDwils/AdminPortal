package com.android.adminportal.ui.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.android.adminportal.R
import com.android.adminportal.data.model.Device
import com.android.adminportal.databinding.SheetDevicesBinding
import com.android.adminportal.ui.activities.devicedetails.DeviceDetailsActivity
import com.android.adminportal.ui.adapters.DeviceListAdapter
import com.android.adminportal.ui.base.BaseBottomSheetFragment
import com.android.adminportal.utils.ktx.launchActivityWithDataRTL

class DevicesDialogSheet(val list: MutableList<Device>) :
    BaseBottomSheetFragment<SheetDevicesBinding>() {

    private lateinit var adapter: DeviceListAdapter

    override fun setLayoutId(): Int {
        return R.layout.sheet_devices
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        adapter = DeviceListAdapter()
        adapter.setData(list)
        adapter.setItemClickListener(object : DeviceListAdapter.OnItemClickListener {
            override fun onItemClickListener(device: Device?) {
                val bundle = Bundle()
                bundle.putSerializable("device", device)
                baseActivity?.launchActivityWithDataRTL(DeviceDetailsActivity::class.java, bundle)
                dismissAllowingStateLoss()
            }

            override fun onItemLongClickListener(device: Device?) {
                //TODO("Not yet implemented")
            }
        })
        viewDataBinding.listDevices.adapter = adapter
        setSearch()
    }

    /**
     * Set search
     * */
    private fun setSearch() {
        viewDataBinding.edtSearch.doOnTextChanged { text, start, before, count ->
            adapter.filter.filter(text)
        }
    }
}