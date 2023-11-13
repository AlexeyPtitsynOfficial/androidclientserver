package com.jkhrs.jkhrsoi.ui.regions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jkhrs.jkhrsoi.data.Region
import com.jkhrs.jkhrsoi.databinding.RegionItemBinding
import com.jkhrs.jkhrsoi.ui.regions.RegionsAdapter.ViewHolder

class RegionsAdapter(private val viewModel: RegionsViewModel): ListAdapter<Region, ViewHolder>(TaskDiffCallback()) {

    var onItemClick: ((Region) -> Unit)? = null

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

    class ViewHolder private constructor(private val binding: RegionItemBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bind(viewModel: RegionsViewModel, item: Region) {

                binding.viewmodel = viewModel
                binding.region = item
                binding.executePendingBindings()
            }

            companion object {
                fun from(parent: ViewGroup): ViewHolder {
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val binding = RegionItemBinding.inflate(layoutInflater, parent, false)

                    return ViewHolder(binding)
                }
            }
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<Region>() {
    override fun areItemsTheSame(oldItem: Region, newItem: Region): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Region, newItem: Region): Boolean {
        return oldItem == newItem
    }
}