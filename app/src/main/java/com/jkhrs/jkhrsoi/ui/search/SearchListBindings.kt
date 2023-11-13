package com.jkhrs.jkhrsoi.ui.search

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jkhrs.jkhrsoi.data.Search

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Search>){
    (listView.adapter as SearchAdapter).submitList(items)
}