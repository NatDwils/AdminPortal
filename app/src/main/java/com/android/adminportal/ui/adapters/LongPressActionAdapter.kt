package com.android.adminportal.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.adminportal.R
import com.android.adminportal.databinding.RowActionItemBinding
import com.android.adminportal.utils.ktx.setVisibleGone
import com.android.adminportal.utils.ktx.titleCase
import javax.inject.Inject

class LongPressActionAdapter @Inject constructor() :
    RecyclerView.Adapter<LongPressActionAdapter.ViewHolder>() {
    private var action: List<String> = listOf()
    lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: RowActionItemBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.row_action_item, parent, false
        )
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return action.size
    }

    class ViewHolder(var binding: RowActionItemBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setView(position, holder.binding)
    }

    private fun setView(position: Int, binding: RowActionItemBinding) {
        val item = action[position]
        binding.tvName.text = item.titleCase()
        binding.root.setOnClickListener {
            listener.onItemClick(null, binding.rootView, position, (binding.rootView.id).toLong())
        }
        binding.divider.setVisibleGone(position != action.size - 1)
    }

    fun setData(action: List<String>) {
        this.action = action
        notifyItemRangeChanged(0, this.action.size - 1)
    }

    fun setItemListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}