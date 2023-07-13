package com.android.adminportal.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.android.adminportal.R
import com.android.adminportal.databinding.DialogRecyclerBinding
import com.android.adminportal.ui.adapters.LongPressActionAdapter
import com.android.adminportal.utils.ktx.gone


class LongClickActionsDialog(private val actions: List<String>) : DialogFragment() {

    private lateinit var binding: DialogRecyclerBinding
    private var onItemClickListener: OnItemClickListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.GeneralAlertDialog)
    }


    fun display(fragmentManager: FragmentManager): LongClickActionsDialog {
        val dialog = LongClickActionsDialog(actions)
        dialog.show(fragmentManager, "")
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.dialog_recycler, container, false
        )
        return binding.root

    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.WRAP_CONTENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = LongPressActionAdapter()
        adapter.setData(mutableListOf("Delete"))
        binding.rvItems.adapter = adapter
        binding.tvActionOne.gone()
        binding.tvActionTwo.setOnClickListener { dismiss() }
        adapter.setItemListener { parent, view, position, id ->
            onItemClickListener?.let {
                onItemClickListener!!.onItemClick(null, view, position, view.id.toLong())
            }
            dismiss()
        }
    }

}