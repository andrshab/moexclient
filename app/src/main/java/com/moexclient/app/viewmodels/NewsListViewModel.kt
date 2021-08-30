package com.moexclient.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.moexclient.app.data.NewsItem
import com.moexclient.app.data.MoexRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class NewsListViewModel @Inject constructor(private val repository: MoexRepository) : ViewModel() {


    fun searchNewsList(): Flow<PagingData<NewsItem>> =
        repository.getNewsListStream()
            .cachedIn(viewModelScope)

}