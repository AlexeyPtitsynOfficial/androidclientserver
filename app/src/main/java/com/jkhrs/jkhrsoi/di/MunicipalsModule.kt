package com.jkhrs.jkhrsoi.di

import androidx.lifecycle.ViewModel
import com.jkhrs.jkhrsoi.ui.municipals.MunicipalsFragment
import com.jkhrs.jkhrsoi.ui.municipals.MunicipalsViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class MunicipalsModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])

    internal abstract fun municipalsFragment(): MunicipalsFragment

    @Binds
    @IntoMap
    @ViewModelKey(MunicipalsViewModel::class)
    abstract fun bindViewModel(viewmodel: MunicipalsViewModel): ViewModel
}