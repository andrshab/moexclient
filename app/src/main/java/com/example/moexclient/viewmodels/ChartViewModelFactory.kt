package com.example.moexclient.viewmodels

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moexclient.data.MoexRepository
import javax.inject.Inject

class ChartViewModelFactory @Inject constructor(private val repository: MoexRepository, private val sPrefs: SharedPreferences): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChartViewModel::class.java)) {
            return ChartViewModel(repository, sPrefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}