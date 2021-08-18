package com.example.moexclient.di

import android.content.SharedPreferences
import com.example.moexclient.fragments.ChartFragment
import com.example.moexclient.fragments.NewsFragment
import com.example.moexclient.fragments.NewsListFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [NetworkModule::class, SharedPreferencesModule::class])
@Singleton
interface ApplicationComponent {
    fun inject(fragment: NewsListFragment)
    fun inject(fragment: NewsFragment)
    fun inject(fragment: ChartFragment)
    fun sharedPreferences(): SharedPreferences
}