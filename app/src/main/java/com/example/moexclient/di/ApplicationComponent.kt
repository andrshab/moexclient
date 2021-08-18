package com.example.moexclient.di

import android.content.SharedPreferences
import com.example.moexclient.fragments.RecordsListFragment
import com.example.moexclient.data.local.AppDatabase
import com.example.moexclient.fragments.ChartFragment
import com.example.moexclient.fragments.NewsFragment
import com.example.moexclient.fragments.NewsListFragment
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