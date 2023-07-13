package com.android.adminportal.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.android.adminportal.data.model.Device
import com.android.adminportal.databinding.RowItemDeviceBinding

class DeviceListAdapter : RecyclerView.Adapter<DeviceListAdapter.DeviceViewHolder>(), Filterable {

    private val deviceListFiltered = MutableLiveData<MutableList<Device>>()
    private var deviceList = mutableListOf<Device>()
    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClickListener(device: Device?)
        fun onItemLongClickListener(device: Device?)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowItemDeviceBinding.inflate(inflater, parent, false)
        return DeviceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = deviceListFiltered.value?.get(position)
        /*
        * Item click
        * */
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it.onItemClickListener(device)
            }
        }

        /*
        * Item long click
        * */
        holder.itemView.setOnLongClickListener {
            onItemClickListener?.let {
                it.onItemLongClickListener(device)
            }
            true
        }
        holder.bind(device)
    }

    override fun getItemCount(): Int {
        return deviceListFiltered.value?.size ?: 0
    }

    inner class DeviceViewHolder(private val binding: RowItemDeviceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(device: Device?) {
            binding.device = device
            binding.executePendingBindings()
        }
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun setData(list: List<Device>) {
        this.deviceListFiltered.value = list.toMutableList()
        this.deviceList = list.toMutableList()
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val searchText = constraint?.toString()!!
                val filteredDevices = mutableListOf<Device>()

                if (searchText.isEmpty()) {
                    deviceList.let { filteredDevices.addAll(it) }
                } else {
                    deviceList.forEach {
                        if (it.employeeId.contains(constraint.toString(), true)||
                            it.deviceType.contains(constraint.toString(), true)||
                            it.deviceId.contains(constraint.toString(), true)) {
                            filteredDevices.add(it)
                        }
                    }
                }

                return FilterResults().apply { values = filteredDevices }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                deviceListFiltered.postValue(
                    if (results!!.values == null) ArrayList() else results.values as MutableList<Device>
                )
                notifyDataSetChanged()
            }

        }
    }
}
