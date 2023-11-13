package com.jkhrs.jkhrsoi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jkhrs.jkhrsoi.ui.municipals.MunicipalsFragment

class MunicipalsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.municipal_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container,
                    MunicipalsFragment.newInstance()
                )
                .commitNow()
        }
    }

}
