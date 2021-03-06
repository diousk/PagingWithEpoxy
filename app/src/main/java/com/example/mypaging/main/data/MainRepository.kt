package com.example.mypaging.main.data

import androidx.lifecycle.Transformations.switchMap
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.example.mypaging.api.AppApi
import com.example.mypaging.model.Article
import timber.log.Timber

interface MainRepository {
    fun postsOfArticle(pageSize: Int): Listing<Article>
}

class MainRepositoryImpl(
    private val appApi: AppApi
): MainRepository {

    override fun postsOfArticle(pageSize: Int): Listing<Article> {
        val sourceFactory = MainDataSourceFactory(appApi)
        val livePagedList = sourceFactory.toLiveData(
            config = Config(
                pageSize = pageSize,
                enablePlaceholders = false,
                initialLoadSizeHint = pageSize * 2),
            boundaryCallback = object : PagedList.BoundaryCallback<Article>() {
                override fun onZeroItemsLoaded() {
                    Timber.d("onZeroItemsLoaded")
                }

                override fun onItemAtEndLoaded(itemAtEnd: Article) {
                    Timber.d("onItemAtEndLoaded")
                }

                override fun onItemAtFrontLoaded(itemAtFront: Article) {
                    Timber.d("onItemAtFrontLoaded")
                }
            }
        )

        val refreshState = switchMap(sourceFactory.sourceLiveData) {
            it.initialLoad
        }

        val networkState = switchMap(sourceFactory.sourceLiveData) {
            it.networkState
        }

        return Listing(
            pagedList = livePagedList,
            refreshState = refreshState,
            networkState = networkState,
            retry = {
                sourceFactory.sourceLiveData.value?.retryFailed()
            },
            refresh = {
                sourceFactory.sourceLiveData.value?.invalidate()
            },
            dispose = {
                sourceFactory.sourceLiveData.value?.clear()
            }
        )
    }
}