package com.jkhrs.jkhrsoi.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jkhrs.jkhrsoi.data.Search
import com.jkhrs.jkhrsoi.databinding.SearchItemBinding
import com.jkhrs.jkhrsoi.ui.search.SearchAdapter.ViewHolder

class SearchAdapter(private val viewModel: SearchViewModel): androidx.recyclerview.widget.ListAdapter<Search, ViewHolder>(SearchDiffCallback()) {

    var onItemClick: ((Search) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item);
        }

        holder.bind(viewModel, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: SearchViewModel, item: Search) {

            binding.viewmodel = viewModel
            binding.search = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SearchItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class SearchDiffCallback : DiffUtil.ItemCallback<Search>() {
    override fun areItemsTheSame(oldItem: Search, newItem: Search): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Search, newItem: Search): Boolean {
        return oldItem == newItem
    }
}