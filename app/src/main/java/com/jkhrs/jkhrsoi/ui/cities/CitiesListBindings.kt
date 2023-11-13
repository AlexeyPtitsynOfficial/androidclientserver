package com.jkhrs.jkhrsoi.ui.cities

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jkhrs.jkhrsoi.data.City

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<City>){
    (listView.adapter as CitiesAdapter).submitList(items)
}