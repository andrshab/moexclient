package com.example.moexclient.di

import com.example.moexclient.MainActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [NetworkModule::class])
@Singleton
interface ApplicationComponent {
    fun inject(activity: MainActivity)
}