package com.example.moexclient

import android.app.Application
import com.example.moexclient.di.ApplicationComponent
import com.example.moexclient.di.DaggerApplicationComponent

class App(val appComponent: ApplicationComponent = DaggerApplicationComponent.create()): Application()