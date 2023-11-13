package com.jkhrs.jkhrsoi.ui.municipals

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jkhrs.jkhrsoi.CitiesActivity
import com.jkhrs.jkhrsoi.databinding.MunicipalFragmentBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class MunicipalsFragment : DaggerFragment() {

    companion object {
        fun newInstance() = MunicipalsFragment()
    }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MunicipalsViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: MunicipalFragmentBinding

    private lateinit var listAdapter: MunicipalsAdapter

    private var title : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = MunicipalFragmentBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        title = this.activity!!.intent.extras!!.getString("title").toString()
        val idRegion = this.activity!!.intent.extras!!.getInt("id_region")
        val idBudget = this.activity!!.intent.extras!!.getInt("id_budget")

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupListAdapter(idRegion, idBudget)

        viewDataBinding?.viewmodel?.dataLoading?.observe(this, Observer<Boolean> { isLoading ->
            viewDataBinding.swipeRefresh.isRefreshing = isLoading
        })

        viewDataBinding.swipeRefresh.setOnRefreshListener { viewModel.loadMunicipals(true, idRegion, idBudget) }

        viewDataBinding.swipeRefresh.post{ viewModel.loadMunicipals(true, idRegion, idBudget) }

        viewDataBinding.periodText.text = title
    }

    private fun setupListAdapter(idRegion:Int, idBudget: Int) {
        val viewModel = viewDataBinding.viewmodel

        if (viewModel != null) {
            listAdapter = MunicipalsAdapter(viewModel)
            viewDataBinding.municipalsList.adapter = listAdapter

            listAdapter.onItemClick = {municipal ->

            val intent = Intent(context, CitiesActivity::class.java)
                intent.putExtra("title", title)
                intent.putExtra("id_region", idRegion)
                intent.putExtra("id_budget", idBudget)
                intent.putExtra("id_municipal", municipal.id)
            startActivity(intent)
            }
        } else {
            //Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }
}
