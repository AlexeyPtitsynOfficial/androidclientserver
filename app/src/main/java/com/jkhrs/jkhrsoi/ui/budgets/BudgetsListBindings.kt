package com.jkhrs.jkhrsoi.ui.budgets

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jkhrs.jkhrsoi.data.Budget

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Budget>){
    (listView.adapter as BudgetsAdapter).submitList(items)
}