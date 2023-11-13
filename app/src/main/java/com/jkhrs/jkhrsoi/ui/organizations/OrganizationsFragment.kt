package com.jkhrs.jkhrsoi.ui.organizations

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jkhrs.jkhrsoi.databinding.OrganizationsFragmentBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class OrganizationsFragment : DaggerFragment() {

    companion object {
        fun newInstance() = OrganizationsFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<OrganizationsViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: OrganizationsFragmentBinding

    private lateinit var listAdapter: OrganizationsAdapter

    private var title : String = ""
    private var idRegion : Int = 0
    private var idBudget : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        title = this.activity!!.intent.extras!!.getString("title").toString()
        idRegion = this.activity!!.intent.extras!!.getInt("id_region")
        idBudget = this.activity!!.intent.extras!!.getInt("id_budget")

        viewDataBinding = OrganizationsFragmentBinding.inflate(inflater, container, false).apply {
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
        viewDataBinding.swipeRefresh.setOnRefreshListener { viewModel.loadOrganizations(true, idRegion, idBudget,"") }

        viewDataBinding.swipeRefresh.post{ viewModel.loadOrganizations(true, idRegion, idBudget, "") }

        viewDataBinding.periodText.text = title
        //viewDataBinding.searchButton.setOnClickListener{ viewModel.loadOrganizations(true, idRegion, idBudget,"") }
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            listAdapter = OrganizationsAdapter(viewModel)
            viewDataBinding.organizationsList.adapter = listAdapter

            /*listAdapter.onItemClick = {organization ->
                //val intent = Intent(context, BudgetsActivity::class.java)
                //intent.putExtra("id_budget", budget.id)
                //startActivity(intent)
            }*/
        } else {
            //Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }
}
