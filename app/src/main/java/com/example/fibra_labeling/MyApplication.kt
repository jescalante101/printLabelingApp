package com.example.fibra_labeling

import android.app.Application
import com.example.fibra_labeling.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {

            androidContext(this@MyApplication)
           modules(appModules)
        }
    }
}