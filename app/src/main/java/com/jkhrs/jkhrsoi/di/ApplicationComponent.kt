package com.jkhrs.jkhrsoi.di

import android.content.Context
import com.jkhrs.jkhrsoi.JKHRSOIApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        AndroidSupportInjectionModule::class,
        RegionsModule::class,
        BudgetsModule::class,
        OrganizationsModule::class,
        MunicipalsModule::class,
        CitiesModule::class,
        SearchModule::class
    ])
interface ApplicationComponent : AndroidInjector<JKHRSOIApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}