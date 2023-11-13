package com.jkhrs.jkhrsoi.di

import androidx.lifecycle.ViewModel
import com.jkhrs.jkhrsoi.ui.budgets.BudgetsFragment
import com.jkhrs.jkhrsoi.ui.budgets.BudgetsViewModel
import com.jkhrs.jkhrsoi.ui.organizations.OrganizationsFragment
import com.jkhrs.jkhrsoi.ui.organizations.OrganizationsViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class OrganizationsModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])

    internal abstract fun organizationsFragment(): OrganizationsFragment

    @Binds
    @IntoMap
    @ViewModelKey(OrganizationsViewModel::class)
    abstract fun bindViewModel(viewmodel: OrganizationsViewModel): ViewModel
}