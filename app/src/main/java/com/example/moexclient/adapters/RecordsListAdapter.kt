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
import kotlin.math.abs

class RecordsListAdapter(private val recordsList: List<Record>): RecyclerView.Adapter<RecordsListAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val profitTv: TextView = view.findViewById(R.id.records_list_item_profit)
        val nameTv: TextView = view.findViewById(R.id.records_list_item_name)
        val green  = ContextCompat.getColor(view.context, android.R.color.holo_green_light)
        val red = ContextCompat.getColor(view.context, android.R.color.holo_red_light)
        val posStr = view.context.getString(R.string.pos_profit)
        val negStr = view.context.getString(R.string.neg_profit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_records, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val p = recordsList[position].profit ?: 0f

        if( p >= 0f) {
            holder.profitTv.text = String.format(holder.posStr, abs(p))
            holder.profitTv.setTextColor(holder.green)
        } else {
            holder.profitTv.text = String.format(holder.negStr, abs(p))
            holder.profitTv.setTextColor(holder.red)
        }
        holder.nameTv.text = recordsList[position].sec_name
    }

    override fun getItemCount(): Int {
        return recordsList.size
    }
}