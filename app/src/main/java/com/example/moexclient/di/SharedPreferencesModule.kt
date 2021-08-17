package com.example.moexclient.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

@Module
class SharedPreferencesModule {
    @Provides
    fun provideSharedPreferences(application: Application): SharedPreferences{
        return application.getSharedPreferences("settings", Context.MODE_PRIVATE)
    }
}