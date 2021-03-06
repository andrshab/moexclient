package com.moexclient.app.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.moexclient.app.api.MoexService
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

    suspend fun getTopSecsData(): SecsData {
        return SecsData(service.topSecsHistoryList())
    }

    suspend fun getSecData(secId: String?, startId: Int? = null): SecData {
        return SecData(service.secHistoryList(secId, startId))
    }

    suspend fun getSecOnBoardData(
        secId: String?,
        startId: Int? = null,
        boardId: String? = null,
        sortOrder: String? = null,
        from: String? = null
    ): SecData {
        return SecData(service.secOnBoardHistoryList(secId, boardId, startId, sortOrder, from))
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 50
    }
}