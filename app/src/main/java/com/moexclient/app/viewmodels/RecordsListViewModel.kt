package com.moexclient.app.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moexclient.app.data.local.LocalRepository
import com.moexclient.app.data.local.Record
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