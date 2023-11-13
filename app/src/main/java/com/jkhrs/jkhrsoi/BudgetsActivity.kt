package com.jkhrs.jkhrsoi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jkhrs.jkhrsoi.ui.budgets.BudgetsFragment

class BudgetsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.budgets_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, BudgetsFragment.newInstance())
                .commitNow()
        }
    }

}
