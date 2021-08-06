package com.example.moexclient

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moexclient.adapters.NewsListAdapter
import com.example.moexclient.api.Exceptions
import com.example.moexclient.api.MoexService
import com.example.moexclient.data.NewsList
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject


class NewsListFragment : Fragment() {
    @Inject
    lateinit var moexService: MoexService
    lateinit var recyclerView: RecyclerView
    private val adapter = NewsListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity?.applicationContext as App).appComponent.inject(this)
        val root = inflater.inflate(R.layout.fragment_news_list, container, false)
        recyclerView = root.findViewById(R.id.news_list_rv)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter.onItemClick = {id ->
            val direction = NewsListFragmentDirections.actionNewsListFragmentToNewsFragment(id)
            findNavController().navigate(direction)
        }
        recyclerView.adapter = adapter

        return root
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch(Exceptions.handler) {
            val lm = moexService.newsList().listMap
            adapter.setData(lm)
        }
    }
}