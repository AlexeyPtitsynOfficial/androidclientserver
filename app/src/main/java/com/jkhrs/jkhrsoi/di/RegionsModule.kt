package com.jkhrs.jkhrsoi.di

import androidx.lifecycle.ViewModel
import com.jkhrs.jkhrsoi.ui.regions.RegionsFragment
import com.jkhrs.jkhrsoi.ui.regions.RegionsViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class RegionsModule {

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])

    internal abstract fun regionsFragment(): RegionsFragment

    @Binds
    @IntoMap
    @ViewModelKey(RegionsViewModel::class)
    abstract fun bindViewModel(viewmodel: RegionsViewModel): ViewModel
}