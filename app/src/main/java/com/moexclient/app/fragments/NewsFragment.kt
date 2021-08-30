package com.moexclient.app.fragments

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.moexclient.app.App
import com.moexclient.app.R
import com.moexclient.app.api.ApiConstants
import com.moexclient.app.data.News
import com.moexclient.app.viewmodels.NewsViewModel
import com.moexclient.app.viewmodels.ViewModelFactory
import javax.inject.Inject


class NewsFragment : Fragment() {
    @Inject lateinit var viewModelFactory: ViewModelFactory
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
        viewModel = ViewModelProvider(this, viewModelFactory)[NewsViewModel::class.java]
        val root = inflater.inflate(R.layout.fragment_news, container, false)
        newsText = root.findViewById(R.id.news_text_tv)
        newsTitle = root.findViewById(R.id.news_title_tv)
        newsTime = root.findViewById(R.id.news_time_tv)
        val newsObserver = Observer<News> {
            newsTitle.text = it.map[ApiConstants.TITLE]
            newsTime.text = it.map[ApiConstants.PUBLISHED_AT]
            newsText.text = Html.fromHtml(it.map[ApiConstants.TEXT], Html.FROM_HTML_MODE_COMPACT)
        }
        viewModel.news.observe(viewLifecycleOwner, newsObserver)
        viewModel.loadNews(args.newsId)
        return root
    }
}
