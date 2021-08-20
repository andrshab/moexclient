package com.example.moexclient.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.moexclient.adapters.RecordsListAdapter
import com.example.moexclient.data.local.LocalRepository
import com.example.moexclient.data.local.Record
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecordsListViewModel @Inject constructor(private val localRepository: LocalRepository): ViewModel() {
    val recordsList: MutableLiveData<List<Record>> = MutableLiveData()
    fun loadRecords() {
        viewModelScope.launch {
            recordsList.value = localRepository.getAll()
        }
    }
    fun clear() {
        viewModelScope.launch {
            localRepository.clear()
            loadRecords()
        }
    }
}