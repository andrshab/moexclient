package com.example.moexclient.fragments

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moexclient.App
import com.example.moexclient.R
import com.example.moexclient.adapters.RecordsListAdapter
import com.example.moexclient.data.local.Record
import com.example.moexclient.viewmodels.RecordsListViewModel
import com.example.moexclient.viewmodels.ViewModelFactory
import sumBy
import java.lang.Float.sum
import javax.inject.Inject


class RecordsListFragment : Fragment() {
    @Inject lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: RecordsListViewModel
    lateinit var recordsListRv: RecyclerView
    lateinit var summaryTv: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity?.applicationContext as App).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[RecordsListViewModel::class.java]
        val root = inflater.inflate(R.layout.fragment_records_list, container, false)
        recordsListRv = root.findViewById(R.id.records_list_rv)
        summaryTv = root.findViewById(R.id.records_summary_tv)
        recordsListRv.layoutManager = LinearLayoutManager(context)
        val recordsListObserver = Observer<List<Record>> {
            recordsListRv.adapter = RecordsListAdapter(it, requireContext())
            val sum = it.map { it1 -> it1.profit?:0f }.sum()
            if(sum >= 0) {
                summaryTv.text = "+${sum}RUB"
                summaryTv.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_light))
            } else {
                summaryTv.text = "-${sum}RUB"
                summaryTv.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_light))
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

}