package com.jkhrs.jkhrsoi.ui.cities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jkhrs.jkhrsoi.data.City
import com.jkhrs.jkhrsoi.databinding.CityItemBinding
import com.jkhrs.jkhrsoi.ui.cities.CitiesAdapter.ViewHolder

class CitiesAdapter(private val viewModel: CitiesViewModel): ListAdapter<City, ViewHolder>(CityDiffCallBack()) {

    var onItemClick: ((City) -> Unit)? = null

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

    class ViewHolder private constructor(private val binding: CityItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(viewModel: CitiesViewModel, item: City){
            binding.viewmodel = viewModel
            binding.city = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent:ViewGroup):ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CityItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }


}

class CityDiffCallBack : DiffUtil.ItemCallback<City>() {
    override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
        return oldItem == newItem
    }
}