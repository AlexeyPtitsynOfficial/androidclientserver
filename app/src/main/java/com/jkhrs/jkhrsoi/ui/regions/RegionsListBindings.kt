package com.jkhrs.jkhrsoi.ui.regions

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jkhrs.jkhrsoi.data.Region

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Region>){
    (listView.adapter as RegionsAdapter).submitList(items)
}