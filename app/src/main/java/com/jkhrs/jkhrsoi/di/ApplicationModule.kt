package com.jkhrs.jkhrsoi.di

import android.content.Context
import androidx.room.Room
import com.jkhrs.jkhrsoi.data.source.datasources.*
import com.jkhrs.jkhrsoi.data.source.local.JKHRSOIDatabase
import com.jkhrs.jkhrsoi.data.source.local.datasources.MunicipalsLocalDataSource
import com.jkhrs.jkhrsoi.data.source.local.datasources.RegionsLocalDataSource
import com.jkhrs.jkhrsoi.data.source.local.datasources.BudgetsLocalDataSource
import com.jkhrs.jkhrsoi.data.source.local.datasources.OrganizationsLocalDataSource
import com.jkhrs.jkhrsoi.data.source.local.datasources.CitiesLocalDataSource
import com.jkhrs.jkhrsoi.data.source.local.datasources.SearchLocalDataSource

import com.jkhrs.jkhrsoi.data.source.remote.datasources.MunicipalsRemoteDataSource
import com.jkhrs.jkhrsoi.data.source.remote.datasources.BudgetsRemoteDataSource
import com.jkhrs.jkhrsoi.data.source.remote.datasources.RegionsRemoteDataSource
import com.jkhrs.jkhrsoi.data.source.remote.datasources.OrganizationsRemoteDataSource
import com.jkhrs.jkhrsoi.data.source.remote.datasources.CitiesRemoteDataSource
import com.jkhrs.jkhrsoi.data.source.remote.datasources.SearchRemoteDataSource
import com.jkhrs.jkhrsoi.data.source.repositories.*
import com.jkhrs.jkhrsoi.data.source.repositories.interfaces.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.annotation.AnnotationRetention.RUNTIME

@Module(includes = [ApplicationModuleBinds::class])
object ApplicationModule {
    @Qualifier
    @Retention(RUNTIME)
    annotation class RegionsRemoteDataSource

    @Qualifier
    @Retention(RUNTIME)
    annotation class RegionsLocalDataSource

    @Qualifier
    @Retention(RUNTIME)
    annotation class BudgetsRemoteDataSource

    @Qualifier
    @Retention(RUNTIME)
    annotation class BudgetsLocalDataSource

    @Qualifier
    @Retention(RUNTIME)
    annotation class OrganizationsRemoteDataSource

    @Qualifier
    @Retention(RUNTIME)
    annotation class OrganizationsLocalDataSource

    @Qualifier
    @Retention(RUNTIME)
    annotation class MunicipalsRemoteDataSource

    @Qualifier
    @Retention(RUNTIME)
    annotation class MunicipalsLocalDataSource

    @Qualifier
    @Retention(RUNTIME)
    annotation class CitiesRemoteDataSource

    @Qualifier
    @Retention(RUNTIME)
    annotation class CitiesLocalDataSource

    @Qualifier
    @Retention(RUNTIME)
    annotation class SearchRemoteDataSource

    @Qualifier
    @Retention(RUNTIME)
    annotation class SearchLocalDataSource

    @JvmStatic
    @Singleton
    @RegionsRemoteDataSource
    @Provides
    fun provideRegionsRemoteDataSource(): RegionsDataSource {
        return RegionsRemoteDataSource
    }

    @JvmStatic
    @Singleton
    @RegionsLocalDataSource
    @Provides
    fun provideRegionsLocalDataSource(
        database: JKHRSOIDatabase,
        ioDispatcher: CoroutineDispatcher
    ): RegionsDataSource {
        return RegionsLocalDataSource(
            database.regionDao(), ioDispatcher
        )
    }

    @JvmStatic
    @Singleton
    @BudgetsRemoteDataSource
    @Provides
    fun provideBudgetsRemoteDataSource(): BudgetsDataSource {
        return BudgetsRemoteDataSource
    }

    @JvmStatic
    @Singleton
    @BudgetsLocalDataSource
    @Provides
    fun provideBudgetsLocalDataSource(
        database: JKHRSOIDatabase,
        ioDispatcher: CoroutineDispatcher
    ): BudgetsDataSource {
        return BudgetsLocalDataSource(
            database.budgetDao(), ioDispatcher
        )
    }

    @JvmStatic
    @Singleton
    @OrganizationsRemoteDataSource
    @Provides
    fun provideOrganizationsRemoteDataSource(): OrganizationsDataSource {
        return OrganizationsRemoteDataSource
    }

    @JvmStatic
    @Singleton
    @OrganizationsLocalDataSource
    @Provides
    fun provideOrganizationsLocalDataSource(
        database: JKHRSOIDatabase,
        ioDispatcher: CoroutineDispatcher
    ): OrganizationsDataSource {
        return OrganizationsLocalDataSource(
            database.organizationDao(), ioDispatcher
        )
    }

    @JvmStatic
    @Singleton
    @MunicipalsRemoteDataSource
    @Provides
    fun provideMunicipalsRemoteDataSource(): MunicipalsDataSource {
        return MunicipalsRemoteDataSource
    }

    @JvmStatic
    @Singleton
    @MunicipalsLocalDataSource
    @Provides
    fun provideMunicipalsLocalDataSource(
        database: JKHRSOIDatabase,
        ioDispatcher: CoroutineDispatcher
    ): MunicipalsDataSource {
        return MunicipalsLocalDataSource(
            database.municipalDao(), ioDispatcher
        )
    }

    @JvmStatic
    @Singleton
    @CitiesRemoteDataSource
    @Provides
    fun provideCitiesRemoteDataSource(): CitiesDataSource {
        return CitiesRemoteDataSource
    }

    @JvmStatic
    @Singleton
    @CitiesLocalDataSource
    @Provides
    fun provideCitiesLocalDataSource(
        database: JKHRSOIDatabase,
        ioDispatcher: CoroutineDispatcher
    ): CitiesDataSource {
        return CitiesLocalDataSource(
            database.cityDao(), ioDispatcher
        )
    }

    @JvmStatic
    @Singleton
    @SearchRemoteDataSource
    @Provides
    fun provideSearchRemoteDataSource(): SearchDataSource {
        return SearchRemoteDataSource
    }

    @JvmStatic
    @Singleton
    @SearchLocalDataSource
    @Provides
    fun provideSearchLocalDataSource(
        database: JKHRSOIDatabase,
        ioDispatcher: CoroutineDispatcher
    ): SearchDataSource {
        return SearchLocalDataSource(
            database.searchDao(), ioDispatcher
        )
    }


    @JvmStatic
    @Singleton
    @Provides
    fun provideDataBase(context: Context): JKHRSOIDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            JKHRSOIDatabase::class.java,
            "JKHRSOI.db"
        ).build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}

@Module
abstract class ApplicationModuleBinds {

    @Singleton
    @Binds
    abstract fun bindRegionsRepository(repo: DefaultRegionsRepository): RegionsRepository

    @Singleton
    @Binds
    abstract fun bindBudgetsRepository(repo: DefaultBudgetsRepository): BudgetsRepository

    @Singleton
    @Binds
    abstract fun bindOrganizationsRepository(repo: DefaultOrganizationsRepository): OrganizationsRepository

    @Singleton
    @Binds
    abstract fun bindMunicipalsRepository(repo: DefaultMunicipalsRepository): MunicipalsRepository

    @Singleton
    @Binds
    abstract fun bindCitiesRepository(repo: DefaultCitiesRepository): CitiesRepository

    @Singleton
    @Binds
    abstract fun bindSearchRepository(repo: DefaultSearchRepository): SearchRepository
}