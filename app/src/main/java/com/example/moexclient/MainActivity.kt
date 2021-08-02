package com.example.moexclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        moexService.siteNews().enqueue(object : Callback<NewsList> {

            override fun onResponse(call: Call<NewsList>, response: Response<NewsList>) {
                val rp = (response.body() as NewsList).responseParts
                for (item in rp.data) {
//                    Log.d("MainAct", item[rp.col("id")]
//                            + item[rp.col("published_at")]
//                            + item[rp.col("title")])
                }
            }

            override fun onFailure(call: Call<NewsList>, t: Throwable) {
                Log.d("MainAct", t.toString())
            }
        })

        moexService.newsWithId(35436).enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                val news = (response.body() as News)
                Log.d("MainActNews",  news.title + news.publishedAt + news.text)
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.d("MainAct", t.toString())
            }
        })
    }
}