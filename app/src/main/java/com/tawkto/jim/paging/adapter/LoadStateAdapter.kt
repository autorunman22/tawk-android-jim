package com.tawkto.jim.paging.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tawkto.jim.databinding.LayoutUserLoadStateBinding
import timber.log.Timber

class LoadStateViewHolder(val binding: LayoutUserLoadStateBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(loadState: LoadState) {
        Timber.d("loadstate: $loadState")

        with(binding) {
            btnRetry.isVisible = true
            tvError.isVisible = true
            loader.isVisible = true
        }
    }
}

class UserLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadStateViewHolder>() {

    init {
        Timber.d("are we even getting called?")
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        Timber.d("are we on binding")
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        Timber.d("are we creating")
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutUserLoadStateBinding.inflate(layoutInflater, parent, false)

        return LoadStateViewHolder(binding).also {
            binding.btnRetry.setOnClickListener { retry() }
        }
    }


}