package com.moexclient.app.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.moexclient.app.api.MoexService
import com.moexclient.app.data.MoexRepository.Companion.NETWORK_PAGE_SIZE

private const val NEWS_DEFAULT_START_ID = 0
class NewsPagingSource(
    private val service: MoexService): PagingSource<Int, NewsItem>() {
    override fun getRefreshKey(state: PagingState<Int, NewsItem>): Int? {
        return state.anchorPosition?.let{anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(NETWORK_PAGE_SIZE)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(NETWORK_PAGE_SIZE)}
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsItem> {
        val startId = params.key ?: NEWS_DEFAULT_START_ID
        return try {
            val response = service.newsList(startId)
            val newsList = response.list
            val nextKey = if(newsList.isEmpty()) {
                null
            } else {
                startId + NETWORK_PAGE_SIZE
            }
            LoadResult.Page(
                data = newsList,
                prevKey = if (startId == NEWS_DEFAULT_START_ID) null else startId - NETWORK_PAGE_SIZE,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}