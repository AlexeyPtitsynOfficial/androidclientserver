package com.jkhrs.jkhrsoi.ui.municipals

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jkhrs.jkhrsoi.data.Municipal

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Municipal>){
    (listView.adapter as MunicipalsAdapter).submitList(items)
}