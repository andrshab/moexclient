package com.example.moexclient

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moexclient.adapters.NewsListAdapter
import com.example.moexclient.api.MoexService
import com.example.moexclient.data.NewsList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        moexService.newsList().enqueue(object: Callback<NewsList> {

            override fun onResponse(call: Call<NewsList>, response: Response<NewsList>) {
                val newsList = response.body() as NewsList
                adapter.setData(newsList.listMap)
            }

            override fun onFailure(call: Call<NewsList>, t: Throwable) {
                Log.d("NewsListFragment", t.toString())
            }
        })
    }
}