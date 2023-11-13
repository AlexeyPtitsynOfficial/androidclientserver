package com.jkhrs.jkhrsoi.ui.organizations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jkhrs.jkhrsoi.data.Organization
import com.jkhrs.jkhrsoi.databinding.OrganizationItemBinding
import com.jkhrs.jkhrsoi.ui.organizations.OrganizationsAdapter.ViewHolder

class OrganizationsAdapter(private val viewModel: OrganizationsViewModel) : ListAdapter<Organization, ViewHolder>(OrganizationDiffCallback()) {

    var onItemClick: ((Organization) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item);
        }

        holder.bind(viewModel, item)
    }
    class ViewHolder private constructor(private val binding: OrganizationItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: OrganizationsViewModel, item: Organization){
            binding.viewmodel = viewModel
            binding.organization = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = OrganizationItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class OrganizationDiffCallback : DiffUtil.ItemCallback<Organization>() {
    override fun areItemsTheSame(oldItem: Organization, newItem: Organization): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Organization, newItem: Organization): Boolean {
        return oldItem == newItem
    }
}