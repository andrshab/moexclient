package com.example.moexclient.viewmodels

import androidx.lifecycle.ViewModel
import com.example.moexclient.data.News
import com.example.moexclient.data.MoexRepository
import javax.inject.Inject

class NewsViewModel @Inject constructor(private val repository: MoexRepository) : ViewModel() {
    suspend fun searchNews(id: Int): News = repository.getNews(id)
}