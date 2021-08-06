package com.example.moexclient

import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.moexclient.api.ApiConstants
import com.example.moexclient.api.Exceptions
import com.example.moexclient.api.MoexService
import com.example.moexclient.data.News
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class NewsFragment : Fragment() {
    @Inject
    lateinit var moexService: MoexService
    private val args: NewsFragmentArgs by navArgs()
    lateinit var newsText: TextView
    lateinit var newsTitle: TextView
    lateinit var newsTime: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity?.applicationContext as App).appComponent.inject(this)
        val root = inflater.inflate(R.layout.fragment_news, container, false)
        newsText = root.findViewById(R.id.news_text_tv)
        newsTitle = root.findViewById(R.id.news_title_tv)
        newsTime = root.findViewById(R.id.news_time_tv)
        return root
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch(Exceptions.handler) {
                val news = moexService.news(args.newsId).map
                newsTitle.text = news[ApiConstants.TITLE]
                newsTime.text = news[ApiConstants.PUBLISHED_AT]
                newsText.text = Html.fromHtml(news[ApiConstants.TEXT], Html.FROM_HTML_MODE_COMPACT)
        }
    }
}
