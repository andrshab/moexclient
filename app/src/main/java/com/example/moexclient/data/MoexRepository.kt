package com.example.moexclient.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moexclient.App
import com.example.moexclient.api.MoexService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoexRepository @Inject constructor(private val service: MoexService) {
    fun getNewsListStream(): Flow<PagingData<NewsItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NewsPagingSource(service) }
        ).flow
    }

    suspend fun getNews(id: Int): News {
        return service.news(id)
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 50
    }
}