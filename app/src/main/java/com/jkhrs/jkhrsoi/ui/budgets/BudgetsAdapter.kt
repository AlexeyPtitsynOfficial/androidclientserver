package com.jkhrs.jkhrsoi.ui.budgets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jkhrs.jkhrsoi.data.Budget
import com.jkhrs.jkhrsoi.databinding.BudgetItemBinding
import com.jkhrs.jkhrsoi.ui.budgets.BudgetsAdapter.ViewHolder

class BudgetsAdapter(private val viewModel: BudgetsViewModel): ListAdapter<Budget, ViewHolder>(BudgetDiffCallBack()) {

    var onItemClick: ((Budget) -> Unit)? = null

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

    class ViewHolder private constructor(private val binding: BudgetItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(viewModel: BudgetsViewModel, item: Budget){
            binding.viewmodel = viewModel
            binding.budget = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) :ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = BudgetItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class BudgetDiffCallBack : DiffUtil.ItemCallback<Budget>() {
    override fun areItemsTheSame(oldItem: Budget, newItem: Budget): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Budget, newItem: Budget): Boolean {
        return oldItem == newItem
    }
}