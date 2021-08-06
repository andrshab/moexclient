package com.example.moexclient.api

import com.example.moexclient.data.News
import com.example.moexclient.data.NewsList
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface MoexService {
    @GET("iss/sitenews.json")
    suspend fun newsList(): NewsList

    @GET("iss/sitenews/{id}.json")
    suspend fun news(@Path(value = "id") newsId: Int): News

    companion object Factory {
        fun create(): MoexService {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiConstants.BASE_URL)
                .build()
                .create(MoexService::class.java)
        }
    }
}