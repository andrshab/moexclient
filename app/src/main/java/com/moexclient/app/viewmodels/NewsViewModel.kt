package com.moexclient.app.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moexclient.app.data.News
import com.moexclient.app.data.MoexRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsViewModel @Inject constructor(private val repository: MoexRepository) : ViewModel() {
    val news: MutableLiveData<News> = MutableLiveData()
    fun loadNews(id: Int) {
        viewModelScope.launch {
            news.value = repository.getNews(id)
        }
    }
}