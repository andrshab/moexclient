package com.moexclient.app.fragments

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moexclient.app.App
import com.moexclient.app.R
import com.moexclient.app.adapters.RecordsListAdapter
import com.moexclient.app.data.local.Record
import com.moexclient.app.viewmodels.RecordsListViewModel
import com.moexclient.app.viewmodels.ViewModelFactory
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.round


class RecordsListFragment : Fragment() {
    @Inject lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: RecordsListViewModel
    lateinit var recordsListRv: RecyclerView
    lateinit var summaryTv: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity?.applicationContext as App).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[RecordsListViewModel::class.java]
        val root = inflater.inflate(R.layout.fragment_records_list, container, false)
        val greenColor = ContextCompat.getColor(requireContext(), android.R.color.holo_green_light)
        val redColor = ContextCompat.getColor(requireContext(), android.R.color.holo_red_light)
        recordsListRv = root.findViewById(R.id.records_list_rv)
        summaryTv = root.findViewById(R.id.records_summary_tv)
        recordsListRv.layoutManager = LinearLayoutManager(context)
        val recordsListObserver = Observer<List<Record>> {
            recordsListRv.adapter = RecordsListAdapter(it)
            val sum = it.map { it1 -> it1.profit?:0f }.sum().roundToFirst()
            if(sum >= 0) {
                summaryTv.text = String.format(getString(R.string.pos_profit), abs(sum))
                summaryTv.setTextColor(greenColor)
            } else {
                summaryTv.text = String.format(getString(R.string.neg_profit), abs(sum))
                summaryTv.setTextColor(redColor)
            }

        }
        viewModel.recordsList.observe(viewLifecycleOwner, recordsListObserver)
        viewModel.loadRecords()
        setHasOptionsMenu(true)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_records, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_records_clear -> viewModel.clear()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun Float.roundToFirst(): Float {
        return round( this * 10.0f) / 10
    }

}