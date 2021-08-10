package com.example.moexclient.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moexclient.data.NewsItem
import com.example.moexclient.data.MoexRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class NewsListViewModel @Inject constructor(private val repository: MoexRepository) : ViewModel() {


    fun searchNewsList(): Flow<PagingData<NewsItem>> =
        repository.getNewsListStream()
            .cachedIn(viewModelScope)

}