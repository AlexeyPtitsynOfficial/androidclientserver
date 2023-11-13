package com.jkhrs.jkhrsoi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jkhrs.jkhrsoi.ui.organizations.OrganizationsFragment

class OrganizationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.organizations_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, OrganizationsFragment.newInstance())
                .commitNow()
        }
    }

}
