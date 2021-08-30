package com.moexclient.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.moexclient.app.R
import com.moexclient.app.data.NewsItem

class NewsListAdapter() :
    PagingDataAdapter<NewsItem, NewsListAdapter.ItemViewHolder>(COMPARATOR) {
    var onItemClick: ((Int) -> Unit)? = null

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTv: TextView = view.findViewById(R.id.list_item_title)
        val timeTv: TextView = view.findViewById(R.id.list_item_time)

        init {
            view.setOnClickListener {
                onItemClick?.invoke(getItem(absoluteAdapterPosition)?.id ?: 0)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListAdapter.ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_news, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holderItem: NewsListAdapter.ItemViewHolder, position: Int) {

        holderItem.titleTv.text = getItem(position)?.title
        holderItem.timeTv.text = getItem(position)?.time.toString()
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<NewsItem>() {
            override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean =
                oldItem == newItem
        }
    }

}