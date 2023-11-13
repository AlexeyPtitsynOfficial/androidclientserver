package com.jkhrs.jkhrsoi.ui.budgets

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jkhrs.jkhrsoi.MunicipalsActivity
import com.jkhrs.jkhrsoi.OrganizationsActivity
import com.jkhrs.jkhrsoi.databinding.BudgetsFragmentBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class BudgetsFragment : DaggerFragment() {

    companion object {
        fun newInstance() = BudgetsFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<BudgetsViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: BudgetsFragmentBinding

    private lateinit var listAdapter: BudgetsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        viewDataBinding = BudgetsFragmentBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val idRegion = this.activity!!.intent.extras!!.getInt("id_region")

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupListAdapter(idRegion)

        viewDataBinding?.viewmodel?.dataLoading?.observe(this, Observer<Boolean> { isLoading ->
            viewDataBinding.swipeRefresh.isRefreshing = isLoading
        })



        viewDataBinding.swipeRefresh.setOnRefreshListener { viewModel.loadBudgets(true, idRegion) }

        viewDataBinding.swipeRefresh.post{ viewModel.loadBudgets(true, idRegion) }
    }

    private fun setupListAdapter(idRegion: Int) {
        val viewModel = viewDataBinding.viewmodel

        if (viewModel != null) {
            listAdapter = BudgetsAdapter(viewModel)
            viewDataBinding.budgetsList.adapter = listAdapter

            listAdapter.onItemClick = {budget ->

                if(budget.id ==5 || budget.id == 6) {
                    val intent = Intent(context, MunicipalsActivity::class.java)
                    intent.putExtra("title", budget.getDateText)
                    intent.putExtra("id_region", idRegion)
                    intent.putExtra("id_budget", budget.id)
                    startActivity(intent)
                } else {
                    val intent = Intent(context, OrganizationsActivity::class.java)
                    intent.putExtra("title", budget.getDateText)
                    intent.putExtra("id_region", idRegion)
                    intent.putExtra("id_budget", budget.id)
                    startActivity(intent)
                }
            }
        } else {
            //Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }
}
