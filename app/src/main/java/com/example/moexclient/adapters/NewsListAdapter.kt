package com.example.moexclient.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moexclient.R
import com.example.moexclient.api.ApiConstants

class NewsListAdapter() :
    RecyclerView.Adapter<NewsListAdapter.ItemViewHolder>() {
    var newsListMap: List<Map<String, String>> = mutableListOf()
    var onItemClick: ((Int) -> Unit)? = null

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTv: TextView = view.findViewById(R.id.list_item_title)
        val timeTv: TextView = view.findViewById(R.id.list_item_time)

        init {
            view.setOnClickListener {
                onItemClick?.invoke(newsListMap[adapterPosition][ApiConstants.ID]?.toInt() ?: 0)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_news, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holderItem: ItemViewHolder, position: Int) {
        holderItem.titleTv.text = newsListMap[position][ApiConstants.TITLE]
        holderItem.timeTv.text = newsListMap[position][ApiConstants.PUBLISHED_AT]
    }

    override fun getItemCount(): Int {
        return newsListMap.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(lm: List<Map<String, String>>) {
        newsListMap = lm
        notifyDataSetChanged()
    }

}