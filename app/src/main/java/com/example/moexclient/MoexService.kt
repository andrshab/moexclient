package com.example.moexclient

import com.example.moexclient.model.News
import com.example.moexclient.model.NewsList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MoexService {
    @GET("iss/sitenews.json")
    fun siteNews(): Call<NewsList>
    @GET("iss/sitenews/{id}.json")
    fun newsWithId(@Path(value = "id") newsId: Int): Call<News>
}