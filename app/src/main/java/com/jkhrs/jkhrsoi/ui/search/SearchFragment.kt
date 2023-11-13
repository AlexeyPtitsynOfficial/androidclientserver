package com.jkhrs.jkhrsoi.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.jkhrs.jkhrsoi.BudgetsActivity
import com.jkhrs.jkhrsoi.R
import com.jkhrs.jkhrsoi.databinding.FragmentSearchBinding
import com.jkhrs.jkhrsoi.ui.regions.RegionsAdapter
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class SearchFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<SearchViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: FragmentSearchBinding

    private lateinit var listAdapter : SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentSearchBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Set the lifecycle owner to the lifecycle of the view
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupListAdapter()

        viewDataBinding?.viewmodel?.dataLoading?.observe(this, Observer<Boolean> { isLoading ->
            viewDataBinding.swipeRefresh.isRefreshing = isLoading
        })

        viewDataBinding.swipeRefresh.setOnRefreshListener {
            if(viewDataBinding.searchText.text.isNotEmpty()) {
                viewModel.loadSearchResult(true, viewDataBinding.searchText.text.toString())
                viewDataBinding.searchText.hideKeyboard()
            }
        }

        viewDataBinding.swipeRefresh.post{
            if(viewDataBinding.searchText.text.isNotEmpty()) {
                viewModel.loadSearchResult(true, viewDataBinding.searchText.text.toString())
                viewDataBinding.searchText.hideKeyboard()
            }
        }

        viewDataBinding.searchButton.setOnClickListener {

            if(viewDataBinding.searchText.text.isNotEmpty()) {
                viewModel.loadSearchResult(true, viewDataBinding.searchText.text.toString())
                viewDataBinding.searchText.hideKeyboard()
            }
        }
    }


    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            listAdapter = SearchAdapter(viewModel)
            viewDataBinding.searchList.adapter = listAdapter

            listAdapter.onItemClick = {region ->
                val intent = Intent(context, BudgetsActivity::class.java)
                intent.putExtra("id_region", region.id)
                startActivity(intent)
            }
        } else {
            //Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}