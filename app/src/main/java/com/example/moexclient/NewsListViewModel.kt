package com.example.moexclient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moexclient.data.NewsItem
import com.example.moexclient.data.NewsListRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class NewsListViewModel(private val repository: NewsListRepository): ViewModel() {

    fun searchNewsList(): Flow<PagingData<NewsItem>> =
        repository.getSearchResultStream()
            .cachedIn(viewModelScope)

}