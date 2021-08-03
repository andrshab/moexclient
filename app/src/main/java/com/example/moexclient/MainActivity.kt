package com.example.moexclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.moexclient.api.MoexService
import com.example.moexclient.api.ApiConstants
import com.example.moexclient.model.News
import com.example.moexclient.model.NewsList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var moexService: MoexService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (applicationContext as App).appComponent.inject(this)
        //https://medium.com/mindorks/kotlin-mvp-dagger-2-retrofit-sample-android-application-e6fe3af7acd

        moexService.newsList().enqueue(object : Callback<NewsList> {

            override fun onResponse(call: Call<NewsList>, response: Response<NewsList>) {
                val newsList = (response.body() as NewsList)
                for (news in newsList.listMap) {
                    Log.d("MainAct", "id = " + news[ApiConstants.ID])
                }
            }

            override fun onFailure(call: Call<NewsList>, t: Throwable) {
                Log.d("MainAct", t.toString())
            }
        })

        moexService.news(35436).enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
//                Log.d("MainActNews", (response.body() as News).responseParts.data.isEmpty().toString())
                val news = (response.body() as News).map
                Log.d("MainActNews",  news?.get(ApiConstants.ID) + news?.get(ApiConstants.TEXT))
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.d("MainAct", t.toString())
            }
        })
    }
}