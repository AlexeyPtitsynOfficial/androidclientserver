package com.jkhrs.jkhrsoi.ui.organizations

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jkhrs.jkhrsoi.data.Organization

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Organization>){
    (listView.adapter as OrganizationsAdapter).submitList(items)
}