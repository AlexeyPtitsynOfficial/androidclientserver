package com.jkhrs.jkhrsoi.ui.cities

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jkhrs.jkhrsoi.R
import com.jkhrs.jkhrsoi.databinding.CitiesFragmentBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CitiesFragment : DaggerFragment() {

    companion object {
        fun newInstance() = CitiesFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<CitiesViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: CitiesFragmentBinding

    private lateinit var listAdapter: CitiesAdapter

    private var title : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = CitiesFragmentBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupListAdapter()

        viewDataBinding?.viewmodel?.dataLoading?.observe(this, Observer<Boolean> { isLoading ->
            viewDataBinding.swipeRefresh.isRefreshing = isLoading
        })

        title = this.activity!!.intent.extras!!.getString("title").toString()
        val idRegion = this.activity!!.intent.extras!!.getInt("id_region")
        val idBudget = this.activity!!.intent.extras!!.getInt("id_budget")
        val idMunicipal = this.activity!!.intent.extras!!.getInt("id_municipal")

        viewDataBinding.swipeRefresh.setOnRefreshListener { viewModel.loadCities(true, idRegion, idBudget, idMunicipal) }

        viewDataBinding.swipeRefresh.post{ viewModel.loadCities(true, idRegion, idBudget, idMunicipal) }

        viewDataBinding.periodText.text = title
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel

        if (viewModel != null) {
            listAdapter = CitiesAdapter(viewModel)
            viewDataBinding.citiesList.adapter = listAdapter

            listAdapter.onItemClick = {city ->


            }
        } else {
            //Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }
}
