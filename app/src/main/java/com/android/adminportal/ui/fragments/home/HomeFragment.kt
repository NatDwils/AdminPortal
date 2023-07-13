package com.android.adminportal.ui.fragments.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.android.adminportal.BR
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.android.adminportal.R
import com.android.adminportal.data.model.Device
import com.android.adminportal.databinding.FragmentHomeBinding
import com.android.adminportal.ui.base.BaseFragment
import com.android.adminportal.ui.dialogs.ProgressDialog
import com.android.adminportal.ui.fragments.devices.DevicesViewModel
import com.android.adminportal.utils.ktx.showToast
import com.android.adminportal.utils.viewState.ResponseResult
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, DevicesViewModel>() {

    private val viewModel: DevicesViewModel by viewModels()

    @Inject
    lateinit var progress: ProgressDialog

    override fun setBindingVariable(): Int {
        return BR.homeVM
    }

    override fun setLayoutId(): Int {
        return R.layout.fragment_home
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
    }

    private fun init() {
        viewDataBinding.toolbar.tvTitle.text = "Home"
        viewModel.fetchAllDevices()
    }

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

    private fun onGetAllDevicesSuccessful(data: List<Device>) {
        initChart(data)
    }

    @SuppressLint("SuspiciousIndentation")
    private fun initChart(list: List<Device>) {
        /*
        * Prepare chart entries...
        * */
        val entries = ArrayList<PieEntry>()
        if(list.any { it.deviceType.lowercase(Locale.getDefault()) == "laptop" })
        entries.add(
            PieEntry(
                list.filter { it.deviceType.lowercase(Locale.getDefault()) == "laptop" }.size.toFloat(),
                "Laptop"
            )
        )

        if(list.any { it.deviceType.lowercase(Locale.getDefault()) == "desktop" })
            entries.add(
            PieEntry(
                list.filter { it.deviceType.lowercase(Locale.getDefault()) == "desktop" }.size.toFloat(),
                "Desktop"
            )
        )

        if(list.any { it.deviceType.lowercase(Locale.getDefault()) == "mobile" })
        entries.add(
            PieEntry(
                list.filter { it.deviceType.lowercase(Locale.getDefault()) == "mobile" }.size.toFloat(),
                "Mobile"
            )
        )

        if(list.any { it.deviceType.lowercase(Locale.getDefault()) == "peripherals" })
        entries.add(
            PieEntry(
                list.filter { it.deviceType.lowercase(Locale.getDefault()) == "peripherals" }.size.toFloat(),
                "Peripherals"
            )
        )

        /*
        * Prepare chart entries colors
        * */
        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.color_entity_laptop))
        colors.add(resources.getColor(R.color.color_entity_desktop))
        colors.add(resources.getColor(R.color.color_entity_mobile))
        colors.add(resources.getColor(R.color.color_entity_peripherals))

        /*
        * set entries color...
        * */
        val chartData = PieDataSet(entries, "")
        chartData.colors = colors

        /*
        * styling of single entries..
        * */
        val data = PieData(chartData)
        data.setValueTextSize(11f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)

        /*
        * Enable center text of chart with styling..
        * */
        viewDataBinding.chart.setDrawCenterText(true)
        viewDataBinding.chart.setCenterTextSize(25f)
        viewDataBinding.chart.setCenterTextColor(R.color.black)
        viewDataBinding.chart.centerText = list.size.toString()


        /*
        * Set entries to chart with some config changes..
        * */
        viewDataBinding.chart.invalidate()
        viewDataBinding.chart.setUsePercentValues(false)
        viewDataBinding.chart.description.isEnabled = false
        viewDataBinding.chart.data = data
    }

}