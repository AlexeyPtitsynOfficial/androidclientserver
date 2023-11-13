package com.jkhrs.jkhrsoi.di

import androidx.lifecycle.ViewModel
import com.jkhrs.jkhrsoi.ui.cities.CitiesFragment
import com.jkhrs.jkhrsoi.ui.cities.CitiesViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class CitiesModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])

    internal abstract fun citiesFragment(): CitiesFragment

    @Binds
    @IntoMap
    @ViewModelKey(CitiesViewModel::class)
    abstract fun bindViewModel(viewmodel: CitiesViewModel): ViewModel
}