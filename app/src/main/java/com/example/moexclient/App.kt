package com.example.moexclient

import android.app.Application
import com.example.moexclient.di.ApplicationComponent
import com.example.moexclient.di.DaggerApplicationComponent
import com.example.moexclient.di.SharedPreferencesModule

class App: Application() {
    lateinit var appComponent: ApplicationComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder()
            .sharedPreferencesModule(SharedPreferencesModule(this))
            .build()
    }
}