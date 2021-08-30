package com.moexclient.app

import android.app.Application
import com.moexclient.app.di.ApplicationComponent
import com.moexclient.app.di.DaggerApplicationComponent
import com.moexclient.app.di.DataBaseModule
import com.moexclient.app.di.SharedPreferencesModule

class App: Application() {
    lateinit var appComponent: ApplicationComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder()
            .sharedPreferencesModule(SharedPreferencesModule(this))
            .dataBaseModule(DataBaseModule(this))
            .build()
    }
}