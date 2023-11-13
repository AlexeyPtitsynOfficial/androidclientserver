package com.jkhrs.jkhrsoi.data.source.local.Daos

import androidx.room.Dao
import androidx.room.Query
import com.jkhrs.jkhrsoi.data.Budget

@Dao
interface BudgetsDao {
    @Query("SELECT * FROM budgets")
    suspend fun getBudgets(): List<Budget>
}