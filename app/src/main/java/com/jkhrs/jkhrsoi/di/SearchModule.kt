package com.jkhrs.jkhrsoi.di

import androidx.lifecycle.ViewModel
import com.jkhrs.jkhrsoi.ui.regions.RegionsFragment
import com.jkhrs.jkhrsoi.ui.regions.RegionsViewModel
import com.jkhrs.jkhrsoi.ui.search.SearchFragment
import com.jkhrs.jkhrsoi.ui.search.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class SearchModule {

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])

    internal abstract fun searchFragment(): SearchFragment

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindViewModel(viewmodel: SearchViewModel): ViewModel
}