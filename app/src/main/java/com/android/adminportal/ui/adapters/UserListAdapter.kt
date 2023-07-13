package com.android.adminportal.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.android.adminportal.data.model.User
import com.android.adminportal.databinding.RowItemUserBinding

class UserListAdapter : RecyclerView.Adapter<UserListAdapter.UserViewHolder>(), Filterable {

    private val userListFiltered = MutableLiveData<MutableList<User>>()
    private var userList = mutableListOf<User>()
    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClickListener(user: User?)
        fun onItemLongClickListener(user: User?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowItemUserBinding.inflate(inflater, parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userListFiltered.value?.get(position)
        /*
        * Item click
        * */
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it.onItemClickListener(user)
            }
        }

        /*
        * Item long click
        * */
        holder.itemView.setOnLongClickListener {
            onItemClickListener?.let {
                it.onItemLongClickListener(user)
            }
            true
        }
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return userListFiltered.value?.size ?: 0
    }

    inner class UserViewHolder(private val binding: RowItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User?) {
            binding.user = user
            binding.executePendingBindings()
        }
    }

    fun setData(list: List<User>) {
        this.userListFiltered.value = list.toMutableList()
        this.userList = list.toMutableList()
        notifyDataSetChanged()
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val searchText = constraint?.toString()!!
                val filteredUsers = mutableListOf<User?>()

                if (searchText.isEmpty()) {
                    userList.let { filteredUsers.addAll(it) }
                } else {
                    userList.forEach {
                        if (it.name.contains(constraint.toString(), true)||
                            it.employeeId.contains(constraint.toString(), true)) {
                            filteredUsers.add(it)
                        }
                    }
                }

                return FilterResults().apply { values = filteredUsers }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                userListFiltered.postValue(
                    if (results!!.values == null) ArrayList() else results.values as MutableList<User>
                )
                notifyDataSetChanged()
            }

        }
    }

}
