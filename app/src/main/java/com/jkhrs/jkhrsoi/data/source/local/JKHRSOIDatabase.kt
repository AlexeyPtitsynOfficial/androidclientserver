package com.jkhrs.jkhrsoi.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jkhrs.jkhrsoi.data.*
import com.jkhrs.jkhrsoi.data.source.local.Daos.*

@Database(entities = [Region::class,Budget::class,Organization::class,Municipal::class,City::class,Search::class], version = 1, exportSchema = false)
abstract class JKHRSOIDatabase : RoomDatabase() {
    abstract fun regionDao(): RegionsDao
    abstract fun budgetDao(): BudgetsDao
    abstract fun organizationDao(): OrganizationsDao
    abstract fun municipalDao(): MunicipalsDao
    abstract fun cityDao(): CitiesDao
    abstract fun searchDao(): SearchDao

}