package com.example.moexclient.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.moexclient.R
import com.example.moexclient.data.local.Record

class RecordsListAdapter(private val recordsList: List<Record>, val context: Context): RecyclerView.Adapter<RecordsListAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val profitTv: TextView = view.findViewById(R.id.records_list_item_profit)
        val nameTv: TextView = view.findViewById(R.id.records_list_item_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_records, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val p = recordsList[position].profit ?: 0f

        if( p >= 0f) {
            holder.profitTv.text = "+$p%"
            holder.profitTv.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_light))
        } else {
            holder.profitTv.text = "-$p%"
            holder.profitTv.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_light))
        }
        holder.nameTv.text = recordsList[position].sec_name
    }

    override fun getItemCount(): Int {
        return recordsList.size
    }
}