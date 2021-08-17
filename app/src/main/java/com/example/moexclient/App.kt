package com.example.moexclient

import android.app.Application
import com.example.moexclient.di.AppModule
import com.example.moexclient.di.ApplicationComponent
import com.example.moexclient.di.DaggerApplicationComponent

class App: Application() {
    lateinit var appComponent: ApplicationComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder().appModule(AppModule(this)).build()
    }
}