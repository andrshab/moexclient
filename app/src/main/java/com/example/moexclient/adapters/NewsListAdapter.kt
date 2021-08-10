package com.example.moexclient.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moexclient.R
import com.example.moexclient.data.NewsItem

class NewsListAdapter() :
    PagingDataAdapter<NewsItem,NewsListAdapter.ItemViewHolder>(COMPARATOR) {
    var onItemClick: ((Int) -> Unit)? = null

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTv: TextView = view.findViewById(R.id.list_item_title)
        val timeTv: TextView = view.findViewById(R.id.list_item_time)

        init {
            view.setOnClickListener {
                onItemClick?.invoke(getItem(adapterPosition)?.id ?: 0)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_news, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holderItem: ItemViewHolder, position: Int) {

        holderItem.titleTv.text = getItem(position)?.title
        holderItem.timeTv.text = getItem(position)?.id.toString()
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