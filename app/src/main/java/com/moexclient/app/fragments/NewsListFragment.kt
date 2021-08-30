package com.moexclient.app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moexclient.app.App
import com.moexclient.app.R
import com.moexclient.app.adapters.NewsListAdapter
import com.moexclient.app.api.Exceptions
import com.moexclient.app.viewmodels.NewsListViewModel
import com.moexclient.app.viewmodels.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class NewsListFragment : Fragment() {
    @Inject lateinit var viewModelFactory: ViewModelFactory
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: NewsListViewModel
    private val adapter = NewsListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as App).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[NewsListViewModel::class.java]

        adapter.onItemClick = {id ->
            val direction = NewsListFragmentDirections.actionNewsListFragmentToNewsFragment(id)
            findNavController().navigate(direction)
        }

        lifecycleScope.launch(Exceptions.handler) {
            viewModel.searchNewsList().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_news_list, container, false)
        recyclerView = root.findViewById(R.id.news_list_rv)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        return root
    }
}