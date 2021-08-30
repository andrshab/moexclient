package com.moexclient.app.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moexclient.app.data.MoexRepository
import com.moexclient.app.data.local.LocalRepository
import javax.inject.Inject

class ViewModelFactory @Inject constructor(private val repository: MoexRepository,
                                                private val sPrefs: SharedPreferences,
                                                private val localRepository: LocalRepository
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChartViewModel::class.java)) {
            return ChartViewModel(repository, sPrefs, localRepository) as T
        } else if (modelClass.isAssignableFrom(NewsListViewModel::class.java)) {
            return NewsListViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(RecordsListViewModel::class.java)) {
            return RecordsListViewModel(localRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}