package com.moexclient.app.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides

@Module
class SharedPreferencesModule(private val context: Context) {
    @Provides
    fun provideSharedPreferences(): SharedPreferences{
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}