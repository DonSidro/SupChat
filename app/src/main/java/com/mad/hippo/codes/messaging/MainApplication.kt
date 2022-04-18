package com.mad.hippo.codes.messaging

import android.app.Application
import com.mad.hippo.codes.messaging.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(firebaseModule)
            modules(repositoryModule)
            modules(useCaseModule)
            modules(profileViewModelModule)
            modules(authViewModelModule)
            modules(mainViewModelModule)
            modules(conversationsViewModelModule)
        }
    }

}