package com.tawkto.jim.paging.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tawkto.jim.databinding.LayoutUserBinding
import com.tawkto.jim.model.User
import com.tawkto.jim.ui.MainViewModel

class UserAdapter(private val viewModel: MainViewModel) : PagingDataAdapter<User, UserAdapter.UserViewHolder>(COMPARATOR){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = LayoutUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position) ?: return

        holder.binding.apply {
            setUser(user)
            vm = viewModel
            index = position
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<User>() {
            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class UserViewHolder(val binding: LayoutUserBinding) : RecyclerView.ViewHolder(binding.root)
}