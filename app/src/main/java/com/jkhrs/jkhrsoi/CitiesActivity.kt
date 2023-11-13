package com.jkhrs.jkhrsoi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jkhrs.jkhrsoi.ui.cities.CitiesFragment

class CitiesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cities_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CitiesFragment.newInstance())
                .commitNow()
        }
    }

}
