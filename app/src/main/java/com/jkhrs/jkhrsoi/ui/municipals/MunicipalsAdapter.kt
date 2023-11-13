package com.jkhrs.jkhrsoi.ui.municipals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jkhrs.jkhrsoi.data.Municipal
import com.jkhrs.jkhrsoi.databinding.MunicipalItemBinding
import com.jkhrs.jkhrsoi.ui.municipals.MunicipalsAdapter.ViewHolder

class MunicipalsAdapter(private val viewModel: MunicipalsViewModel) : androidx.recyclerview.widget.ListAdapter<Municipal, ViewHolder>(MunicipalDIffCallback()){

    var onItemClick: ((Municipal) -> Unit)? = null

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

    class ViewHolder private constructor(private val binding: MunicipalItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(viewModel: MunicipalsViewModel, item: Municipal){
            binding.viewmodel = viewModel
            binding.municipal = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MunicipalItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class MunicipalDIffCallback : DiffUtil.ItemCallback<Municipal>() {
    override fun areItemsTheSame(oldItem: Municipal, newItem: Municipal): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Municipal, newItem: Municipal): Boolean {
        return oldItem == newItem
    }

}