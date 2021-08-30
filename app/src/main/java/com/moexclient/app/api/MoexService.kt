package com.moexclient.app.api

import com.moexclient.app.data.News
import com.moexclient.app.data.NewsList
import com.moexclient.app.data.HistoryList
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoexService {
    @GET("iss/sitenews.json")
    suspend fun newsList(@Query("start") startId: Int? = null): NewsList

    @GET("iss/sitenews/{id}.json")
    suspend fun news(@Path(value = "id") newsId: Int): News

    @GET("iss/history/engines/stock/markets/shares/securities.json")
    suspend fun topSecsHistoryList(
        @Query("sort_column") sortCol: String = "VALUE",
        @Query("sort_order") sortOrder: String = "desc"
    ): HistoryList

    @GET("iss/history/engines/stock/markets/shares/securities/{secid}.json")
    suspend fun secHistoryList(
        @Path(value = "secid") secId: String?,
        @Query("start") startId: Int?
    ): HistoryList

    @GET("iss/history/engines/stock/markets/shares/boards/{boardid}/securities/{secid}.json")
    suspend fun secOnBoardHistoryList(
        @Path(value = "secid") secId: String?,
        @Path(value = "boardid") boardId: String?,
        @Query("start") startId: Int?,
        @Query("sort_order") sortOrder: String?,
        @Query("from") from: String?
    ): HistoryList



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