package com.moexclient.app.di

import com.moexclient.app.api.MoexService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofitService(): MoexService {
        return MoexService.create()
    }
}