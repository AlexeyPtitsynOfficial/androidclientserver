package com.jkhrs.jkhrsoi.ui.regions

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.jkhrs.jkhrsoi.BudgetsActivity
import com.jkhrs.jkhrsoi.databinding.FragmentRegionsBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class RegionsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<RegionsViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: FragmentRegionsBinding

    private lateinit var listAdapter : RegionsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentRegionsBinding.inflate(inflater, container, false).apply {
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

        viewDataBinding.swipeRefresh.setOnRefreshListener { viewModel.loadRegions(true) }

        viewDataBinding.swipeRefresh.post{ viewModel.loadRegions(true) }
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            listAdapter = RegionsAdapter(viewModel)
            viewDataBinding.regionsList.adapter = listAdapter

            listAdapter.onItemClick = {region ->
                val intent = Intent(context, BudgetsActivity::class.java)
                intent.putExtra("id_region", region.id)
                startActivity(intent)
            }
        } else {
            //Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }
}