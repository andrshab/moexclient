package com.example.moexclient.di

import com.example.moexclient.MainActivity
import com.example.moexclient.NewsFragment
import com.example.moexclient.NewsListFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [NetworkModule::class])
@Singleton
interface ApplicationComponent {
    fun inject(fragment: NewsListFragment)
    fun inject(fragment: NewsFragment)
}