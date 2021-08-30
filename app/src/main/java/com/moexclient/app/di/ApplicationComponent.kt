package com.moexclient.app.di

import android.content.SharedPreferences
import com.moexclient.app.fragments.RecordsListFragment
import com.moexclient.app.data.local.AppDatabase
import com.moexclient.app.fragments.ChartFragment
import com.moexclient.app.fragments.NewsFragment
import com.moexclient.app.fragments.NewsListFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [NetworkModule::class, SharedPreferencesModule::class, DataBaseModule::class])
@Singleton
interface ApplicationComponent {
    fun inject(fragment: NewsListFragment)
    fun inject(fragment: NewsFragment)
    fun inject(fragment: ChartFragment)
    fun inject(fragment: RecordsListFragment)
    fun sharedPreferences(): SharedPreferences
    fun dataBase(): AppDatabase
}