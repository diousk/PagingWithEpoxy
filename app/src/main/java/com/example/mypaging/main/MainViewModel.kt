package com.example.mypaging.main

import com.example.mypaging.AutoDisposeViewModel
import com.example.mypaging.api.AppApi
import com.example.mypaging.main.data.MainRepository
import timber.log.Timber

class MainViewModel(
    private val view: MainView,
    private val repo: MainRepository
): AutoDisposeViewModel() {
    private val listing by lazy {
        repo.postsOfArticle(15)
    }

    // public for observe
    val articleLivedata = listing.pagedList
    val networkState = listing.networkState
    val refreshState = listing.refreshState

    fun fetchPosts() {
        Timber.d("fetchPosts")
        refresh()
    }

    fun refresh() {
        Timber.d("refresh")
        listing.refresh()
    }

    fun retry() {
        Timber.d("retry")
        listing.retry()
    }

    override fun onCleared() {
        listing.dispose()
        super.onCleared()
    }
}