package com.example.moexclient.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moexclient.data.local.LocalRepository
import javax.inject.Inject

class RecordsViewModelFactory @Inject constructor(private val localRepository: LocalRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordsListViewModel::class.java)) {
            return RecordsListViewModel(localRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}