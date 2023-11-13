package com.jkhrs.jkhrsoi.di

import androidx.lifecycle.ViewModel
import com.jkhrs.jkhrsoi.ui.budgets.BudgetsFragment
import com.jkhrs.jkhrsoi.ui.budgets.BudgetsViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class BudgetsModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])

    internal abstract fun budgetsFragment(): BudgetsFragment

    @Binds
    @IntoMap
    @ViewModelKey(BudgetsViewModel::class)
    abstract fun bindViewModel(viewmodel: BudgetsViewModel): ViewModel
}