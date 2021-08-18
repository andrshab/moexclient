package com.example.moexclient.fragments

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.moexclient.App
import com.example.moexclient.R
import com.example.moexclient.api.ApiConstants
import com.example.moexclient.api.Exceptions
import com.example.moexclient.viewmodels.NewsViewModel
import com.example.moexclient.viewmodels.NewsViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject


class NewsFragment : Fragment() {
    @Inject lateinit var viewModelFactory: NewsViewModelFactory
    lateinit var viewModel: NewsViewModel
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
        viewModel = ViewModelProvider(this, viewModelFactory)[NewsViewModel::class.java]
        return root
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch(Exceptions.handler) {
                val news = viewModel.searchNews(args.newsId).map
                newsTitle.text = news[ApiConstants.TITLE]
                newsTime.text = news[ApiConstants.PUBLISHED_AT]
                newsText.text = Html.fromHtml(news[ApiConstants.TEXT], Html.FROM_HTML_MODE_COMPACT)
        }
    }
}
