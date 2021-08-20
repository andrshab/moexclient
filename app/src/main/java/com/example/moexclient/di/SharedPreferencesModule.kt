package com.example.moexclient.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedPreferencesModule(private val context: Context) {
    @Provides
    fun provideSharedPreferences(): SharedPreferences{
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}