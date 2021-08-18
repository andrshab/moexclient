package com.example.moexclient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moexclient.adapters.RecordsListAdapter
import com.example.moexclient.data.local.LocalRepository
import com.example.moexclient.data.local.Record
import kotlinx.coroutines.launch
import javax.inject.Inject


class RecordsListFragment : Fragment() {
    @Inject lateinit var localRepository: LocalRepository
    lateinit var recordsRv: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity?.applicationContext as App).appComponent.inject(this)
        val root = inflater.inflate(R.layout.fragment_records_list, container, false)
        recordsRv = root.findViewById(R.id.records_list_rv)
        recordsRv.layoutManager = LinearLayoutManager(context)

        lifecycleScope.launch {
            val records = localRepository.getAll()
            recordsRv.adapter = RecordsListAdapter(records, requireContext())
        }

        return root
    }

}