package com.example.moexclient

import android.app.Application

class App(val appComponent: ApplicationComponent = DaggerApplicationComponent.create()): Application() {

}