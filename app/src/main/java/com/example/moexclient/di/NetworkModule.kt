package com.example.moexclient.di

import com.example.moexclient.api.ApiConstants
import com.example.moexclient.api.MoexService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofitService(): MoexService {
        return MoexService.create()
    }
}