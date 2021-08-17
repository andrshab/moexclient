package com.example.moexclient.di

import android.app.Application
import android.content.Context
import com.example.moexclient.*
import dagger.Component
import javax.inject.Singleton

@Component(modules = [NetworkModule::class, AppModule::class])
@Singleton
interface ApplicationComponent {
    fun inject(fragment: NewsListFragment)
    fun inject(fragment: NewsFragment)
    fun inject(fragment: ChartFragment)
    fun context(): Context
}