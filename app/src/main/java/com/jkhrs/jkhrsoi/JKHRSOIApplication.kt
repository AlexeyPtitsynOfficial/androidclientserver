package com.jkhrs.jkhrsoi

import android.content.Context
import com.jkhrs.jkhrsoi.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import org.acra.ACRA
import org.acra.annotation.AcraMailSender
import org.acra.config.CoreConfigurationBuilder
import org.acra.config.MailSenderConfigurationBuilder
import org.acra.data.StringFormat

@AcraMailSender(
    mailTo = "ocnkp@jkhsakha.ru")
open class JKHRSOIApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.factory().create(applicationContext)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        ACRA.init(this)
    }
}