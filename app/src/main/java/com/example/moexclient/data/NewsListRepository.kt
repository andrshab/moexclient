package com.example.moexclient.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moexclient.App
import com.example.moexclient.api.MoexService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsListRepository(private val service: MoexService) {

    fun getSearchResultStream(): Flow<PagingData<NewsItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NewsPagingSource(service) }
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 50
    }
}