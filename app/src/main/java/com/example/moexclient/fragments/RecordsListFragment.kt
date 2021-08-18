package com.example.moexclient.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moexclient.App
import com.example.moexclient.R
import com.example.moexclient.adapters.RecordsListAdapter
import com.example.moexclient.data.local.Record
import com.example.moexclient.viewmodels.RecordsListViewModel
import com.example.moexclient.viewmodels.RecordsViewModelFactory
import javax.inject.Inject


class RecordsListFragment : Fragment() {
    @Inject lateinit var viewModelFactory: RecordsViewModelFactory
    lateinit var recordsListRv: RecyclerView
    private lateinit var viewModel: RecordsListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity?.applicationContext as App).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[RecordsListViewModel::class.java]
        val root = inflater.inflate(R.layout.fragment_records_list, container, false)
        recordsListRv = root.findViewById(R.id.records_list_rv)
        recordsListRv.layoutManager = LinearLayoutManager(context)
        val recordsListObserver = Observer<List<Record>> {
            recordsListRv.adapter = RecordsListAdapter(it, requireContext())
        }
        viewModel.recordsList.observe(viewLifecycleOwner, recordsListObserver)
        viewModel.loadRecords()

        return root
    }

}