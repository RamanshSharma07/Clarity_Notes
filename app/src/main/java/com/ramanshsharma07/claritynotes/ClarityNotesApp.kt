package com.ramanshsharma07.claritynotes

import android.app.Application
import com.ramanshsharma07.claritynotes.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ClarityNotesApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@ClarityNotesApp)
            modules(appModule)
        }
    }

}