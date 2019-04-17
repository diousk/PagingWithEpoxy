package com.example.mypaging.main.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.mypaging.api.AppApi
import com.example.mypaging.model.Article
import com.example.mypaging.model.Feed
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MainDataSource(
    private val appApi: AppApi
): PageKeyedDataSource<Long, Article>(), PagingCallback<Feed, Article> {

    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()
    private var compositeDisposable: CompositeDisposable? = null

    private var retryAction: Action? = null

    init {
        compositeDisposable = CompositeDisposable()
    }

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, Article>) {
        Timber.d("loadInitial")
        initialLoad.postValue(NetworkState.LOADING)

        // don't show footer loading when start load initial
        networkState.postValue(null)

        val disposable = appApi.fetchFeed(QUERY, API_KEY, 1, params.requestedLoadSize)
            .subscribe({
                onInitialSuccess(it, callback, it.articles)
            }, {
                onInitialError(params, callback, it)
            })
        addDisposable(disposable)
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Article>) {
        Timber.d("loadAfter, key = ${params.key}")
        networkState.postValue(NetworkState.LOADING)
        val disposable = appApi.fetchFeed(QUERY, API_KEY, params.key, params.requestedLoadSize)
            .subscribe({
                onPaginationSuccess(it, callback, params, it.articles)
            }, {
                onPaginationError(params, callback, it)
            })
        addDisposable(disposable)
    }

    fun retryFailed() {
        Timber.d("retryFailed")
        val prevRetry = retryAction
        // reset to null in case duplicate retry
        retryAction = null
        prevRetry?.let {
            val disposable = Completable.fromAction(it).subscribeOn(Schedulers.io()).subscribe()
            addDisposable(disposable)
        }
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Article>) {}

    override fun onInitialSuccess(response: Feed, callback: LoadInitialCallback<Long, Article>, model: List<Article>) {
        Timber.d("feed = $response, thread = ${Thread.currentThread()}")
        retryAction = null
        networkState.postValue(NetworkState.LOADED)
        initialLoad.postValue(NetworkState.LOADED)
        callback.onResult(response.articles, null, 2)
    }

    override fun onInitialError(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, Article>,
        throwable: Throwable) {
        Timber.e("error loadInitial = $throwable, thread = ${Thread.currentThread().name}")
        retryAction = Action {
            Timber.d("retry initial")
            loadInitial(params, callback)
        }
        networkState.postValue(NetworkState.error(throwable.message))
        initialLoad.postValue(NetworkState.error(throwable.message))
    }

    override fun onPaginationSuccess(
        response: Feed,
        callback: LoadCallback<Long, Article>,
        params: LoadParams<Long>,
        model: List<Article>
    ) {
        Timber.d("onPaginationSuccess = $response, key = ${params.key}, thread = ${Thread.currentThread().name}")
        retryAction = null
        // test page 15 as end of data
        val nextKey = if (15L == params.key) null else params.key+1
        callback.onResult(response.articles, nextKey)
        networkState.postValue(NetworkState.LOADED)
    }

    override fun onPaginationError(
        params: LoadParams<Long>,
        callback: LoadCallback<Long, Article>,
        throwable: Throwable
    ) {
        Timber.e("onPaginationError = $throwable, thread = ${Thread.currentThread().name}")
        retryAction = Action {
            Timber.d("retry page = ${params.key}")
            loadAfter(params, callback)
        }
        networkState.postValue(NetworkState.error(throwable.message))
    }

    override fun addDisposable(disposable: Disposable) {
        if (compositeDisposable == null || compositeDisposable?.isDisposed == true) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(disposable)
    }

    override fun clear() {
        retryAction = null
        Timber.d("clear dispose")
        compositeDisposable?.dispose()
        compositeDisposable = null
    }

    companion object {
        const val QUERY = "movies"
        const val API_KEY = "079dac74a5f94ebdb990ecf61c8854b7"
    }
}